package com.example.ftms_java_spring_boot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.ftms_java_spring_boot.model.Transaction;
import com.example.ftms_java_spring_boot.model.User;
import com.example.ftms_java_spring_boot.repository.TransactionRepository;

@Service
public class TransactionService {
  @Autowired
  private TransactionRepository transactionRepository;

  public List<Transaction> getAll(User user) {
    return transactionRepository.findAllTransactions(user);
  }

  public Page<Transaction> getAllPaginage(User user, Pageable pageable) {
    return transactionRepository.findAllTransactionsPaginate(user, pageable);
  }

  public List<Transaction> getUserExpenses(User user) {
    return transactionRepository.findAllExpenses(user);
  }

  public List<Transaction> getUserIncomes(User user) {
    return transactionRepository.findAllIncomes(user);
  }

  public Optional<Transaction> getById(Long id) {
    return transactionRepository.findById(id);
  }

  public boolean create(Transaction transaction) {
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
