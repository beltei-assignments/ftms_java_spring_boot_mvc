package com.example.ftms_java_spring_boot.model;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  @Column(nullable = false)
  private String password;

  @Column(nullable = true, unique = true, length = 6)
  private String codeReset;

  @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
  private boolean disabled = false;

  // Constructors
  public User() {
  }

  public User(String email, String fName, String lName, String password) {
    this.email = email;
    this.firstName = fName;
    this.lastName = lName;
    this.password = password;
  }

  // Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void hashPassword(String password) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    this.password = encoder.encode(password); // Hash the password
  }

  public boolean verifyPassword(String rawPassword) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    return encoder.matches(rawPassword, this.password);
  }

  public String getCodeReset() {
    return codeReset;
  }

  public void setCodeReset() {
    String randomCode = ThreadLocalRandom.current()
        .ints(1, 10)
        .distinct()
        .limit(6)
        .mapToObj(String::valueOf)
        .collect(Collectors.joining());
    this.codeReset = randomCode;
  }

  public void clearCodeReset() {
    this.codeReset = "";
  }
}
