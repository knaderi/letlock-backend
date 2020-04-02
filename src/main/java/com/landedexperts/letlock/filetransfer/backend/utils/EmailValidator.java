/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Internet email address validation, largely based on PTY email validation
 * and RFC 3696 http://www.rfc-editor.org/errata_search.php?rfc=3696&eid=1690
 *
 * Specifically the following rules are implemented:
 *
 * 1.	The e-mail address should be in the format x@a.b, where x represents at
 *    least one character. Valid characters for x include letters (a-z, A-Z),
 *    numbers (0-9), characters (! # $ % & ' * + - / = ? ^ _ ` { | } ~), and
 *    periods (.).
 *
 *    Additional characters (()[]\;,<>@) are permitted for x as long as they
 *    are inside quotation marks.
 *
 * 2.	There must be at least one (@) symbol in the e-mail address.
 *
 * 3.	There must be at least one period (.) in the e-mail address.
 *
 * 4. Two or more adjacent periods are not permitted.
 *
 * 5. The e-mail address cannot end with an underscore, a period, or a hyphen.
 *
 * 6.	The @ symbol cannot be preceded with or followed by a period.
 *
 * 7.	The e-mail address cannot have a space in the middle.
 *    NOTE: The RFC permits a space as long as it is within quotes, but the PTY
 *    validation does not allow the space.
 *
 * 8.	The maximum length of an e-mail address is 254 per the RFC, but PTY only
 *    permits 128 at this time.
 *
 * 9.	The maximum length of the local part is 64 characters.
 *
 * 10. There should be at least two characters following the last period in the
 *     domain portion of the e-mail address.
 *
 * 11. Leading and trailing spaces are trimmed.
 *
 * Other notes:
 * - IP addresses, although valid per the RFC, are not supported at this time.
 * - unicode address, although valid per the RFC, are not supported at this time
 *
 * Sample usage:
 *
 * EmailValidator validator = new EmailValidator(maxLength); // e.g. 128 is current length
 * if (validator.isValid("user123@abc.com")) {
 *   // do something
 * }
 *
 **/

public class EmailValidator {
    private final static String REGEX =
        "^(([A-Za-z0-9_.!#$%&'*+/=?`{|}~^-]+)|(\"([A-Za-z0-9\\[\\]\\\\_.!#$%&'*+/=?`{|}~()\\;:,<>@^-]+)\"))@([A-Za-z0-9-]+\\.)+([A-Za-z]{2,6})$";

    public static final int MAX_LOCAL_LENGTH = 64;
    public static final int DEFAULT_MAX_LENGTH = 254;

    private int maxLength = DEFAULT_MAX_LENGTH;

    
    public EmailValidator() {
    }

    public EmailValidator(int maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * @return true if email is valid, false otherwise.
     * @throws IllegalStateException if custom length specified in constructor 
     * is less than MAX_LOCAL_LENGTH + 5 or greater than DEFAULT_MAX_LENGTH
     *   1 char for @ symbol
     *   2 chars min for domain left part
     *   2 chars min for domain right part
     */
    public boolean isValid(String email) {
        ensureMaxLengthValid();
        return doValidate(email);
    }

    private void ensureMaxLengthValid() {
        int minLength = MAX_LOCAL_LENGTH + 5;
        boolean valid = maxLength >= minLength && maxLength <= DEFAULT_MAX_LENGTH;
        if (!valid) {
            throw new IllegalStateException("Max length of " + maxLength + " must be greater than or equal to " + minLength + " and less than or equal to " +
                    DEFAULT_MAX_LENGTH + ".");
        }
    }

    private boolean doValidate(String email) {
        try {
            validateFormatAndLength(email);
            return true;
        } catch (RuntimeException ex) {
            return false;
        }
    }

    private void validateFormatAndLength(String email) {
        sanityCheck(email);

        String emailTrimmed = email.trim();
        validateFormat(emailTrimmed);
        validateLengthAndDot(emailTrimmed);
    }

    private void sanityCheck(String email) {
        if (email == null || email.trim().length() == 0) {
            throw new InvalidEmailException();
        }
    }

    private void validateFormat(String email) {
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            throw new InvalidEmailException();
        }
    }

    private void validateLengthAndDot(String email) {
        String local = extractLocal(email);

        if (local.length() > MAX_LOCAL_LENGTH || email.length() > maxLength) {
            throw new InvalidEmailException();
        }

        validateDotInLocal(local);
    }

    private String extractLocal(String email) {
        int pos = email.lastIndexOf("@");
        String local = email.substring(0, pos);
        return local;
    }

    private void validateDotInLocal(String local) {
        if (local.startsWith(".") || local.endsWith(".")) {
            throw new InvalidEmailException();
        }

        if (local.contains("..")) {
            throw new InvalidEmailException();
        }
    }

    private static class InvalidEmailException extends RuntimeException {
    }
}

