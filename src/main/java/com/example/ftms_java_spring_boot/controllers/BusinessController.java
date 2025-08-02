package com.example.ftms_java_spring_boot.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.ftms_java_spring_boot.model.Business;
import com.example.ftms_java_spring_boot.model.User;
import com.example.ftms_java_spring_boot.service.BusinessService;
import com.example.ftms_java_spring_boot.service.UserService;

import jakarta.servlet.http.HttpSession;
import javassist.NotFoundException;

@Controller
public class BusinessController {
  @Autowired
  private UserService userService;
  private BusinessService businessService;

  public BusinessController(UserService userService, BusinessService businessService) {
    this.userService = userService;
    this.businessService = businessService;
  }

  @GetMapping("/business")
  public String businessHome(
      HttpSession session,
      @RequestParam(defaultValue = "0") int page,
      Model model) {
    try {
      Long userId = (Long) session.getAttribute("userId");
      User user = userService.getById(userId);

      Pageable pageable = PageRequest.of(page, 10);
      Page<Business> businesses = businessService.getAllWithPagination(pageable, user);
      model.addAttribute("businesses", businesses);

      return "pages/business/home_business";
    } catch (NotFoundException e) {
      return "redirect:/login";
    }
  }

  @GetMapping("/business/add")
  public String businessAdd(Model model) {
    model.addAttribute("title", "Add new business");

    return "pages/business/edit_business";
  }

  @GetMapping("/business/{id}")
  public String businessEdit(@PathVariable Long id, Model model) {
    Optional<Business> business = businessService.getById(id);

    if (business.isEmpty()) {
      return "redirect:/business";
    }

    model.addAttribute("title", "Update business");
    model.addAttribute("business", business.get());

    return "pages/business/edit_business";
  }

  // Create or Update business
  @PostMapping("/business")
  public String saveBusiness(
      HttpSession session,
      @RequestParam("name") String name,
      @RequestParam("id") Optional<Long> id) {

    try {
      Long userId = (Long) session.getAttribute("userId");
      User user = userService.getById(userId);

      if (id.isEmpty()) {
        Business newBusiness = new Business(name);
        newBusiness.setUser(user);
        businessService.create(newBusiness);
      } else {
        Business updateBusiness = businessService.getById(id.get()).get();
        updateBusiness.setName(name);
        businessService.update(updateBusiness);
      }

      return "redirect:/business";
    } catch (Exception e) {
      return "redirect:/login";
    }
  }

  // Delete business
  @GetMapping("/business/delete/{id}")
  public String deleteBusiness(@PathVariable Long id) {
    businessService.deleteById(id);

    return "redirect:/business";
  }
}
