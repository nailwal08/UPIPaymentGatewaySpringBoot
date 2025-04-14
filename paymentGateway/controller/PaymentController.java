package com.example.paymentGateway.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.paymentGateway.auth.Dto;
import com.example.paymentGateway.entities.UserEntity;
import com.example.paymentGateway.repository.UserRepo;
import com.example.paymentGateway.service.PaymentService;
import com.example.paymentGateway.service.UserService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;


import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/pay")
@Slf4j
public class PaymentController {

	@Autowired
	PaymentService paymentService;

	@Autowired
	UserRepo userRepo;

	@Autowired
	UserService Userservice;

	@Autowired
	PasswordEncoder passwordEncoder;

//expiry time
	@GetMapping("/upi")
	public ResponseEntity<Object> generateLink(@RequestParam String role, @RequestParam String email, @RequestParam String password, @RequestParam String upiId, @RequestParam String name,
			@RequestParam double amount, @RequestParam(defaultValue = "Default") String note) {
		// String upiId="";
		try {
		log.info("Parameters are Role : "+role+ " UPI :"+upiId+" Name :"+name+" Amount :"+amount+" Note :"+note);
		if(role.equalsIgnoreCase("Admin")) {
			Optional<UserEntity> optUser = userRepo.findByEmail(email);
			if(optUser.isPresent()) {
				UserEntity user = optUser.get();
				String storedPass = user.getPassword();
				if(passwordEncoder.matches(password, storedPass)) {
					String upiLink = paymentService.generateLink(upiId, name, amount, note);
					QRCodeWriter qrCodeWriter = new QRCodeWriter();
					BitMatrix bitMatrix = qrCodeWriter.encode(upiLink, BarcodeFormat.QR_CODE, 200, 200);
					BufferedImage qrImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
					for (int x = 0; x < 200; x++) {
						for (int y = 0; y < 200; y++) {
							qrImage.setRGB(x, y, bitMatrix.get(x, y) ? 0x000000 : 0xFFFFFF);
						}
					}
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
					ImageIO.write(qrImage, "png", outputStream);
					byte[] imageByte = outputStream.toByteArray();
					HttpHeaders headers = new HttpHeaders();
					headers.add("Content-Type", "image/png");
					return new ResponseEntity<>(imageByte, headers, HttpStatus.OK);
				}
				else {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body("Please enter correct Password.");
				}
			}
			else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("User not found with email: " + email);
			}
			
			
		}
		else {
			//return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Denied");
		}
		
		} catch (Exception e) {
			log.info("Exception in generateLink :" + e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Please contact admin.");
		}
	}

	/*
	 * { "email": "testOneuser@example.com", "password": "securePassword123" }
	 */
	@PostMapping(value = "/registeruser", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> registerUser(@Valid @RequestBody Dto dto) {
		try {
			log.info("incoming request is :" + dto);
			// UserService Userservice = new UserService();
			String res = Userservice.registerUser(dto);
			// Set<RolesEntity> roleEntities = Userentity.getRoles();
			if (res.equalsIgnoreCase("200")) {
				UserEntity user = new UserEntity();
				user.setEmail(dto.getEmail());
				user.setPassword(passwordEncoder.encode(dto.getPassword()));
				userRepo.save(user);
				return ResponseEntity.status(HttpStatus.OK).body("User registed successfully.");
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
			}

		} catch (Exception e) {
			log.info("Exception in registerUser:" + e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Please contact admin.");
		}

	}

	/*
	 * { "email": "testOneu@example.com", "roles": "ADMIN" }
	 */
	@PostMapping(value = "/updaterole", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> registerrole(@Valid @RequestBody Dto dto) {
		try {
			log.info("incoming request is :" + dto);
			if (dto.getEmail() == null || dto.getEmail().isBlank()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email is required.");
			}
			Optional<UserEntity> optionalUser = userRepo.findByEmail(dto.getEmail());
			if (optionalUser.isEmpty()) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("User not found with email: " + dto.getEmail());
			} else {
				UserEntity user = optionalUser.get();
				// UserEntity user = new UserEntity();
				// user.setEmail(dto.getEmail());
				// user.setPassword(passwordEncoder.encode(dto.getPassword()));
				user.setRoles(dto.getRoles());
				userRepo.save(user);
				return ResponseEntity.status(HttpStatus.OK).body("Role updated successfully.");
			}

		} catch (Exception e) {
			log.info("Exception in registerrole" + e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Please contact admin.");
		}
	}
	
	@GetMapping("/test")
	public String test() {
	    return "Working!";
	}
	
}
