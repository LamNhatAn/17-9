package com.example.categoryapp.controller;

import com.example.categoryapp.model.Category;
import com.example.categoryapp.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String listCategories(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Category> categoriesPage = categoryService.searchCategories(keyword, pageable);

        model.addAttribute("categories", categoriesPage.getContent());
        model.addAttribute("page", categoriesPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);

        return "categories/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new Category());
        return "categories/form";
    }

    @PostMapping
    public String createCategory(@Valid @ModelAttribute("category") Category category,
                                 BindingResult bindingResult,
                                 @RequestParam(defaultValue = "") String keyword,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            return "categories/form";
        }

        categoryService.save(category);
        return "redirect:/categories" + (keyword.isEmpty() ? "" : "?keyword=" + keyword);
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        Category category = categoryService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục với id: " + id));
        model.addAttribute("category", category);
        return "categories/form";
    }

    @PostMapping("/{id}")
    public String updateCategory(@PathVariable Long id,
                                 @Valid @ModelAttribute("category") Category category,
                                 BindingResult bindingResult,
                                 @RequestParam(defaultValue = "") String keyword,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            return "categories/form";
        }

        Category existing = categoryService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục với id: " + id));
        existing.setName(category.getName());
        existing.setDescription(category.getDescription());
        categoryService.save(existing);

        return "redirect:/categories" + (keyword.isEmpty() ? "" : "?keyword=" + keyword);
    }

    @PostMapping("/{id}/delete")
    public String deleteCategory(@PathVariable Long id,
                                @RequestParam(defaultValue = "") String keyword) {
        categoryService.deleteById(id);
        return "redirect:/categories" + (keyword.isEmpty() ? "" : "?keyword=" + keyword);
    }
}
