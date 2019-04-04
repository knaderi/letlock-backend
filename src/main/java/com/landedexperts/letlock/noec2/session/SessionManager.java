package com.landedexperts.letlock.noec2.session;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SessionManager {
	private static Random random;
	private static Map<String, UserSession> sessionTokens;

	public static void initialize() {
		SessionManager.random = new Random();
		SessionManager.sessionTokens = new HashMap<>();
	}

	/*
	 * Generates a new session token for the user
	 */
	public static String generateSessionToken(int userId) {
		/* Generate the token */
		byte[] binStuff = new byte[128];
		SessionManager.random.nextBytes(binStuff);
		String token = Base64.getEncoder().encodeToString(binStuff);

		/* Generate the user's session and associate it with the generated token */
		SessionManager.sessionTokens.put(token, new UserSession(userId));

		/* The token shall be transmitted to the user */
		return token;
	}

	/*
	 * Remove non active user sessions
	 */
	public static void clean() {
		for(Map.Entry<String, UserSession> entry : SessionManager.sessionTokens.entrySet()) {
			if(!entry.getValue().isActive()) {
				SessionManager.cleanSession(entry.getKey());
			}
		}
	}

	/*
	 * Remove the user session associated with the token
	 */
	public static void cleanSession(String token) {
		SessionManager.sessionTokens.remove(token);
	}

	/*
	 * Tells whether the token is active
	 */
	public static Boolean isActive(String token) {
		Boolean result = false;
		if( SessionManager.sessionTokens.containsKey(token) ) {
			result = SessionManager.sessionTokens.get(token).isActive();
			if(!result) {
				SessionManager.cleanSession(token);
			}
		}
		return result;
	}

	/*
	 * Gives the user id (for the database) associated with this token
	 * Returned value to be used internally only
	 */
	public static int getUserId(String token) {
		int result = -1;
		if(SessionManager.isActive(token)) {
			UserSession blip = SessionManager.sessionTokens.get(token);
			result = blip.getUserId();
			blip.extend();
		}
		else {
			SessionManager.cleanSession(token);
		}
		return result;
	}

	/*
	 * Extend the lifetime of the token
	 */
	public static void extendToken(String token) {
		if(SessionManager.isActive(token)) {
			SessionManager.sessionTokens.get(token).extend();
		}
	}
}
