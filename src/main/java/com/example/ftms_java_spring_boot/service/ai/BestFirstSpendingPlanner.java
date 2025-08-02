package com.example.ftms_java_spring_boot.service.ai;

import java.util.ArrayList;
import java.util.List;

import com.example.ftms_java_spring_boot.dtos.ExpensePrediction;
import com.example.ftms_java_spring_boot.dtos.ExpenseRecommendation;

public class BestFirstSpendingPlanner {
	public static class SpendingOption {
		private String category;
		private Double amount;
		private Integer priority;
		private Double benefit; // Simple benefit calculation

		public SpendingOption(String category, Double amount, Integer priority) {
			this.category = category;
			this.amount = amount;
			this.priority = priority;
			// Simple benefit = priority / amount (more priority, less cost = better)
			this.benefit = (double) priority / amount;
		}

		// Getters
		public String getCategory() {
			return category;
		}

		public Double getAmount() {
			return amount;
		}

		public Integer getPriority() {
			return priority;
		}

		public Double getBenefit() {
			return benefit;
		}
	}

	// Best-First Search: Always choose option with highest benefit first
	public static List<ExpenseRecommendation> createSpendingPlan(List<ExpensePrediction> predictions,
			Double availableBalance) {
		List<SpendingOption> options = new ArrayList<>();

		// Create spending options from predictions
		for (ExpensePrediction prediction : predictions) {
			Integer priority = calculatePriority(prediction.getCategory());
			options.add(new SpendingOption(prediction.getCategory(), prediction.getPredictedAmount(), priority));
		}

		// Best-First: Sort by benefit (highest first)
		options.sort((a, b) -> Double.compare(b.getBenefit(), a.getBenefit()));

		List<ExpenseRecommendation> recommendations = new ArrayList<>();
		Double remainingBalance = availableBalance;

		for (SpendingOption option : options) {
			if (option.getAmount() <= remainingBalance) {
				recommendations.add(new ExpenseRecommendation(
						"Recommend spending $" + option.getAmount() + " on " + option.getCategory(),
						option.getPriority()));
				remainingBalance -= option.getAmount();
			} else {
				recommendations.add(new ExpenseRecommendation(
						"Insufficient funds for " + option.getCategory() + " (need $" + option.getAmount() + ")",
						3 // Low priority warning
				));
			}
		}

		return recommendations;
	}

	private static Integer calculatePriority(String category) {
		// Simple priority scoring
		switch (category.toLowerCase()) {
			case "rent":
			case "utilities":
				return 5; // High priority
			case "payroll":
			case "office supplies":
				return 4;
			case "marketing":
			case "travel":
				return 3;
			default:
				return 2; // Medium priority
		}
	}
}
