package com.example.ftms_java_spring_boot.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.ftms_java_spring_boot.model.Business;
import com.example.ftms_java_spring_boot.model.Transaction;
import com.example.ftms_java_spring_boot.service.BusinessService;
import com.example.ftms_java_spring_boot.service.TransactionService;

@Controller
public class TransactionController {

  private final BusinessService businessService;
  private final TransactionService transactionService;

  @Autowired
  public TransactionController(BusinessService businessService, TransactionService transactionService) {
    this.transactionService = transactionService;
      this.businessService = businessService;
  }

  // TODO
  @GetMapping("/transaction")
  public String transactionHome(Model model) {
    List<Transaction> transactions = transactionService.getAll();
    model.addAttribute("transactions", transactions);
    return "pages/transaction/home_transaction";
  }

  @GetMapping("/transaction/add")
  public String transactionAdd(Model model) {
    model.addAttribute("title", "Add new transaction");

    List<Business> businesses = businessService.getAll();
    model.addAttribute("businesses", businesses);

    return "pages/transaction/edit_transaction";
  }
}
