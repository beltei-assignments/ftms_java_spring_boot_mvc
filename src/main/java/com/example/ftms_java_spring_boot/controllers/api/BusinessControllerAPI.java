package com.example.ftms_java_spring_boot.controllers.api;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.ftms_java_spring_boot.model.Business;

import com.example.ftms_java_spring_boot.service.BusinessService;

@RestController
@RequestMapping("/api/businesses")
public class BusinessControllerAPI {
  @Autowired
  private BusinessService businessService;

  @GetMapping
  public List<Business> getAllBusinesses() {
    return Collections.emptyList();
    // return businessService.getAll();
  }
}
