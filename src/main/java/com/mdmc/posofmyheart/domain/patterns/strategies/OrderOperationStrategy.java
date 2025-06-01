package com.mdmc.posofmyheart.domain.patterns.strategies;

public interface OrderOperationStrategy<T, R> {
    R execute(T input);
}
