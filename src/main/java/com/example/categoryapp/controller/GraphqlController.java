package com.example.categoryapp.controller;

import com.example.categoryapp.model.Category;
import com.example.categoryapp.model.Product;
import com.example.categoryapp.service.CategoryService;
import com.example.categoryapp.service.ProductService;
import graphql.GraphQLException;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class GraphqlController {
    private final ProductService productService;
    private final CategoryService categoryService;

    public GraphqlController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @QueryMapping
    public List<Product> products(@Argument String keyword, @Argument Long categoryId) {
        return productService.findAll(keyword == null ? "" : keyword, categoryId);
    }

    @QueryMapping
    public List<Category> categories(@Argument String keyword) {
        return categoryService.searchCategories(keyword == null ? "" : keyword,
                org.springframework.data.domain.PageRequest.of(0, Integer.MAX_VALUE)).getContent();
    }

    @MutationMapping
    public Product createProduct(@Argument ProductInput input) {
        Category category = categoryService.findById(input.categoryId())
                .orElseThrow(() -> new GraphQLException("Category not found"));
        Product product = new Product();
        apply(product, input, category);
        return productService.save(product);
    }

    @MutationMapping
    public Product updateProduct(@Argument Long id, @Argument ProductInput input) {
        Product product = productService.findById(id)
                .orElseThrow(() -> new GraphQLException("Product not found"));
        Category category = categoryService.findById(input.categoryId())
                .orElseThrow(() -> new GraphQLException("Category not found"));
        apply(product, input, category);
        return productService.save(product);
    }

    @MutationMapping
    public Boolean deleteProduct(@Argument Long id) {
        if (productService.findById(id).isEmpty()) {
            return false;
        }
        productService.deleteById(id);
        return true;
    }

    @MutationMapping
    public Category createCategory(@Argument CategoryInput input) {
        return categoryService.save(new Category(input.name(), input.description()));
    }

    @MutationMapping
    public Category updateCategory(@Argument Long id, @Argument CategoryInput input) {
        Category category = categoryService.findById(id)
                .orElseThrow(() -> new GraphQLException("Category not found"));
        category.setName(input.name());
        category.setDescription(input.description());
        return categoryService.save(category);
    }

    @MutationMapping
    public Boolean deleteCategory(@Argument Long id) {
        if (categoryService.findById(id).isEmpty()) {
            return false;
        }
        categoryService.deleteById(id);
        return true;
    }

    private void apply(Product product, ProductInput input, Category category) {
        product.setName(input.name());
        product.setPrice(input.price());
        product.setQuantity(input.quantity());
        product.setDescription(input.description());
        product.setImageUrl(input.imageUrl());
        product.setCategory(category);
    }

    public record ProductInput(String name, java.math.BigDecimal price, Integer quantity,
                               String description, String imageUrl, Long categoryId) {
    }

    public record CategoryInput(String name, String description) {
    }
}