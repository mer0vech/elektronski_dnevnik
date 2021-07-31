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
import com.iktakademija.elektronskidnevnik.entities.PredmetEntity;
import com.iktakademija.elektronskidnevnik.repositories.NastavnikRepository;
import com.iktakademija.elektronskidnevnik.repositories.PredmetRepository;

@RestController
public class PredmetController
{
	@Autowired
	private PredmetRepository predmetRepo;
	@Autowired
	private NastavnikRepository nastavnikRepo;

	
	
	/*
	 * GET REQUESTS
	 */
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/api/v1/predmeti")
	public ResponseEntity<?> getAllPredmeti() 
	{
		try {
			List<PredmetEntity> retVal = (List<PredmetEntity>) predmetRepo.findAll();
			return new ResponseEntity<List<PredmetEntity>>(retVal, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/api/v1/predmeti/{id}")
	public ResponseEntity<?> getPredmetById(@PathVariable Integer id)
	{
		try {
			Optional<PredmetEntity> retVal = predmetRepo.findById(id);
			if(retVal.isPresent())
				return new ResponseEntity<PredmetEntity>(retVal.get(), HttpStatus.OK);
			return new ResponseEntity<RESTError>(new RESTError("Predmet not found", 1), HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	
	/*
	 * POST REQUESTS
	 */
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/api/v1/predmeti")
	public ResponseEntity<?> createPredmet(@RequestBody PredmetEntity predmetEntity)
	{
		try {
			PredmetEntity newPredmet = new PredmetEntity();
			newPredmet.setIme(predmetEntity.getIme());
			newPredmet.setNedeljni_fond(predmetEntity.getNedeljni_fond());
			newPredmet.setNastavnik(nastavnikRepo.findById(predmetEntity.getNastavnik().getId()).get());
			predmetRepo.save(newPredmet);
			return new ResponseEntity<PredmetEntity>(predmetRepo.findById(newPredmet.getId()).get(), HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	
	/*
	 * PUT REQUESTS
	 */
	
	@Secured("ROLE_ADMIN")
	@PutMapping("/api/v1/predmeti/{id}")
	public ResponseEntity<?> updatePredmet(@PathVariable Integer id, @RequestBody PredmetEntity updatedPredmet)
	{
		try {
			Optional<PredmetEntity> retVal = predmetRepo.findById(id);
			if(retVal.isPresent()) {
				if(updatedPredmet.getIme() != null)
					retVal.get().setIme(updatedPredmet.getIme());
				if(updatedPredmet.getNedeljni_fond() != null)
					retVal.get().setNedeljni_fond(updatedPredmet.getNedeljni_fond());
				if(updatedPredmet.getNastavnik() != null)
					retVal.get().setNastavnik(updatedPredmet.getNastavnik());
				return new ResponseEntity<PredmetEntity>(retVal.get(), HttpStatus.OK);
			}
			return new ResponseEntity<RESTError>(new RESTError("Predmet not found", 1), HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	
	/*
	 * DELETE REQUESTS
	 */
	
	@Secured("ROLE_ADMIN")
	@DeleteMapping("/api/v1/predmeti/{id}")
	public ResponseEntity<?> deletePredmetById(@PathVariable Integer id)
	{
		try {
			Optional<PredmetEntity> retVal = predmetRepo.findById(id);
			if(retVal.isPresent()) {
				predmetRepo.deleteById(id);
				return new ResponseEntity<PredmetEntity>(retVal.get(), HttpStatus.OK);
			}
			return new ResponseEntity<RESTError>(new RESTError("Predmet not found", 1), HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
}
