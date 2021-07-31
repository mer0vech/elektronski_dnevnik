package com.iktakademija.elektronskidnevnik.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Encryption
{
	public static String getPasswordEncoded(String password)
	{
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(password);
	}
	
	public static Boolean validatePassword(String password, String encodedPassword)
	{
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.matches(password, encodedPassword);
	}
	
	public static void main(String[] args)
	{
		System.out.println(getPasswordEncoded("secret"));
	}
}
