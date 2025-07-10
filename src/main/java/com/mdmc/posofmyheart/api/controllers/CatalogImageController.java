package com.mdmc.posofmyheart.api.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mdmc.posofmyheart.api.exceptions.ResourceNotFoundException;
import com.mdmc.posofmyheart.application.services.CatalogImageService;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.images.CatalogImageEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.CatalogImageRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

import static com.mdmc.posofmyheart.util.ImageUtils.convertFileToBase64;
import static com.mdmc.posofmyheart.util.ImageUtils.convertImageFileToBytes;
import static com.mdmc.posofmyheart.util.ImageUtils.convertMultipartFileToBase64;
import static com.mdmc.posofmyheart.util.ImageUtils.convertMultipartFileToBytes;
import static com.mdmc.posofmyheart.util.ImageUtils.isValidImageFile;

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
                .hasImageData(image.getImageData() != null && image.getImageData().length > 0)
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
    @GetMapping(value = "/{imageId}/data", produces = {
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE,
            MediaType.APPLICATION_OCTET_STREAM_VALUE
    })
    @Cacheable(value = "images", key = "'image-data-' + #imageId")
    public ResponseEntity<byte[]> getImageData(
            @Parameter(description = "ID de la imagen", required = true)
            @PathVariable Long imageId) {

        log.debug("🔍 Obteniendo datos binarios de imagen: {}", imageId);

        CatalogImageEntity image = catalogImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen con ID " + imageId + " no encontrada"));

        if (!image.isActive()) {
            log.warn("⚠️ Imagen {} no está disponible", imageId);
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getContentType()))
                .contentLength(image.getFileSize())
                .header("Cache-Control", "public, max-age=86400")
                .body(image.getImageDataSafe());
    }

    @Operation(
            summary = "Obtener imágenes por tipo",
            description = "Devuelve todas las imágenes de un tipo específico (PRODUCT_MAIN, PRODUCT_SAUCE, etc.)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Imágenes obtenidas exitosamente"),
            @ApiResponse(responseCode = "400", description = "Tipo de imagen inválido"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/type/{idCategory}")
    @Cacheable(value = "images", key = "'id-category-' + #idCategory")
    public ResponseEntity<List<CatalogImageResponse>> getImagesByType(
            @Parameter(description = "Tipo de imagen", required = true)
            @PathVariable String imageType,
            @Parameter(description = "Solo imágenes activas", required = false)
            @RequestParam(defaultValue = "true") boolean activeOnly) {

        log.debug("🔍 Obteniendo imágenes por tipo: {} (solo activas: {})", imageType, activeOnly);

        CatalogImageEntity.ImageType type;
        try {
            type = CatalogImageEntity.ImageType.valueOf(imageType.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.warn("⚠️ Tipo de imagen inválido: {}", imageType);
            return ResponseEntity.badRequest().build();
        }

        List<CatalogImageEntity> images = activeOnly
                ? catalogImageRepository.findActiveByImageType(type)
                : catalogImageRepository.findByImageTypeAndStatus(type, true);

        List<CatalogImageResponse> response = images.stream()
                .map(this::mapToResponse)
                .toList();

        log.info("✅ {} imágenes de tipo {} obtenidas", response.size(), imageType);
        return ResponseEntity.ok(response);
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
                .map(this::mapToResponse)
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
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // Validar que sea una imagen
            if (!isValidImageFile(file.getOriginalFilename())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "El archivo debe ser una imagen válida"));
            }

            // Convertir a bytes
            byte[] imageBytes = convertMultipartFileToBytes(file);

            // Convertir a Base64 para la respuesta JSON
            String base64Image = convertMultipartFileToBase64(file);

            Map<String, Object> response = new HashMap<>();
            response.put("fileName", file.getOriginalFilename());
            response.put("size", imageBytes.length);
            response.put("contentType", file.getContentType());
            response.put("base64", base64Image);
            response.put("message", "Imagen convertida exitosamente");

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            log.error("❌ Error al procesar la imagen: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al procesar la imagen: " + e.getMessage()));
        }
    }

    // ========== MÉTODOS AUXILIARES ==========

    private CatalogImageResponse mapToResponse(CatalogImageEntity image) {
        return CatalogImageResponse.builder()
                .idImage(image.getIdImage())
                .fileName(image.getFileName())
                .contentType(image.getContentType())
                .fileSize(image.getFileSize())
                .width(image.getWidth())
                .height(image.getHeight())
                .altText(image.getAltText())
                .active(image.isActive())
                .createdAt(image.getCreatedAt())
                .hasImageData(image.getImageData() != null && image.getImageData().length > 0)
                .build();
    }

    // ========== DTO DE RESPUESTA ==========

    public record CatalogImageResponse(
            Long idImage,
            String fileName,
            String imageType,
            String contentType,
            Long fileSize,
            Integer width,
            Integer height,
            String altText,
            boolean active,
            java.time.LocalDateTime createdAt,
            boolean hasImageData
    ) {
        public static CatalogImageResponseBuilder builder() {
            return new CatalogImageResponseBuilder();
        }

        public static class CatalogImageResponseBuilder {
            private Long idImage;
            private String fileName;
            private String imageType;
            private String contentType;
            private Long fileSize;
            private Integer width;
            private Integer height;
            private String altText;
            private boolean active;
            private java.time.LocalDateTime createdAt;
            private boolean hasImageData;

            public CatalogImageResponseBuilder idImage(Long idImage) {
                this.idImage = idImage;
                return this;
            }

            public CatalogImageResponseBuilder fileName(String fileName) {
                this.fileName = fileName;
                return this;
            }

            public CatalogImageResponseBuilder imageType(String imageType) {
                this.imageType = imageType;
                return this;
            }

            public CatalogImageResponseBuilder contentType(String contentType) {
                this.contentType = contentType;
                return this;
            }

            public CatalogImageResponseBuilder fileSize(Long fileSize) {
                this.fileSize = fileSize;
                return this;
            }

            public CatalogImageResponseBuilder width(Integer width) {
                this.width = width;
                return this;
            }

            public CatalogImageResponseBuilder height(Integer height) {
                this.height = height;
                return this;
            }

            public CatalogImageResponseBuilder altText(String altText) {
                this.altText = altText;
                return this;
            }

            public CatalogImageResponseBuilder active(boolean active) {
                this.active = active;
                return this;
            }

            public CatalogImageResponseBuilder createdAt(java.time.LocalDateTime createdAt) {
                this.createdAt = createdAt;
                return this;
            }

            public CatalogImageResponseBuilder hasImageData(boolean hasImageData) {
                this.hasImageData = hasImageData;
                return this;
            }

            public CatalogImageResponse build() {
                return new CatalogImageResponse(idImage, fileName, imageType,
                        contentType, fileSize, width, height, altText, active,
                        createdAt, hasImageData);
            }
        }
    }
}