package com.example.ftms_java_spring_boot.dtos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ExpensePrediction {
	private String category;
	private Double predictedAmount;
	private LocalDateTime predictedDate;
	private String reason;

	public ExpensePrediction(String category, Double predictedAmount, LocalDateTime predictedDate, String reason) {
		this.category = category;
		this.predictedAmount = predictedAmount;
		this.predictedDate = predictedDate;
		this.reason = reason;
	}

	// Getters and setters
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Double getPredictedAmount() {
		return predictedAmount;
	}

	public void setPredictedAmount(Double predictedAmount) {
		this.predictedAmount = predictedAmount;
	}

	public LocalDateTime getPredictedDate() {
		return predictedDate;
	}

	public String getPredictedDateFormatted() {
		return predictedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

	public void setPredictedDate(LocalDateTime predictedDate) {
		this.predictedDate = predictedDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
