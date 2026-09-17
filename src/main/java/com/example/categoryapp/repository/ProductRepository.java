package com.example.categoryapp.repository;

import com.example.categoryapp.model.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @Query("select p from Product p where (:keyword = '' or lower(p.name) like lower(concat('%', :keyword, '%')) "
            + "or lower(coalesce(p.description, '')) like lower(concat('%', :keyword, '%')) "
            + "or lower(p.category.name) like lower(concat('%', :keyword, '%'))) "
            + "and (:categoryId is null or p.category.id = :categoryId)")
    List<Product> search(@Param("keyword") String keyword, @Param("categoryId") Long categoryId, Sort sort);

    default List<Product> search(String keyword, Sort sort) {
        return search(keyword, null, sort);
    }
}