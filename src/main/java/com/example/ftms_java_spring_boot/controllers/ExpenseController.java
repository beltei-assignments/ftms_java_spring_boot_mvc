package com.example.ftms_java_spring_boot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ExpenseController {
  // TODO
  @GetMapping("/expense")
  public String expenseHome(Model model) {
    return "pages/expense";
  }
}
