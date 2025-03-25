package com.example.ftms_java_spring_boot.controllers;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.ftms_java_spring_boot.model.Balance;
import com.example.ftms_java_spring_boot.model.Transaction;
import com.example.ftms_java_spring_boot.model.User;
import com.example.ftms_java_spring_boot.service.BalanceService;
import com.example.ftms_java_spring_boot.service.TransactionService;
import com.example.ftms_java_spring_boot.service.UserService;

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

            List<Balance> balances = balanceService.getAll(user);

            Pageable pageableTransactions = PageRequest.of(0, 10);
            Page<Transaction> transactions = transactionService.getAllWithPagination(pageableTransactions, user,
                    Optional.empty());

            // double totalBalance = balances.stream()
            // .mapToDouble(Balance::getBalance)
            // .sum();
            // double totalExpense = transactions.stream()
            // .filter(trans -> trans.getTransactionType().equals("Expense"))
            // .mapToDouble(Transaction::getAmount)
            // .sum();
            // double totalIncome = transactions.stream()
            // .filter(trans -> trans.getTransactionType().equals("Income"))
            // .mapToDouble(Transaction::getAmount)
            // .sum();
            // double totalPortfilio = totalIncome - totalExpense;

            double totalBalance = 0;
            double totalExpense = 0;
            double totalIncome = 0;
            double totalPortfilio = 0;

            List<Double> weeklyIncomes = transactionService.getWeeklyIncomes();

            model.addAttribute("totalBalance", currencyFormat.format(totalBalance));
            model.addAttribute("totalExpense", currencyFormat.format(totalExpense));
            model.addAttribute("totalIncome", currencyFormat.format(totalIncome));
            model.addAttribute("totalPortfilio", currencyFormat.format(totalPortfilio));
            model.addAttribute("weeklyIncomes", weeklyIncomes);
            model.addAttribute("transactions", transactions);

            return "pages/home";
        } catch (NotFoundException e) {
            return "redirect:/login";
        }
    }
}
