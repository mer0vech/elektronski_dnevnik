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
import com.iktakademija.elektronskidnevnik.entities.OdeljenjeEntity;
import com.iktakademija.elektronskidnevnik.entities.PredmetEntity;
import com.iktakademija.elektronskidnevnik.entities.UcenikEntity;
import com.iktakademija.elektronskidnevnik.repositories.NastavnikRepository;
import com.iktakademija.elektronskidnevnik.repositories.OdeljenjeRepository;
import com.iktakademija.elektronskidnevnik.repositories.PredmetRepository;
import com.iktakademija.elektronskidnevnik.repositories.UcenikRepository;

@RestController
public class OdeljenjeController
{
	@Autowired
	private OdeljenjeRepository odeljenjeRepo;
	@Autowired
	private NastavnikRepository nastavnikRepo;
	@Autowired
	private PredmetRepository predmetRepo;
	@Autowired
	private UcenikRepository ucenikRepo;
	
	
	/*
	 * GET REQUESTS
	 */
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/api/v1/odeljenja")
	public ResponseEntity<?> getAllOdeljenja() 
	{
		try {
			List<OdeljenjeEntity> retVal = (List<OdeljenjeEntity>) odeljenjeRepo.findAll();
			return new ResponseEntity<List<OdeljenjeEntity>>(retVal, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/api/v1/odeljenja/{id}")
	public ResponseEntity<?> getOdeljenjeById(@PathVariable Integer id)
	{
		try {
			Optional<OdeljenjeEntity> retVal = odeljenjeRepo.findById(id);
			if(retVal.isPresent())
				return new ResponseEntity<OdeljenjeEntity>(retVal.get(), HttpStatus.OK);
			return new ResponseEntity<RESTError>(new RESTError("Odeljenje not found", 1), HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	
	/*
	 * POST REQUESTS
	 */
	
	@Secured("ROLE_ADMIN")
	@PostMapping("/api/v1/odeljenja")
	public ResponseEntity<?> createOdeljenje(@RequestBody OdeljenjeEntity odeljenjeEntity)
	{
		try {
			OdeljenjeEntity newOdeljenje = new OdeljenjeEntity();
			newOdeljenje.setGodina(odeljenjeEntity.getGodina());
			newOdeljenje.setNaziv(odeljenjeEntity.getNaziv());
			newOdeljenje.setRazredni(nastavnikRepo.findById(odeljenjeEntity.getRazredni().getId()).get());
			odeljenjeRepo.save(newOdeljenje);
			return new ResponseEntity<OdeljenjeEntity>(odeljenjeRepo.findById(newOdeljenje.getId()).get(), HttpStatus.OK);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	
	/*
	 * PUT REQUESTS
	 */
	
	@Secured("ROLE_ADMIN")
	@PutMapping("/api/v1/odeljenja/{id}")
	public ResponseEntity<?> updateOdeljenja(@PathVariable Integer id, @RequestBody OdeljenjeEntity updatedOdeljenje)
	{
		try {
			Optional<OdeljenjeEntity> retVal = odeljenjeRepo.findById(id);
			if(retVal.isPresent()) {
				if(updatedOdeljenje.getGodina() != null)
					retVal.get().setGodina(updatedOdeljenje.getGodina());
				if(updatedOdeljenje.getNaziv() != null)
					retVal.get().setNaziv(updatedOdeljenje.getNaziv());
				if(updatedOdeljenje.getRazredni() != null)
					retVal.get().setRazredni(updatedOdeljenje.getRazredni());
				odeljenjeRepo.save(retVal.get());
				return new ResponseEntity<OdeljenjeEntity>(retVal.get(), HttpStatus.OK);
			}
			return new ResponseEntity<RESTError>(new RESTError("Odeljenje not found", 1), HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	@Secured("ROLE_ADMIN")
	@PutMapping("/api/v1/odeljenja/add-predmet/{id}")
	public ResponseEntity<?> dodajPredmetUOdeljenje(@PathVariable Integer id, @RequestBody PredmetEntity predmetDodavanje)
	{
		try {
			Optional<OdeljenjeEntity> retVal = odeljenjeRepo.findById(id);
			if(retVal.isPresent()) {
				List<PredmetEntity> predmeti = retVal.get().getPredmeti();
				if(!predmeti.contains(predmetDodavanje) && predmetRepo.findById(predmetDodavanje.getId()).get() != null) {
					predmeti.add(predmetRepo.findById(predmetDodavanje.getId()).get());
					retVal.get().setPredmeti(predmeti);
				}
				odeljenjeRepo.save(retVal.get());
				return new ResponseEntity<OdeljenjeEntity>(retVal.get(), HttpStatus.OK);
			}
			return new ResponseEntity<RESTError>(new RESTError("Odeljenje not found", 1), HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	@Secured("ROLE_ADMIN")
	@PutMapping("/api/v1/odeljenja/add-ucenik/{id}")
	public ResponseEntity<?> dodajUcenikaUOdeljenje(@PathVariable Integer id, @RequestBody UcenikEntity ucenik)
	{
		try {
			Optional<OdeljenjeEntity> retVal = odeljenjeRepo.findById(id);
			if(retVal.isPresent()) {
				List<UcenikEntity> ucenici = retVal.get().getUcenici();
				if(!ucenici.contains(ucenik) && ucenikRepo.findById(ucenik.getId()).get() != null) {
					ucenici.add(ucenikRepo.findById(ucenik.getId()).get());
					retVal.get().setUcenici(ucenici);
				}
				odeljenjeRepo.save(retVal.get());
				return new ResponseEntity<OdeljenjeEntity>(retVal.get(), HttpStatus.OK);
			}
			return new ResponseEntity<RESTError>(new RESTError("Odeljenje not found", 1), HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
	
	/*
	 * DELETE REQUESTS
	 */
	
	@Secured("ROLE_ADMIN")
	@DeleteMapping("/api/v1/odeljenja/{id}")
	public ResponseEntity<?> deleteOdeljenjeById(@PathVariable Integer id)
	{
		try {
			Optional<OdeljenjeEntity> retVal = odeljenjeRepo.findById(id);
			if(retVal.isPresent()) {
				odeljenjeRepo.deleteById(id);
				return new ResponseEntity<OdeljenjeEntity>(retVal.get(), HttpStatus.OK);
			}
			return new ResponseEntity<RESTError>(new RESTError("Odeljenje not found", 1), HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<RESTError>(new RESTError("Exception occured: " + e.getMessage(), 2), HttpStatus.INTERNAL_SERVER_ERROR); 
		}
	}
}
