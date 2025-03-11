package com.example.ftms_java_spring_boot.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.ftms_java_spring_boot.model.Balance;
import com.example.ftms_java_spring_boot.service.BalanceService;

@Controller
public class BalanceController {
  @Autowired
  private BalanceService balanceService;

  @GetMapping("/balance")
  public String balanceHome(Model model) {
    List<Balance> balances = balanceService.getAll();

    model.addAttribute("balances", balances);
    return "pages/balance";
  }
}
