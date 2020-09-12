/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.landedexperts.letlock.filetransfer.backend.AbstractTest;

public class EmailValidatorTest extends AbstractTest {
    private EmailValidator validator;

    public EmailValidatorTest() {
        super();
    }

    @BeforeEach
    public void beforeTest() {
        validator = new EmailValidator();
    }

    public void testEmptyEmail() {
        assertNotValid("");
        assertNotValid(null);
        assertNotValid(" ");
    }

    private void assertValid(String email) {
        Assertions.assertTrue(validator.isValid(email));
    }

    private void assertNotValid(String email) {
        Assertions.assertFalse(validator.isValid(email));
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
        // This is a valid email but our current validator (also in PTY) does not
        // support it
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
        Assertions.assertEquals(EmailValidator.DEFAULT_MAX_LENGTH, email.length());
        assertValid(email);
    }

    @Test
    public void testLengthTooLong() {
        String email = "user@" + stringOf(EmailValidator.DEFAULT_MAX_LENGTH - 8) + ".com";
        Assertions.assertEquals(EmailValidator.DEFAULT_MAX_LENGTH + 1, email.length());
        assertNotValid(email);
    }

    @Test
    public void testCustomMaxLength() {
        validator = new EmailValidator(128);
        String email = "user@" + stringOf(119) + ".com"; // 128 max
        Assertions.assertEquals(128, email.length());
        assertValid(email);
    }

    @Test
    public void testCustomMaxLengthTooShort() {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            validator = new EmailValidator(EmailValidator.MAX_LOCAL_LENGTH); // too short
            assertValid("user@abc.com");
        });
    }

    @Test
    public void testCustomMaxLengthShortByOne() {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            validator = new EmailValidator(EmailValidator.MAX_LOCAL_LENGTH + 4); // still too short
            assertValid("user@abc.com");
        });
    }

    @Test
    public void testCustomMaxLengthTooLong() {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            validator = new EmailValidator(EmailValidator.DEFAULT_MAX_LENGTH + 1); // too short
            assertValid("user@abc.com");
        });
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

//    @Test
//    public void testAntideo() throws Exception {
//
//        String url = "http://api.antideo.com/email/asdfdsaf@sdfd.com";
//        HttpClient client = HttpClientBuilder.create().build();
//        HttpGet request = new HttpGet(url);
//
//        // add request header
//        HttpResponse response = client.execute(request);
//        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
//        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//        StringBuffer result = new StringBuffer();
//        String line = "";
//        while ((line = rd.readLine()) != null)
//            result.append(line);
//        AntideoEmailValiationVO content = null;
//            AntideoEmailValiationVO vo = new Gson().fromJson(result.toString(),
//                    AntideoEmailValiationVO.class);
//        System.out.println(vo);      
//
//    }
}
