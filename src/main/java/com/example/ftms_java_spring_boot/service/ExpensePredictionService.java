package com.example.ftms_java_spring_boot.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ftms_java_spring_boot.dtos.ExpensePrediction;
import com.example.ftms_java_spring_boot.dtos.ExpensePredictionResponse;
import com.example.ftms_java_spring_boot.dtos.ExpenseRecommendation;
import com.example.ftms_java_spring_boot.model.Business;
import com.example.ftms_java_spring_boot.model.Transaction;
import com.example.ftms_java_spring_boot.repository.BalanceRepository;
import com.example.ftms_java_spring_boot.repository.BusinessRepository;
import com.example.ftms_java_spring_boot.repository.TransactionRepository;
import com.example.ftms_java_spring_boot.service.ai.BestFirstSpendingPlanner;
import com.example.ftms_java_spring_boot.service.ai.GreedyExpenseAnalyzer;

import javassist.NotFoundException;

@Service
public class ExpensePredictionService {
	@Autowired
	private BusinessRepository businessRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private BalanceRepository balanceRepository;

	public ExpensePredictionResponse predictExpenses(Long businessId) throws NotFoundException {
		// Get business info
		Business business = businessRepository.findById(businessId)
				.orElseThrow(() -> new NotFoundException("Business not found"));

		// Get recent transactions (last 3 months)
		LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
		List<Transaction> recentTransactions = transactionRepository
				.findByBusinessIdAndCreatedAtAfterOrderByCreatedAtDesc(businessId, threeMonthsAgo);

		// Get current balance
		Double currentBalance = balanceRepository.findTotalBalanceByUserId(business.getUser());
		if (currentBalance == null)
			currentBalance = 0.0;

		// AI Algorithm 1: Greedy Analysis for predictions
		List<ExpensePrediction> predictions = GreedyExpenseAnalyzer.predictExpenses(recentTransactions);

		// AI Algorithm 2: Best-First Search for recommendations
		List<ExpenseRecommendation> recommendations = BestFirstSpendingPlanner
				.createSpendingPlan(predictions, currentBalance);

		// Simple risk assessment
		String riskLevel = assessRisk(currentBalance, predictions);

		// Build response
		ExpensePredictionResponse response = new ExpensePredictionResponse();
		response.setBusinessId(businessId);
		response.setBusinessName(business.getName());
		response.setCurrentBalance(currentBalance);
		response.setPredictions(predictions);
		response.setRecommendations(recommendations);
		response.setRiskLevel(riskLevel);

		return response;
	}

	private String assessRisk(Double balance, List<ExpensePrediction> predictions) {
		Double totalPredicted = predictions.stream()
				.mapToDouble(ExpensePrediction::getPredictedAmount)
				.sum();

		Double ratio = totalPredicted / balance;

		if (ratio > 0.8)
			return "HIGH";
		else if (ratio > 0.5)
			return "MEDIUM";
		else
			return "LOW";
	}
}
