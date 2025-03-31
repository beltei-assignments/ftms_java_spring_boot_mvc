package com.example.ftms_java_spring_boot.controllers.api;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.ftms_java_spring_boot.model.Balance;
import com.example.ftms_java_spring_boot.model.User;
import com.example.ftms_java_spring_boot.service.BalanceService;
import com.example.ftms_java_spring_boot.service.UserService;

import jakarta.servlet.http.HttpSession;
import javassist.NotFoundException;

@RestController
@RequestMapping("/api/balances")
public class BalanceControllerAPI {
  @Autowired
  private BalanceService balanceService;
  private UserService userService;

  public BalanceControllerAPI(BalanceService balanceService, UserService userService) {
    this.balanceService = balanceService;
    this.userService = userService;
  }

  @GetMapping()
  public ResponseEntity<Object> balanceHome(HttpSession session, Model model) {
    try {
      Long userId = (Long) session.getAttribute("userId");
      User user = userService.getById(userId);

      return ResponseEntity.ok(user);
    } catch (NotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body("User not found");
    }
  }
}
