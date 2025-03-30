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
import com.example.ftms_java_spring_boot.service.BusinessService;
import com.example.ftms_java_spring_boot.service.TransactionService;
import com.example.ftms_java_spring_boot.service.UserService;

import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpSession;
import javassist.NotFoundException;

@Controller
public class TransactionController {
  private final UserService userService;
  private final BusinessService businessService;
  private final BalanceService balanceService;
  private final TransactionService transactionService;

  public TransactionController(UserService userService, BusinessService businessService,
      TransactionService transactionService, BalanceService balanceService) {
    this.userService = userService;
    this.transactionService = transactionService;
    this.businessService = businessService;
    this.balanceService = balanceService;
  }

  @GetMapping("/transaction")
  public String transactionHome(
      HttpSession session,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(required = false) Optional<Long> businessId,
      @RequestParam(required = false) Optional<Long> balanceId,
      Model model) throws NotFoundException, Exception {
    try {
      Pageable pageable = PageRequest.of(page, 10);

      Long userId = (Long) session.getAttribute("userId");
      User user = userService.getById(userId);

      Specification<Transaction> filters = (root, query, criteriaBuilder) -> {
        List<Predicate> predicates = new ArrayList<>();

        // Filter user scope
        predicates.add(criteriaBuilder.equal(root.get("user"), user));

        // Filter business
        if (!businessId.isEmpty() || session.getAttribute("businessIdTransaction") != null) {
          Long businessIdFrom = businessId.isEmpty() ? (Long) session.getAttribute("businessIdTransaction")
              : businessId.get();
          Optional<Business> business = businessService.getById(businessIdFrom);

          if (!business.isEmpty()) {
            predicates.add(
                criteriaBuilder.equal(root.get("business"), business.get()));
            session.setAttribute("businessIdTransaction", businessIdFrom);
          }
        }

        // Filter balance
        if (!balanceId.isEmpty() || session.getAttribute("balanceIdTransaction") != null) {
          Long balanceIdFrom = balanceId.isEmpty() ? (Long) session.getAttribute("balanceIdTransaction")
              : balanceId.get();
          Optional<Balance> balance = balanceService.getId(balanceIdFrom);

          if (!balance.isEmpty()) {
            predicates.add(
                criteriaBuilder.equal(root.get("balance"), balance.get()));
            session.setAttribute("balanceIdTransaction", balanceIdFrom);
          }
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
      };

      Page<Transaction> transactions = transactionService.getAllWithPagination(
          pageable,
          filters);
      List<Business> businesses = businessService.getAll(user);
      List<Balance> balances = balanceService.getAll(user);
      model.addAttribute("businesses", businesses);
      model.addAttribute("balances", balances);
      model.addAttribute("transactions", transactions);

      return "pages/transaction/home_transaction";
    } catch (Exception e) {
      throw e;
      // return "redirect:/";
    }
  }

  @GetMapping("/transaction/clear-filters")
  public String clearFilters(HttpSession session) {
    session.removeAttribute("businessIdTransaction");
    session.removeAttribute("balanceIdTransaction");

    return "redirect:/transaction";
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
      return "redirect:/login";
    }
  }
}
