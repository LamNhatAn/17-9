package com.example.mapperproject1.service;

import com.example.mapperproject1.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Page<ProductDto> findAll(String keyword, Pageable pageable);
    ProductDto findById(Long id);
    ProductDto save(ProductDto dto);
    void deleteById(Long id);
}
