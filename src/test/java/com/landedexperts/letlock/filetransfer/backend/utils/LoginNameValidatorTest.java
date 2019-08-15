package com.landedexperts.letlock.filetransfer.backend.utils;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;


public class LoginNameValidatorTest {
	
	LoginNameValidator validator;
	
    public LoginNameValidatorTest() {
        super();
    }

    @Before
    public void beforeTest() {
        validator = new LoginNameValidator();
    }

    public void testEmptyLoginName() {
        assertNotValid("");
        assertNotValid(null);
        assertNotValid(" ");
    }

    private void assertValid(String loginName) {
        assertTrue(validator.isValid(loginName));
    }

    private void assertNotValid(String loginName) {
        assertFalse(validator.isValid(loginName));
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
