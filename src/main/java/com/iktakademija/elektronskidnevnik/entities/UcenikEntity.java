package com.iktakademija.elektronskidnevnik.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "ucenik")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})

public class UcenikEntity extends UserEntity
{
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinTable(name = "roditelj_ucenik", 
		joinColumns = { @JoinColumn(name = "ucenik_id", nullable = false, updatable = false) },
		inverseJoinColumns = { @JoinColumn(name = "roditelj_id", nullable = false, updatable = false) })
	private List<RoditeljEntity> roditelji;
	
	@JsonBackReference
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "odeljenje")
	private OdeljenjeEntity odeljenje;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "ucenik", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private List<OcenaEntity> ocene;
	
	public UcenikEntity() {}

	public List<RoditeljEntity> getRoditelji()
	{
		return roditelji;
	}

	public void setRoditelji(List<RoditeljEntity> roditelji)
	{
		this.roditelji = roditelji;
	}

	public OdeljenjeEntity getOdeljenje()
	{
		return odeljenje;
	}

	public void setOdeljenje(OdeljenjeEntity odeljenje)
	{
		this.odeljenje = odeljenje;
	}

	public List<OcenaEntity> getOcene()
	{
		return ocene;
	}

	public void setOcene(List<OcenaEntity> ocene)
	{
		this.ocene = ocene;
	}
	
	
}
