package com.example.ftms_java_spring_boot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ftms_java_spring_boot.model.Balance;
import com.example.ftms_java_spring_boot.model.User;

public interface BalanceRepository extends JpaRepository<Balance, Long> {
  // Custom query with @Query to filter by user
  @Query("SELECT b FROM Balance b WHERE b.disabled = false AND user = :user")
  List<Balance> findAllBalances(@Param("user") User user);
}
