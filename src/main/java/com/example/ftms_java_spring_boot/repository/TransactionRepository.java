package com.example.ftms_java_spring_boot.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ftms_java_spring_boot.model.Transaction;
import com.example.ftms_java_spring_boot.model.User;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    @Query("SELECT t FROM Transaction t WHERE user = :user ORDER BY createdAt DESC")
    Page<Transaction> findAllTransactionsPaginate(@Param("user") User user, Pageable pageable);

    @Query("SELECT t FROM Transaction t WHERE user = :user ORDER BY createdAt DESC")
    List<Transaction> findAllTransactions(@Param("user") User user);

    @Query("SELECT e FROM Transaction e WHERE e.user = :user AND e.transactionType = 'Expense' AND e.disabled = false ORDER BY createdAt DESC")
    List<Transaction> findAllExpenses(@Param("user") User user);

    @Query("SELECT e FROM Transaction e WHERE e.user = :user AND e.transactionType = 'Income' AND e.disabled = false ORDER BY createdAt DESC")
    List<Transaction> findAllIncomes(@Param("user") User user);

    @Query("SELECT FUNCTION('DAYNAME', t.createdAt), SUM(t.amount) FROM Transaction t " +
            "WHERE t.createdAt BETWEEN :startDate AND :endDate " +
            "GROUP BY FUNCTION('DAYNAME', t.createdAt) " +
            "ORDER BY MIN(t.createdAt)")
    List<Object[]> getWeeklyIncomes(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // @Query("SELECT SUM(t.amount) FROM Transaction t " +
    //         "INNER JOIN Business b ON b.id = t.business_id " +
    //         "WHERE t.transaction_type = 'Expense' " +
    //         "GROUP BY t.business_id")
    @Query("SELECT t.business.name AS name, SUM(t.amount) AS totalAmount FROM Transaction t " +
            "WHERE t.transactionType = 'Expense' " +
            "GROUP BY t.business.id")
    List<Object[]> getTransactionBusinesses();
}
