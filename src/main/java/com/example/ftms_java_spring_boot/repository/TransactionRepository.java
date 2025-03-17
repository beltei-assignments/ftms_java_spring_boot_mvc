package com.example.ftms_java_spring_boot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ftms_java_spring_boot.model.Transaction;
import com.example.ftms_java_spring_boot.model.User;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  // Custom query with @Query to filter by user
  @Query("SELECT t FROM Transaction t WHERE user = :user")
  List<Transaction> findAllTransactions(@Param("user") User user);

  // Custom query with @Query to filter by user to get all expenses
  @Query("SELECT e FROM Transaction e WHERE e.user = :user AND e.transactionType = 'Expense' AND e.disabled = false")
  List<Transaction> findAllExpenses(@Param("user") User user);
}
