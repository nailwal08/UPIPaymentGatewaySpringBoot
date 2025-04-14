package com.example.paymentGateway.entities;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@Getter
@Setter
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String email;
	
	@Column(nullable = false)
	private String roles = "DEFAULT";

	@Column(nullable = false)
	private String password;

}
