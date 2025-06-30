package com.mdmc.posofmyheart.infrastructure.configurations; // Asegúrate de que este sea tu paquete

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class CacheConfig {

    // --- 1. Especificaciones definidas a partir de tu configuración ---
    private static final String DEFAULT_SPEC = "maximumSize=200,expireAfterWrite=1440m,expireAfterAccess=720m,recordStats";
    private static final String ORDERS_SPEC = "maximumSize=50,expireAfterWrite=180m,expireAfterAccess=90m,recordStats";

    // --- 2. Mapa que asocia nombres de caché con sus especificaciones ---
    private static final Map<String, String> CACHE_SPECS = Map.of(
            "orders", ORDERS_SPEC
            // Si tuvieras más configuraciones específicas, las añadirías aquí.
            // Por ejemplo: "users", "maximumSize=1000,expireAfterWrite=12h"
    );

    @Bean
    @Primary
    public CacheManager caffeineCacheManager() {
        List<String> cacheNames = List.of(
                "products",
                "orders",
                "extras",
                "flavors",
                "sauces",
                "variants",
                "methods"
        );

        List<Cache> caches = cacheNames.stream()
                .map(name -> {
                    String spec = CACHE_SPECS.getOrDefault(name, DEFAULT_SPEC);
                    com.github.benmanes.caffeine.cache.Cache<Object, Object> nativeCache =
                            Caffeine.from(spec).build();
                    return new CaffeineCache(name, nativeCache, true);
                })
                .collect(Collectors.toList());

        List<Cache> loggingCaches = caches.stream()
                .map(LoggingCache::new)
                .collect(Collectors.toList());

        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(loggingCaches);

        return manager;
    }
}