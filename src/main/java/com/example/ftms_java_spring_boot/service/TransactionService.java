package com.example.ftms_java_spring_boot.service;

import jakarta.persistence.criteria.Predicate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

  public Page<Transaction> getAllWithPagination(
      Pageable pageable,
      User user,
      Optional<String> type) {
    // return transactionRepository.findAllTransactionsPaginate(user, pageable);

    Specification<Transaction> spec = (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      // Filter by User

      predicates.add(criteriaBuilder.equal(root.get("user"), user));
      predicates.add(criteriaBuilder.equal(root.get("disabled"), false));

      // Filter transaction type
      if (!type.isEmpty()) {
        predicates.add(
            criteriaBuilder.equal(root.get("transactionType"), type.get()));
      }

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };

    return transactionRepository.findAll(spec, pageable);
  }

  public List<Transaction> getUserExpenses(User user) {
    return transactionRepository.findAllExpenses(user);
  }

  public List<Transaction> getUserIncomes(User user) {
    return transactionRepository.findAllIncomes(user);
  }

  public List<Double> getWeeklyIncomes() {
    LocalDateTime now = LocalDateTime.now();
    LocalDateTime startDate = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).with(LocalTime.MIN);
    LocalDateTime endDate = now.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).with(LocalTime.MAX);

    List<Object[]> results = transactionRepository.getWeeklyIncomes(startDate, endDate);

    // Map to store transaction amounts by day name
    Map<String, Double> transactionMap = new HashMap<>();
    for (Object[] result : results) {
      String fullDayName = (String) result[0];
      transactionMap.put(fullDayName, (Double) result[1]);
    }

    // List to hold ordered amounts (Mon-Sun)
    List<Double> amounts = new ArrayList<>();
    for (DayOfWeek day : DayOfWeek.values()) {
      String fullDayName = day.name().charAt(0) + day.name().substring(1).toLowerCase(); // Convert to "Monday"
      amounts.add(transactionMap.getOrDefault(fullDayName, 0.0)); // Default to 0 if no data
    }
    return amounts;
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
