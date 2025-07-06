package teng.dev.expenseapi.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import teng.dev.expenseapi.dto.UserRequestDTO;
import teng.dev.expenseapi.entity.User;
import teng.dev.expenseapi.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService
{
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void registerUser(@Valid UserRequestDTO request)
	{
		if (userRepository.findByUsername(request.getUsername()).isPresent())
		{
			throw new RuntimeException("Username already exists");
		}

		User user = User.builder()
				.firstName(request.getFirstName())
				.lastName(request.getLastName())
				.dateOfBirth(request.getDateOfBirth())
				.username(request.getUsername())
				.password(passwordEncoder.encode(request.getPassword()))
				.role(request.getRole())
				.build();

		userRepository.save(user);
	}
}
