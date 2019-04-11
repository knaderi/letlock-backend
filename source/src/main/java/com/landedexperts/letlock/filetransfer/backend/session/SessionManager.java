package com.landedexperts.letlock.filetransfer.backend.session;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SessionManager {
	private static SessionManager singleInstance = null;

	public static SessionManager getInstance() {
		if( singleInstance == null ) {
			singleInstance = new SessionManager();
		}
		return singleInstance;
	}

	private Random random;
	private Map<String, UserSession> sessionTokens;

	private SessionManager() {
		random = new Random();
		sessionTokens = new HashMap<>();
	}

	/*
	 * Generates a new session token for the user
	 */
	public String generateSessionToken(int userId) {
		/* Generate the token */
		byte[] randomBytes = new byte[128];
		random.nextBytes(randomBytes);
		String token = Base64.getEncoder().encodeToString(randomBytes);

		/* Generate the user's session and associate it with the generated token */
		sessionTokens.put(token, new UserSession(userId));

		/* The token shall be transmitted to the user */
		return token;
	}

	/*
	 * Remove non active user sessions
	 */
	public void clean() {
		for(Map.Entry<String, UserSession> entry : sessionTokens.entrySet()) {
			if(!entry.getValue().isActive()) {
				cleanSession(entry.getKey());
			}
		}
	}

	/*
	 * Remove the user session associated with the token
	 */
	public void cleanSession(String token) {
		sessionTokens.remove(token);
	}

	/*
	 * Tells whether the token is active
	 */
	public boolean isActive(String token) {
		boolean result = false;
		if( sessionTokens.containsKey(token) ) {
			result = sessionTokens.get(token).isActive();
			if(!result) {
				cleanSession(token);
			}
		}
		return result;
	}

	/*
	 * Gives the user id (for the database) associated with this token
	 * Returned value to be used internally only
	 */
	public int getUserId(final String token) {
		int result = -1;
		if(isActive(token)) {
			UserSession userSession = sessionTokens.get(token);
			result = userSession.getUserId();
			userSession.extend();
		}
		return result;
	}

	/*
	 * Extend the lifetime of the token
	 */
	public void extendToken(String token) {
		if(isActive(token)) {
			sessionTokens.get(token).extend();
		}
	}
}
