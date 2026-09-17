package com.example.categoryapp.service;

import com.example.categoryapp.model.Product;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    List<Product> findAll(String keyword);
    List<Product> findAll(String keyword, Long categoryId);
    Page<Product> findPage(String keyword, Long categoryId, Pageable pageable);
    Optional<Product> findById(Long id);
    Product save(Product product);
    void deleteById(Long id);
}