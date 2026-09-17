package com.example.mapperproject2.mapper;

import com.example.mapperproject2.dto.CategoryDto;
import com.example.mapperproject2.model.Category;
import com.example.mapperproject2.dto.ProductDto;
import com.example.mapperproject2.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);
    Category toEntity(CategoryDto dto);
    ProductDto toProductDto(Product product);
    Product toProduct(ProductDto dto);
}
