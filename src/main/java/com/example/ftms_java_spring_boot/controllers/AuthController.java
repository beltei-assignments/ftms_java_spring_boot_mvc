package com.example.ftms_java_spring_boot.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.ftms_java_spring_boot.model.Balance;
import com.example.ftms_java_spring_boot.model.Business;
import com.example.ftms_java_spring_boot.model.Transaction;
import com.example.ftms_java_spring_boot.model.User;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginHome(Model model) {
        User newUser = new User();
        model.addAttribute("User", newUser);
        return "pages/auth/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") User user) {

        return "redirect:/";
    }
}
