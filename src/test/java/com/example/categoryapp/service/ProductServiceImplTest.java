package com.example.categoryapp.service;

import com.example.categoryapp.model.Product;
import com.example.categoryapp.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock ProductRepository repository;

    @Test
    void findAllWithoutKeywordUsesPriceAscendingSort() {
        List<Product> products = List.of(new Product());
        Sort sort = Sort.by(Sort.Direction.ASC, "price").and(Sort.by(Sort.Direction.ASC, "id"));
        when(repository.search("", null, sort)).thenReturn(products);

        List<Product> result = new ProductServiceImpl(repository).findAll("  ");

        assertSame(products, result);
        verify(repository).search("", null, sort);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findAllWithKeywordTrimsAndSearches() {
        List<Product> products = List.of(new Product());
        Sort sort = Sort.by(Sort.Direction.ASC, "price").and(Sort.by(Sort.Direction.ASC, "id"));
        when(repository.search("phone", null, sort)).thenReturn(products);

        List<Product> result = new ProductServiceImpl(repository).findAll(" phone ");

        assertSame(products, result);
        verify(repository).search("phone", null, sort);
    }

    @Test
    void findAllWithNullKeywordUsesEmptySearch() {
        when(repository.search(eq(""), isNull(), any(Sort.class))).thenReturn(List.of());

        new ProductServiceImpl(repository).findAll(null);

        verify(repository).search(eq(""), isNull(), any(Sort.class));
    }

    @Test
    void delegatesCrudOperations() {
        Product product = new Product();
        when(repository.findById(7L)).thenReturn(Optional.of(product));
        when(repository.save(product)).thenReturn(product);

        ProductService service = new ProductServiceImpl(repository);

        assertEquals(Optional.of(product), service.findById(7L));
        assertSame(product, service.save(product));
        service.deleteById(7L);

        verify(repository).findById(7L);
        verify(repository).save(product);
        verify(repository).deleteById(7L);
    }
}
