package com.iktakademija.elektronskidnevnik.repositories;


import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.iktakademija.elektronskidnevnik.entities.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Integer>
{
	public Optional<UserEntity> findByUsername(String username);
}
