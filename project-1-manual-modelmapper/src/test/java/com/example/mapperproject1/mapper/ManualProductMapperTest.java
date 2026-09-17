package com.example.mapperproject1.mapper;

import com.example.mapperproject1.dto.ProductDto;
import com.example.mapperproject1.model.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ManualProductMapperTest {
    private final ManualProductMapper mapper = new ManualProductMapper();

    @Test
    void mapsProductToDto() {
        Product product = new Product("Phone", BigDecimal.valueOf(12.50), 4);
        product.setId(8L);

        ProductDto dto = mapper.toDto(product);

        assertEquals(8L, dto.getId());
        assertEquals("Phone", dto.getName());
        assertEquals(BigDecimal.valueOf(12.50), dto.getPrice());
        assertEquals(4, dto.getQuantity());
    }

    @Test
    void mapsDtoToEntityAndPreservesId() {
        ProductDto dto = new ProductDto(9L, "Tablet", BigDecimal.valueOf(20), 2);

        Product product = mapper.toEntity(dto);

        assertEquals(9L, product.getId());
        assertEquals("Tablet", product.getName());
        assertEquals(BigDecimal.valueOf(20), product.getPrice());
        assertEquals(2, product.getQuantity());
    }
}
