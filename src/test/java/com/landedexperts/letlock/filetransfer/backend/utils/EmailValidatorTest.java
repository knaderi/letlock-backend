/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;


public class EmailValidatorTest {
    private EmailValidator validator;

    public EmailValidatorTest() {
        super();
    }

    @Before
    public void beforeTest() {
        validator = new EmailValidator();
    }

    public void testEmptyEmail() {
        assertNotValid("");
        assertNotValid(null);
        assertNotValid(" ");
    }

    private void assertValid(String email) {
        assertTrue(validator.isValid(email));
    }

    private void assertNotValid(String email) {
        assertFalse(validator.isValid(email));
    }

    @Test
    public void testLeadingTrailingSpaces() {
        assertValid("  user@abc.com");
        assertValid("  user@abc.com  ");
        assertValid("user@abc.com");
        assertValid("user@abc.com  ");
    }

    @Test
    public void testBadDomain() {
        assertNotValid("a@b.c");
        assertNotValid("a@.abc.com");
        assertNotValid("a@com1");
    }

    @Test
    public void testSimpleEmails() {
        assertValid("a@a.co");
        assertValid("a@abc.com");
        assertValid("niceandsimple@example.com");
        assertValid("a.little.lengthy.but.fine@dept.example.com");
        assertValid("disposable.style.email.with+symbol@example.com");
        assertValid("other.email-with-dash@example.com");
    }

    @Test
    public void testMixedCase() {
        assertValid("user@abc.com");
        assertValid("USER@abc.COM");
        assertValid("USER@ABC.COM");
    }

    @Test
    public void testIPv6Email() {
        // This is a valid email but our current validator (also in PTY) does not support it
        assertNotValid("user@[IPv6:2001:db8:1ff::a0b:dbd0]");
    }

    @Test
    public void testUnsualEmails() {
        assertValid("\"more.unusual\"@example.com");
        assertValid("\"very.unusual.@.unusual.com\"@example.com");
        assertValid("!#$%&'*+-/=?^_`{}|~@example.org");
    }

    @Test
    public void testSpaceInLocal() {
        // it is a valid email but our validator does not permit spaces
        assertNotValid("\"much.more unusual\"@example.com");
        assertNotValid("\" \"@example.org");
    }

    @Test
    public void testUnicodeEmails() {
        // it is valid but our validator does not allow it
        assertNotValid("�������@example.com");
        assertNotValid("�������@�������.com");
    }

    @Test
    public void testInvalidCharactersAtEnd() {
        assertNotValid("homerj@abc.com_");
        assertNotValid("homerj@abc.com.");
        assertNotValid("homerj@abc.com-");
    }

    @Test
    public void testMaxLocalLength() {
        String email = stringOf(64) + "@abc.com";
        assertValid(email);
    }

    private String stringOf(int n) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < n; i++) {
            b.append("a");
        }
        return b.toString();
    }

    @Test
    public void testLocalTooLong() {
        String email = stringOf(65) + "@abc.com";
        assertNotValid(email);
    }

    @Test
    public void testMaxLength() {
        String email = "user@" + stringOf(EmailValidator.DEFAULT_MAX_LENGTH - 9) + ".com";
        assertEquals(EmailValidator.DEFAULT_MAX_LENGTH, email.length());
        assertValid(email);
    }

    @Test
    public void testLengthTooLong() {
        String email = "user@" + stringOf(EmailValidator.DEFAULT_MAX_LENGTH - 8) + ".com";
        assertEquals(EmailValidator.DEFAULT_MAX_LENGTH + 1, email.length());
        assertNotValid(email);
    }

    @Test
    public void testCustomMaxLength() {
        validator = new EmailValidator(128);
        String email = "user@" + stringOf(119) + ".com"; // 128 max
        assertEquals(128, email.length());
        assertValid(email);
    }

    @Test(expected = IllegalStateException.class)
    public void testCustomMaxLengthTooShort() {
        validator = new EmailValidator(EmailValidator.MAX_LOCAL_LENGTH); // too short
        assertValid("user@abc.com");
    }

    @Test(expected = IllegalStateException.class)
    public void testCustomMaxLengthShortByOne() {
        validator = new EmailValidator(EmailValidator.MAX_LOCAL_LENGTH + 4); // still too short
        assertValid("user@abc.com");
    }

    @Test(expected = IllegalStateException.class)
    public void testCustomMaxLengthTooLong() {
        validator = new EmailValidator(EmailValidator.DEFAULT_MAX_LENGTH + 1); // too short
        assertValid("user@abc.com");
    }

    @Test
    public void testSpecialChars() {
        assertValid("\"()[]\\;,<>@\"@abc.com");
        assertValid("!#$%&'*+-/=?^_`{|}.~@abc.com");
    }

    @Test
    public void testDots() { 
        assertValid("homer.j@abc.com");
        assertNotValid("homerj.@abc.com");
        assertNotValid(".homerj@abc.com");
        assertNotValid("homer..j@abc.com");
        assertNotValid("homerj@.abc.com");
    }
}
