package teng.dev.expenseapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import teng.dev.expenseapi.entity.User;
import teng.dev.expenseapi.repository.UserRepository;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService
{
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		User user =
				userRepository.findByUsername(username)
						.orElseThrow(() -> new UsernameNotFoundException("User not found."));
		
		return new org.springframework.security.core.userdetails.User(
				user.getUsername(),
				user.getPassword(),
				getAuthority(user)
		);
	}

	private Collection<? extends GrantedAuthority> getAuthority(User user)
	{
		return List.of(new SimpleGrantedAuthority(user.getRole().name()));
	}
}
