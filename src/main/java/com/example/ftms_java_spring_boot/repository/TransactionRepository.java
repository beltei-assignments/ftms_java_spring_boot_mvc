package com.example.ftms_java_spring_boot.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ftms_java_spring_boot.model.Transaction;
import com.example.ftms_java_spring_boot.model.User;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
  // Custom query with @Query to filter by user
  @Query("SELECT t FROM Transaction t WHERE user = :user ORDER BY createdAt DESC")
  List<Transaction> findAllTransactions(@Param("user") User user);

  @Query("SELECT t FROM Transaction t WHERE user = :user ORDER BY createdAt DESC")
  Page<Transaction> findAllTransactionsPaginate(@Param("user") User user, Pageable pageable);

  // Custom query with @Query to filter by user to get all expenses
  @Query("SELECT e FROM Transaction e WHERE e.user = :user AND e.transactionType = 'Expense' AND e.disabled = false ORDER BY createdAt DESC")
  List<Transaction> findAllExpenses(@Param("user") User user);

  // Custom query with @Query to filter by user to get all expenses
  @Query("SELECT e FROM Transaction e WHERE e.user = :user AND e.transactionType = 'Income' AND e.disabled = false ORDER BY createdAt DESC")
  List<Transaction> findAllIncomes(@Param("user") User user);
}
