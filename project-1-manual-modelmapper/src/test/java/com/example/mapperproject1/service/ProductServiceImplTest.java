package com.example.mapperproject1.service;

import com.example.mapperproject1.dto.ProductDto;
import com.example.mapperproject1.mapper.ManualProductMapper;
import com.example.mapperproject1.model.Product;
import com.example.mapperproject1.repository.ProductRepository;
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
    @Mock ManualProductMapper mapper;

    @Test
    void findAllUsesRepositoryBranchAndMapsPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Product product = new Product("Phone", BigDecimal.ONE, 1);
        ProductDto dto = new ProductDto(1L, "Phone", BigDecimal.ONE, 1);
        Page<Product> page = new PageImpl<>(List.of(product), pageable, 1);
        when(repository.findByNameContainingIgnoreCase("phone", pageable)).thenReturn(page);
        when(mapper.toDto(product)).thenReturn(dto);

        Page<ProductDto> result = new ProductServiceImpl(repository, mapper, mock(org.modelmapper.ModelMapper.class))
                .findAll("phone", pageable);

        assertEquals(List.of(dto), result.getContent());
        verify(repository).findByNameContainingIgnoreCase("phone", pageable);
        verify(mapper).toDto(product);
    }

    @Test
    void findAllBlankKeywordUsesAll() {
        Pageable pageable = PageRequest.of(0, 10);
        when(repository.findAll(pageable)).thenReturn(Page.empty(pageable));

        new ProductServiceImpl(repository, mapper, mock(org.modelmapper.ModelMapper.class)).findAll("", pageable);

        verify(repository).findAll(pageable);
    }

    @Test
    void findByIdSaveAndDeleteDelegateThroughMapperAndRepository() {
        Product product = new Product("Phone", BigDecimal.ONE, 1);
        ProductDto dto = new ProductDto(1L, "Phone", BigDecimal.ONE, 1);
        when(repository.findById(1L)).thenReturn(Optional.of(product));
        when(mapper.toDto(product)).thenReturn(dto);
        when(mapper.toEntity(dto)).thenReturn(product);
        when(repository.save(product)).thenReturn(product);

        ProductService service = new ProductServiceImpl(repository, mapper, mock(org.modelmapper.ModelMapper.class));

        assertEquals(dto, service.findById(1L));
        assertEquals(dto, service.save(dto));
        service.deleteById(1L);

        verify(repository).findById(1L);
        verify(repository).save(product);
        verify(repository).deleteById(1L);
    }
}
