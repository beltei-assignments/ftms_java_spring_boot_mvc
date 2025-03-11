package com.example.ftms_java_spring_boot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ftms_java_spring_boot.model.Business;
import com.example.ftms_java_spring_boot.model.User;

public interface BusinessRepository extends JpaRepository<Business, Long> {
  // Custom query with @Query to filter by user
  @Query("SELECT b FROM Business b WHERE b.disabled = false AND user = :user")
  List<Business> findAllBusinesses(@Param("user") User user);
}
