package com.mdmc.posofmyheart.api.controllers;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.List;

@TestConfiguration
@EnableCaching
@Profile("test")
public class CacheConfigTest {

    @Bean
    @Primary
    public CacheManager cacheManager() {
        // Usar ConcurrentMapCacheManager para tests (simple y rápido)
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();

        // Pre-configurar los nombres de cache que necesitas
        cacheManager.setCacheNames(List.of("menu", "products", "product"));

        return cacheManager;
    }
}