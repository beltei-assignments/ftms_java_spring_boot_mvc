package com.example.ftms_java_spring_boot.controllers.api;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.ftms_java_spring_boot.model.Balance;
import com.example.ftms_java_spring_boot.service.BalanceService;

@RestController
@RequestMapping("/api/balances")
public class BalanceControllerAPI {
  @Autowired
  private BalanceService balanceService;

  @GetMapping()
  public List<Balance> balanceHome(Model model) {
    return Collections.emptyList();
    // return balanceService.getAll();
  }
}
