package com.example.ftms_java_spring_boot.controllers.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.ftms_java_spring_boot.dtos.ExpensePredictionResponse;
import com.example.ftms_java_spring_boot.service.ExpensePredictionService;

import javassist.NotFoundException;

@RestController
@RequestMapping("/api/expense")
public class ExpensePredictionControllerAPI {
	@Autowired
	private ExpensePredictionService expensePredictionService;

	@PostMapping("/predict")
	public ResponseEntity<Object> predictExpenses(
			@RequestParam("businessId") Long businessId) {
		try {
			ExpensePredictionResponse response = expensePredictionService
					.predictExpenses(businessId);

			return ResponseEntity.ok(response);
		} catch (NotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
