package com.mdmc.posofmyheart.infrastructure.configurations;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.Cache;
import org.springframework.lang.NonNull;

import java.util.concurrent.Callable;

@AllArgsConstructor
@Log4j2
public class LoggingCache implements Cache {

    private final Cache delegate;

    @Override
    public ValueWrapper get(@NonNull Object key) {
        ValueWrapper wrapper = delegate.get(key);
        if (wrapper != null) {
            log.info("✅ [CACHE HIT] Clave '{}' encontrada en la caché '{}'", key, getName());
        } else {
            log.warn("❌ [CACHE MISS] Clave '{}' no encontrada en la caché '{}'", key, getName());
        }
        return wrapper;
    }

    @Override
    public void put(@NonNull Object key, Object value) {
        log.info("⬆️ [CACHE PUT] Guardando clave '{}' en la caché '{}'", key, getName());
        delegate.put(key, value);
    }

    @Override
    public void evict(@NonNull Object key) {
        log.info("🗑️ [CACHE EVICT] Eliminando clave '{}' de la caché '{}'", key, getName());
        delegate.evict(key);
    }

    // --- Métodos delegados (sin lógica de log adicional) ---
    @NonNull
    @Override
    public String getName() {
        return delegate.getName();
    }

    @NonNull
    @Override
    public Object getNativeCache() {
        return delegate.getNativeCache();
    }

    @Override
    public <T> T get(@NonNull Object key, Class<T> type) {
        return delegate.get(key, type);
    }

    @Override
    public <T> T get(@NonNull Object key, @NonNull Callable<T> valueLoader) {
        return delegate.get(key, valueLoader);
    }

    @Override
    public void clear() {
        delegate.clear();
    }

    @Override
    public ValueWrapper putIfAbsent(@NonNull Object key, Object value) {
        return delegate.putIfAbsent(key, value);
    }

    @Override
    public boolean evictIfPresent(@NonNull Object key) {
        return delegate.evictIfPresent(key);
    }

    @Override
    public boolean invalidate() {
        return delegate.invalidate();
    }
}