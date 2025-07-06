package teng.dev.expenseapi.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import teng.dev.expenseapi.service.JwtService;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
	private final JwtService jwtService;

	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
	                                HttpServletResponse response,
	                                FilterChain filterChain) throws ServletException, IOException
	{
		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		final String username;

		if (authHeader == null || !authHeader.startsWith("Bearer "))
		{
			filterChain.doFilter(request, response);
			return;
		}

		jwt = authHeader.substring(7);

		if (!jwtService.isValidToken(jwt))
		{
			filterChain.doFilter(request, response);
			return;
		}

		username = jwtService.extractUsernameFromToken(jwt);

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null)
		{
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);

			if (jwtService.isValidTokenForUser(jwt, userDetails))
			{
				UsernamePasswordAuthenticationToken authToken =
						new UsernamePasswordAuthenticationToken(
								userDetails,
								null,
								userDetails.getAuthorities()
						);

				authToken.setDetails(
						new WebAuthenticationDetailsSource().buildDetails(request)
				);

				SecurityContextHolder.getContext().setAuthentication(authToken);
			}

			filterChain.doFilter(request, response);
		}
	}
}
