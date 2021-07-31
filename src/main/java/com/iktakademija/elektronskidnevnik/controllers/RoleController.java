package com.iktakademija.elektronskidnevnik.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iktakademija.elektronskidnevnik.controllers.util.RESTError;
import com.iktakademija.elektronskidnevnik.entities.RoleEntity;
import com.iktakademija.elektronskidnevnik.repositories.RoleRepository;

@RestController
public class RoleController
{
	@Autowired
	private RoleRepository roleRepo;
	
	/*
	 * GET REQUESTS
	 */
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/api/v1/roles")
	public ResponseEntity<?> getAllRoles() 
	{
		try {
			List<RoleEntity> retVal = (List<RoleEntity>) roleRepo.findAll();
			return new ResponseEntity<List<RoleEntity>>(retVal, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/api/v1/roles/{id}")
	public ResponseEntity<?> getRoleById(@RequestParam Integer id)
	{
		try {
			Optional<RoleEntity> retVal = roleRepo.findById(id);
			if(retVal.isPresent())
				return new ResponseEntity<RoleEntity>(retVal.get(), HttpStatus.OK);
			return new ResponseEntity<RESTError>(new RESTError("Role not found", 1), HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	
	/*
	 * POST REQUESTS
	 */
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/api/v1/roles")
	public ResponseEntity<?> createRole(@RequestParam String name)
	{
		try {
			RoleEntity newRole = new RoleEntity();
			newRole.setName(name);
			roleRepo.save(newRole);
			return new ResponseEntity<RoleEntity>(roleRepo.findById(newRole.getId()).get(), HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	
	/*
	 * PUT REQUESTS
	 */
	
	@Secured("ROLE_ADMIN")
	@PutMapping("/api/v1/roles/{id}")
	public ResponseEntity<?> changeRoleName(@RequestParam Integer id, @RequestParam String newName)
	{
		try {
			Optional<RoleEntity> retVal = roleRepo.findById(id);
			if(retVal.isPresent()) {
				retVal.get().setName(newName);
				roleRepo.save(retVal.get());
				return new ResponseEntity<RoleEntity>(retVal.get(), HttpStatus.OK);
			}
			return new ResponseEntity<RESTError>(new RESTError("Role not found", 1), HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	
	/*
	 * DELETE REQUESTS
	 */
	
	@Secured("ROLE_ADMIN")
	@DeleteMapping("/api/v1/roles/{id}")
	public ResponseEntity<?> deleteRoleById(@RequestParam Integer id)
	{
		try {
			Optional<RoleEntity> retVal = roleRepo.findById(id);
			if(retVal.isPresent()) {
				roleRepo.deleteById(id);
				return new ResponseEntity<RoleEntity>(retVal.get(), HttpStatus.OK);
			}
			return new ResponseEntity<RESTError>(new RESTError("Role not found", 1), HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
}
