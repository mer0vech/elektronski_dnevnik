package com.iktakademija.elektronskidnevnik.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iktakademija.elektronskidnevnik.controllers.util.RESTError;
import com.iktakademija.elektronskidnevnik.entities.RoditeljEntity;
import com.iktakademija.elektronskidnevnik.repositories.RoditeljRepository;
import com.iktakademija.elektronskidnevnik.repositories.RoleRepository;
import com.iktakademija.elektronskidnevnik.utils.Encryption;

@RestController
public class RoditeljController
{
	@Autowired
	private RoditeljRepository roditeljRepo;
	@Autowired
	private RoleRepository roleRepo;
	
	/*
	 * GET REQUESTS
	 */
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/api/v1/roditelj")
	public ResponseEntity<?> getAllRoditelj() 
	{
		try {
			List<RoditeljEntity> retVal = (List<RoditeljEntity>) roditeljRepo.findAll();
			return new ResponseEntity<List<RoditeljEntity>>(retVal, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/api/v1/roditelj/{id}")
	public ResponseEntity<?> getRoditeljById(@PathVariable Integer id)
	{
		try {
			Optional<RoditeljEntity> retVal = roditeljRepo.findById(id);
			if(retVal.isPresent())
				return new ResponseEntity<RoditeljEntity>(retVal.get(), HttpStatus.OK);
			return new ResponseEntity<RESTError>(new RESTError("Roditelj not found", 1), HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	
	/*
	 * POST REQUESTS
	 */
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/api/v1/roditelj")
	public ResponseEntity<?> createRoditelj(@RequestBody RoditeljEntity roditelj)
	{
		try {
			RoditeljEntity newRoditelj = new RoditeljEntity();
			newRoditelj.setIme(roditelj.getIme());
			newRoditelj.setPrezime(roditelj.getPrezime());
			newRoditelj.setEmail(roditelj.getEmail());
			newRoditelj.setUsername(roditelj.getUsername());
			newRoditelj.setPassword(Encryption.getPasswordEncoded(roditelj.getPassword()));
			newRoditelj.setRole(roleRepo.findByName(roditelj.getRole().getName()).get());
			roditeljRepo.save(newRoditelj);
			return new ResponseEntity<RoditeljEntity>(roditeljRepo.findById(newRoditelj.getId()).get(), HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	
	/*
	 * PUT REQUESTS
	 */
	
	@Secured("ROLE_ADMIN")
	@PutMapping("/api/v1/roditelj/{id}")
	public ResponseEntity<?> updateRoditelj(@PathVariable Integer id, @RequestBody RoditeljEntity updatedRoditelj)
	{
		try {
			Optional<RoditeljEntity> retVal = roditeljRepo.findById(id);
			if(retVal.isPresent()) {
				if(updatedRoditelj.getIme() != null)
					retVal.get().setIme(updatedRoditelj.getIme());
				if(updatedRoditelj.getPrezime() != null)
					retVal.get().setPrezime(updatedRoditelj.getPrezime());
				if(updatedRoditelj.getEmail() != null)
					retVal.get().setEmail(updatedRoditelj.getEmail());
				roditeljRepo.save(retVal.get());
				return new ResponseEntity<RoditeljEntity>(retVal.get(), HttpStatus.OK);
			}
			return new ResponseEntity<RESTError>(new RESTError("Roditelj not found", 1), HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	@Secured("ROLE_ADMIN")
	@PutMapping("/api/v1/roditelj/change-pass/{id}")
	public ResponseEntity<?> updatePassword(@PathVariable Integer id, @RequestParam String stara, String nova)
	{
		try {
			Optional<RoditeljEntity> user = roditeljRepo.findById(id);
			if(user.isPresent() && Encryption.validatePassword(stara, user.get().getPassword())) {
				if(user.get().getPassword().equals(stara)) {
					user.get().setPassword(Encryption.getPasswordEncoded(nova));
					roditeljRepo.save(user.get());
					return new ResponseEntity<RoditeljEntity>(user.get(), HttpStatus.OK);
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
	@DeleteMapping("/api/v1/roditelj/{id}")
	public ResponseEntity<?> deleteRoditeljById(@PathVariable Integer id)
	{
		try {
			Optional<RoditeljEntity> retVal = roditeljRepo.findById(id);
			if(retVal.isPresent()) {
				roditeljRepo.deleteById(id);
				return new ResponseEntity<RoditeljEntity>(retVal.get(), HttpStatus.OK);
			}
			return new ResponseEntity<RESTError>(new RESTError("Roditelj not found", 1), HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
}
