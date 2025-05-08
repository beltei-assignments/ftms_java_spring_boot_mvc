package com.example.ftms_java_spring_boot.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.ftms_java_spring_boot.service.BusinessService;
import com.example.ftms_java_spring_boot.service.BalanceService;
import com.example.ftms_java_spring_boot.service.UserService;

import jakarta.persistence.criteria.Predicate;
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
  public String expenseHome(
      HttpSession session,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(required = false) Optional<Long> businessId,
      @RequestParam(required = false) Optional<Long> balanceId,
      Model model) {
    try {
      Pageable pageable = PageRequest.of(page, 10);
      Long userId = (Long) session.getAttribute("userId");
      User user = userService.getById(userId);
      Specification<Transaction> filters = (root, query, criteriaBuilder) -> {
        List<Predicate> predicates = new ArrayList<>();

        // Filter user scope
        predicates.add(criteriaBuilder.equal(root.get("user"), user));

        // Filter transaction type
        predicates.add(criteriaBuilder.equal(root.get("transactionType"), "Expense"));

        // Filter business
        if (!businessId.isEmpty() || session.getAttribute("businessIdExpense") != null) {
          Long businessIdFrom = businessId.isEmpty() ? (Long) session.getAttribute("businessIdExpense")
              : businessId.get();
          Optional<Business> business = businessService.getById(businessIdFrom);

          if (!business.isEmpty()) {
            predicates.add(
                criteriaBuilder.equal(root.get("business"), business.get()));
            session.setAttribute("businessIdExpense", businessIdFrom);
          }
        }

        // Filter balance
        if (!balanceId.isEmpty() || session.getAttribute("balanceIdExpense") != null) {
          Long balanceIdFrom = balanceId.isEmpty() ? (Long) session.getAttribute("balanceIdExpense")
              : balanceId.get();
          Optional<Balance> balance = balanceService.getId(balanceIdFrom);

          if (!balance.isEmpty()) {
            predicates.add(
                criteriaBuilder.equal(root.get("balance"), balance.get()));
            session.setAttribute("balanceIdExpense", balanceIdFrom);
          }
        }

        // Apply ORDER BY id DESC
        query.orderBy(criteriaBuilder.desc(root.get("id")));

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
      };

      Page<Transaction> expenses = transactionService.getAllWithPagination(pageable, filters);
      List<Business> businesses = businessService.getAll(user);
      List<Balance> balances = balanceService.getAll(user);
      model.addAttribute("businesses", businesses);
      model.addAttribute("balances", balances);
      model.addAttribute("expenses", expenses);

      return "pages/expense/home_expense";
    } catch (Exception e) {
      return "redirect:/login";
    }
  }

  @GetMapping("/expense/clear-filters")
  public String clearFilters(HttpSession session) {
    session.removeAttribute("businessIdExpense");
    session.removeAttribute("balanceIdExpense");

    return "redirect:/expense";
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
      Balance balance = balanceService.getById(balanceId);
      Business business = businessService.getById(businessId)
          .orElseThrow(() -> new RuntimeException("Business not found"));

      if (id.isEmpty()) {
        // Creating a new expense
        if (balance.getBalance() < amount) {
          model.addAttribute("error", "Insufficient balance. Available only: " + balance.getBalanceFormatted());
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
        balanceService.save(balance);

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
          balanceService.save(oldBalance);

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

        balanceService.save(balance);

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
      balanceService.save(balance);

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
