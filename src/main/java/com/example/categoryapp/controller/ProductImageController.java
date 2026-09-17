package com.example.categoryapp.controller;

import com.example.categoryapp.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductImageController {
    private final ProductService productService;
    private final Path uploadRoot;

    public ProductImageController(ProductService productService, @Value("${app.upload-dir:uploads}") String uploadDir) {
        this.productService = productService;
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    @PostMapping(value = "/{id}/image", consumes = "multipart/form-data")
    public ResponseEntity<?> upload(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return productService.findById(id).map(product -> {
            try {
                if (file.isEmpty() || file.getContentType() == null || !file.getContentType().toLowerCase(Locale.ROOT).startsWith("image/")) {
                    return ResponseEntity.badRequest().body(Map.of("message", "Chỉ chấp nhận file hình ảnh."));
                }
                if (file.getSize() > 5 * 1024 * 1024) {
                    return ResponseEntity.badRequest().body(Map.of("message", "Ảnh không được vượt quá 5MB."));
                }
                String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
                String filename = UUID.randomUUID() + (extension == null ? ".img" : "." + extension.replaceAll("[^A-Za-z0-9]", ""));
                Path directory = uploadRoot.resolve("products").normalize();
                Files.createDirectories(directory);
                Files.copy(file.getInputStream(), directory.resolve(filename));
                String imageUrl = "/uploads/products/" + filename;
                product.setImageUrl(imageUrl);
                productService.save(product);
                return ResponseEntity.ok(Map.of("imageUrl", imageUrl));
            } catch (IOException exception) {
                return ResponseEntity.internalServerError().body(Map.of("message", "Không thể lưu ảnh."));
            }
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}