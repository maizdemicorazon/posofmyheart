package com.mdmc.posofmyheart.domain.patterns.chain.configurations;

import com.mdmc.posofmyheart.domain.patterns.chain.ExtrasProcessor;
import com.mdmc.posofmyheart.domain.patterns.chain.FlavorsProcessor;
import com.mdmc.posofmyheart.domain.patterns.chain.OrderItemProcessorChain;
import com.mdmc.posofmyheart.domain.patterns.chain.SaucesProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderProcessorConfig {

    @Bean
    public OrderItemProcessorChain orderItemProcessorChain(
            ExtrasProcessor extrasProcessor,
            SaucesProcessor saucesProcessor,
            FlavorsProcessor flavorsProcessor) {

        // Configurar la cadena
        extrasProcessor.setNext(saucesProcessor);
        saucesProcessor.setNext(flavorsProcessor);

        return new OrderItemProcessorChain(extrasProcessor);
    }
}