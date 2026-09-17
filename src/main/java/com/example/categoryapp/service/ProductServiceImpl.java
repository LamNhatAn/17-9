package com.example.categoryapp.service;

import com.example.categoryapp.model.Product;
import com.example.categoryapp.repository.ProductRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll(String keyword) {
        return findAll(keyword, null);
    }

    @Override
    public List<Product> findAll(String keyword, Long categoryId) {
        String normalized = keyword == null ? "" : keyword.trim();
        Sort sort = Sort.by(Sort.Direction.ASC, "price").and(Sort.by(Sort.Direction.ASC, "id"));
        return productRepository.search(normalized, categoryId, sort);
    }

    @Override
    public Page<Product> findPage(String keyword, Long categoryId, Pageable pageable) {
        String normalized = keyword == null ? "" : keyword.trim();
        return productRepository.findAll((root, query, builder) -> {
            var predicates = builder.conjunction();
            if (!normalized.isEmpty()) {
                String pattern = "%" + normalized.toLowerCase() + "%";
                predicates = builder.and(predicates, builder.or(
                        builder.like(builder.lower(root.get("name")), pattern),
                        builder.like(builder.lower(root.get("description")), pattern),
                        builder.like(builder.lower(root.get("category").get("name")), pattern)));
            }
            if (categoryId != null) {
                predicates = builder.and(predicates, builder.equal(root.get("category").get("id"), categoryId));
            }
            return predicates;
        }, pageable);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}