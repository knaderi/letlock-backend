/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.utils;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class LoginNameValidatorTest {
	

    public LoginNameValidatorTest() {
        super();
    }


    public void testEmptyLoginName() {
        assertNotValid("");
        assertNotValid(null);
        assertNotValid(" ");
    }

    private void assertValid(String loginName) {
        Assertions.assertTrue(LoginNameValidator.isValid(loginName));
    }

    private void assertNotValid(String loginName) {
        Assertions.assertFalse(LoginNameValidator.isValid(loginName));
    }

    @Test
    public void testLeadingTrailingSpaces() {
        assertValid("  someloginname");
        assertValid("  someloginname  ");
        assertValid("someloginname");
        assertValid("someloginname  ");
    }


    @Test
    public void testShortLoginName() {
        // it is valid but our validator does not allow it
        assertNotValid(" ");
        assertNotValid(" ab");
        assertNotValid(" ");
        assertNotValid(null);
        assertNotValid(" ");
        assertNotValid("");
    }
    
    @Test
    public void testLongLoginName() {
        // it is valid but our validator does not allow it
        assertNotValid("dsgfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfsdfdsfsddfs");
    }
    
    @Test
    public void testBadLoginName() {
        // it is valid but our validator does not allow it
        assertNotValid("HelloThere!");
    }
}
