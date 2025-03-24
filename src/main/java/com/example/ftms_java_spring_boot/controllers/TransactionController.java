package com.example.ftms_java_spring_boot.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.ftms_java_spring_boot.model.Business;
import com.example.ftms_java_spring_boot.model.Transaction;
import com.example.ftms_java_spring_boot.model.User;
import com.example.ftms_java_spring_boot.service.BusinessService;
import com.example.ftms_java_spring_boot.service.TransactionService;
import com.example.ftms_java_spring_boot.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class TransactionController {
  private final UserService userService;
  private final BusinessService businessService;
  private final TransactionService transactionService;

  public TransactionController(UserService userService, BusinessService businessService,
      TransactionService transactionService) {
    this.userService = userService;
    this.transactionService = transactionService;
    this.businessService = businessService;
  }

  @GetMapping("/transaction")
  public String transactionHome(HttpSession session, Pageable pageable, Model model) {
    try {
      Long userId = (Long) session.getAttribute("userId");
      User user = userService.getById(userId);

      // List<Transaction> transactions = transactionService.getAll(user);
      // model.addAttribute("transactions", transactions);

      Page<Transaction> transactions = transactionService.getAllPaginage(user, pageable);
      model.addAttribute("transactions", transactions);

      return "pages/transaction/home_transaction";
    } catch (Exception e) {
      return "redirct:/login";
    }
  }

  @GetMapping("/transaction/add")
  public String transactionAdd(HttpSession session, Model model) {
    try {
      Long userId = (Long) session.getAttribute("userId");
      User user = userService.getById(userId);

      model.addAttribute("title", "Add new transaction");

      List<Business> businesses = businessService.getAll(user);
      model.addAttribute("businesses", businesses);

      return "pages/transaction/edit_transaction";
    } catch (Exception e) {
      return "redirct:/login";
    }
  }
}
