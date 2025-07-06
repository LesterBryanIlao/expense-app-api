package teng.dev.expenseapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import teng.dev.expenseapi.dto.UserRequestDTO;
import teng.dev.expenseapi.service.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController
{
	private final AuthenticationService authenticationService;

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequestDTO request)
	{
		authenticationService.registerUser(request);
		return ResponseEntity.ok("User created successfully.");
	}
}

