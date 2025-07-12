package com.mdmc.posofmyheart.api.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import static com.mdmc.posofmyheart.util.ImageUtils.convertFileToBase64;
import static com.mdmc.posofmyheart.util.ImageUtils.convertImageFileToBytes;
import static com.mdmc.posofmyheart.util.ImageUtils.convertMultipartFileToBase64;
import static com.mdmc.posofmyheart.util.ImageUtils.convertMultipartFileToBytes;
import static com.mdmc.posofmyheart.util.ImageUtils.isValidImageFile;

import com.mdmc.posofmyheart.api.exceptions.ResourceNotFoundException;
import com.mdmc.posofmyheart.application.dtos.CatalogImageResponse;
import com.mdmc.posofmyheart.application.services.CatalogImageService;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.images.CatalogImageEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.CatalogImageRepository;

@Log4j2
@RestController
@RequestMapping("/catalogs/images")
@RequiredArgsConstructor
@Tag(name = "Catalog Images", description = "Gestión de imágenes del catálogo")
public class CatalogImageController {

    private final CatalogImageService catalogImageService;
    private final CatalogImageRepository catalogImageRepository;

    @Operation(
            summary = "Obtener imagen por ID",
            description = "Devuelve los datos completos de una imagen específica por su ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Imagen encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Imagen no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{imageId}")
    @Cacheable(value = "images", key = "'image-' + #imageId")
    public ResponseEntity<CatalogImageResponse> getImageById(
            @Parameter(description = "ID de la imagen", required = true)
            @PathVariable Long imageId) {

        log.debug("🔍 Obteniendo imagen por ID: {}", imageId);

        CatalogImageEntity image = catalogImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen con ID " + imageId + " no encontrada"));

        CatalogImageResponse response = CatalogImageResponse.builder()
                .idImage(image.getIdImage())
                .fileName(image.getFileName())
                .imageType(image.getImageType().name())
                .contentType(image.getContentType())
                .fileSize(image.getFileSize())
                .width(image.getWidth())
                .height(image.getHeight())
                .altText(image.getAltText())
                .active(image.isActive())
                .createdAt(image.getCreatedAt())
                .build();

        log.info("✅ Imagen {} obtenida exitosamente", imageId);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtener datos binarios de imagen",
            description = "Devuelve los datos binarios de una imagen para mostrar directamente"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Datos de imagen obtenidos"),
            @ApiResponse(responseCode = "404", description = "Imagen no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping(value = "/{idImage}/view", produces = {
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE,
            MediaType.APPLICATION_OCTET_STREAM_VALUE
    })
    @Cacheable(value = "images", key = "'image-' + #idImage")
    public ResponseEntity<byte[]> getImageView(
            @Parameter(description = "ID de la imagen", required = true)
            @PathVariable Long idImage) {

        log.debug("🔍 Obteniendo datos binarios de imagen: {}", idImage);

        CatalogImageEntity image = catalogImageRepository.findById(idImage)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen con ID " + idImage + " no encontrada"));

        if (!image.isActive()) {
            log.warn("⚠️ Imagen {} no está disponible", idImage);
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .contentLength(image.getFileSize())
                .header("Cache-Control", "public, max-age=86400")
                .body(image.getImageData());
    }

    @Operation(
            summary = "Obtener estadísticas de imágenes",
            description = "Devuelve estadísticas agrupadas por tipo y estado"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/statistics")
    @Cacheable(value = "images", key = "'image-statistics'")
    public ResponseEntity<Map<String, Object>> getImageStatistics() {
        log.debug("📊 Obteniendo estadísticas de imágenes");

        List<Object[]> stats = catalogImageRepository.getImageStatistics();

        Map<String, Object> response = new HashMap<>();
        Map<String, Map<String, Long>> statisticsByType = new HashMap<>();

        long totalImages = 0;
        long totalSize = 0;

        for (Object[] stat : stats) {
            String imageType = (String) stat[0];
            String status = (String) stat[1];
            Long count = (Long) stat[2];

            statisticsByType.computeIfAbsent(imageType, k -> new HashMap<>())
                    .put(status, count);

            if ("ACTIVE".equals(status)) {
                totalImages += count;
            }
        }

        // Calcular tamaño total de imágenes activas
        totalSize = catalogImageRepository.findAllActive().stream()
                .mapToLong(CatalogImageEntity::getFileSize)
                .sum();

        response.put("totalActiveImages", totalImages);
        response.put("totalSizeBytes", totalSize);
        response.put("totalSizeMB", String.format("%.2f MB", totalSize / (1024.0 * 1024.0)));
        response.put("statisticsByType", statisticsByType);

        log.info("📊 Estadísticas calculadas: {} imágenes activas, {} MB total",
                totalImages, totalSize / (1024.0 * 1024.0));

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Buscar imágenes por nombre",
            description = "Busca imágenes que contengan el texto especificado en el nombre del archivo"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Búsqueda completada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Parámetro de búsqueda inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/search")
    public ResponseEntity<List<CatalogImageResponse>> searchImagesByName(
            @Parameter(description = "Texto a buscar en el nombre del archivo", required = true)
            @RequestParam String fileName) {

        if (fileName == null || fileName.trim().length() < 2) {
            log.warn("⚠️ Parámetro de búsqueda muy corto: {}", fileName);
            return ResponseEntity.badRequest().build();
        }

        log.debug("🔍 Buscando imágenes por nombre: {}", fileName);

        List<CatalogImageEntity> images = catalogImageRepository.findByFileNameContaining(fileName.trim());

        List<CatalogImageResponse> response = images.stream()
                .map(CatalogImageResponse::mapToResponse)
                .toList();

        log.info("✅ {} imágenes encontradas para búsqueda: {}", response.size(), fileName);
        return ResponseEntity.ok(response);
    }

    // ========== MÉTODOS HEREDADOS DEL CONTROLLER ORIGINAL ==========

    @Operation(
            summary = "Convertir imagen desde servidor",
            description = "Convierte una imagen del servidor a Base64"
    )
    @GetMapping("/convert")
    public ResponseEntity<Map<String, Object>> convertServerImage(@RequestParam String imagePath) {
        try {
            byte[] imageBytes = convertImageFileToBytes(imagePath);
            String base64Image = convertFileToBase64(imagePath);

            Map<String, Object> response = new HashMap<>();
            response.put("path", imagePath);
            response.put("size", imageBytes.length);
            response.put("base64", base64Image);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            log.error("❌ Error al convertir imagen desde servidor: {}", e.getMessage());
            throw new ResourceNotFoundException("Error al convertir imagen desde servidor");
        }
    }

    @Operation(
            summary = "Descargar imagen como bytes",
            description = "Descarga una imagen como archivo binario"
    )
    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> downloadImageAsBytes(@RequestParam String imagePath) {
        try {
            byte[] imageBytes = convertImageFileToBytes(imagePath);

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"image.jpg\"")
                    .body(imageBytes);

        } catch (IOException e) {
            log.error("❌ Error al descargar imagen: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(
            summary = "Subir imagen",
            description = "Sube una imagen y la convierte a Base64"
    )
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam("image") MultipartFile image) {
        try {
            // Validar que sea una imagen
            if (!isValidImageFile(image.getOriginalFilename())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "El archivo debe ser una imagen válida"));
            }

            byte[] imageBytes = convertMultipartFileToBytes(image);

            String base64Image = convertMultipartFileToBase64(image);

            Map<String, Object> response = new HashMap<>();
            response.put("fileName", image.getOriginalFilename());
            response.put("size", imageBytes.length);
            response.put("contentType", image.getContentType());
            response.put("base64", base64Image);
            response.put("message", "Imagen convertida exitosamente");
            //TODO Persistir imagen
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            log.error("❌ Error al procesar la imagen: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al procesar la imagen: " + e.getMessage()));
        }
    }

    @Operation(
            summary = "Obtener imagen por ID",
            description = "Devuelve los datos completos de una imagen específica por su ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Imagen encontrada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Imagen no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{imageId}/data")
    @Cacheable(value = "images", key = "'image-' + #imageId")
    public ResponseEntity<CatalogImageResponse> getImageDataById(
            @Parameter(description = "ID de la imagen", required = true)
            @PathVariable Long imageId) {

        log.debug("🔍 Obteniendo imagen por ID: {}", imageId);

        CatalogImageEntity image = catalogImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen con ID " + imageId + " no encontrada"));

        CatalogImageResponse response = CatalogImageResponse.builder()
                .idImage(image.getIdImage())
                .fileName(image.getFileName())
                .imageType(image.getImageType().name())
                .contentType(image.getContentType())
                .fileSize(image.getFileSize())
                .width(image.getWidth())
                .height(image.getHeight())
                .altText(image.getAltText())
                .active(image.isActive())
                .createdAt(image.getCreatedAt())
                .build();

        log.info("✅ Imagen {} obtenida exitosamente", imageId);
        return ResponseEntity.ok(response);
    }
}