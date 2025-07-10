package com.mdmc.posofmyheart.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.multipart.MultipartFile;

@Log4j2
public class ImageUtils {

    private static final int MAX_WIDTH = 1200;
    private static final int MAX_HEIGHT = 1200;

    private ImageUtils(){}

    // Configuración
    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "image/jpeg", "image/jpg", "image/png", "image/webp"
    );
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    public static String detectContentType(byte[] imageData) {
        if (imageData == null || imageData.length < 4) {
            return "image/jpg"; // Default
        }

        // Verificar signature de diferentes formatos
        if (imageData[0] == (byte) 0xFF && imageData[1] == (byte) 0xD8 && imageData[2] == (byte) 0xFF) {
            return "image/jpeg";
        }

        if (imageData.length >= 8 &&
                imageData[0] == (byte) 0x89 &&
                imageData[1] == 0x50 &&
                imageData[2] == 0x4E &&
                imageData[3] == 0x47) {
            return "image/png";
        }

        if (imageData.length >= 12 &&
                imageData[0] == 0x52 &&
                imageData[1] == 0x49 &&
                imageData[2] == 0x46 &&
                imageData[3] == 0x46 &&
                imageData[8] == 0x57 &&
                imageData[9] == 0x45 &&
                imageData[10] == 0x42 &&
                imageData[11] == 0x50) {
            return "image/webp";
        }

        return "image/jpeg";
    }

    /**
     * Valida el archivo subido desde cliente
     */
    public static void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            log.error("El archivo no puede estar vacío");
        }

        if (file != null && file.getSize() > MAX_FILE_SIZE) {
            log.error("El archivo es demasiado grande. Máximo permitido: {} MB", MAX_FILE_SIZE / (1024.0 * 1024.0));
        }

        String contentType = file!= null ? file.getContentType(): "";
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            log.error(
                    "Tipo de archivo no permitido. Formatos soportados: {}", String.join(", ", ALLOWED_CONTENT_TYPES)
            );
        }

        // Validar que realmente sea una imagen
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                log.error("El archivo no es una imagen válida");
            }
        } catch (IOException e) {
            log.error("Error leyendo el archivo de imagen");
        }
    }

    /**
     * Valida archivo desde resources
     */
    public static void validateResourceFile(InputStream inputStream, String contentType, long fileSize) {
        if (inputStream == null) {
            throw new IllegalArgumentException("El stream de imagen no puede ser nulo");
        }

        if (fileSize > MAX_FILE_SIZE) {
            log.error("El archivo es demasiado grande. Máximo permitido: {} MB", MAX_FILE_SIZE / (1024.0 * 1024.0));
        }

        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
            log.error(
                    "Tipo de archivo no permitido. Formatos soportados: {}", String.join(", ", ALLOWED_CONTENT_TYPES)
            );
        }

        // Validar que realmente sea una imagen
        try {
            // Marcar el stream si es posible para poder reutilizarlo
            if (inputStream.markSupported()) {
                inputStream.mark(Integer.MAX_VALUE);
            }

            BufferedImage image = ImageIO.read(inputStream);
            if (image == null) {
               log.error("El archivo no es una imagen válida");
            }

            // Resetear el stream si está marcado
            if (inputStream.markSupported()) {
                inputStream.reset();
            }
        } catch (IOException e) {
            log.error("Error leyendo el archivo de imagen: " + e.getMessage());
        }
    }

    /**
     * Procesa y optimiza la imagen desde MultipartFile
     */
    public static byte[] processAndOptimizeImage(MultipartFile file) throws IOException {
        BufferedImage originalImage = ImageIO.read(file.getInputStream());

        if (originalImage == null) {
            log.error("No se pudo leer la imagen");
        }

        BufferedImage optimizedImage = resizeImage(originalImage);

        return convertImageToBytes(optimizedImage, getOutputFormat(file.getContentType()));
    }

    /**
     * Procesa y optimiza la imagen desde InputStream (para archivos locales)
     */
    public static byte[] processAndOptimizeImageFromStream(InputStream inputStream, String contentType) throws IOException {
        BufferedImage originalImage = ImageIO.read(inputStream);
        BufferedImage optimizedImage = null;
        if (originalImage == null) {
           log.error("No se pudo leer la imagen desde el stream");
        }else {
            optimizedImage = resizeImage(originalImage);
        }

        return convertImageToBytes(optimizedImage, getOutputFormat(contentType));
    }

    /**
     * Redimensiona la imagen manteniendo la proporción
     */
    public static BufferedImage resizeImage(BufferedImage originalImage) {
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

        log.info("🔄 Redimensionando imagen de {}x{} a {}x{}",
                originalWidth, originalHeight, newWidth, newHeight);

        // Crear imagen redimensionada
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resizedImage.createGraphics();

        // Mejorar calidad del redimensionado
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION,
                java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING,
                java.awt.RenderingHints.VALUE_RENDER_QUALITY);

        g2d.drawImage(originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH), 0, 0, null);
        g2d.dispose();

        return resizedImage;
    }

    /**
     * Convierte BufferedImage a byte array
     */
    public static byte[] convertImageToBytes(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        return baos.toByteArray();
    }

    /**
     * Determina el formato de salida basado en el content type
     */
    public static String getOutputFormat(String contentType) {
        if (contentType == null) return "jpg";

        return switch (contentType.toLowerCase()) {
            case "image/png" -> "png";
            case "image/gif" -> "gif";
            case "image/jpeg" -> "jpeg";
            case "image/webp" -> "webp";
            default -> "jpg";
        };
    }
}