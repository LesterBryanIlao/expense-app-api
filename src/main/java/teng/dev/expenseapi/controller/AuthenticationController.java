package teng.dev.expenseapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teng.dev.expenseapi.dto.LoginRequest;
import teng.dev.expenseapi.dto.RefreshTokenRequest;
import teng.dev.expenseapi.dto.RegisterRequest;
import teng.dev.expenseapi.dto.TokenPair;
import teng.dev.expenseapi.service.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController
{
	private final AuthenticationService authenticationService;

	@PostMapping("/register")
	public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request)
	{
		authenticationService.register(request);

		return ResponseEntity.ok("User created successfully.");
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request)
	{
		TokenPair tokenPair = authenticationService.login(request);

		return ResponseEntity.ok(tokenPair);
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<?> refresh(@Valid @RequestBody RefreshTokenRequest request)
	{
		TokenPair tokenPair = authenticationService.refresh(request);

		return ResponseEntity.ok(tokenPair);
	}

}

