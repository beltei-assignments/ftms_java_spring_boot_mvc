package com.example.ftms_java_spring_boot.dtos;

public class ExpenseRecommendation {
	private String message;
	private Integer priority; // 1=High, 2=Medium, 3=Low

	public ExpenseRecommendation(String message, Integer priority) {
		this.message = message;
		this.priority = priority;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}
}
