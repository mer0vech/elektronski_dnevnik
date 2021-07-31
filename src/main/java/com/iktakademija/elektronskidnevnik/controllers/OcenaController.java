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
import org.springframework.web.bind.annotation.RestController;

import com.iktakademija.elektronskidnevnik.controllers.util.RESTError;
import com.iktakademija.elektronskidnevnik.entities.OcenaEntity;
import com.iktakademija.elektronskidnevnik.repositories.OcenaRepository;
import com.iktakademija.elektronskidnevnik.repositories.PredmetRepository;
import com.iktakademija.elektronskidnevnik.repositories.UcenikRepository;

@RestController
public class OcenaController
{
	@Autowired
	private OcenaRepository ocenaRepo;
	@Autowired
	private UcenikRepository ucenikRepo;
	@Autowired
	private PredmetRepository predmetRepo;
	
	
	/*
	 * GET REQUESTS
	 */
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/api/v1/ocene")
	public ResponseEntity<?> getAllOcene() 
	{
		try {
			List<OcenaEntity> retVal = (List<OcenaEntity>) ocenaRepo.findAll();
			return new ResponseEntity<List<OcenaEntity>>(retVal, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/api/v1/ocene/{id}")
	public ResponseEntity<?> getOcenaById(@PathVariable Integer id)
	{
		try {
			Optional<OcenaEntity> retVal = ocenaRepo.findById(id);
			if(retVal.isPresent())
				return new ResponseEntity<OcenaEntity>(retVal.get(), HttpStatus.OK);
			return new ResponseEntity<RESTError>(new RESTError("Ocena not found", 1), HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	
	/*
	 * POST REQUESTS
	 */
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/api/v1/ocene")
	public ResponseEntity<?> createOcena(@RequestBody OcenaEntity ocenaEntity)
	{
		try {
			OcenaEntity newOcena = new OcenaEntity();
			newOcena.setVrednost(ocenaEntity.getVrednost());
			newOcena.setKategorija(ocenaEntity.getKategorija());
			newOcena.setPolugodiste(ocenaEntity.getPolugodiste());
			newOcena.setPredmet(predmetRepo.findById(ocenaEntity.getPredmet().getId()).get());
			newOcena.setUcenik(ucenikRepo.findById(ocenaEntity.getUcenik().getId()).get());
			ocenaRepo.save(newOcena);
			return new ResponseEntity<OcenaEntity>(ocenaRepo.findById(newOcena.getId()).get(), HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	
	/*
	 * PUT REQUESTS
	 */
	
	@Secured("ROLE_ADMIN")
	@PutMapping("/api/v1/ocene/{id}")
	public ResponseEntity<?> updateOcena(@PathVariable Integer id, @RequestBody OcenaEntity updatedOcena)
	{
		try {
			Optional<OcenaEntity> retVal = ocenaRepo.findById(id);
			if(retVal.isPresent()) {
				if(updatedOcena.getVrednost() != null)
					retVal.get().setVrednost(updatedOcena.getVrednost());
				if(updatedOcena.getKategorija() != null)
					retVal.get().setKategorija(updatedOcena.getKategorija());
				if(updatedOcena.getPolugodiste() != null)
					retVal.get().setPolugodiste(updatedOcena.getPolugodiste());
				if(updatedOcena.getPredmet() != null)
					retVal.get().setPredmet(updatedOcena.getPredmet());
				if(updatedOcena.getUcenik() != null)
					retVal.get().setUcenik(updatedOcena.getUcenik());
				return new ResponseEntity<OcenaEntity>(retVal.get(), HttpStatus.OK);
			}
			return new ResponseEntity<RESTError>(new RESTError("Ocena not found", 1), HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	
	/*
	 * DELETE REQUESTS
	 */
	
	@Secured("ROLE_ADMIN")
	@DeleteMapping("/api/v1/ocene/{id}")
	public ResponseEntity<?> deletePredmetById(@PathVariable Integer id)
	{
		try {
			Optional<OcenaEntity> retVal = ocenaRepo.findById(id);
			if(retVal.isPresent()) {
				ocenaRepo.deleteById(id);
				return new ResponseEntity<OcenaEntity>(retVal.get(), HttpStatus.OK);
			}
			return new ResponseEntity<RESTError>(new RESTError("Ocena not found", 1), HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
}
