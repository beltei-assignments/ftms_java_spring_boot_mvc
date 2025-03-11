package com.example.ftms_java_spring_boot.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.ftms_java_spring_boot.model.Business;

import com.example.ftms_java_spring_boot.service.BusinessService;

@Controller
public class BusinessController {
  @Autowired
  private BusinessService businessService;

  public BusinessController(BusinessService businessService) {
    this.businessService = businessService;
  }

  @GetMapping("/business")
  public String businessHome(Model model) {
    List<Business> businesses = businessService.getAll();
    model.addAttribute("businesses", businesses);
    return "pages/business/home_business";
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
      @RequestParam("name") String name,
      @RequestParam("id") Optional<Long> id) {

    if (id.isEmpty()) {
      Business newBusiness = new Business(name);
      businessService.create(newBusiness);
    } else {
      Business updateBusiness = businessService.getById(id.get()).get();
      updateBusiness.setName(name);
      businessService.update(updateBusiness);
    }

    return "redirect:/business";
  }

  // Delete business
  @GetMapping("/business/delete/{id}")
  public String deleteBusiness(@PathVariable Long id) {
    businessService.deleteById(id);
    return "redirect:/business";
  }
}
