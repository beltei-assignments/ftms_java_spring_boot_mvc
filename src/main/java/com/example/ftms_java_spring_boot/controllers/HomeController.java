package com.example.ftms_java_spring_boot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    // TODO
    @GetMapping("/")
    public String home(Model model) {
        return "pages/home";
    }
}
