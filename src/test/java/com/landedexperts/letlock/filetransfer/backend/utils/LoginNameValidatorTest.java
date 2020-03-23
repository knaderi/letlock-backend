/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.utils;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;


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
        assertTrue(LoginNameValidator.isValid(loginName));
    }

    private void assertNotValid(String loginName) {
        assertFalse(LoginNameValidator.isValid(loginName));
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
