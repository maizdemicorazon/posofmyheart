package com.mdmc.posofmyheart.api.controllers;

import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.mdmc.posofmyheart.application.services.CacheService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/cache")
@RequiredArgsConstructor
@Log4j2
public class CacheController {

    private final CacheManager cacheManager;
    private final CacheService cacheService;
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    @Operation(summary = "Invalidar todos los caches")
    @DeleteMapping
    public ResponseEntity<Void> evictAllCaches() {
        cacheService.clearCache();
        return ResponseEntity.noContent().build();
    }

    /**
     * 📊 ESTADÍSTICAS COMPLETAS DE TODOS LOS CACHES
     */
    @GetMapping("/stats")
    public Map<String, Object> getAllCacheStats() {
        Map<String, Object> allStats = new HashMap<>();

        Collection<String> cacheNames = cacheManager.getCacheNames();
        allStats.put("totalCaches", cacheNames.size());
        allStats.put("timestamp", System.currentTimeMillis());

        Map<String, Object> cacheStats = new HashMap<>();
        double totalHitRate = 0;
        long totalRequests = 0;
        long totalHits = 0;
        long totalMisses = 0;
        long totalEvictions = 0;

        for (String cacheName : cacheNames) {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache instanceof CaffeineCache caffeineCache) {
                CacheStats stats = caffeineCache.getNativeCache().stats();

                Map<String, Object> cacheDetail = new HashMap<>();

                // ✅ MÉTRICAS BÁSICAS (siempre disponibles)
                cacheDetail.put("requestCount", stats.requestCount());
                cacheDetail.put("hitCount", stats.hitCount());
                cacheDetail.put("missCount", stats.missCount());
                cacheDetail.put("hitRate", Double.parseDouble(decimalFormat.format(stats.hitRate() * 100)) + "%");
                cacheDetail.put("missRate", Double.parseDouble(decimalFormat.format(stats.missRate() * 100)) + "%");
                cacheDetail.put("evictionCount", stats.evictionCount());
                cacheDetail.put("evictionWeight", stats.evictionWeight());

                // ✅ MÉTRICAS DE CARGA (solo si están disponibles)
                if (stats.loadCount() > 0) {
                    cacheDetail.put("loadCount", stats.loadCount());
                    cacheDetail.put("loadFailureCount", stats.loadFailureCount());
                    cacheDetail.put("totalLoadTime", decimalFormat.format(stats.totalLoadTime() / 1_000_000) + "ms");
                    cacheDetail.put("averageLoadPenalty", decimalFormat.format(stats.averageLoadPenalty() / 1_000_000) + "ms");
                } else {
                    cacheDetail.put("loadCount", "N/A (usando @Cacheable)");
                    cacheDetail.put("loadFailureCount", "N/A");
                    cacheDetail.put("totalLoadTime", "N/A");
                    cacheDetail.put("averageLoadPenalty", "N/A");
                }

                // Performance indicators
                if (stats.hitRate() > 0.8) {
                    cacheDetail.put("performance", "🟢 Excelente");
                } else if (stats.hitRate() > 0.6) {
                    cacheDetail.put("performance", "🟡 Bueno");
                } else if (stats.requestCount() == 0) {
                    cacheDetail.put("performance", "🔵 Sin uso");
                } else {
                    cacheDetail.put("performance", "🔴 Necesita optimización");
                }

                cacheStats.put(cacheName, cacheDetail);

                // Acumular para totales
                totalRequests += stats.requestCount();
                totalHits += stats.hitCount();
                totalMisses += stats.missCount();
                totalEvictions += stats.evictionCount();
            }
        }

        // Estadísticas globales
        if (totalRequests > 0) {
            totalHitRate = (double) totalHits / totalRequests;
        }

        Map<String, Object> globalStats = new HashMap<>();
        globalStats.put("totalRequests", totalRequests);
        globalStats.put("totalHits", totalHits);
        globalStats.put("totalMisses", totalMisses);
        globalStats.put("totalEvictions", totalEvictions);
        globalStats.put("globalHitRate", Double.parseDouble(decimalFormat.format(totalHitRate * 100)) + "%");

        allStats.put("globalStats", globalStats);
        allStats.put("cacheStats", cacheStats);

        log.info("📊 Cache stats requested - Global hit rate: {}%",
                decimalFormat.format(totalHitRate * 100));

        return allStats;
    }

    /**
     * 📊 ESTADÍSTICAS DE UN CACHE ESPECÍFICO - CORREGIDO
     */
    @GetMapping("/stats/{cacheName}")
    public Map<String, Object> getCacheStats(@PathVariable String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);

        if (cache == null) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Cache '" + cacheName + "' no encontrado");
            error.put("availableCaches", cacheManager.getCacheNames());
            return error;
        }

        if (!(cache instanceof CaffeineCache caffeineCache)) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Cache '" + cacheName + "' no es Caffeine");
            error.put("cacheType", cache.getClass().getSimpleName());
            return error;
        }

        CacheStats stats = caffeineCache.getNativeCache().stats();

        Map<String, Object> result = new HashMap<>();
        result.put("cacheName", cacheName);
        result.put("cacheType", "Caffeine ☕");
        result.put("timestamp", System.currentTimeMillis());

        // ✅ ESTADÍSTICAS BÁSICAS (siempre disponibles)
        result.put("requestCount", stats.requestCount());
        result.put("hitCount", stats.hitCount());
        result.put("missCount", stats.missCount());
        result.put("hitRate", stats.hitRate());
        result.put("hitRatePercentage", Double.parseDouble(decimalFormat.format(stats.hitRate() * 100)) + "%");
        result.put("missRate", stats.missRate());
        result.put("missRatePercentage", Double.parseDouble(decimalFormat.format(stats.missRate() * 100)) + "%");

        // ✅ ESTADÍSTICAS DE EVICTION (siempre disponibles)
        result.put("evictionCount", stats.evictionCount());
        result.put("evictionWeight", stats.evictionWeight());

        // ✅ ESTADÍSTICAS DE CARGA (solo si hay CacheLoader)
        if (stats.loadCount() > 0) {
            result.put("loadCount", stats.loadCount());
            result.put("loadExceptionCount", stats.averageLoadPenalty());
            result.put("totalLoadTime", stats.totalLoadTime());
            result.put("averageLoadPenalty", stats.averageLoadPenalty());
            result.put("averageLoadPenaltyMs", decimalFormat.format(stats.averageLoadPenalty() / 1_000_000) + "ms");
            result.put("hasLoader", true);
        } else {
            result.put("loadCount", "N/A");
            result.put("loadExceptionCount", "N/A");
            result.put("totalLoadTime", "N/A");
            result.put("averageLoadPenalty", "N/A");
            result.put("averageLoadPenaltyMs", "N/A");
            result.put("hasLoader", false);
            result.put("loaderNote", "Usando @Cacheable - No hay CacheLoader configurado");
        }

        // Análisis de rendimiento
        Map<String, Object> analysis = new HashMap<>();

        if (stats.requestCount() == 0) {
            analysis.put("status", "🔵 Sin uso");
            analysis.put("recommendation", "Cache no ha sido utilizado aún");
        } else if (stats.hitRate() > 0.9) {
            analysis.put("status", "🟢 Excelente rendimiento");
            analysis.put("recommendation", "Cache funcionando óptimamente");
        } else if (stats.hitRate() > 0.7) {
            analysis.put("status", "🟡 Buen rendimiento");
            analysis.put("recommendation", "Considerar aumentar tiempo de expiración");
        } else if (stats.hitRate() > 0.5) {
            analysis.put("status", "🟠 Rendimiento moderado");
            analysis.put("recommendation", "Revisar patrones de acceso y configuración");
        } else {
            analysis.put("status", "🔴 Bajo rendimiento");
            analysis.put("recommendation", "Revisar configuración: tamaño máximo, tiempo de expiración");
        }

        // Información adicional
        if (stats.evictionCount() > 0) {
            analysis.put("evictionNote", "Se han eliminado " + stats.evictionCount() + " entradas");
            if (stats.evictionCount() > stats.hitCount()) {
                analysis.put("evictionWarning", "⚠️ Muchas evictions - considerar aumentar maximumSize");
            }
        }

        result.put("analysis", analysis);

        log.info("📊 Stats for cache '{}' - Hit rate: {}%, Requests: {}",
                cacheName, decimalFormat.format(stats.hitRate() * 100), stats.requestCount());

        return result;
    }

    /**
     * 📈 RESUMEN EJECUTIVO DE RENDIMIENTO - CORREGIDO
     */
    @GetMapping("/performance")
    public Map<String, Object> getPerformanceSummary() {
        Map<String, Object> summary = new HashMap<>();

        Collection<String> cacheNames = cacheManager.getCacheNames();
        long totalRequests = 0;
        long totalHits = 0;
        long totalEvictions = 0;
        int activeCaches = 0;

        Map<String, String> cacheStatus = new HashMap<>();

        for (String cacheName : cacheNames) {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache instanceof CaffeineCache caffeineCache) {
                CacheStats stats = caffeineCache.getNativeCache().stats();

                totalRequests += stats.requestCount();
                totalHits += stats.hitCount();
                totalEvictions += stats.evictionCount();

                if (stats.requestCount() > 0) {
                    activeCaches++;
                    if (stats.hitRate() > 0.8) {
                        cacheStatus.put(cacheName, "🟢 Excelente (" + decimalFormat.format(stats.hitRate() * 100) + "%)");
                    } else if (stats.hitRate() > 0.6) {
                        cacheStatus.put(cacheName, "🟡 Bueno (" + decimalFormat.format(stats.hitRate() * 100) + "%)");
                    } else {
                        cacheStatus.put(cacheName, "🔴 Bajo (" + decimalFormat.format(stats.hitRate() * 100) + "%)");
                    }
                } else {
                    cacheStatus.put(cacheName, "🔵 Sin uso");
                }
            }
        }



        summary.put("totalCaches", cacheNames.size());
        summary.put("activeCaches", activeCaches);
        summary.put("totalRequests", totalRequests);
        summary.put("totalHits", totalHits);
        summary.put("globalHitRate", totalRequests > 0 ?
                Double.parseDouble(decimalFormat.format((double) totalHits / totalRequests * 100)) + "%" : "0%");
        summary.put("totalEvictions", totalEvictions);
        summary.put("cacheStatus", cacheStatus);
        summary.put("timestamp", System.currentTimeMillis());

        // Recomendaciones generales
        Map<String, Object> recommendations = new HashMap<>();
        if (totalRequests == 0) {
            recommendations.put("general", "🔵 Caches configurados pero no utilizados");
        } else if ((double) totalHits / totalRequests > 0.8) {
            recommendations.put("general", "🟢 Excelente rendimiento general del cache");
        } else if ((double) totalHits / totalRequests > 0.6) {
            recommendations.put("general", "🟡 Buen rendimiento, posible optimización");
        } else {
            recommendations.put("general", "🔴 Rendimiento bajo, revisar configuración");
        }

        if (totalEvictions > totalHits && totalHits > 0) {
            recommendations.put("eviction", "⚠️ Muchas evictions - considerar aumentar maximumSize");
        }

        summary.put("recommendations", recommendations);

        return summary;
    }

    /**
     * 🔍 INFORMACIÓN DEL SISTEMA DE CACHE
     */
    @GetMapping("/info")
    public Map<String, Object> getCacheInfo() {
        Map<String, Object> info = new HashMap<>();

        // Información del manager
        info.put("managerClass", cacheManager.getClass().getName());
        info.put("managerType", cacheManager.getClass().getSimpleName());

        // Verificar si es Caffeine
        boolean isCaffeine = cacheManager.getClass().getName().contains("Caffeine");
        info.put("usingCaffeine", isCaffeine);

        if (isCaffeine) {
            info.put("status", "✅ Cache Caffeine configurado correctamente");
            info.put("performance", "Optimizado");
            info.put("features", new String[]{
                    "Expiración automática",
                    "Límite de tamaño",
                    "Estadísticas básicas",
                    "Algoritmo W-TinyLFU"
            });
        } else {
            info.put("status", "⚠️ Usando cache simple");
            info.put("performance", "Básico");
            info.put("recommendation", "Considera migrar a Caffeine para producción");
        }

        // Configuración detectada
        Collection<String> cacheNames = cacheManager.getCacheNames();
        info.put("configuredCaches", cacheNames);
        info.put("totalCaches", cacheNames.size());

        // Verificar si las estadísticas están habilitadas
        boolean statsEnabled = false;
        for (String cacheName : cacheNames) {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache instanceof CaffeineCache caffeineCache) {
                CacheStats stats = caffeineCache.getNativeCache().stats();
                // Si podemos obtener estadísticas, están habilitadas
                statsEnabled = true;
                break;
            }
        }

        info.put("statisticsEnabled", statsEnabled);
        if (!statsEnabled) {
            info.put("warning", "⚠️ Estadísticas no habilitadas - agregar 'recordStats' a configuración");
        }

        log.info("📋 Cache info requested - Type: {}, Caffeine: {}, Stats: {}",
                cacheManager.getClass().getSimpleName(), isCaffeine, statsEnabled);

        return info;
    }

}
