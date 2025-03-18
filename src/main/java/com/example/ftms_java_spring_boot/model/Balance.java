package com.example.ftms_java_spring_boot.model;

import java.text.NumberFormat;
import java.util.Locale;

import jakarta.persistence.*;

@Entity
@Table(name = "balances")
public class Balance {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private double balance = 0.0;

  // https://chatgpt.com/share/67cae71f-a268-800b-8481-c0c846950dd9
  // Many-to-One with User
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
  private boolean disabled = false;

  // Constructors
  public Balance() {
  }

  public Balance(String name, double balance) {
    this.name = name;
    this.balance = balance;
  }

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getBalance() {
    return balance;
  }

  public String getBalanceFormatted() {
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
    return currencyFormat.format(balance);
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }
}
