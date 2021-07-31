package com.iktakademija.elektronskidnevnik.enums;

public enum OcenaEnum
{
	OCENA_JEDAN(1, "Nedovoljan"),
	OCENA_DVA(2, "Dovoljan"),
	OCENA_TRI(3, "Dobar"),
	OCENA_CETIRI(4, "Vrlo dobar"),
	OCENA_PET(5, "Odličan");
	
	private final Integer key;
	private final String value;
	
	OcenaEnum(Integer key, String value)
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
