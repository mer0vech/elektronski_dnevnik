package com.iktakademija.elektronskidnevnik.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "predmet")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class PredmetEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "naziv")
	private String ime;
	
	@Column(name = "nfond")
	private Integer nedeljni_fond;
	
	@JsonBackReference
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "nastavnik")
	private NastavnikEntity nastavnik;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "predmet", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private List<OcenaEntity> ocene;
	
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinTable(name = "predmet_odeljenje", 
		joinColumns = { @JoinColumn(name = "predmet_id", nullable = false, updatable = false) },
		inverseJoinColumns = { @JoinColumn(name = "odeljenje_id", nullable = false, updatable = false) })
	private List<OdeljenjeEntity> odeljenja;
	
	@Version
	private Integer version;
	
	public PredmetEntity() {}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getIme()
	{
		return ime;
	}

	public void setIme(String ime)
	{
		this.ime = ime;
	}

	public Integer getNedeljni_fond()
	{
		return nedeljni_fond;
	}

	public void setNedeljni_fond(Integer nedeljni_fond)
	{
		this.nedeljni_fond = nedeljni_fond;
	}

	public Integer getVersion()
	{
		return version;
	}

	public void setVersion(Integer version)
	{
		this.version = version;
	}

	public NastavnikEntity getNastavnik()
	{
		return nastavnik;
	}

	public void setNastavnik(NastavnikEntity nastavnik)
	{
		this.nastavnik = nastavnik;
	}

	public List<OcenaEntity> getOcene()
	{
		return ocene;
	}

	public void setOcene(List<OcenaEntity> ocene)
	{
		this.ocene = ocene;
	}

	public List<OdeljenjeEntity> getOdeljenja()
	{
		return odeljenja;
	}

	public void setOdeljenja(List<OdeljenjeEntity> odeljenja)
	{
		this.odeljenja = odeljenja;
	}
	
	
}
