package com.example.ftms_java_spring_boot.controllers;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.ftms_java_spring_boot.model.Balance;
import com.example.ftms_java_spring_boot.model.Transaction;
import com.example.ftms_java_spring_boot.service.BalanceService;
import com.example.ftms_java_spring_boot.service.TransactionService;

@Controller
public class BalanceController {
  private BalanceService balanceService;
  private TransactionService transactionService;

  public BalanceController(BalanceService balanceService, TransactionService transactionService) {
    this.balanceService = balanceService;
    this.transactionService = transactionService;
  }

  @GetMapping("/balance")
  public String balanceHome(Model model) {
    List<Balance> balances = balanceService.getAll();
    List<Transaction> transactions = transactionService.getAll();

    model.addAttribute("balances", balances);
    model.addAttribute("transactions", transactions);
    return "pages/balance/home_balance";
  }
}
