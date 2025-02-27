package com.example.ftms_java_spring_boot.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BussinessController {
  // TODO
  @GetMapping("/bussiness")
  public String bussinessHome(Model model) {
    return "pages/bussiness";
  }
}
