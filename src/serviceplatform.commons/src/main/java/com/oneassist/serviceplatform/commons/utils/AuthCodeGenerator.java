package com.oneassist.serviceplatform.commons.utils;

import java.util.Random;


public class AuthCodeGenerator {

	public static String authPin;
	
	public static String generateFourDigitAuthPin(){
		Random random = new Random();
		authPin = String.valueOf(random.nextInt(9000) + 1000);
		return authPin;
	}
}
