package com.iktakademija.elektronskidnevnik.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "nastavnik")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class NastavnikEntity extends UserEntity
{
	@Column(name = "ucitelj")
	private Boolean isUcitelj;
	
	@Column(name = "email")
	private String email;
	
	@OneToOne(fetch = FetchType.LAZY, cascade =  CascadeType.ALL, mappedBy = "razredni")
	private OdeljenjeEntity odeljenje;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "nastavnik", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private List<PredmetEntity> lista_predmeta = new ArrayList<PredmetEntity>();
	
	public NastavnikEntity() 
	{
		super();
	}

	public Boolean getIsUcitelj()
	{
		return isUcitelj;
	}

	public void setIsUcitelj(Boolean isUcitelj)
	{
		this.isUcitelj = isUcitelj;
	}

	public OdeljenjeEntity getOdeljenje()
	{
		return odeljenje;
	}

	public void setOdeljenje(OdeljenjeEntity odeljenje)
	{
		this.odeljenje = odeljenje;
	}

	public List<PredmetEntity> getLista_predmeta()
	{
		return lista_predmeta;
	}

	public void setLista_predmeta(List<PredmetEntity> lista_predmeta)
	{
		this.lista_predmeta = lista_predmeta;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}
	
	
}
