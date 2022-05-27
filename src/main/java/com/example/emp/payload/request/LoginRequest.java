package com.example.emp.payload.request;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;
}
