package teng.dev.expenseapi.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import teng.dev.expenseapi.dto.LoginRequest;
import teng.dev.expenseapi.dto.RefreshTokenRequest;
import teng.dev.expenseapi.dto.TokenPair;
import teng.dev.expenseapi.dto.RegisterRequest;
import teng.dev.expenseapi.entity.User;
import teng.dev.expenseapi.repository.UserRepository;

@Service
@AllArgsConstructor
public class AuthenticationService
{
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;

	@Transactional
	public void register(@Valid RegisterRequest request)
	{
		if (userRepository.findByUsername(request.getUsername()).isPresent())
		{
			throw new IllegalArgumentException("Username is already in use");
		}

		User user = User
				.builder()
				.firstName(request.getFirstName())
				.lastName(request.getLastName())
				.dateOfBirth(request.getDateOfBirth())
				.username(request.getUsername())
				.password(passwordEncoder.encode(request.getPassword()))
				.role(request.getRole())
				.build();

		userRepository.save(user);
	}

	public TokenPair login(@Valid LoginRequest request)
	{
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getUsername(),
						request.getPassword()
				)
		);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		return jwtService.generateTokenPair(authentication);
	}

	public TokenPair refresh(@Valid RefreshTokenRequest request)
	{
		val refreshToken = request.getRefreshToken();

		if(!jwtService.isValidToken(refreshToken)){
			throw new IllegalArgumentException("Invalid refresh token.");
		}

		String user = jwtService.extractUsernameFromToken(refreshToken);

		UserDetails userDetails = userDetailsService.loadUserByUsername(user);

		if (userDetails == null) {
			throw new IllegalArgumentException("User not found.");
		}

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				userDetails,
				null,
				userDetails.getAuthorities()
		);

		val accessToken = jwtService.generateAccessToken(authentication);

		return new TokenPair(accessToken, refreshToken);
	}
}
