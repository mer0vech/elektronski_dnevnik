package com.iktakademija.elektronskidnevnik.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "admin")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class AdministratorEntity extends UserEntity
{
	@Column(name = "email")
	private String email;
	
	public AdministratorEntity() {}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}
	
	
}
