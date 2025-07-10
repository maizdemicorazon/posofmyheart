package com.mdmc.posofmyheart.api.controllers;

import com.mdmc.posofmyheart.application.dtos.ProductImageInfo;
import com.mdmc.posofmyheart.application.services.ProductImageService;
import com.mdmc.posofmyheart.application.services.ProductService;
import com.mdmc.posofmyheart.application.dtos.ProductImageResponse;
import com.mdmc.posofmyheart.domain.dtos.ImageUploadResponse;
import com.mdmc.posofmyheart.domain.models.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.mdmc.posofmyheart.util.ImageUtils.detectContentType;

@RestController
@RequestMapping("/products/image")
@RequiredArgsConstructor
@Log4j2
public class ProductImageController {

    private final ProductService productService;
    private final ProductImageService productImageService;

    @Operation(
            summary = "Obtener imagen de producto",
            description = "Devuelve la imagen de un producto específico almacenada en base de datos"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagen obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado o sin imagen"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{idProduct}")
    public ResponseEntity<byte[]> getImageById(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable Long idProduct) {

        log.info("🖼️ Solicitando imagen para producto ID: {}", idProduct);

        byte[] image = productImageService.getImageById(idProduct);

        String contentType = detectContentType(image);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentLength(image.length);
        headers.setCacheControl("max-age=3600");

        log.info("✅ Imagen servida exitosamente para producto {}, tamaño: {} bytes",
                idProduct, image.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(image);
    }

    @Operation(
            summary = "Obtener imagen en formato base64",
            description = "Devuelve la imagen de un producto en formato base64 con data URL"
    )
    @GetMapping("/{idProduct}/b64")
    public ResponseEntity<ProductImageResponse> getBase64Image(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable Long idProduct) {

        log.info("🖼️ Solicitando imagen base64 para producto ID: {}", idProduct);

        Product product = productService.getProductById(idProduct);

        String contentType = detectContentType(product.image());
        String base64 = java.util.Base64.getEncoder().encodeToString(product.image());
        String dataUrl = "data:" + contentType + ";base64," + base64;


        log.info("✅ Imagen base64 servida exitosamente para producto {}", idProduct);
        return ResponseEntity.ok(ProductImageResponse.builder()
                .name(product.name())
                .base64(dataUrl)
                .build()
        );

    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Imagen eliminada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Archivo inválido o producto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Operation(
            summary = "Eliminar imagen de producto",
            description = "Elimina la imagen de un producto específico"
    )
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{idProduct}/info")
    public ProductImageInfo getImageInfo(@PathVariable Long idProduct) {
        log.info("🗑️ Consultando información de la imagen: {}", idProduct);

        try {
            log.info("✅ Información de la imagen {} obtenida exitosamente", idProduct);
            return productImageService.getImageInfo(idProduct);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Operation(
            summary = "Subir imagen de producto",
            description = "Carga una imagen y la asigna a un producto específico"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Imagen subida exitosamente"),
            @ApiResponse(responseCode = "400", description = "Archivo inválido o producto no encontrado"),
            @ApiResponse(responseCode = "413", description = "Archivo demasiado grande"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping(value = "/{idProduct}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageUploadResponse> uploadImage(
            @Parameter(description = "ID del producto", required = true)
            @PathVariable Long idProduct,
            @Parameter(description = "Archivo de imagen", required = true)
            @RequestParam("image") MultipartFile image) {

        log.info("📤 Iniciando carga de imagen para producto ID: {}", idProduct);

        ImageUploadResponse response = productImageService.uploadImageToProduct(idProduct, image);
        log.info("✅ Imagen subida exitosamente para producto: {}", idProduct);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Actualizar imagen de producto",
            description = "Reemplaza la imagen existente de un producto"
    )
    @PutMapping(value = "/update/{idProduct}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageUploadResponse> updateImage(
            @PathVariable Long idProduct,
            @RequestParam("image") MultipartFile image) {

        log.info("🔄 Actualizando imagen para producto ID: {}", idProduct);

        ImageUploadResponse response = productImageService.updateProductImage(idProduct, image);
        log.info("✅ Imagen actualizada exitosamente para producto: {}", idProduct);

        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Imagen eliminada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Archivo inválido o producto no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @Operation(
            summary = "Eliminar imagen de producto",
            description = "Elimina la imagen de un producto específico"
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{idProduct}")
    public void deleteImage(@PathVariable Long idProduct) {
        log.info("🗑️ Eliminando imagen para producto ID: {}", idProduct);

        productImageService.deleteProductImage(idProduct);
        log.info("✅ Imagen eliminada exitosamente para producto: {}", idProduct);
    }

}
