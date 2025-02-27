package com.example.ftms_java_spring_boot.controllers;

import com.example.ftms_java_spring_boot.model.User;
import com.example.ftms_java_spring_boot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
  // TODO
  @Autowired
  private UserService userService;
}
