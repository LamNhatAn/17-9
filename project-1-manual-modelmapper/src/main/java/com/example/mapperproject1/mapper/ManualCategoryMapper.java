package com.example.mapperproject1.mapper;

import com.example.mapperproject1.dto.CategoryDto;
import com.example.mapperproject1.model.Category;
import org.springframework.stereotype.Component;

@Component
public class ManualCategoryMapper {
    public CategoryDto toDto(Category category) {
        return new CategoryDto(category.getId(), category.getName(), category.getDescription());
    }

    public Category toEntity(CategoryDto dto) {
        Category category = new Category(dto.getName(), dto.getDescription());
        category.setId(dto.getId());
        return category;
    }
}
