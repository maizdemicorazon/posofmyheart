package com.mdmc.posofmyheart.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ImageUtils {

    // Configuración
    private static final int MAX_WIDTH = 1200;
    private static final int MAX_HEIGHT = 1200;
    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/webp"
    );
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    // ============= MÉTODOS DE CONVERSIÓN BÁSICA (STATIC) =============

    /**
     * Convierte un archivo del sistema de archivos a bytes
     */
    public static byte[] convertFileToBytes(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            throw new IOException("El archivo no existe: " + filePath);
        }
        return Files.readAllBytes(path);
    }

    /**
     * Convierte un File a bytes
     */
    public static byte[] convertFileToBytes(File file) throws IOException {
        if (!file.exists()) {
            throw new IOException("El archivo no existe: " + file.getAbsolutePath());
        }
        return Files.readAllBytes(file.toPath());
    }

    /**
     * Convierte un MultipartFile a bytes (sin procesamiento)
     */
    public static byte[] convertMultipartFileToBytes(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new IOException("El archivo está vacío");
        }
        return multipartFile.getBytes();
    }

    /**
     * Convierte archivo a Base64
     */
    public static String convertFileToBase64(String filePath) throws IOException {
        byte[] imageBytes = convertFileToBytes(filePath);
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    /**
     * Convierte MultipartFile a Base64
     */
    public static String convertMultipartFileToBase64(MultipartFile multipartFile) throws IOException {
        byte[] imageBytes = convertMultipartFileToBytes(multipartFile);
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    // ============= MÉTODOS DE PROCESAMIENTO Y OPTIMIZACIÓN (STATIC) =============

    /**
     * Procesa, valida y optimiza imagen desde MultipartFile
     */
    public static byte[] processAndOptimizeImage(MultipartFile file) throws IOException {
        validateFile(file);

        BufferedImage originalImage = ImageIO.read(file.getInputStream());
        if (originalImage == null) {
            throw new IOException("No se pudo leer la imagen");
        }

        BufferedImage optimizedImage = resizeImageIfNeeded(originalImage);
        return convertImageToBytes(optimizedImage, getOutputFormat(file.getContentType()));
    }

    /**
     * Procesa y optimiza imagen desde InputStream
     */
    public static byte[] processAndOptimizeImageFromStream(InputStream inputStream, String contentType) throws IOException {
        validateContentType(contentType);

        BufferedImage originalImage = ImageIO.read(inputStream);
        if (originalImage == null) {
            throw new IOException("No se pudo leer la imagen desde el stream");
        }

        BufferedImage optimizedImage = resizeImageIfNeeded(originalImage);
        return convertImageToBytes(optimizedImage, getOutputFormat(contentType));
    }

    // ============= MÉTODOS DE VALIDACIÓN (STATIC) =============

    /**
     * Valida MultipartFile completo
     */
    public static void validateFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("El archivo no puede estar vacío");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IOException(String.format("El archivo es demasiado grande. Máximo permitido: %.1f MB",
                    MAX_FILE_SIZE / (1024.0 * 1024.0)));
        }

        validateContentType(file.getContentType());
        validateImageContent(file.getInputStream());
    }

    /**
     * Valida stream de recursos
     */
    public static void validateResourceFile(InputStream inputStream, String contentType, long fileSize) throws IOException {
        if (inputStream == null) {
            throw new IOException("El stream de imagen no puede ser nulo");
        }

        if (fileSize > MAX_FILE_SIZE) {
            throw new IOException(String.format("El archivo es demasiado grande. Máximo permitido: %.1f MB",
                    MAX_FILE_SIZE / (1024.0 * 1024.0)));
        }

        validateContentType(contentType);
        validateImageContent(inputStream);
    }

    /**
     * Valida tipo de contenido
     */
    private static void validateContentType(String contentType) throws IOException {
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            throw new IOException(String.format("Tipo de archivo no permitido. Formatos soportados: %s",
                    String.join(", ", ALLOWED_CONTENT_TYPES)));
        }
    }

    /**
     * Valida que el contenido sea realmente una imagen
     */
    private static void validateImageContent(InputStream inputStream) throws IOException {
        if (inputStream.markSupported()) {
            inputStream.mark(Integer.MAX_VALUE);
        }

        try {
            BufferedImage image = ImageIO.read(inputStream);
            if (image == null) {
                throw new IOException("El archivo no es una imagen válida");
            }
        } finally {
            if (inputStream.markSupported()) {
                inputStream.reset();
            }
        }
    }

    /**
     * Valida extensión de archivo
     */
    public static boolean isValidImageFile(String fileName) {
        return Optional.ofNullable(fileName)
                .map(String::toLowerCase)
                .map(name -> name.endsWith(".jpg") || name.endsWith(".jpeg") ||
                        name.endsWith(".png") || name.endsWith(".gif") ||
                        name.endsWith(".bmp") || name.endsWith(".webp"))
                .orElse(false);
    }

    // ============= MÉTODOS DE UTILIDAD INTERNA (STATIC) =============

    /**
     * Detecta tipo de contenido por bytes de la imagen
     */
    public static String detectContentType(byte[] imageData) {
        if (imageData == null || imageData.length < 4) {
            return "image/jpeg"; // Default
        }

        // JPEG
        if (imageData[0] == (byte) 0xFF && imageData[1] == (byte) 0xD8 && imageData[2] == (byte) 0xFF) {
            return "image/jpeg";
        }

        // PNG
        if (imageData.length >= 8 &&
                imageData[0] == (byte) 0x89 && imageData[1] == 0x50 &&
                imageData[2] == 0x4E && imageData[3] == 0x47) {
            return "image/png";
        }

        // WebP
        if (imageData.length >= 12 &&
                imageData[0] == 0x52 && imageData[1] == 0x49 && imageData[2] == 0x46 && imageData[3] == 0x46 &&
                imageData[8] == 0x57 && imageData[9] == 0x45 && imageData[10] == 0x42 && imageData[11] == 0x50) {
            return "image/webp";
        }

        return "image/jpeg";
    }

    /**
     * Detecta tipo de contenido por extensión de archivo
     */
    private static String detectContentTypeFromPath(String filePath) {
        String fileName = Paths.get(filePath).getFileName().toString().toLowerCase();

        if (fileName.endsWith(".png")) return "image/png";
        if (fileName.endsWith(".webp")) return "image/webp";
        if (fileName.endsWith(".gif")) return "image/gif";
        return "image/jpeg"; // Default para jpg, jpeg
    }

    /**
     * Redimensiona imagen solo si es necesario
     */
    private static BufferedImage resizeImageIfNeeded(BufferedImage originalImage) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        // Si la imagen ya es del tamaño correcto, no redimensionar
        if (originalWidth <= MAX_WIDTH && originalHeight <= MAX_HEIGHT) {
            return originalImage;
        }

        // Calcular nuevas dimensiones manteniendo proporción
        double ratio = Math.min((double) MAX_WIDTH / originalWidth, (double) MAX_HEIGHT / originalHeight);
        int newWidth = (int) (originalWidth * ratio);
        int newHeight = (int) (originalHeight * ratio);

        log.info("Redimensionando imagen de {}x{} a {}x{}", originalWidth, originalHeight, newWidth, newHeight);

        // Crear imagen redimensionada con mejor calidad
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();

        // Configurar renderizado de alta calidad
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.drawImage(originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
        g2d.dispose();

        return resizedImage;
    }

    /**
     * Convierte BufferedImage a array de bytes
     */
    private static byte[] convertImageToBytes(BufferedImage image, String format) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, format, baos);
            return baos.toByteArray();
        }
    }

    /**
     * Determina formato de salida basado en content type
     */
    private static String getOutputFormat(String contentType) {
        if (contentType == null) return "jpg";

        return switch (contentType.toLowerCase()) {
            case "image/png" -> "png";
            case "image/gif" -> "gif";
            case "image/jpeg", "image/jpg" -> "jpg";
            case "image/webp" -> "webp";
            default -> "jpg";
        };
    }

    public static byte[] convertImageFileToBytes(String filePath) throws IOException {
        if (!isValidImageFile(filePath)) {
            throw new IOException("El archivo no es una imagen válida: " + filePath);
        }

        log.info("Convirtiendo imagen a bytes: {}", filePath);
        byte[] bytes = convertFileToBytes(filePath);
        log.info("Imagen convertida exitosamente. Tamaño: {} bytes", bytes.length);

        return bytes;
    }

}
