package com.example.mapperproject1.mapper;

import com.example.mapperproject1.dto.ProductDto;
import com.example.mapperproject1.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ManualProductMapper {
    public ProductDto toDto(Product p) { return new ProductDto(p.getId(), p.getName(), p.getPrice(), p.getQuantity()); }
    public Product toEntity(ProductDto d) { Product p = new Product(d.getName(), d.getPrice(), d.getQuantity()); p.setId(d.getId()); return p; }
}
