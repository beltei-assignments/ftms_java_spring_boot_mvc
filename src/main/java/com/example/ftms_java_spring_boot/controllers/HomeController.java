package com.example.ftms_java_spring_boot.controllers;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.ftms_java_spring_boot.model.Balance;
import com.example.ftms_java_spring_boot.model.Transaction;
import com.example.ftms_java_spring_boot.model.User;
import com.example.ftms_java_spring_boot.service.BalanceService;
import com.example.ftms_java_spring_boot.service.TransactionService;
import com.example.ftms_java_spring_boot.service.UserService;

import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpSession;
import javassist.NotFoundException;

@Controller
public class HomeController {
    private UserService userService;
    private BalanceService balanceService;
    private TransactionService transactionService;
    static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

    public HomeController(UserService userService, BalanceService balanceService,
            TransactionService transactionService) {
        this.userService = userService;
        this.balanceService = balanceService;
        this.transactionService = transactionService;
    }

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            User user = userService.getById(userId);

            List<Map<String, Object>> expenses = transactionService.getTransactionBusinesses(user, "Expense");
            List<Map<String, Object>> incomes = transactionService.getTransactionBusinesses(user, "Income");
            double totalBalance = balanceService.getTotalBalance(user);
            double totalExpense = expenses.stream().mapToDouble(map -> ((Number) map.get("totalAmount")).doubleValue())
                    .sum();
            double totalIncome = incomes.stream().mapToDouble(map -> ((Number) map.get("totalAmount")).doubleValue())
                    .sum();
            double totalPortfilio = totalIncome - totalExpense;
            LocalDateTime now = LocalDateTime.now();
            List<Double> weeklyIncomes = transactionService.getWeeklyIncomes(user, now);

            Pageable transactionPageable = PageRequest.of(0, 5);
            Specification<Transaction> transactionfilters = (root, query, criteriaBuilder) -> {
                List<Predicate> predicates = new ArrayList<>();

                // Filter user scope
                predicates.add(criteriaBuilder.equal(root.get("user"), user));

                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            };
            Page<Transaction> transactions = transactionService.getAllWithPagination(transactionPageable,
                    transactionfilters);

            model.addAttribute("totalBalance", currencyFormat.format(totalBalance));
            model.addAttribute("totalExpense", currencyFormat.format(totalExpense));
            model.addAttribute("totalIncome", currencyFormat.format(totalIncome));
            model.addAttribute("totalPortfilio", currencyFormat.format(totalPortfilio));
            model.addAttribute("weeklyIncomes", weeklyIncomes);
            model.addAttribute("transactions", transactions);
            model.addAttribute("expenses", expenses);
            model.addAttribute("incomes", incomes);

            return "pages/home";
        } catch (NotFoundException e) {
            return "redirect:/login";
        }
    }
}
