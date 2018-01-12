package util;

import java.util.HashMap;

public final class Permissions {

	private static HashMap<String,Integer> sessionMap = new HashMap<String,Integer>();
	
	private Permissions() {}		
	
	public static Integer hasSession(String ip) {
		return sessionMap.get(ip);
	}
	
	public static boolean isAuthorized(String ip) {
		return false;
	}
	
}
