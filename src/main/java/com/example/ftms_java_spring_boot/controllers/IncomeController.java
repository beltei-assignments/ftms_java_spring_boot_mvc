package com.example.ftms_java_spring_boot.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.ftms_java_spring_boot.model.Balance;
import com.example.ftms_java_spring_boot.model.Business;
import com.example.ftms_java_spring_boot.model.Transaction;
import com.example.ftms_java_spring_boot.model.User;
import com.example.ftms_java_spring_boot.service.BusinessService;
import com.example.ftms_java_spring_boot.service.BalanceService;
import com.example.ftms_java_spring_boot.service.UserService;

import jakarta.servlet.http.HttpSession;
import javassist.NotFoundException;

import com.example.ftms_java_spring_boot.service.TransactionService;

@Controller
public class IncomeController {
  private final UserService userService;
  private final BusinessService businessService;
  private final TransactionService transactionService;
  private final BalanceService balanceService;

  @Autowired
  public IncomeController(BusinessService businessService, TransactionService transactionService,
      BalanceService balanceService, UserService userService) {
    this.userService = userService;
    this.balanceService = balanceService;
    this.businessService = businessService;
    this.transactionService = transactionService;
  }

  @GetMapping("/income")
  public String incomeHome(HttpSession session, Model model) {
    try {
      Long userId = (Long) session.getAttribute("userId");
      User user = userService.getById(userId);

      List<Transaction> transactions = transactionService.getUserIncomes(user);
      model.addAttribute("incomes", transactions);

      return "pages/income/home_income";
    } catch (Exception e) {
      return "redirect:/login";
    }
  }

  @GetMapping("/income/add")
  public String incomeAdd(HttpSession session, Model model) {
    try {
      Long userId = (Long) session.getAttribute("userId");
      User user = userService.getById(userId);

      model.addAttribute("title", "Add Income");

      List<Business> businesses = businessService.getAll(user);
      model.addAttribute("businesses", businesses);

      List<Balance> balances = balanceService.getAll(user);
      model.addAttribute("balances", balances);

      return "pages/income/edit_income";
    } catch (Exception e) {
      return "redirect:/login";
    }
  }

  @GetMapping("/income/{id}")
  public String incomeEdit(HttpSession session, @PathVariable Long id, Model model) {
    try {
      Long userId = (Long) session.getAttribute("userId");
      User user = userService.getById(userId);

      Optional<Transaction> transaction = transactionService.getById(id);

      if (transaction.isEmpty()) {
        return "redirect:/income";
      }

      model.addAttribute("title", "Update income");
      model.addAttribute("income", transaction.get());

      List<Business> businesses = businessService.getAll(user);
      model.addAttribute("businesses", businesses);

      List<Balance> balances = balanceService.getAll(user);
      model.addAttribute("balances", balances);

      return "pages/income/edit_income";
    } catch (Exception e) {
      return "redirect:/login";
    }
  }

  // Create or Update Expense
  @PostMapping("/income")
  public String saveIncome(
      HttpSession session,
      @RequestParam("business_id") Long businessId,
      @RequestParam("amount") Double amount,
      @RequestParam("balance_id") Long balanceId,
      @RequestParam("notes") String notes,
      @RequestParam("id") Optional<Long> id,
      Model model) {

    try {
      Long userId = (Long) session.getAttribute("userId");
      User user = userService.getById(userId);

      Transaction income;
      Balance balance = balanceService.getById(balanceId);
      Business business = businessService.getById(businessId)
          .orElseThrow(() -> new RuntimeException("Business not found"));

      if (id.isEmpty()) {
        // Creating a new income
        income = new Transaction();
        income.setBusiness(business);
        income.setBalance(balance);
        income.setAmount(amount);
        income.setNotes(Optional.ofNullable(notes));
        income.setTransactionType("Income");
        income.setUser(user);

        // Add amount to balance
        balance.setBalance(balance.getBalance() + amount);
        balanceService.save(balance);
        transactionService.create(income);
      } else {
        // Updating an existing income
        income = transactionService.getById(id.get())
            .orElseThrow(() -> new RuntimeException("Income not found"));

        double oldAmount = income.getAmount();
        Balance oldBalance = income.getBalance();

        // If changing to a different balance
        if (!oldBalance.getId().equals(balanceId)) {
          // Remove amount from old balance
          oldBalance.setBalance(oldBalance.getBalance() - oldAmount);
          balanceService.save(oldBalance);

          // Add to new balance
          balance.setBalance(balance.getBalance() + amount);
          balanceService.save(balance);
        } else {
          // Same balance, just update the difference
          double difference = amount - oldAmount;
          // Update balance with the difference
          balance.setBalance(balance.getBalance() + difference);
          balanceService.save(balance);
        }

        // Update income details
        income.setBusiness(business);
        income.setBalance(balance);
        income.setAmount(amount);
        income.setNotes(Optional.ofNullable(notes));
        transactionService.update(income);
      }

      return "redirect:/income";
    } catch (NotFoundException e) {
      return "redirect:/income";
    } catch (RuntimeException e) {
      return "redirect:/income";
    }
  }

  // Delete income
  @GetMapping("/income/delete/{id}")
  public String deleteIncome(@PathVariable Long id) {
    try {
      // Get the income before deleting
      Transaction income = transactionService.getById(id)
          .orElseThrow(() -> new RuntimeException("Income not found"));

      // Get the associated balance
      Balance balance = income.getBalance();

      // Restore the amount to the balance
      balance.setBalance(balance.getBalance() - income.getAmount());

      // Update the balance first
      balanceService.save(balance);

      // Then delete the income
      transactionService.deleteById(id);

      return "redirect:/income";
    } catch (RuntimeException e) {
      // You might want to handle the error more gracefully
      // For now, just redirect to income page
      return "redirect:/income";
    }
  }
}
