package com.mdmc.posofmyheart.application.services.impl;

import com.mdmc.posofmyheart.application.services.CacheService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class CacheServiceImpl implements CacheService {
    private final CacheManager cacheManager;

    @Override
    public void clearCache() {
        cacheManager.getCacheNames()
                .stream()
                .filter(cache -> !cache.equals("products"))
                .forEach(cacheName -> {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
                log.info("Cache {} invalidado", cacheName);
            }
        });
    }
}
