package com.landedexperts.letlock.filetransfer.backend.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ContactUsModelTest {

    @Test
    void testValidate() {
        ContactUsModel model = new ContactUsModel();
        model.setEmail("john.doe@landedexperts.com");
        Assertions.assertEquals("First name submitted cannot be empty or contain html.\r\n" + 
                "Last name submitted cannot be empty or contain html.\r\n" + 
                "Message content cannot be empty or contain html.\r\n" + 
                "Subject submitted cannot be empty or contain html.", model.validate());
        model.setFirstName("John");
        Assertions.assertEquals("Last name submitted cannot be empty or contain html.\r\n" + 
                "Message content cannot be empty or contain html.\r\n" + 
                "Subject submitted cannot be empty or contain html.", model.validate());
        model.setLastName("Doe");
        Assertions.assertEquals("Message content cannot be empty or contain html.\r\n" + 
                "Subject submitted cannot be empty or contain html.", model.validate());
        model.setSubject("test subject");
        Assertions.assertEquals("Message content cannot be empty or contain html.", model.validate());
        model.setUserMessage("test user message");
        Assertions.assertEquals(ContactUsModel.VALID_MSG, model.validate());
        
    }
    
    @Test
    void testValidateForConetntWithHtML() {
        ContactUsModel model = new ContactUsModel();
        model.setEmail("john.doe@landedexperts.com");
        model.setFirstName("John");
        model.setLastName("Doe");
        model.setSubject("test subject");
        model.setUserMessage("test user message </html>");
        Assertions.assertEquals("Message content cannot be empty or contain html.", model.validate());
        
    }
    
    @Test
    void testValidateForSubjectWithHtML() {
        ContactUsModel model = new ContactUsModel();
        model.setEmail("john.doe@landedexperts.com");
        model.setFirstName("John");
        model.setLastName("Doe");
        model.setSubject("<b>test subject </b>");
        model.setUserMessage("test user message");
        Assertions.assertEquals("Subject submitted cannot be empty or contain html.", model.validate());
        
    }
    
    @Test
    void testValidateForContentOverFourThousandsChar() {
        ContactUsModel model = new ContactUsModel();
        model.setEmail("john.doe@landedexperts.com");
        model.setFirstName("John");
        model.setLastName("Doe");
        model.setSubject("test subject");
        model.setUserMessage(RandomStringUtils.randomAlphanumeric(4000));
        Assertions.assertEquals(ContactUsModel.VALID_MSG, model.validate());
        model.setUserMessage(RandomStringUtils.randomAlphanumeric(4001));
        Assertions.assertEquals("Message content cannot be longer than 4000 characters.", model.validate());
        
    }

}
