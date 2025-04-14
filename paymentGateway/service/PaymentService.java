package com.example.paymentGateway.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

	public String generateLink(String upiId, String name,double amount ,String note) {
		String url=
		"upi://pay?pa=" + URLEncoder.encode(upiId, StandardCharsets.UTF_8) +
        "&pn=" + URLEncoder.encode(name, StandardCharsets.UTF_8) +
        "&am=" + URLEncoder.encode(String.valueOf(amount), StandardCharsets.UTF_8) +
        "&cu=INR" +
        "&tn=" + URLEncoder.encode(note, StandardCharsets.UTF_8);
		return url;
	}
	
}
