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
public class ExpenseController {
  private final UserService userService;
  private final BusinessService businessService;
  private final TransactionService transactionService;
  private final BalanceService balanceService;

  @Autowired
  public ExpenseController(BusinessService businessService, TransactionService transactionService,
      BalanceService balanceService, UserService userService) {
    this.userService = userService;
    this.balanceService = balanceService;
    this.businessService = businessService;
    this.transactionService = transactionService;
  }

  @GetMapping("/expense")
  public String expenseHome(HttpSession session, Model model) {
    try {
      Long userId = (Long) session.getAttribute("userId");
      User user = userService.getById(userId);

      List<Transaction> transactions = transactionService.getUserExpenses(user);
      model.addAttribute("expenses", transactions);

      return "pages/expense/home_expense";
    } catch (Exception e) {
      return "redirect:/login";
    }
  }

  @GetMapping("/expense/add")
  public String expenseAdd(HttpSession session, Model model) {
    try {
      Long userId = (Long) session.getAttribute("userId");
      User user = userService.getById(userId);

      model.addAttribute("title", "Add Expense");

      List<Business> businesses = businessService.getAll(user);
      model.addAttribute("businesses", businesses);

      List<Balance> balances = balanceService.getAll(user);
      model.addAttribute("balances", balances);

      return "pages/expense/edit_expense";
    } catch (NotFoundException e) {
      return "redirect:/login";
    }
  }

  @GetMapping("/expense/{id}")
  public String expenseEdit(HttpSession session, @PathVariable Long id, Model model) {
    try {
      Long userId = (Long) session.getAttribute("userId");
      User user = userService.getById(userId);
      Optional<Transaction> transaction = transactionService.getById(id);

      if (transaction.isEmpty()) {
        return "redirect:/expense";
      }

      model.addAttribute("title", "Update expense");
      model.addAttribute("expense", transaction.get());

      List<Business> businesses = businessService.getAll(user);
      model.addAttribute("businesses", businesses);

      List<Balance> balances = balanceService.getAll(user);
      model.addAttribute("balances", balances);
      return "pages/expense/edit_expense";
    } catch (Exception e) {
      return "redirect:/login";
    }
  }

  // Create or Update Expense
  @PostMapping("/expense")
  public String saveExpense(
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

      Transaction expense;
      Balance balance = balanceService.getById(balanceId)
          .orElseThrow(() -> new RuntimeException("Balance not found"));
      Business business = businessService.getById(businessId)
          .orElseThrow(() -> new RuntimeException("Business not found"));

      if (id.isEmpty()) {
        // Creating a new expense
        if (balance.getBalance() < amount) {
          model.addAttribute("error", "Insufficient balance. Available: $" + balance.getBalance());
          model.addAttribute("title", "Add Expense");
          model.addAttribute("businesses", businessService.getAll(user));
          model.addAttribute("balances", balanceService.getAll(user));
          return "pages/expense/edit_expense";
        }

        // Create the expense
        expense = new Transaction();
        expense.setUser(user);
        expense.setBusiness(business);
        expense.setBalance(balance);
        expense.setAmount(amount);
        expense.setNotes(Optional.ofNullable(notes));
        expense.setTransactionType("Expense");

        // Update balance amount
        balance.setBalance(balance.getBalance() - amount);
        balanceService.update(balance);

        transactionService.create(expense);
      } else {
        // Updating an existing expense
        expense = transactionService.getById(id.get())
            .orElseThrow(() -> new RuntimeException("Expense not found"));

        // Calculate the difference in amount
        double amountDifference = amount - expense.getAmount();

        // If the balance is changing, restore old balance and deduct from new
        if (!expense.getBalance().getId().equals(balanceId)) {
          // Restore amount to old balance
          Balance oldBalance = expense.getBalance();
          oldBalance.setBalance(oldBalance.getBalance() + expense.getAmount());
          balanceService.update(oldBalance);

          // Deduct from new balance
          if (balance.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance in new account");
          }
          balance.setBalance(balance.getBalance() - amount);
        } else {
          // Same balance, just update the difference
          if (balance.getBalance() < amountDifference) {
            throw new RuntimeException("Insufficient balance for amount update");
          }
          balance.setBalance(balance.getBalance() - amountDifference);
        }

        balanceService.update(balance);

        // Update expense details
        expense.setBusiness(business);
        expense.setBalance(balance);
        expense.setAmount(amount);
        expense.setNotes(Optional.ofNullable(notes));
        transactionService.update(expense);
      }

      return "redirect:/expense";
    } catch (NotFoundException e) {
      // model.addAttribute("error", e.getMessage());
      // model.addAttribute("title", id.isEmpty() ? "Add Expense" : "Update Expense");
      // model.addAttribute("businesses", businessService.getAll());
      // model.addAttribute("balances", balanceService.getAll(user));
      // return "pages/expense/edit_expense";
      return "redirect:/login";
    }
  }

  // Delete expense
  @GetMapping("/expense/delete/{id}")
  public String deleteExpense(@PathVariable Long id) {
    try {
      // Get the expense before deleting
      Transaction expense = transactionService.getById(id)
          .orElseThrow(() -> new RuntimeException("Expense not found"));

      // Get the associated balance
      Balance balance = expense.getBalance();

      // Restore the amount to the balance
      balance.setBalance(balance.getBalance() + expense.getAmount());

      // Update the balance first
      balanceService.update(balance);

      // Then delete the expense
      transactionService.deleteById(id);

      return "redirect:/expense";
    } catch (RuntimeException e) {
      // You might want to handle the error more gracefully
      // For now, just redirect to expense page
      return "redirect:/expense";
    }
  }

  // Add this method to handle errors if needed
  @ExceptionHandler(RuntimeException.class)
  public String handleError(RuntimeException ex, Model model) {
    model.addAttribute("error", ex.getMessage());
    return "redirect:/expense";
  }
}
