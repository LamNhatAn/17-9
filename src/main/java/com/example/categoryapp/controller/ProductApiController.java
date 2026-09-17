package com.example.categoryapp.controller;

import com.example.categoryapp.model.Category;
import com.example.categoryapp.model.Product;
import com.example.categoryapp.service.CategoryService;
import com.example.categoryapp.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductApiController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Product> findAll(@RequestParam(defaultValue = "") String keyword,
                                 @RequestParam(required = false) Long categoryId) {
        return productService.findAll(keyword, categoryId);
    }

    @GetMapping("/page")
    public Page<Product> findPage(@RequestParam(defaultValue = "") String keyword,
                                  @RequestParam(required = false) Long categoryId,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size) {
        return productService.findPage(keyword, categoryId,
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "price")));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        return productService.findById(id).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody Product input) {
        return resolveCategory(input).map(category -> {
            input.setId(null);
            input.setCategory(category);
            return ResponseEntity.status(HttpStatus.CREATED).body((Object) productService.save(input));
        }).orElseGet(() -> ResponseEntity.badRequest().body("Category không tồn tại"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody Product input) {
        if (productService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return resolveCategory(input).map(category -> productService.findById(id).map(existing -> {
            existing.setName(input.getName());
            existing.setPrice(input.getPrice());
            existing.setQuantity(input.getQuantity());
            existing.setDescription(input.getDescription());
            existing.setImageUrl(input.getImageUrl());
            existing.setCategory(category);
            return ResponseEntity.ok((Object) productService.save(existing));
        }).orElseGet(() -> ResponseEntity.notFound().build()))
                .orElseGet(() -> ResponseEntity.badRequest().body("Category không tồn tại"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (productService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private java.util.Optional<Category> resolveCategory(Product product) {
        return product.getCategory() == null || product.getCategory().getId() == null
                ? java.util.Optional.empty()
                : categoryService.findById(product.getCategory().getId());
    }
}