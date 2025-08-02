package com.example.ftms_java_spring_boot.service.ai;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.ftms_java_spring_boot.dtos.ExpensePrediction;
import com.example.ftms_java_spring_boot.model.Transaction;

public class GreedyExpenseAnalyzer {

	public static class ExpenseCategory {
		private String name;
		private Double totalAmount;
		private Integer frequency;
		private Double avgAmount;
		private Double score;
		private List<LocalDateTime> transactionDates;

		public ExpenseCategory(String name) {
			this.name = name;
			this.totalAmount = 0.0;
			this.frequency = 0;
			this.transactionDates = new ArrayList<>();
		}

		public void addExpense(Double amount, LocalDateTime date) {
			this.totalAmount += amount;
			this.frequency++;
			this.transactionDates.add(date);
			this.avgAmount = this.totalAmount / this.frequency;
			// Greedy scoring: prioritize frequent, high-amount categories
			this.score = this.frequency * this.avgAmount;
		}

		public Integer getAverageDaysBetweenTransactions() {
			if (transactionDates.size() < 2)
				return 30; // Default monthly

			transactionDates.sort(Comparator.naturalOrder());
			long totalDays = 0;

			for (int i = 1; i < transactionDates.size(); i++) {
				totalDays += java.time.temporal.ChronoUnit.DAYS.between(
						transactionDates.get(i - 1), transactionDates.get(i));
			}

			return (int) (totalDays / (transactionDates.size() - 1));
		}

		// Getters
		public String getName() {
			return name;
		}

		public Double getTotalAmount() {
			return totalAmount;
		}

		public Integer getFrequency() {
			return frequency;
		}

		public Double getAvgAmount() {
			return avgAmount;
		}

		public Double getScore() {
			return score;
		}
	}

	// Greedy approach: Always pick the category with highest score first
	public static List<ExpensePrediction> predictExpenses(List<Transaction> transactions) {
		Map<String, ExpenseCategory> expenseCategories = new HashMap<>();
		double totalIncome = 0.0;
		int incomeCount = 0;

		// First, separate incomes and expenses
		for (Transaction transaction : transactions) {
			String type = transaction.getTransactionType();
			Double amount = transaction.getAmount();

			if (amount == null || amount <= 0)
				continue;

			if ("income ".equalsIgnoreCase(type)) {
				totalIncome += amount;
				incomeCount++;
			} else if ("expense".equalsIgnoreCase(type)) {
				String category = transaction.getTransactionCategory();
				if (category == null || category.trim().isEmpty()) {
					category = "Uncategorized";
				}

				expenseCategories.computeIfAbsent(category, ExpenseCategory::new)
						.addExpense(amount, transaction.getCreatedAt());
			}
		}

		// Average income per transaction (or per period, if needed)
		double averageIncome = incomeCount > 0 ? totalIncome / incomeCount : 0.0;

		// Adjust scores based on income
		for (ExpenseCategory category : expenseCategories.values()) {
			// Example adjustment:
			// Lower the score if avg expense in that category is more than 50% of avg
			// income
			if (averageIncome > 0 && category.getAvgAmount() > (0.5 * averageIncome)) {
				// Penalize score (greedy but cautious)
				category.score *= 0.75;
			}
		}

		// Sort by adjusted greedy score
		List<ExpenseCategory> sortedCategories = expenseCategories.values().stream()
				.filter(cat -> cat.getFrequency() >= 1)
				.sorted((a, b) -> Double.compare(b.getScore(), a.getScore()))
				.collect(Collectors.toList());

		List<ExpensePrediction> predictions = new ArrayList<>();

		for (int i = 0; i < Math.min(5, sortedCategories.size()); i++) {
			ExpenseCategory category = sortedCategories.get(i);
			Integer avgDays = category.getAverageDaysBetweenTransactions();
			LocalDateTime predictedDate = LocalDateTime.now().plusDays(avgDays);

			String reason = (category.getFrequency() == 1)
					? "Based on 1 transaction, estimated monthly recurrence"
					: String.format("Based on %d transactions, avg every %d days", category.getFrequency(), avgDays);

			predictions.add(new ExpensePrediction(
					category.getName(),
					Math.round(category.getAvgAmount() * 100.0) / 100.0,
					predictedDate,
					reason));
		}

		return predictions;
	}
}
