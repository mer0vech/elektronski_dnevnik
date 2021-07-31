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
import com.iktakademija.elektronskidnevnik.entities.AdministratorEntity;
import com.iktakademija.elektronskidnevnik.repositories.AdministratorRepository;
import com.iktakademija.elektronskidnevnik.repositories.RoleRepository;
import com.iktakademija.elektronskidnevnik.utils.Encryption;

@RestController
public class AdministratorController
{
	@Autowired
	private AdministratorRepository adminRepo;
	@Autowired
	private RoleRepository roleRepo;
	
	/*
	 * GET REQUESTS
	 */
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/api/v1/admin")
	public ResponseEntity<?> getAllAdmin() 
	{
		try {
			List<AdministratorEntity> retVal = (List<AdministratorEntity>) adminRepo.findAll();
			return new ResponseEntity<List<AdministratorEntity>>(retVal, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/api/v1/admin/{id}")
	public ResponseEntity<?> getAdminById(@PathVariable Integer id)
	{
		try {
			Optional<AdministratorEntity> retVal = adminRepo.findById(id);
			if(retVal.isPresent())
				return new ResponseEntity<AdministratorEntity>(retVal.get(), HttpStatus.OK);
			return new ResponseEntity<RESTError>(new RESTError("Admin not found", 1), HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	
	/*
	 * POST REQUESTS
	 */
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/api/v1/admin")
	public ResponseEntity<?> createAdmin(@RequestBody AdministratorEntity admin)
	{
		try {
			AdministratorEntity newAdmin = new AdministratorEntity();
			newAdmin.setIme(admin.getIme());
			newAdmin.setPrezime(admin.getPrezime());
			newAdmin.setEmail(admin.getEmail());
			newAdmin.setUsername(admin.getUsername());
			newAdmin.setPassword(Encryption.getPasswordEncoded(admin.getPassword()));
			newAdmin.setRole(roleRepo.findByName(admin.getRole().getName()).get());
			adminRepo.save(newAdmin);
			return new ResponseEntity<AdministratorEntity>(adminRepo.findById(newAdmin.getId()).get(), HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	
	/*
	 * PUT REQUESTS
	 */
	
	@Secured("ROLE_ADMIN")
	@PutMapping("/api/v1/admin/{id}")
	public ResponseEntity<?> updateAdmin(@PathVariable Integer id, @RequestBody AdministratorEntity updatedAdmin)
	{
		try {
			Optional<AdministratorEntity> retVal = adminRepo.findById(id);
			if(retVal.isPresent()) {
				if(updatedAdmin.getIme() != null)
					retVal.get().setIme(updatedAdmin.getIme());
				if(updatedAdmin.getPrezime() != null)
					retVal.get().setPrezime(updatedAdmin.getPrezime());
				if(updatedAdmin.getEmail() != null)
					retVal.get().setEmail(updatedAdmin.getEmail());
				adminRepo.save(retVal.get());
				return new ResponseEntity<AdministratorEntity>(retVal.get(), HttpStatus.OK);
			}
			return new ResponseEntity<RESTError>(new RESTError("Admin not found", 1), HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	@Secured("ROLE_ADMIN")
	@PutMapping("/api/v1/admin/change-pass/{id}")
	public ResponseEntity<?> updatePassword(@PathVariable Integer id, @RequestParam String stara, String nova)
	{
		try {
			Optional<AdministratorEntity> user = adminRepo.findById(id);
			if(user.isPresent() && Encryption.validatePassword(stara, user.get().getPassword())) {
				if(user.get().getPassword().equals(stara)) {
					user.get().setPassword(Encryption.getPasswordEncoded(nova));
					adminRepo.save(user.get());
					return new ResponseEntity<AdministratorEntity>(user.get(), HttpStatus.OK);
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
	@DeleteMapping("/api/v1/admin/{id}")
	public ResponseEntity<?> deleteAdminById(@PathVariable Integer id)
	{
		try {
			Optional<AdministratorEntity> retVal = adminRepo.findById(id);
			if(retVal.isPresent()) {
				adminRepo.deleteById(id);
				return new ResponseEntity<AdministratorEntity>(retVal.get(), HttpStatus.OK);
			}
			return new ResponseEntity<RESTError>(new RESTError("Admin not found", 1), HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
}
