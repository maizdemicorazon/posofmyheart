package com.mdmc.posofmyheart.domain.models;

import java.math.BigInteger;

public record ProductVariant(
        String size,
        BigInteger price
) {
}
