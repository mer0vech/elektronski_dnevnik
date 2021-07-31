package com.iktakademija.elektronskidnevnik.enums;

public enum GodinaEnum
{
	GODINA_PRVA(1, "I"),
	GODINA_DRUGA(2, "II"),
	GODINA_TRECA(3, "III"),
	GODINA_CETVRTA(4, "IV"),
	GODINA_PETA(5, "V"),
	GODINA_SESTA(6, "VI"),
	GODINA_SEDMA(7, "VII"),
	GODINA_OSMA(8, "VIII");
	
	private final Integer key;
	private final String value;
	
	GodinaEnum(Integer key, String value)
	{
		this.key = key;
		this.value = value;
	}
	
	public Integer getKey() 
	{
		return key;
	}
	
	public String getValue()
	{
		return value;
	}
}
