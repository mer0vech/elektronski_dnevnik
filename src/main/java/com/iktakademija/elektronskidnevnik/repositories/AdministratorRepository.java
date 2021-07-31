package com.iktakademija.elektronskidnevnik.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktakademija.elektronskidnevnik.entities.AdministratorEntity;

public interface AdministratorRepository extends CrudRepository<AdministratorEntity, Integer>
{

}
