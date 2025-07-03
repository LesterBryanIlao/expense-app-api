package teng.dev.expenseapi.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter
{

	@Override
	protected void doFilterInternal(HttpServletRequest request,
	                                HttpServletResponse response,
	                                FilterChain filterChain) throws ServletException, IOException
	{
		String path = request.getRequestURI();
		String method = request.getMethod();

		filterChain.doFilter(request, response);
		Map<String, String> track = new HashMap<>();
		track.put("method", method);
		track.put("path", path);
		track.put("status", String.valueOf(response.getStatus()));
		log.info(track.toString());
	}
}
