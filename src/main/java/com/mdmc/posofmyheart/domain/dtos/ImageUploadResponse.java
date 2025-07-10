package com.mdmc.posofmyheart.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageUploadResponse {
    private boolean success;
    private String message;
    private Long productId;
    private String fileName;
    private String contentType;
    private long fileSize;
}