package com.iktakademija.elektronskidnevnik.config;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JWTAuthorizationFilter extends OncePerRequestFilter
{

	private final String HEADER = "Authorization";
	private final String PREFIX = "Bearer ";
	
	private String SECRET;
	
	public JWTAuthorizationFilter(String SECRET)
	{
		this.SECRET = SECRET;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException
	{
		// check if jwt token exists
		if(checkJWTToken(request)) {
			// check validity of jwt token, return authorities
			Claims claims = validateToken(request);
			if(claims.get("authorities") != null) {
				// if valid, setup spring security based on token
				setupSpringAuthentication(claims);
			} else {
				// if not, clear context
				SecurityContextHolder.clearContext();
			}
		} else {
			SecurityContextHolder.clearContext();
		}
		
		// invoke filter chain
		filterChain.doFilter(request, response);

	}
	
	private Boolean checkJWTToken(HttpServletRequest request)
	{
		String authorizationHeader = request.getHeader(HEADER);
		
		if(authorizationHeader == null || !authorizationHeader.startsWith(PREFIX)) {
			return false;
		}
		return true;
	}
	
	private Claims validateToken(HttpServletRequest request)
	{
		String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
		return Jwts.parser().setSigningKey(this.SECRET)
				   .parseClaimsJws(jwtToken).getBody();
	}
	
	private void setupSpringAuthentication(Claims claims)
	{
		@SuppressWarnings("unchecked")
		List<String> authorities = (List<String>) claims.get("authorities");

		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
				claims.getSubject(), null, 
				authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
				);
		
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

}
