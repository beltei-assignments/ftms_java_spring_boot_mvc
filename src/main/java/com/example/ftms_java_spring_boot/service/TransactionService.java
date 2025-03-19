package com.example.ftms_java_spring_boot.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ftms_java_spring_boot.model.Transaction;
import com.example.ftms_java_spring_boot.model.User;
import com.example.ftms_java_spring_boot.repository.TransactionRepository;
import com.example.ftms_java_spring_boot.repository.UserRepository;

@Service
public class TransactionService {
  @Autowired
  private TransactionRepository transactionRepository;
  private UserRepository userRepository;

  public TransactionService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public List<Transaction> getAll() {
    // Imprement User authentication later
    Optional<User> user = userRepository.findById(1L);
    // If user is not found return []
    if (user.isEmpty()) {
      return Collections.emptyList();
    }

    return transactionRepository.findAllTransactions(user.get());
  }

  public List<Transaction> getUserExpenses() {
    // Imprement User authentication later
    Optional<User> user = userRepository.findById(1L);
    // If user is not found return []
    if (user.isEmpty()) {
      return Collections.emptyList();
    }

    return transactionRepository.findAllExpenses(user.get());
  }

  public List<Transaction> getUserIncomes() {
    // Imprement User authentication later
    Optional<User> user = userRepository.findById(1L);
    // If user is not found return []
    if (user.isEmpty()) {
      return Collections.emptyList();
    }

    return transactionRepository.findAllIncomes(user.get());
  }

  public Optional<Transaction> getById(Long id) {
    return transactionRepository.findById(id);
  }

  public boolean create(Transaction transaction) {
    Optional<User> user = userRepository.findById(1L);
    if (user.isEmpty()) {
      return false;
    }

    transaction.setUser(user.get());
    transactionRepository.save(transaction);

    return true;
  }

  public void update(Transaction transaction) {
    transactionRepository.save(transaction);
  }

  public void deleteById(Long id) {
    Transaction transaction = getById(id).get();
    transaction.setDisabled(true);
    update(transaction);
  }
}
