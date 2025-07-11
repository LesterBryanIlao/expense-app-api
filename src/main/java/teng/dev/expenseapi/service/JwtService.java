package teng.dev.expenseapi.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import teng.dev.expenseapi.dto.TokenPair;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class JwtService
{
	private static final String TOKEN_PREFIX = "Bearer ";

	@Value("${app.jwt.secret}")
	private String jwtSecret;

	@Value("${app.jwt.expiration}")
	private long expirationInMs;

	@Value("${app.jwt.refresh-token.expiration}")
	private long refreshExpirationInMs;

	public TokenPair generateTokenPair(Authentication authentication)
	{
		return new TokenPair(
				generateAccessToken(authentication),
				generateRefreshToken(authentication)
		);
	}

	public String generateAccessToken(Authentication authentication)
	{
		return generateToken(authentication, expirationInMs, new HashMap<>());
	}

	public String generateRefreshToken(Authentication authentication)
	{
		Map<String, String> claims = new HashMap<>();
		claims.put("tokenType", "refresh");

		return generateToken(authentication, refreshExpirationInMs, claims);
	}

	public String generateToken(Authentication authentication, long expirationMs, Map<String, String> claims)
	{

		UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

		final var now = new Date();
		final var expiryDate = new Date(now.getTime() + expirationMs);

		return Jwts.builder()
//				.header()
//				.add("type", "JWT")
//				.and()
				.subject(userPrincipal.getUsername())
				.claims(claims)
				.issuedAt(now)
				.expiration(expiryDate)
				.signWith(getSignInKey())
				.compact();
	}

	public boolean isValidToken(String token)
	{
		return extractAllClaims(token) != null;
	}

	public boolean isValidTokenForUser(String token, UserDetails userDetails)
	{

		final String username = extractUsernameFromToken(token);

		return username != null && username.equals(userDetails.getUsername());

//		if (username == null || !username.equals(userDetails.getUsername()))
//			return false;
//
//		try
//		{
//			Jwts.parser()
//					.verifyWith(getSignInKey())
//					.build()
//					.parseSignedClaims(token);
//			return true;
//		} catch (SignatureException e)
//		{
//			log.error("Invalid JWT signature: {}", e.getMessage());
//		} catch (MalformedJwtException e)
//		{
//			log.error("Invalid JWT token: {}", e.getMessage());
//		} catch (ExpiredJwtException e)
//		{
//			log.error("Expired JWT token: {}", e.getMessage());
//		} catch (UnsupportedJwtException e)
//		{
//			log.error("Unsupported JWT token: {}", token);
//		} catch (IllegalArgumentException e)
//		{
//			log.error("JWT claims string is empty: {}", token);
//		}
//
//		return false;
	}

	public String extractUsernameFromToken(String token)
	{
		final var claims = extractAllClaims(token);

		return claims == null ? null : claims.getSubject();
	}

	public boolean isRefreshToken(String token)
	{
		Claims claims = extractAllClaims(token);

		return claims != null && "refresh".equals(claims.get("tokenType"));
	}

	private Claims extractAllClaims(String token)
	{
		Claims claims = null;

		try
		{
			claims = Jwts.parser()
					.verifyWith(getSignInKey())
					.build()
					.parseSignedClaims(token)
					.getPayload();
		} catch (JwtException | IllegalArgumentException e)
		{
			throw new RuntimeException(e);
		}

		return claims;
	}

	private SecretKey getSignInKey()
	{
		byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);

		return Keys.hmacShaKeyFor(keyBytes);
	}

	private String getJwtSecret(boolean isAutogenerated) throws NoSuchAlgorithmException
	{
		String randomJwtSecret = "";
		try
		{
			KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
			keyGen.init(256); // for HS256
			SecretKey key = keyGen.generateKey();
			randomJwtSecret = Encoders.BASE64.encode(key.getEncoded());
		}catch(NoSuchAlgorithmException e){
			log.error(e.getMessage());
		}
		return !randomJwtSecret.isEmpty()? randomJwtSecret: jwtSecret;
	}

}
