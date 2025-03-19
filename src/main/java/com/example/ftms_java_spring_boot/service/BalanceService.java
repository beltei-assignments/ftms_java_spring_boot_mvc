package com.example.ftms_java_spring_boot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ftms_java_spring_boot.model.Balance;
import com.example.ftms_java_spring_boot.model.User;
import com.example.ftms_java_spring_boot.repository.BalanceRepository;

@Service
public class BalanceService {
  @Autowired
  private BalanceRepository balanceRepository;

  public List<Balance> getAll(User user) {
    return balanceRepository.findAllBalances(user);
  }

  public Optional<Balance> getById(Long id) {
    return balanceRepository.findById(id);
  }

  public double getBalance(Long id) {
    return balanceRepository.findById(id)
        .map(Balance::getBalance)
        .orElseThrow(() -> new RuntimeException("Balance not found"));
  }

  public void update(Balance balance) {
    balanceRepository.save(balance);
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

  // public void deleteById(Long id) {
  // Business business = getById(id).get();
  // business.setDisabled(true);
  // update(business);
  // }
}
