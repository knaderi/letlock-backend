package com.landedexperts.letlock.filetransfer.backend.service;



import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.javafaker.Faker;
import com.landedexperts.letlock.filetransfer.backend.AbstractTest;
import com.landedexperts.letlock.filetransfer.backend.BackendTestConstants;
import com.landedexperts.letlock.filetransfer.backend.database.jpa.UserDTO;

public class UserServiceTest extends AbstractTest implements BackendTestConstants {

    @Autowired
    UserService userService;
    
    Faker faker = null;
    
    
    @Override
    @Before
    public void setUp() {
        faker = new Faker();
        super.setUp();
    }
    
    @Test
    public void getUserByEmailUsingUnexisitingEmail() throws Exception {
        String email = faker.internet().emailAddress();
        Optional<UserDTO> user = userService.findUserByEmail(email);
        assertFalse("email" + email +  "should not be in the system",user.isPresent());
        
    }
    
    @Test
    public void getUserByEmailUsingExisitingEmail() throws Exception {
        Optional<UserDTO> user = userService.findUserByEmail(TEST_EMAIL);
        assertTrue("email" + TEST_EMAIL +  "should be in the system",user.isPresent());
        
    }
}
