package com.example.ftms_java_spring_boot.controllers;

import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ftms_java_spring_boot.model.User;
import com.example.ftms_java_spring_boot.service.MailService;
import com.example.ftms_java_spring_boot.service.UserService;

import jakarta.servlet.http.HttpSession;
import javassist.NotFoundException;

@Controller
public class AuthController {
    private final UserService userService;
    private final MailService mailService;

    public AuthController(UserService userService, MailService mailService) {
        this.userService = userService;
        this.mailService = mailService;
    }

    @GetMapping("/login")
    public String loginHome(Model model) {
        User newUser = new User();
        model.addAttribute("User", newUser);
        return "pages/auth/login";
    }

    @GetMapping("/forgot_password")
    public String forgotPasswordHome(Model model) {
        return "pages/auth/forgot_password";
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

    @PostMapping("/forgot_password")
    public String login(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserByEmail(email);
            user.setCodeReset();

            userService.saveUser(user);
            mailService.sendEmail(user.getEmail(), "Reset New Password", user.getCodeReset());

            redirectAttributes.addFlashAttribute("success", "We have sent verification code to your email address.");

            return "redirect:/forgot_password";

        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("message", "Imposible to send to this email address.");
            return "redirect:/forgot_password";
        }

    }

    @PostMapping("/verify_code")
    public String verifyCode(@RequestParam("code") String code, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.findUserByCodeReset(code);
            user.setCodeReset();

            userService.saveUser(user);
            mailService.sendEmail(user.getEmail(), "Reset New Password", user.getCodeReset());

            redirectAttributes.addFlashAttribute("success", "We have sent verification code to your email address.");

            return "redirect:/forgot_password";

        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("message", "Imposible to send to this email address.");
            return "redirect:/forgot_password";
        }

    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Clears all session attributes
        session.invalidate();

        return "redirect:/login";
    }
}
