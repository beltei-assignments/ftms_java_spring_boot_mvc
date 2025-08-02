package com.example.ftms_java_spring_boot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ftms_java_spring_boot.model.Balance;
import com.example.ftms_java_spring_boot.model.User;
import com.example.ftms_java_spring_boot.repository.BalanceRepository;

import javassist.NotFoundException;

@Service
public class BalanceService {
  @Autowired
  private BalanceRepository balanceRepository;

  public List<Balance> getAll(User user) {
    return balanceRepository.findAllBalances(user);
  }

  public Optional<Balance> getId(Long id) {
    return balanceRepository.findById(id);
  }

  public Balance getById(Long id) throws NotFoundException {
    Balance balance = balanceRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Balance not found"));

    return balance;
    // return balanceRepository.findById(id);
  }

  public double getTotalBalance(User user) {
    Optional<Double> amount = balanceRepository.getTotalBalance(user);
    return amount.isEmpty() ? 0 : amount.get();
  }

  public double getBalance(Long id) {
    return balanceRepository.findById(id)
        .map(Balance::getBalance)
        .orElseThrow(() -> new RuntimeException("Balance not found"));
  }

  public void save(Balance balance) {
    balanceRepository.save(balance);
  }

  public void deleteById(Long id) throws NotFoundException {
    Balance business = getById(id);
    business.setDisabled(true);

    save(business);
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
