package com.example.ftms_java_spring_boot.service;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ftms_java_spring_boot.repository.UserRepository;
import com.example.ftms_java_spring_boot.model.Business;
import com.example.ftms_java_spring_boot.model.User;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  public User addUser(User user) {
    return userRepository.save(user);
  }

  public Optional<User> getById(Long id) {
    return userRepository.findById(id);
  }
}
