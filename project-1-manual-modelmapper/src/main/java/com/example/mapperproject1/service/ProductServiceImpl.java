package com.example.mapperproject1.service;

import com.example.mapperproject1.dto.ProductDto;
import com.example.mapperproject1.mapper.ManualProductMapper;
import com.example.mapperproject1.model.Product;
import com.example.mapperproject1.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository; private final ManualProductMapper manualMapper; private final ModelMapper modelMapper;
    public ProductServiceImpl(ProductRepository repository, ManualProductMapper manualMapper, ModelMapper modelMapper) { this.repository=repository; this.manualMapper=manualMapper; this.modelMapper=modelMapper; }
    public Page<ProductDto> findAll(String keyword, Pageable pageable) { Page<Product> page=keyword.isBlank()?repository.findAll(pageable):repository.findByNameContainingIgnoreCase(keyword.trim(),pageable); return page.map(manualMapper::toDto); }
    public ProductDto findById(Long id) { return manualMapper.toDto(repository.findById(id).orElseThrow()); }
    public ProductDto save(ProductDto dto) { return manualMapper.toDto(repository.save(manualMapper.toEntity(dto))); }
    public void deleteById(Long id) { repository.deleteById(id); }
}
