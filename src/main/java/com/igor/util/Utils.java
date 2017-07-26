package com.igor.util;

public class Utils {
	
	/**
	 * Returns true when production string is found upon app start 
	 * 
	 * @param args
	 * @return
	 */
	public static boolean isProduction(String[] args) {
		if (args != null && args.length > 0) {
			String prod = args[0];
			if ("production".equals(prod)) {
				return true;
			}
		}
		return false;
	}
}
