/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

public class LoginNameValidator {

	private final static String REGEX = "^[A-Za-z0-9]{7,32}$";

	public static final int MAX_LENGTH = 32;
	public static final int MIN_LENGTH = 7;

	public LoginNameValidator() {
	}

	private static void doValidate(String loginName) {

		Pattern pattern = Pattern.compile(REGEX);
		Matcher matcher = pattern.matcher(loginName);
		if (!matcher.matches()) {
			throw new InvalidLoginNameException();
		}
	}

	public static boolean isValid(String loginName) {
		try {
			loginName = loginName.trim();
			ensureMaxLengthValid(loginName);
			doValidate(loginName);
			return true;
		} catch (RuntimeException ex) {
			return false;
		}

	}

	private static void ensureMaxLengthValid(String loginName) {
		boolean valid = !StringUtils.isEmpty(loginName) && loginName.trim().length() >= MIN_LENGTH
				&& loginName.trim().length() <= MAX_LENGTH;
		if (!valid) {
			throw new IllegalStateException("Max length of loginName must be greater than or equal to " + MAX_LENGTH
					+ " and less than or equal to " + MAX_LENGTH + ".");
		}
	}

	private static class InvalidLoginNameException extends RuntimeException {
	}
}
