package com.example.ftms_java_spring_boot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class TransactionController {
  // TODO
  @GetMapping("/transaction")
  public String transactionHome(Model model) {
    return "pages/transaction";
  }
}
