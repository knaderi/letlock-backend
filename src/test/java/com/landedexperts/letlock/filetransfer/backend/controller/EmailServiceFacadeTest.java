/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.landedexperts.letlock.filetransfer.backend.AbstractTest;

public class EmailServiceFacadeTest extends AbstractTest {

    @Autowired
    EmailServiceFacade emailSErviceFacade;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetForgotPasswordHTMLEmailBody() throws Exception {

        // read forgotpassword email template and make sure it contains the replacement
        // tokens
        String emailBody = emailSErviceFacade.readForgotPasswordEmailBody();
        assertNotNull(emailBody);
        assertTrue(emailBody.contains(EmailServiceFacade.RESET_TOKEN));
        assertTrue(emailBody.contains(EmailServiceFacade.VALIDATE_RESET_PASSWORD_SERVICE_URL_TOKEN));
        assertTrue(emailBody.contains(EmailServiceFacade.LETLOCK_LOGO_URL_TOKEN));
        assertTrue(emailBody.contains(EmailServiceFacade.LETLOCK_FOOTER_LOGO_TOKEN));

        // read replace forgotpassword email template and make sure it contains values
        // instead of tokens
        String dummyResetToken = "123456789";
        emailBody = emailSErviceFacade.getForgotPasswordHTMLEmailBody(dummyResetToken);
        assertNotNull(emailBody);
        assertFalse(emailBody.contains(EmailServiceFacade.RESET_TOKEN));
        assertFalse(emailBody.contains(EmailServiceFacade.VALIDATE_RESET_PASSWORD_SERVICE_URL_TOKEN));
        assertFalse(emailBody.contains(EmailServiceFacade.LETLOCK_LOGO_URL_TOKEN));
        assertFalse(emailBody.contains(EmailServiceFacade.LETLOCK_FOOTER_LOGO_TOKEN));

        assertTrue("The email does not contain the resetPaswordTokenURL",
                emailBody.contains(emailSErviceFacade.validateResetPasswordTokenURL));
        assertTrue("The email does not contain the logo url", emailBody.contains(emailSErviceFacade.letlockLogoURL));
        assertTrue("The email does not contain the footer logo url", emailBody.contains(emailSErviceFacade.letlockFooterLogoURL));
        assertTrue("The email does not contain the token", emailBody.contains(dummyResetToken));

    }

}
