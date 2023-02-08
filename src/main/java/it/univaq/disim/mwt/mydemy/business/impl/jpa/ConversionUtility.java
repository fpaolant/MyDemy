package it.univaq.disim.mwt.mydemy.business.impl.jpa;

public class ConversionUtility {

	public static String addPercentSuffix(String s) {
		if (s == null || "".equals(s)) {
			return "%";
		}
		return s + "%";
	}
	
}
