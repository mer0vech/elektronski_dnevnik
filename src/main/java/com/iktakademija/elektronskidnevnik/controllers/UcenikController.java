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
import com.iktakademija.elektronskidnevnik.entities.UcenikEntity;
import com.iktakademija.elektronskidnevnik.repositories.RoditeljRepository;
import com.iktakademija.elektronskidnevnik.repositories.RoleRepository;
import com.iktakademija.elektronskidnevnik.repositories.UcenikRepository;
import com.iktakademija.elektronskidnevnik.utils.Encryption;

@RestController
public class UcenikController
{
	@Autowired
	private UcenikRepository ucenikRepo;
	@Autowired
	private RoleRepository roleRepo;
	@Autowired
	private RoditeljRepository roditeljRepo;
	
	/*
	 * GET REQUESTS
	 */
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/api/v1/ucenik")
	public ResponseEntity<?> getAllUcenik() 
	{
		try {
			List<UcenikEntity> retVal = (List<UcenikEntity>) ucenikRepo.findAll();
			return new ResponseEntity<List<UcenikEntity>>(retVal, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/api/v1/ucenik/{id}")
	public ResponseEntity<?> getUcenikById(@PathVariable Integer id)
	{
		try {
			Optional<UcenikEntity> retVal = ucenikRepo.findById(id);
			if(retVal.isPresent())
				return new ResponseEntity<UcenikEntity>(retVal.get(), HttpStatus.OK);
			return new ResponseEntity<RESTError>(new RESTError("Ucenik not found", 1), HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	
	/*
	 * POST REQUESTS
	 */
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/api/v1/ucenik")
	public ResponseEntity<?> createUcenik(@RequestBody UcenikEntity ucenik)
	{
		try {
			UcenikEntity newUcenik = new UcenikEntity();
			newUcenik.setIme(ucenik.getIme());
			newUcenik.setPrezime(ucenik.getPrezime());
			newUcenik.setOdeljenje(ucenik.getOdeljenje());
			newUcenik.setUsername(ucenik.getUsername());
			newUcenik.setPassword(Encryption.getPasswordEncoded(ucenik.getPassword()));
			newUcenik.setRole(roleRepo.findByName(ucenik.getRole().getName()).get());
			ucenikRepo.save(newUcenik);
			return new ResponseEntity<UcenikEntity>(ucenikRepo.findById(newUcenik.getId()).get(), HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	
	/*
	 * PUT REQUESTS
	 */
	
	@Secured("ROLE_ADMIN")
	@PutMapping("/api/v1/ucenik/{id}")
	public ResponseEntity<?> updateUcenik(@PathVariable Integer id, @RequestBody UcenikEntity updatedUcenik)
	{
		try {
			Optional<UcenikEntity> retVal = ucenikRepo.findById(id);
			if(retVal.isPresent()) {
				if(updatedUcenik.getIme() != null)
					retVal.get().setIme(updatedUcenik.getIme());
				if(updatedUcenik.getPrezime() != null)
					retVal.get().setPrezime(updatedUcenik.getPrezime());
				if(updatedUcenik.getOdeljenje() != null)
					retVal.get().setOdeljenje(updatedUcenik.getOdeljenje());
				ucenikRepo.save(retVal.get());
				return new ResponseEntity<UcenikEntity>(retVal.get(), HttpStatus.OK);
			}
			return new ResponseEntity<RESTError>(new RESTError("Ucenik not found", 1), HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	@Secured("ROLE_ADMIN")
	@PutMapping("/api/v1/ucenik/add-roditelj/{id}")
	public ResponseEntity<?> addRoditeljToUcenik(@PathVariable Integer id, @RequestBody RoditeljEntity roditelj)
	{
		try {
			Optional<UcenikEntity> retVal = ucenikRepo.findById(id);
			if(retVal.isPresent()) {
				List<RoditeljEntity> roditelji = retVal.get().getRoditelji();
				if(!roditelji.contains(roditelj) && roditeljRepo.findById(roditelj.getId()).get() != null) {
					roditelji.add(roditeljRepo.findById(roditelj.getId()).get());
					retVal.get().setRoditelji(roditelji);
				}
				ucenikRepo.save(retVal.get());
				return new ResponseEntity<UcenikEntity>(retVal.get(), HttpStatus.OK);
			}
			return new ResponseEntity<RESTError>(new RESTError("Ucenik not found", 1), HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	@Secured("ROLE_ADMIN")
	@PutMapping("/api/v1/ucenik/change-pass/{id}")
	public ResponseEntity<?> updatePassword(@PathVariable Integer id, @RequestParam String stara, String nova)
	{
		try {
			Optional<UcenikEntity> user = ucenikRepo.findById(id);
			if(user.isPresent() && Encryption.validatePassword(stara, user.get().getPassword())) {
				if(user.get().getPassword().equals(stara)) {
					user.get().setPassword(Encryption.getPasswordEncoded(nova));
					ucenikRepo.save(user.get());
					return new ResponseEntity<UcenikEntity>(user.get(), HttpStatus.OK);
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
	@DeleteMapping("/api/v1/ucenik/{id}")
	public ResponseEntity<?> deleteUcenikById(@PathVariable Integer id)
	{
		try {
			Optional<UcenikEntity> retVal = ucenikRepo.findById(id);
			if(retVal.isPresent()) {
				ucenikRepo.deleteById(id);
				return new ResponseEntity<UcenikEntity>(retVal.get(), HttpStatus.OK);
			}
			return new ResponseEntity<RESTError>(new RESTError("Ucenik not found", 1), HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
}
