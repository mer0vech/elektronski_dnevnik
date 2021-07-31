package com.iktakademija.elektronskidnevnik.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktakademija.elektronskidnevnik.entities.PredmetEntity;

public interface PredmetRepository extends CrudRepository<PredmetEntity, Integer>
{

}
