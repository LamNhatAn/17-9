package com.example.mapperproject1.controller;

import com.example.mapperproject1.dto.CategoryDto;
import com.example.mapperproject1.mapper.ManualCategoryMapper;
import com.example.mapperproject1.model.Category;
import com.example.mapperproject1.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private final CategoryRepository repository;
    private final ManualCategoryMapper manualMapper;
    private final ModelMapper modelMapper;

    public CategoryController(CategoryRepository repository, ManualCategoryMapper manualMapper, ModelMapper modelMapper) {
        this.repository = repository;
        this.manualMapper = manualMapper;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/manual/categories")
    public List<CategoryDto> findWithManualMapper() {
        return repository.findAll().stream().map(manualMapper::toDto).toList();
    }

    @PostMapping("/manual/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createWithManualMapper(@RequestBody CategoryDto dto) {
        return manualMapper.toDto(repository.save(manualMapper.toEntity(dto)));
    }

    @GetMapping("/modelmapper/categories")
    public List<CategoryDto> findWithModelMapper() {
        return repository.findAll().stream().map(category -> modelMapper.map(category, CategoryDto.class)).toList();
    }

    @PostMapping("/modelmapper/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createWithModelMapper(@RequestBody CategoryDto dto) {
        Category category = modelMapper.map(dto, Category.class);
        return modelMapper.map(repository.save(category), CategoryDto.class);
    }
}
