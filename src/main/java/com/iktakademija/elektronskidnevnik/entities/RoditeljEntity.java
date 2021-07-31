package com.iktakademija.elektronskidnevnik.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "roditelj")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class RoditeljEntity extends UserEntity
{
	@Column(name = "email")
	private String email;
	
	@JsonBackReference
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinTable(name = "roditelj_ucenik", 
		joinColumns = { @JoinColumn(name = "roditelj_id", nullable = false, updatable = false) },
		inverseJoinColumns = { @JoinColumn(name = "ucenik_id", nullable = false, updatable = false) })
	private List<UcenikEntity> deca;
	
	public RoditeljEntity() {}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public List<UcenikEntity> getDeca()
	{
		return deca;
	}

	public void setDeca(List<UcenikEntity> deca)
	{
		this.deca = deca;
	}
	
	
}
