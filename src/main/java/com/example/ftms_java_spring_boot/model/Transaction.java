package com.example.ftms_java_spring_boot.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;

@Entity
@Table(name = "transactions")
public class Transaction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String transactionType;

  @Column(nullable = false)
  private double amount = 0;

  @Column(nullable = true)
  private String notes;

  @ManyToOne
  @JoinColumn(name = "business_id", nullable = false)
  private Business business;

  @ManyToOne
  @JoinColumn(name = "balance_id", nullable = false)
  private Balance balance;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
  private boolean disabled = false;

  @Column(nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  @CreationTimestamp
  private LocalDateTime createdAt;

  // Constructors
  public Transaction() {
  }

  public Transaction(String transactionType, double amount, Business business, Optional<String> notes) {
    this.transactionType = transactionType;
    this.amount = amount;
    this.business = business;
    this.notes = notes.orElse(null);
  }

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTransactionType() {
    return transactionType;
  }

  public String getNotes() {
    if (notes.isEmpty()) return "";

    return notes;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public void setNotes(Optional<String> notes) {
    this.notes = notes.orElse(null);
  }

  public String getCreatedAt() {
    return createdAt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public void setBalance(Balance balance) {
    this.balance = balance;
  }

  public Balance getBalance() {
    return balance;
  }

  public Business getBusiness() {
    return business;
  }

  public void setBusiness(Business business) {
    this.business = business;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
  }

  public void setTransactionType(String type) {
    this.transactionType = type;
    // throw new UnsupportedOperationException("Unimplemented method
    // 'setTransactionType'");
  }
}
