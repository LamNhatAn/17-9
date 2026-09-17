package com.example.categoryapp.service;

import com.example.categoryapp.model.Category;
import com.example.categoryapp.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock CategoryRepository repository;

    @Test
    void searchCategoriesWithoutKeywordFindsAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> page = Page.empty(pageable);
        when(repository.findAll(pageable)).thenReturn(page);

        assertSame(page, new CategoryServiceImpl(repository).searchCategories("  ", pageable));
        verify(repository).findAll(pageable);
    }

    @Test
    void searchCategoriesWithKeywordTrimsAndSearchesBothFields() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> page = Page.empty(pageable);
        when(repository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("tools", "tools", pageable))
                .thenReturn(page);

        assertSame(page, new CategoryServiceImpl(repository).searchCategories(" tools ", pageable));
        verify(repository).findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase("tools", "tools", pageable);
    }

    @Test
    void delegatesCategoryCrudOperations() {
        Category category = new Category();
        when(repository.findById(3L)).thenReturn(Optional.of(category));
        when(repository.save(category)).thenReturn(category);

        CategoryService service = new CategoryServiceImpl(repository);

        assertEquals(Optional.of(category), service.findById(3L));
        assertSame(category, service.save(category));
        service.deleteById(3L);

        verify(repository).findById(3L);
        verify(repository).save(category);
        verify(repository).deleteById(3L);
    }
}
