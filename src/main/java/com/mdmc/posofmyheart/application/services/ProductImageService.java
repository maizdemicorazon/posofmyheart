package com.mdmc.posofmyheart.application.services;

import com.mdmc.posofmyheart.application.dtos.ProductImageInfo;
import com.mdmc.posofmyheart.domain.dtos.ImageUploadResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductImageService {
    ImageUploadResponse uploadImageToProduct(Long productId, MultipartFile file);

    ImageUploadResponse updateProductImage(Long productId, MultipartFile file);

    void deleteProductImage(Long productId);

    @Transactional(readOnly = true)
    ProductImageInfo getImageInfo(Long productId) throws IOException;

    byte[] getImageById(Long idProduct);

    ImageUploadResponse uploadImageToEntityFromResources(Long productId, String resourcePath);

    ImageUploadResponse updateEntityImageFromResources(Long productId, String resourcePath);
}
