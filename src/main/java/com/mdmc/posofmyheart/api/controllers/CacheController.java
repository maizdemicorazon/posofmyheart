package com.mdmc.posofmyheart.api.controllers;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cache")
@RequiredArgsConstructor
@Log4j2
public class CacheController {

    private final CacheManager cacheManager;

    @Operation(summary = "Invalidar todos los caches")
    @DeleteMapping
    public ResponseEntity<Void> evictAllCaches() {
        cacheManager.getCacheNames().forEach(cacheName -> {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
                log.info("Cache {} invalidado", cacheName);
            }
        });
        return ResponseEntity.noContent().build();
    }

}
