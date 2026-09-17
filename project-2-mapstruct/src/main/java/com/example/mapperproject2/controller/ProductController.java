package com.example.mapperproject2.controller;

import com.example.mapperproject2.dto.ProductDto;
import com.example.mapperproject2.mapper.CategoryMapper;
import com.example.mapperproject2.model.Product;
import com.example.mapperproject2.repository.ProductRepository;
import com.example.mapperproject2.service.ProductService;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService) { this.productService=productService; }
    @GetMapping({"/", "/products"})
    public String list(@RequestParam(defaultValue="") String keyword, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="5") int size, Model model) {
        Pageable pageable=PageRequest.of(Math.max(page,0),size,Sort.by("id").descending()); Page<ProductDto> result=productService.findAll(keyword,pageable);
        model.addAttribute("products",result.getContent()); model.addAttribute("keyword",keyword); model.addAttribute("page",result); return "products/list";
    }
    @GetMapping("/products/new") public String createForm(Model model){model.addAttribute("product",new ProductDto());return "products/form";}
    @GetMapping("/products/{id}/edit") public String editForm(@PathVariable Long id,Model model){model.addAttribute("product",productService.findById(id));return "products/form";}
    @PostMapping("/products/save") public String save(@ModelAttribute ProductDto dto){productService.save(dto);return "redirect:/products";}
    @PostMapping("/products/{id}/delete") public String delete(@PathVariable Long id){productService.deleteById(id);return "redirect:/products";}
}
