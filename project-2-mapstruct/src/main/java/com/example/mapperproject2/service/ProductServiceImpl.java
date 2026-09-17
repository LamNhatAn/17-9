package com.example.mapperproject2.service;

import com.example.mapperproject2.dto.ProductDto;
import com.example.mapperproject2.mapper.CategoryMapper;
import com.example.mapperproject2.model.Product;
import com.example.mapperproject2.repository.ProductRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository; private final CategoryMapper mapper;
    public ProductServiceImpl(ProductRepository repository, CategoryMapper mapper) { this.repository=repository; this.mapper=mapper; }
    public Page<ProductDto> findAll(String keyword, Pageable pageable) { Page<Product> page=keyword.isBlank()?repository.findAll(pageable):repository.findByNameContainingIgnoreCase(keyword.trim(),pageable); return page.map(mapper::toProductDto); }
    public ProductDto findById(Long id) { return mapper.toProductDto(repository.findById(id).orElseThrow()); }
    public ProductDto save(ProductDto dto) { return mapper.toProductDto(repository.save(mapper.toProduct(dto))); }
    public void deleteById(Long id) { repository.deleteById(id); }
}
