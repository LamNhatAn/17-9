package com.example.categoryapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "redirect:/ajax";
    }

    @GetMapping("/ajax")
    public String ajax() {
        return "admin/ajax";
    }
}
