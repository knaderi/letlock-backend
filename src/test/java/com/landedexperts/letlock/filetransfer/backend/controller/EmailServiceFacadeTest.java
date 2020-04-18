/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.controller;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.landedexperts.letlock.filetransfer.backend.AbstractTest;

public class EmailServiceFacadeTest extends AbstractTest {

    @Autowired
    EmailServiceFacade emailSErviceFacade;

    @BeforeAll
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterAll
    public static void tearDownAfterClass() throws Exception {
    }

    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @AfterEach
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetForgotPasswordHTMLEmailBody() throws Exception {

        // read forgot password email template and make sure it contains the replacement
        // tokens
        String emailBody = emailSErviceFacade.readForgotPasswordEmailBody();
        Assertions.assertNotNull(emailBody);
        Assertions.assertTrue(emailBody.contains(EmailServiceFacade.USER_CONFIRM_TOKEN));
        Assertions.assertTrue(emailBody.contains(EmailServiceFacade.VALIDATE_RESET_PASSWORD_SERVICE_URL_TOKEN));
        Assertions.assertTrue(emailBody.contains(EmailServiceFacade.LETLOCK_LOGO_URL_TOKEN));
        Assertions.assertTrue(emailBody.contains(EmailServiceFacade.LETLOCK_FOOTER_LOGO_TOKEN));

        // read replace forgot password email template and make sure it contains values
        // instead of tokens
        String dummyResetToken = "123456789";
        String dummyEmailToken = "dummy-token@example.com";
        emailBody = emailSErviceFacade.getForgotPasswordHTMLEmailBody(dummyResetToken, dummyEmailToken);
        Assertions.assertNotNull(emailBody);
        Assertions.assertFalse(emailBody.contains(EmailServiceFacade.USER_CONFIRM_TOKEN));
        Assertions.assertFalse(emailBody.contains(EmailServiceFacade.EMAIL_TOKEN));
        Assertions.assertFalse(emailBody.contains(EmailServiceFacade.VALIDATE_RESET_PASSWORD_SERVICE_URL_TOKEN));
        Assertions.assertFalse(emailBody.contains(EmailServiceFacade.LETLOCK_LOGO_URL_TOKEN));
        Assertions.assertFalse(emailBody.contains(EmailServiceFacade.LETLOCK_FOOTER_LOGO_TOKEN));

        Assertions.assertTrue(
                emailBody.contains(emailSErviceFacade.validateResetPasswordTokenURL),"The email does not contain the resetPaswordTokenURL");
        Assertions.assertTrue(emailBody.contains(emailSErviceFacade.letlockLogoURL), "The email does not contain the logo url");
        Assertions.assertTrue(emailBody.contains(emailSErviceFacade.letlockFooterLogoURL),"The email does not contain the footer logo url");
        Assertions.assertTrue(emailBody.contains(dummyResetToken),"The email does not contain the token");

    }

}
