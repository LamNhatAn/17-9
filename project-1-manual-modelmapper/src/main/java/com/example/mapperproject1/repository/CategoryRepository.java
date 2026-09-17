package com.example.mapperproject1.repository;

import com.example.mapperproject1.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> { }
