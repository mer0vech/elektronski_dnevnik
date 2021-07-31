package com.iktakademija.elektronskidnevnik.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.iktakademija.elektronskidnevnik.enums.KategorijaOcene;
import com.iktakademija.elektronskidnevnik.enums.OcenaEnum;
import com.iktakademija.elektronskidnevnik.enums.PolugodisteEnum;

@Entity
@Table(name = "ocena")
@JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
public class OcenaEntity
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(name = "vrednost")
	private OcenaEnum vrednost;
	
	@Column(name = "kategorija")
	private KategorijaOcene kategorija;
	
	@Column(name = "polugodiste")
	private PolugodisteEnum polugodiste;
	
	@JsonBackReference
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "predmet")
	private PredmetEntity predmet;
	
	@JsonBackReference
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "ucenik")
	private UcenikEntity ucenik;
	
	@Version
	private Integer version;
	
	public OcenaEntity() {}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Integer getVersion()
	{
		return version;
	}

	public void setVersion(Integer version)
	{
		this.version = version;
	}

	public OcenaEnum getVrednost()
	{
		return vrednost;
	}

	public void setVrednost(OcenaEnum vrednost)
	{
		this.vrednost = vrednost;
	}

	public KategorijaOcene getKategorija()
	{
		return kategorija;
	}

	public void setKategorija(KategorijaOcene kategorija)
	{
		this.kategorija = kategorija;
	}

	public PolugodisteEnum getPolugodiste()
	{
		return polugodiste;
	}

	public void setPolugodiste(PolugodisteEnum polugodiste)
	{
		this.polugodiste = polugodiste;
	}

	public PredmetEntity getPredmet()
	{
		return predmet;
	}

	public void setPredmet(PredmetEntity predmet)
	{
		this.predmet = predmet;
	}

	public UcenikEntity getUcenik()
	{
		return ucenik;
	}

	public void setUcenik(UcenikEntity ucenik)
	{
		this.ucenik = ucenik;
	}


	
	
}
