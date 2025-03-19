package com.example.ftms_java_spring_boot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ftms_java_spring_boot.model.User;
import com.example.ftms_java_spring_boot.service.UserService;

import jakarta.servlet.http.HttpSession;
import javassist.NotFoundException;

@Controller
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginHome(Model model) {
        User newUser = new User();
        model.addAttribute("User", newUser);
        return "pages/auth/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") User userPayload, HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserByEmail(userPayload.getEmail());

            if (!user.verifyPassword(userPayload.getPassword())) {
                redirectAttributes.addFlashAttribute("message", userPayload.getPassword());
                throw new NotFoundException("User not found");
            }

            // Everything went fine
            session.setAttribute("userId", user.getId());
            return "redirect:/";

        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("message", "Invalid email or password");

            return "redirect:/login";
        }

    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Clears all session attributes
        session.invalidate();

        return "redirect:/login";
    }
}
