package com.example.ftms_java_spring_boot.controllers.api;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.ftms_java_spring_boot.model.User;
import com.example.ftms_java_spring_boot.service.TransactionService;
import com.example.ftms_java_spring_boot.service.UserService;
import jakarta.servlet.http.HttpSession;
import javassist.NotFoundException;

@RestController
@RequestMapping("/api/transaction")
public class TransactionControllerAPI {
    private UserService userService;
    private TransactionService transactionService;

    public TransactionControllerAPI(UserService userService, TransactionService transactionService) {
        this.userService = userService;
        this.transactionService = transactionService;
    }

    @GetMapping("/weekly")
    public ResponseEntity<Object> balanceHome(
            @RequestParam String date,
            HttpSession session,
            Model model) {
        try {
            Long userId = (Long) session.getAttribute("userId");
            User user = userService.getById(userId);
            LocalDate parsedDate = LocalDate.parse(date);
            LocalDateTime dateTime = parsedDate.atStartOfDay();

            List<Double> weeklyIncomes = transactionService.getWeeklyIncomes(user,
                    dateTime);

            return ResponseEntity.ok(weeklyIncomes);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }
    }
}
