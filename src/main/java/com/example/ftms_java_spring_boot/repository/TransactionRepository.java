package com.example.ftms_java_spring_boot.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ftms_java_spring_boot.model.Transaction;
import com.example.ftms_java_spring_boot.model.User;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
	@Query("SELECT t FROM Transaction t WHERE user = :user ORDER BY createdAt DESC")
	List<Transaction> findAllTransactions(@Param("user") User user);

	@Query("SELECT e FROM Transaction e WHERE e.user = :user AND e.transactionType = 'Expense' AND e.disabled = false ORDER BY createdAt DESC")
	List<Transaction> findAllExpenses(@Param("user") User user);

	@Query("SELECT e FROM Transaction e WHERE e.user = :user AND e.transactionType = 'Income' AND e.disabled = false ORDER BY createdAt DESC")
	List<Transaction> findAllIncomes(@Param("user") User user);

	// Get weekly incomes
	@Query("SELECT FUNCTION('DAYNAME', t.createdAt), SUM(t.amount) FROM Transaction t " +
			"WHERE t.createdAt BETWEEN :startDate AND :endDate " +
			"AND t.user = :user " +
			"AND t.disabled = false " +
			"GROUP BY FUNCTION('DAYNAME', t.createdAt) " +
			"ORDER BY MIN(t.createdAt)")
	List<Object[]> getWeeklyIncomes(
			@Param("user") User user,
			@Param("startDate") LocalDateTime startDate,
			@Param("endDate") LocalDateTime endDate);

	// Get expenses or incomes for businesses
	@Query("SELECT t.business.name AS name, SUM(t.amount) AS totalAmount FROM Transaction t " +
			"WHERE t.transactionType = :type " +
			"AND t.user = :user " +
			"AND t.disabled = false " +
			"GROUP BY t.business.id")
	List<Object[]> getTransactionBusinesses(
			@Param("user") User user,
			@Param("type") String type);

	List<Transaction> findByBusinessIdAndCreatedAtAfterOrderByCreatedAtDesc(
			Long businessId, LocalDateTime after);

	// List<Transaction> findByBusinessIdAndCreatedAtAfterAndDisabledFalseOrderByCreatedAtDesc(
	// 		Long businessId, LocalDateTime after);

	// List<Transaction> findByBusinessIdAndTransactionTypeAndDisabledFalseOrderByCreatedAtDesc(
	// 		Long businessId, String transactionType);
}
