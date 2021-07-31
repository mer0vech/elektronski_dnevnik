package com.iktakademija.elektronskidnevnik.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.iktakademija.elektronskidnevnik.entities.RoleEntity;

public interface RoleRepository extends CrudRepository<RoleEntity, Integer>
{
	Optional<RoleEntity> findByName(String name);
}
