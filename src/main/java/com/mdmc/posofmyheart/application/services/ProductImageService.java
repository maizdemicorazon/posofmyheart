package com.mdmc.posofmyheart.application.services;

import com.mdmc.posofmyheart.application.dtos.ProductImageInfo;
import com.mdmc.posofmyheart.application.dtos.ProductImageResponse;
import com.mdmc.posofmyheart.domain.dtos.ProductImageUploadResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductImageService {
    ProductImageUploadResponse uploadImageToProduct(Long productId, MultipartFile file);

    ProductImageUploadResponse updateProductImage(Long productId, MultipartFile file);

    void deleteProductImage(Long productId);

    @Transactional(readOnly = true)
    ProductImageInfo getImageInfo(Long productId) throws IOException;

    byte[] getImageById(Long idProduct);
}
