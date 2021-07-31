package com.iktakademija.elektronskidnevnik.controllers.util;


public class RESTError
{
	private String message;
	private Integer code;
	
	public RESTError() {}

	public RESTError(String message, Integer code)
	{
		this.message = message;
		this.code = code;
	}

	public String getMessage()
	{
		return message;
	}

	public Integer getCode()
	{
		return code;
	}
	
}
