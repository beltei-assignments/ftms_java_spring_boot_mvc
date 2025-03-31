package com.example.ftms_java_spring_boot.service;

import jakarta.persistence.criteria.Predicate;

import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.ftms_java_spring_boot.model.Business;
import com.example.ftms_java_spring_boot.model.Transaction;
import com.example.ftms_java_spring_boot.model.User;
import com.example.ftms_java_spring_boot.repository.TransactionRepository;

@Service
public class TransactionService {
  @Autowired
  private TransactionRepository transactionRepository;
  static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

  public List<Transaction> getAll(User user) {
    return transactionRepository.findAllTransactions(user);
  }

  public Page<Transaction> getAllWithPagination(
      Pageable pageable,
      Specification<Transaction> filters) {

    Specification<Transaction> defaultFilters = (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      // Filter by delete records
      predicates.add(criteriaBuilder.equal(root.get("disabled"), false));

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };

    Specification<Transaction> combinedFilters = Specification.where(defaultFilters).and(filters);

    return transactionRepository.findAll(combinedFilters, pageable);
  }

  public List<Transaction> getUserExpenses(User user) {
    return transactionRepository.findAllExpenses(user);
  }

  public List<Transaction> getUserIncomes(User user) {
    return transactionRepository.findAllIncomes(user);
  }

  public List<Double> getWeeklyIncomes(User user, LocalDateTime date) {
    // LocalDateTime now = LocalDateTime.now();
    LocalDateTime startDate = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).with(LocalTime.MIN);
    LocalDateTime endDate = date.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).with(LocalTime.MAX);

    List<Object[]> results = transactionRepository.getWeeklyIncomes(user, startDate, endDate);

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

  public List<Map<String, Object>> getTransactionBusinesses(User user, String type) {
    List<Object[]> transactions = transactionRepository.getTransactionBusinesses(user, type);
    List<Map<String, Object>> data = new ArrayList<>();

    for (Object[] transaction : transactions) {
      Map<String, Object> newTransaction = new HashMap<>();
      newTransaction.put("business", transaction[0]);
      newTransaction.put("totalAmount", transaction[1]);
      newTransaction.put("totalAmountFormatted", currencyFormat.format(transaction[1]));

      data.add(newTransaction);
    }

    return data;
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
