package com.example.ftms_java_spring_boot.dtos;

import java.util.List;

public class ExpensePredictionResponse {
	private Long businessId;
	private String businessName;
	private Double currentBalance;
	private List<ExpensePrediction> predictions;
	private List<ExpenseRecommendation> recommendations;
	private String riskLevel;

	public ExpensePredictionResponse() {
	}

	// Getters and setters
	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public Double getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(Double currentBalance) {
		this.currentBalance = currentBalance;
	}

	public List<ExpensePrediction> getPredictions() {
		return predictions;
	}

	public void setPredictions(List<ExpensePrediction> predictions) {
		this.predictions = predictions;
	}

	public List<ExpenseRecommendation> getRecommendations() {
		return recommendations;
	}

	public void setRecommendations(List<ExpenseRecommendation> recommendations) {
		this.recommendations = recommendations;
	}

	public String getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
}
