package com.iktakademija.elektronskidnevnik.controllers;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iktakademija.elektronskidnevnik.controllers.dto.UserTokenDTO;
import com.iktakademija.elektronskidnevnik.controllers.util.RESTError;
import com.iktakademija.elektronskidnevnik.entities.UserEntity;
import com.iktakademija.elektronskidnevnik.repositories.UserRepository;
import com.iktakademija.elektronskidnevnik.utils.Encryption;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class UserController
{
	@Autowired
	private UserRepository userRepo;
	
	@Value("${spring.security.secret-key}")
	private String SECRET;
	@Value("${spring.security.token-duration}")
	private Integer TOKEN_DURATION;
	
	
	/*
	 * GET REQUESTS
	 */
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/api/v1/users")
	public ResponseEntity<?> getAllUsers()
	{
		try {
			List<UserEntity> retVal = (List<UserEntity>) userRepo.findAll();
			return new ResponseEntity<List<UserEntity>>(retVal, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
		
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/api/v1/users/{username}")
	public ResponseEntity<?> getUserByUsername(@RequestParam String username)
	{
		try {
			Optional<UserEntity> retVal = userRepo.findByUsername(username);
			if(retVal.isPresent())
				return new ResponseEntity<UserEntity>(retVal.get(), HttpStatus.OK);
			return new ResponseEntity<RESTError>(new RESTError("User not found", 1), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}

	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/api/v1/users/{id}")
	public ResponseEntity<?> getUserById(@PathVariable Integer id)
	{
		try {
			Optional<UserEntity> retVal = userRepo.findById(id);
			if(retVal.isPresent())
				return new ResponseEntity<UserEntity>(retVal.get(), HttpStatus.OK);
			return new ResponseEntity<RESTError>(new RESTError("User not found", 1), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}

	}
	
	
	/*
	 * POST REQUESTS
	 */

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestParam("username") String username, @RequestParam("password") String password)
	{
		// pronadji korisnika po username-u
		Optional<UserEntity> userEntity = userRepo.findByUsername(username);
		// ako je pronadjen proveri lozinku

		if(userEntity.isPresent() && Encryption.validatePassword(password, userEntity.get().getPassword())) {
			// ako je sve okej, napravi token i vrati povratnu vrednost
			String token = getJWTToken(userEntity.get());
			UserTokenDTO retVal = new UserTokenDTO(username, "Bearer " + token);
			return new ResponseEntity<UserTokenDTO>(retVal, HttpStatus.OK);
		}
		
		// u suprotnom vrati 401
		return new ResponseEntity<RESTError>(new RESTError("Unauthorized...", 0), HttpStatus.UNAUTHORIZED);
	}
	
	
	/*
	 * PUT REQUESTS
	 */
	
	@Secured("ROLE_ADMIN")
	@PutMapping("/api/v1/users/change-pass/{id}")
	public ResponseEntity<?> updatePassword(@PathVariable Integer id, @RequestParam String stara, String nova)
	{
		try {
			Optional<UserEntity> user = userRepo.findById(id);
			if(user.isPresent() && Encryption.validatePassword(stara, user.get().getPassword())) {
				if(user.get().getPassword().equals(stara)) {
					user.get().setPassword(Encryption.getPasswordEncoded(nova));
					userRepo.save(user.get());
					return new ResponseEntity<UserEntity>(user.get(), HttpStatus.OK);
				}
				return new ResponseEntity<RESTError>(new RESTError("Passwords do not match", 4), HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<RESTError>(new RESTError("User not found", 1), HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	/*
	 * DELETE REQUESTS
	 */
	
	@Secured("ROLE_ADMIN")
	@DeleteMapping("/api/v1/users/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Integer id)
	{
		try {
			Optional<UserEntity> user = userRepo.findById(id);
			if(user.isPresent()) {
				userRepo.deleteById(id);
				return new ResponseEntity<UserEntity>(user.get(), HttpStatus.OK);
			}
			return new ResponseEntity<RESTError>(new RESTError("User not found", 1), HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	

	private String getJWTToken(UserEntity userEntity)
	{
		List<GrantedAuthority> grantedAuthority = AuthorityUtils.commaSeparatedStringToAuthorityList(userEntity.getRole().getName());
		String token = Jwts.builder().setId("softtokJWT")
				 		   .setSubject(userEntity.getUsername())
				 		   .claim("authorities", grantedAuthority.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				 		   .setIssuedAt(new Date(System.currentTimeMillis()))
				 		   .setExpiration(new Date(System.currentTimeMillis() + this.TOKEN_DURATION))
				 		   .signWith(SignatureAlgorithm.HS512, this.SECRET).compact();
				
		return token;
	}

}
