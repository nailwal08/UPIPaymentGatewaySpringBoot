package com.example.paymentGateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.paymentGateway.auth.Dto;
import com.example.paymentGateway.repository.UserRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {

	@Autowired
	UserRepo userRepo;

	public String registerUser(Dto request) {
		if (request.getEmail() == null || request.getEmail().isBlank()) {
			return "Email is required.";
		}
		if (request.getPassword() == null || request.getPassword().isBlank()) {
			return "Password is required.";
		}
		if (userRepo.findByEmail(request.getEmail()).isPresent()) {
			return "Email already exists.";
		}
		log.info("Success response");
		return "200";
	}
}