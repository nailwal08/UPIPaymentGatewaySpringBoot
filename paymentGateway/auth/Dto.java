package com.example.paymentGateway.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Dto {

	private String email;
	
    private String password;
    
    private String roles;
   // private Set<String> roles;
}
