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
import com.iktakademija.elektronskidnevnik.entities.NastavnikEntity;
import com.iktakademija.elektronskidnevnik.repositories.NastavnikRepository;
import com.iktakademija.elektronskidnevnik.repositories.RoleRepository;
import com.iktakademija.elektronskidnevnik.utils.Encryption;

@RestController
public class NastavnikController
{
	@Autowired
	private NastavnikRepository nastavnikRepo;
	@Autowired
	private RoleRepository roleRepo;
	
	/*
	 * GET REQUESTS
	 */
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/api/v1/nastavnik")
	public ResponseEntity<?> getAllNastavnik() 
	{
		try {
			List<NastavnikEntity> retVal = (List<NastavnikEntity>) nastavnikRepo.findAll();
			return new ResponseEntity<List<NastavnikEntity>>(retVal, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/api/v1/nastavnik/{id}")
	public ResponseEntity<?> getNastavnikById(@PathVariable Integer id)
	{
		try {
			Optional<NastavnikEntity> retVal = nastavnikRepo.findById(id);
			if(retVal.isPresent())
				return new ResponseEntity<NastavnikEntity>(retVal.get(), HttpStatus.OK);
			return new ResponseEntity<RESTError>(new RESTError("Nastavnik not found", 1), HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	
	/*
	 * POST REQUESTS
	 */
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/api/v1/nastavnik")
	public ResponseEntity<?> createNastavnik(@RequestBody NastavnikEntity nastavnik)
	{
		try {
			NastavnikEntity newNastavnik = new NastavnikEntity();
			newNastavnik.setIme(nastavnik.getIme());
			newNastavnik.setPrezime(nastavnik.getPrezime());
			newNastavnik.setEmail(nastavnik.getEmail());
			newNastavnik.setIsUcitelj(nastavnik.getIsUcitelj());
			newNastavnik.setOdeljenje(nastavnik.getOdeljenje());
			newNastavnik.setUsername(nastavnik.getUsername());
			newNastavnik.setPassword(Encryption.getPasswordEncoded(nastavnik.getPassword()));
			newNastavnik.setRole(roleRepo.findByName(nastavnik.getRole().getName()).get());
			nastavnikRepo.save(newNastavnik);
			return new ResponseEntity<NastavnikEntity>(nastavnikRepo.findById(newNastavnik.getId()).get(), HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	
	/*
	 * PUT REQUESTS
	 */
	
	@Secured("ROLE_ADMIN")
	@PutMapping("/api/v1/nastavnik/{id}")
	public ResponseEntity<?> updateNastavnik(@PathVariable Integer id, @RequestBody NastavnikEntity updatedNastavnik)
	{
		try {
			Optional<NastavnikEntity> retVal = nastavnikRepo.findById(id);
			if(retVal.isPresent()) {
				if(updatedNastavnik.getIme() != null)
					retVal.get().setIme(updatedNastavnik.getIme());
				if(updatedNastavnik.getPrezime() != null)
					retVal.get().setPrezime(updatedNastavnik.getPrezime());
				if(updatedNastavnik.getEmail() != null)
					retVal.get().setEmail(updatedNastavnik.getEmail());
				if(updatedNastavnik.getIsUcitelj() != null)
					retVal.get().setIsUcitelj(updatedNastavnik.getIsUcitelj());
				if(updatedNastavnik.getOdeljenje() != null)
					retVal.get().setOdeljenje(updatedNastavnik.getOdeljenje());
				nastavnikRepo.save(retVal.get());
				return new ResponseEntity<NastavnikEntity>(retVal.get(), HttpStatus.OK);
			}
			return new ResponseEntity<RESTError>(new RESTError("Nastavnik not found", 1), HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	@Secured("ROLE_ADMIN")
	@PutMapping("/api/v1/nastavnik/change-pass/{id}")
	public ResponseEntity<?> updatePassword(@PathVariable Integer id, @RequestParam String stara, String nova)
	{
		try {
			Optional<NastavnikEntity> user = nastavnikRepo.findById(id);
			if(user.isPresent() && Encryption.validatePassword(stara, user.get().getPassword())) {
				if(user.get().getPassword().equals(stara)) {
					user.get().setPassword(Encryption.getPasswordEncoded(nova));
					nastavnikRepo.save(user.get());
					return new ResponseEntity<NastavnikEntity>(user.get(), HttpStatus.OK);
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
	@DeleteMapping("/api/v1/nastavnik/{id}")
	public ResponseEntity<?> deleteNastavnikById(@PathVariable Integer id)
	{
		try {
			Optional<NastavnikEntity> retVal = nastavnikRepo.findById(id);
			if(retVal.isPresent()) {
				nastavnikRepo.deleteById(id);
				return new ResponseEntity<NastavnikEntity>(retVal.get(), HttpStatus.OK);
			}
			return new ResponseEntity<RESTError>(new RESTError("Nastavnik not found", 1), HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
}
