package com.example.ftms_java_spring_boot.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ftms_java_spring_boot.model.Balance;
import com.example.ftms_java_spring_boot.model.User;
import com.example.ftms_java_spring_boot.repository.BalanceRepository;
import com.example.ftms_java_spring_boot.repository.UserRepository;

@Service
public class BalanceService {
  @Autowired
  private BalanceRepository balanceRepository;
  private UserRepository userRepository;

  public BalanceService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<Balance> getAll() {
    Optional<User> user = userRepository.findById(1L);
    // If user is not found return []
    if (user.isEmpty()) {
      return Collections.emptyList();
    }

    return balanceRepository.findAllBalances(user.get());
  }

  // public Optional<Business> getById(Long id) {
  // return businessRepository.findById(id);
  // }

  // public boolean create(Business business) {
  // Optional<User> user = userRepository.findById(1L);
  // if (user.isEmpty()) {
  // return false;
  // }

  // business.setUser(user.get());
  // businessRepository.save(business);

  // return true;
  // }

  // public void update(Business business) {
  // businessRepository.save(business);
  // }

  // public void deleteById(Long id) {
  // Business business = getById(id).get();
  // business.setDisabled(true);
  // update(business);
  // }
}
