package com.example.mapperproject2.repository;

import com.example.mapperproject2.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> { }
