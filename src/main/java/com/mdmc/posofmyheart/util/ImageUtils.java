package com.mdmc.posofmyheart.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Optional;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ImageUtils {

    // ============= CONSTANTES =============
    private static final int MAX_WIDTH = 500;
    private static final int MAX_HEIGHT = 500;
    private static final String DEFAULT_FORMAT = "jpg";
    private static final float JPEG_QUALITY = 0.85f;

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

    // ============= MÉTODOS DE REDIMENSIONAMIENTO =============

    /**
     * Redimensiona imagen desde archivo manteniendo proporción (máximo 500x500)
     */
    public static byte[] resizeImageFromFile(String filePath) throws IOException {
        if (!isValidImageFile(filePath)) {
            throw new IOException("El archivo no es una imagen válida: " + filePath);
        }

        log.info("Redimensionando imagen desde archivo: {}", filePath);
        byte[] originalBytes = convertFileToBytes(filePath);
        return resizeImageBytes(originalBytes, MAX_WIDTH, MAX_HEIGHT, true);
    }

    /**
     * Redimensiona imagen desde MultipartFile manteniendo proporción (máximo 500x500)
     */
    public static byte[] resizeImageFromMultipartFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new IOException("El archivo está vacío");
        }

        log.info("Redimensionando imagen desde MultipartFile: {}", multipartFile.getOriginalFilename());
        byte[] originalBytes = convertMultipartFileToBytes(multipartFile);
        return resizeImageBytes(originalBytes, MAX_WIDTH, MAX_HEIGHT, true);
    }

    /**
     * Redimensiona imagen a tamaño exacto 500x500 (con crop cuadrado)
     */
    public static byte[] resizeImageToSquare(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new IOException("El archivo está vacío");
        }

        log.info("Redimensionando imagen a cuadrado 500x500: {}", multipartFile.getOriginalFilename());
        byte[] originalBytes = convertMultipartFileToBytes(multipartFile);
        return resizeImageBytes(originalBytes, MAX_WIDTH, MAX_HEIGHT, false);
    }

    /**
     * Redimensiona imagen desde archivo a tamaño exacto 500x500 (con crop cuadrado)
     */
    public static byte[] resizeImageFromFileToSquare(String filePath) throws IOException {
        if (!isValidImageFile(filePath)) {
            throw new IOException("El archivo no es una imagen válida: " + filePath);
        }

        log.info("Redimensionando imagen desde archivo a cuadrado 500x500: {}", filePath);
        byte[] originalBytes = convertFileToBytes(filePath);
        return resizeImageBytes(originalBytes, MAX_WIDTH, MAX_HEIGHT, false);
    }

    /**
     * Método principal de redimensionamiento
     */
    public static byte[] resizeImageBytes(byte[] imageData, int maxWidth, int maxHeight, boolean maintainAspectRatio) throws IOException {
        if (imageData == null || imageData.length == 0) {
            throw new IOException("Los datos de la imagen están vacíos");
        }

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData)) {
            BufferedImage originalImage = ImageIO.read(inputStream);

            if (originalImage == null) {
                throw new IOException("No se pudo leer la imagen. Formato no soportado.");
            }

            BufferedImage resizedImage;

            if (maintainAspectRatio) {
                resizedImage = resizeWithAspectRatio(originalImage, maxWidth, maxHeight);
            } else {
                resizedImage = resizeToSquare(originalImage, maxWidth);
            }

            // Convertir a bytes
            String formatName = detectImageFormat(imageData);
            return bufferedImageToBytes(resizedImage, formatName);

        } catch (Exception e) {
            log.error("Error redimensionando imagen", e);
            throw new IOException("Error al redimensionar la imagen: " + e.getMessage(), e);
        }
    }

    /**
     * Redimensiona manteniendo proporción
     */
    private static BufferedImage resizeWithAspectRatio(BufferedImage originalImage, int maxWidth, int maxHeight) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        // Calcular nuevas dimensiones manteniendo proporción
        double aspectRatio = (double) originalWidth / originalHeight;
        int newWidth, newHeight;

        if (originalWidth > originalHeight) {
            newWidth = Math.min(maxWidth, originalWidth);
            newHeight = (int) (newWidth / aspectRatio);
            if (newHeight > maxHeight) {
                newHeight = maxHeight;
                newWidth = (int) (newHeight * aspectRatio);
            }
        } else {
            newHeight = Math.min(maxHeight, originalHeight);
            newWidth = (int) (newHeight * aspectRatio);
            if (newWidth > maxWidth) {
                newWidth = maxWidth;
                newHeight = (int) (newWidth / aspectRatio);
            }
        }

        return createResizedImage(originalImage, newWidth, newHeight);
    }

    /**
     * Redimensiona a cuadrado exacto con crop
     */
    private static BufferedImage resizeToSquare(BufferedImage originalImage, int targetSize) {
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        // Determinar el área de crop (cuadrado centrado)
        int cropSize = Math.min(originalWidth, originalHeight);
        int x = (originalWidth - cropSize) / 2;
        int y = (originalHeight - cropSize) / 2;

        // Hacer crop cuadrado
        BufferedImage croppedImage = originalImage.getSubimage(x, y, cropSize, cropSize);

        // Redimensionar al tamaño objetivo
        return createResizedImage(croppedImage, targetSize, targetSize);
    }

    /**
     * Crea imagen redimensionada con alta calidad
     */
    private static BufferedImage createResizedImage(BufferedImage originalImage, int newWidth, int newHeight) {
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = resizedImage.createGraphics();

        // Configurar para alta calidad
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo blanco para JPEGs
        graphics2D.setColor(Color.WHITE);
        graphics2D.fillRect(0, 0, newWidth, newHeight);

        // Dibujar imagen redimensionada
        graphics2D.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        graphics2D.dispose();

        return resizedImage;
    }

    /**
     * Convierte BufferedImage a bytes
     */
    private static byte[] bufferedImageToBytes(BufferedImage image, String formatName) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // Usar el formato original o JPG como default
            String format = (formatName != null && !formatName.isEmpty()) ? formatName : DEFAULT_FORMAT;

            boolean success = ImageIO.write(image, format, outputStream);
            if (!success) {
                // Fallback a JPG si el formato no es soportado
                log.warn("Formato {} no soportado, usando JPG como fallback", format);
                ImageIO.write(image, DEFAULT_FORMAT, outputStream);
            }

            return outputStream.toByteArray();
        }
    }

    /**
     * Detecta formato de imagen por magic bytes
     */
    private static String detectImageFormat(byte[] imageData) {
        if (imageData == null || imageData.length < 4) {
            return DEFAULT_FORMAT;
        }

        // PNG
        if (imageData.length >= 8 &&
                imageData[0] == (byte) 0x89 && imageData[1] == 0x50 &&
                imageData[2] == 0x4E && imageData[3] == 0x47) {
            return "png";
        }

        // JPEG
        if (imageData[0] == (byte) 0xFF && imageData[1] == (byte) 0xD8 && imageData[2] == (byte) 0xFF) {
            return "jpg";
        }

        return DEFAULT_FORMAT;
    }

    // ============= MÉTODOS DE VALIDACIÓN (STATIC) =============

    /**
     * Valida extensión de archivo
     */
    public static boolean isValidImageFile(String fileName) {
        return Optional.ofNullable(fileName)
                .map(String::toLowerCase)
                .map(name -> name.endsWith(".jpg")
                        || name.endsWith(".jpeg")
                        || name.endsWith(".png")
                        || name.endsWith(".webp"))
                .orElse(false);
    }

    /**
     * Valida que la imagen no exceda el tamaño máximo en bytes (5MB)
     */
    public static boolean isValidImageSize(byte[] imageData) {
        final long MAX_SIZE_BYTES = 5 * 1024 * 1024; // 5MB
        return imageData != null && imageData.length <= MAX_SIZE_BYTES;
    }

    /**
     * Valida dimensiones de imagen
     */
    public static boolean hasValidDimensions(byte[] imageData) throws IOException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(imageData)) {
            BufferedImage image = ImageIO.read(inputStream);
            if (image == null) return false;

            int width = image.getWidth();
            int height = image.getHeight();

            // Mínimo 50x50, máximo 4000x4000
            return width >= 50 && height >= 50 && width <= 4000 && height <= 4000;
        }
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
                imageData[0] == 0x52 && imageData[1] == 0x49 &&
                imageData[2] == 0x46 && imageData[3] == 0x46 &&
                imageData[8] == 0x57 && imageData[9] == 0x45 &&
                imageData[10] == 0x42 && imageData[11] == 0x50) {
            return "image/webp";
        }

        return "image/jpeg";
    }

    /**
     * Método mejorado que incluye redimensionamiento automático
     */
    public static byte[] convertImageFileToBytes(String filePath) throws IOException {
        if (!isValidImageFile(filePath)) {
            throw new IOException("El archivo no es una imagen válida: " + filePath);
        }

        log.info("Convirtiendo y redimensionando imagen: {}", filePath);

        // Redimensionar automáticamente
        byte[] resizedBytes = resizeImageFromFile(filePath);

        log.info("Imagen convertida y redimensionada exitosamente. Tamaño: {} bytes", resizedBytes.length);
        return resizedBytes;
    }

    /**
     * Método mejorado para MultipartFile con redimensionamiento automático
     */
    public static byte[] convertAndResizeMultipartFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new IOException("El archivo está vacío");
        }

        if (!isValidImageFile(multipartFile.getOriginalFilename())) {
            throw new IOException("El archivo no es una imagen válida: " + multipartFile.getOriginalFilename());
        }

        log.info("Convirtiendo y redimensionando MultipartFile: {}", multipartFile.getOriginalFilename());

        // Redimensionar automáticamente
        byte[] resizedBytes = resizeImageFromMultipartFile(multipartFile);

        log.info("Archivo convertido y redimensionado exitosamente. Tamaño: {} bytes", resizedBytes.length);
        return resizedBytes;
    }

}