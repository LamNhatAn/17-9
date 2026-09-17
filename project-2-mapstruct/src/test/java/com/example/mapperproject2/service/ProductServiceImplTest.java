package com.example.mapperproject2.service;

import com.example.mapperproject2.dto.ProductDto;
import com.example.mapperproject2.mapper.CategoryMapper;
import com.example.mapperproject2.model.Product;
import com.example.mapperproject2.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock ProductRepository repository;
    @Mock CategoryMapper mapper;

    @Test
    void findAllSearchesTrimmedKeywordAndMapsResults() {
        Pageable pageable = PageRequest.of(0, 10);
        Product product = new Product("Phone", BigDecimal.ONE, 1, "desc", "image");
        ProductDto dto = new ProductDto(1L, "Phone", BigDecimal.ONE, 1, "desc", "image");
        Page<Product> page = new PageImpl<>(List.of(product), pageable, 1);
        when(repository.findByNameContainingIgnoreCase("phone", pageable)).thenReturn(page);
        when(mapper.toProductDto(product)).thenReturn(dto);

        Page<ProductDto> result = new ProductServiceImpl(repository, mapper).findAll(" phone ", pageable);

        assertEquals(List.of(dto), result.getContent());
        verify(repository).findByNameContainingIgnoreCase("phone", pageable);
        verify(mapper).toProductDto(product);
    }

    @Test
    void findAllBlankKeywordUsesAll() {
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAll(pageable)).thenReturn(Page.empty(pageable));

        new ProductServiceImpl(repository, mapper).findAll("", pageable);

        verify(repository).findAll(pageable);
    }

    @Test
    void findByIdSaveAndDeleteDelegateThroughMapperAndRepository() {
        Product product = new Product("Phone", BigDecimal.ONE, 1, "desc", "image");
        ProductDto dto = new ProductDto(1L, "Phone", BigDecimal.ONE, 1, "desc", "image");
        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(mapper.toProductDto(product)).thenReturn(dto);
        when(mapper.toProduct(dto)).thenReturn(product);
        when(repository.save(product)).thenReturn(product);

        ProductService service = new ProductServiceImpl(repository, mapper);

        assertEquals(dto, service.findById(1L));
        assertEquals(dto, service.save(dto));
        service.deleteById(1L);

        verify(repository).findById(1L);
        verify(repository).save(product);
        verify(repository).deleteById(1L);
    }
}
