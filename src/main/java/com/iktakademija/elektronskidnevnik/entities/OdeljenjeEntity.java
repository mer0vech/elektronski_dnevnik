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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.iktakademija.elektronskidnevnik.enums.GodinaEnum;

@Entity
@Table(name = "odeljenje")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class OdeljenjeEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "godina")
	private GodinaEnum godina;
	
	@Column(name = "naziv")
	private String naziv;
	
	@JsonBackReference
	@OneToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "razredni")
	private NastavnikEntity razredni;
	
	@JsonManagedReference
	@OneToMany(mappedBy = "odeljenje", fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
	private List<UcenikEntity> ucenici;
	
	@JsonBackReference
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinTable(name = "predmet_odeljenje", 
		joinColumns = { @JoinColumn(name = "odeljenje_id", nullable = false, updatable = false) },
		inverseJoinColumns = { @JoinColumn(name = "predmet_id", nullable = false, updatable = false) })
	private List<PredmetEntity> predmeti;
	
	@Version
	private Integer version;
	
	public OdeljenjeEntity() {}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public GodinaEnum getGodina()
	{
		return godina;
	}

	public void setGodina(GodinaEnum godina)
	{
		this.godina = godina;
	}

	public String getNaziv()
	{
		return naziv;
	}

	public void setNaziv(String naziv)
	{
		this.naziv = naziv;
	}

	public List<UcenikEntity> getUcenici()
	{
		return ucenici;
	}

	public void setUcenici(List<UcenikEntity> ucenici)
	{
		this.ucenici = ucenici;
	}

	public Integer getVersion()
	{
		return version;
	}

	public void setVersion(Integer version)
	{
		this.version = version;
	}

	public NastavnikEntity getRazredni()
	{
		return razredni;
	}

	public void setRazredni(NastavnikEntity razredni)
	{
		this.razredni = razredni;
	}

	public List<PredmetEntity> getPredmeti()
	{
		return predmeti;
	}

	public void setPredmeti(List<PredmetEntity> predmeti)
	{
		this.predmeti = predmeti;
	}
	
	
	
}
