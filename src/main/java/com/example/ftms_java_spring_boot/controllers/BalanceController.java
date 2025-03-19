package com.example.ftms_java_spring_boot.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.ftms_java_spring_boot.model.Balance;
import com.example.ftms_java_spring_boot.model.Transaction;
import com.example.ftms_java_spring_boot.model.User;
import com.example.ftms_java_spring_boot.service.BalanceService;
import com.example.ftms_java_spring_boot.service.TransactionService;
import com.example.ftms_java_spring_boot.service.UserService;

import jakarta.servlet.http.HttpSession;
import javassist.NotFoundException;

@Controller
public class BalanceController {
  private UserService userService;
  private BalanceService balanceService;
  private TransactionService transactionService;

  public BalanceController(UserService userService, BalanceService balanceService,
      TransactionService transactionService) {
    this.userService = userService;
    this.balanceService = balanceService;
    this.transactionService = transactionService;
  }

  @GetMapping("/balance")
  public String balanceHome(HttpSession session, Model model) {
    try {
      Long userId = (Long) session.getAttribute("userId");
      User user = userService.getById(userId);

      List<Balance> balances = balanceService.getAll(user);
      List<Transaction> transactions = transactionService.getAll(user);

      model.addAttribute("balances", balances);
      model.addAttribute("transactions", transactions);

      return "pages/balance/home_balance";
    } catch (NotFoundException e) {
      return "redirect:/login";
    }
  }
}
