package com.mdmc.posofmyheart.application.services;

import java.util.Map;

public interface ProductImageBatchService {
    void loadImagesFromResources(Map<Long, String> productImageMapping);

    void updateImagesFromResources(Map<Long, String> productImageMapping);

}
