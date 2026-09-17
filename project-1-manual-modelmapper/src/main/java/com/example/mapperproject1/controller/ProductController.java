package com.example.mapperproject1.controller;

import com.example.mapperproject1.dto.ProductDto;
import com.example.mapperproject1.mapper.ManualProductMapper;
import com.example.mapperproject1.model.Product;
import com.example.mapperproject1.repository.ProductRepository;
import com.example.mapperproject1.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class ProductController {
    private final ProductService productService; private final ModelMapper modelMapper;
    public ProductController(ProductService productService, ModelMapper modelMapper) { this.productService=productService; this.modelMapper=modelMapper; }
    @GetMapping({"/", "/products"})
    public String list(@RequestParam(defaultValue="") String keyword, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="5") int size, Model model) {
        Pageable pageable = PageRequest.of(Math.max(page, 0), size, Sort.by("id").descending());
        Page<ProductDto> result = productService.findAll(keyword, pageable);
        model.addAttribute("products", result.getContent()); model.addAttribute("keyword", keyword); model.addAttribute("page", result); return "products/list";
    }
    @GetMapping("/products/new") public String createForm(Model model) { model.addAttribute("product", new ProductDto()); return "products/form"; }
    @GetMapping("/products/{id}/edit") public String editForm(@PathVariable Long id, Model model) { model.addAttribute("product", productService.findById(id)); return "products/form"; }
    @PostMapping("/products/save") public String save(@ModelAttribute ProductDto dto) { productService.save(dto); return "redirect:/products"; }
    @PostMapping("/products/{id}/delete") public String delete(@PathVariable Long id) { productService.deleteById(id); return "redirect:/products"; }
    @ResponseBody @GetMapping("/api/modelmapper/products") public List<ProductDto> api() { return productService.findAll("", PageRequest.of(0, 100)).getContent().stream().map(dto -> modelMapper.map(dto, ProductDto.class)).toList(); }
}
