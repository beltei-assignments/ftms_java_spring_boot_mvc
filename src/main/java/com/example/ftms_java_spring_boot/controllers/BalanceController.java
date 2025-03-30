package com.example.ftms_java_spring_boot.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.ftms_java_spring_boot.model.Balance;
import com.example.ftms_java_spring_boot.model.Business;
import com.example.ftms_java_spring_boot.model.Transaction;
import com.example.ftms_java_spring_boot.model.User;
import com.example.ftms_java_spring_boot.service.BalanceService;
import com.example.ftms_java_spring_boot.service.TransactionService;
import com.example.ftms_java_spring_boot.service.UserService;

import jakarta.persistence.criteria.Predicate;
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
      Pageable transactionPageable = PageRequest.of(0, 5);
      Specification<Transaction> transactionfilters = (root, query, criteriaBuilder) -> {
        List<Predicate> predicates = new ArrayList<>();

        // Filter user scope
        predicates.add(criteriaBuilder.equal(root.get("user"), user));

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
      };
      Page<Transaction> transactions = transactionService.getAllWithPagination(transactionPageable, transactionfilters);

      model.addAttribute("balances", balances);
      model.addAttribute("transactions", transactions);

      return "pages/balance/home_balance";
    } catch (NotFoundException e) {
      return "redirect:/login";
    }
  }

  @GetMapping("/balance/add")
  public String businessAdd(Model model) {
    Balance balance = new Balance();
    model.addAttribute("title", "Add new balance account");
    model.addAttribute("Balance", balance);

    return "pages/balance/edit_balance";
  }

  @GetMapping("/balance/{id}")
  public String businessEdit(@PathVariable Long id, Model model) {
    try {
      Balance balance = balanceService.getById(id);

      model.addAttribute("title", "Update balance");
      model.addAttribute("Balance", balance);

      return "pages/balance/edit_balance";
    } catch (NotFoundException e) {
      return "redirect:/balance";
    }
  }

  // Create or Update balance
  @PostMapping("/balance")
  public String saveBusiness(
      HttpSession session,
      @ModelAttribute("User") Balance balance) {

    try {
      Long userId = (Long) session.getAttribute("userId");
      User user = userService.getById(userId);
      balance.setUser(user);

      balanceService.save(balance);

      return "redirect:/balance";
    } catch (Exception e) {
      return "redirect:/login";
    }
  }

  // Delete balance
  @GetMapping("/balance/delete/{id}")
  public String deleteBusiness(@PathVariable Long id) {
    try {
      balanceService.deleteById(id);

      return "redirect:/balance";
    } catch (NotFoundException e) {
      return "redirect:/balance";
    }
  }
}
