package com.example.ftms_java_spring_boot.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ftms_java_spring_boot.model.Business;
import com.example.ftms_java_spring_boot.model.User;
import com.example.ftms_java_spring_boot.repository.BusinessRepository;
import com.example.ftms_java_spring_boot.repository.UserRepository;

@Service
public class BusinessService {
  @Autowired
  private BusinessRepository businessRepository;
  private UserRepository userRepository;

  public BusinessService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<Business> getAll() {
    Optional<User> user = userRepository.findById(1L);
    // If user is not found return []
    if (user.isEmpty()) {
      return Collections.emptyList();
    }

    return businessRepository.findAllBusinesses(user.get());
  }

  public Optional<Business> getById(Long id) {
    return businessRepository.findById(id);
  }

  public boolean create(Business business) {
    Optional<User> user = userRepository.findById(1L);
    if (user.isEmpty()) {
      return false;
    }

    business.setUser(user.get());
    businessRepository.save(business);

    return true;
  }

  public void update(Business business) {
    businessRepository.save(business);
  }

  public void deleteById(Long id) {
    Business business = getById(id).get();
    business.setDisabled(true);
    update(business);
  }
}
