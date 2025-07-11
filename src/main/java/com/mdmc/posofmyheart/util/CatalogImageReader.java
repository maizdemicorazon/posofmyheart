package com.mdmc.posofmyheart.util;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.log4j.Log4j2;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.images.CatalogImageEntity;

@Log4j2
public class CatalogImageReader {

    private static Long imageIdCounter = 1L;

    public static List<CatalogImageEntity> readImagesRecursively(File directory) {
        List<CatalogImageEntity> catalogImages = new ArrayList<>();

        String directoryName = directory.getName();
        log.info("directory {}", directoryName);

        CatalogImageEntity.ImageType productMain = switch (directoryName) {
            case "basics" -> CatalogImageEntity.ImageType.PRODUCT_BASIC;
            case "flavors" -> CatalogImageEntity.ImageType.PRODUCT_FLAVOR;
            case "extras" -> CatalogImageEntity.ImageType.PRODUCT_EXTRA;
            case "products" -> CatalogImageEntity.ImageType.PRODUCT_MAIN;
            case "sauces" -> CatalogImageEntity.ImageType.PRODUCT_SAUCE;
            default -> CatalogImageEntity.ImageType.UNKNOWN;
        };

        File[] files = directory.listFiles();
        if (files == null) {
            return catalogImages;
        }


        for (File file : files) {
            if (file.isDirectory()) {
                catalogImages.addAll(readImagesRecursively(file));
            } else if (isImage(file)) {
                CatalogImageEntity entity = new CatalogImageEntity();
                entity.setIdImage(imageIdCounter++);
                entity.setFileName(file.getName());

                entity.setImageType(productMain);
                entity.setContentType(getContentType(file));
                entity.setResource(getRelativeResourcePath(file));
                entity.setAltText(generateAltText(file.getName()));

                catalogImages.add(entity);
            }
        }

        return catalogImages;
    }

    private static boolean isImage(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".webp");
    }

    private static String getContentType(File file) {
        try {
            return Files.probeContentType(file.toPath());
        } catch (Exception e) {
            return "image/jpeg"; // Fallback
        }
    }

    private static String getRelativeResourcePath(File file) {
        // Ajusta esta lógica según tu estructura de recursos
        String basePath = "static\\catalogs\\";
        String fullPath = file.getPath().replace("\\", "/");
        int index = fullPath.indexOf(basePath);
        return index != -1 ? fullPath.substring(index) : fullPath;
    }

    private static String generateAltText(String fileName) {
        // Lógica sencilla: quitar extensión y convertir guiones por espacios
        return fileName.replaceAll("\\.[^.]+$", "") // sin extensión
                .replaceAll("[-_]", " ")
                .toLowerCase(); // capitalizar
    }

//    public static void main(String[] args) throws JsonProcessingException {
//        List<CatalogImageEntity> catalogImageEntities = CatalogImageReader.readImagesRecursively(new File("src/main/resources/static/catalogs"));
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
//        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
//        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(catalogImageEntities);
//        log.info("JSON IMAGES ::: {}", json);
//    }
}