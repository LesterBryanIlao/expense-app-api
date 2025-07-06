package teng.dev.expenseapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teng.dev.expenseapi.dto.LoginRequestDto;
import teng.dev.expenseapi.dto.RegisterRequestDTO;
import teng.dev.expenseapi.dto.TokenPair;
import teng.dev.expenseapi.service.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController
{
	private final AuthenticationService authenticationService;

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDTO request)
	{
		authenticationService.registerUser(request);
		return ResponseEntity.ok("User created successfully.");
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequestDto request)
	{
		TokenPair tokenPair = authenticationService.loginUser(request);
		return ResponseEntity.ok(tokenPair);
	}
}

