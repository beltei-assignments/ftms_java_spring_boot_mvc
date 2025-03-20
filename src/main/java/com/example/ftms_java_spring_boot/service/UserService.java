package com.example.ftms_java_spring_boot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ftms_java_spring_boot.repository.UserRepository;

import javassist.NotFoundException;

import com.example.ftms_java_spring_boot.model.User;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  public User saveUser(User user) {
    return userRepository.save(user);
  }

  public User getById(Long id) throws NotFoundException {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("User not found"));
    return user;
  }

  public User getUserByEmail(String email) throws NotFoundException {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException("User not found"));
    return user;
  }

  public User findUserByCodeReset(String code) throws NotFoundException {
    User user = userRepository.findByCodeReset(code)
        .orElseThrow(() -> new NotFoundException("User not found"));
    return user;
  }
}
