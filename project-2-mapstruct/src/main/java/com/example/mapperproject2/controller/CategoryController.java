package com.example.mapperproject2.controller;

import com.example.mapperproject2.dto.CategoryDto;
import com.example.mapperproject2.mapper.CategoryMapper;
import com.example.mapperproject2.repository.CategoryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryRepository repository;
    private final CategoryMapper mapper;

    public CategoryController(CategoryRepository repository, CategoryMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @GetMapping
    public List<CategoryDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@RequestBody CategoryDto dto) {
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }
}
