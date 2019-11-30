package com.landedexperts.letlock.filetransfer.backend.service;

import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.github.javafaker.Faker;
import com.landedexperts.letlock.filetransfer.backend.AbstractTest;
import com.landedexperts.letlock.filetransfer.backend.BackendTestConstants;
import com.landedexperts.letlock.filetransfer.backend.database.jpa.UserDTO;
import com.landedexperts.letlock.filetransfer.backend.database.jpa.types.UserStatusType;

public class BackendServiceTest extends AbstractTest implements BackendTestConstants {
    String lineSeparator = System.getProperty("line.separator");
    
    @Autowired
    private UserService userService; // using JPA

    private static ResultMatcher ok = MockMvcResultMatchers.status().isOk();

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void registerTest() throws Exception {
        String uri = "/register";

        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri).param("loginName", TEST_USER_ID)
                .param("email", TEST_EMAIL).param("password", TEST_PASSWORD).accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.length() > 0);
        assertTrue("registerTest: content length should be larger than zero", content.length() > 0);
        assertTrue("content should be USER_NAME_TAKEN", content.contains("\"errorCode\":\"USER_NAME_TAKEN"));
    }

    @Test
    public void loginTest() throws Exception {
        String testPassword = TEST_PASSWORD;
        String content = login(testPassword);
        assertTrue("loginTest: content length should be larger than zero", content.length() > 0);
        assertTrue("loginTest: errorCode should be NO_ERROR",content.contains("\"errorCode\":\"NO_ERROR\""));
    }

    private String login(String password) throws Exception, UnsupportedEncodingException {
        String uri = "/login";
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri).param("loginName", TEST_USER_ID)
                .param("password", password).accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        return content;
    }

    @Test
    public void isLoginNameAvailableTest() throws Exception {
        String uri = "/user_is_login_name_available";
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri).param("loginName", TEST_USER_ID)
                .param("password", TEST_PASSWORD).accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue("isLoginNameAvailableTest: content should be larger than zero",content.length() > 0);
        assertTrue("isLoginNameAvailableTest: Content should have no error", content.contains("\"errorCode\":\"NO_ERROR\""));
        assertTrue("isLoginNameAvailableTest: result should be false",content.contains("\"result\":false"));
    }

    @Test
    public void logoutTestForGoodToken() throws Exception {
        loginUser(TEST_USER_ID, TEST_PASSWORD);

        String uri = "/logout";
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri).param("loginName", TEST_USER_ID)
                .param("token", "badToken").accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue("logoutTestForGoodToken: length should be larger than zero",content.length() > 0);
        assertTrue("logoutTestForGoodToken: The error should be Login session  not found" , content.contains("\"errorCode\":\"LOGIN_SESSION_NOT_FOUND\""));
        assertTrue("logoutTestForGoodToken: result should be false",content.contains("\"result\":false"));
    }

    @Test
    public void logoutTestForBadToken() throws Exception {
        String uri = "/logout";
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri).param("loginName", TEST_USER_ID)
                .param("token", "badToken").accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue("logoutTestForBadToken:content length should be larger than zero",content.length() > 0);
        assertTrue("logoutTestForBadToken: The error should be Login session  not found" , content.contains("\"errorCode\":\"LOGIN_SESSION_NOT_FOUND\""));
        assertTrue("logoutTestForBadToken: result should be false",content.contains("\"result\":false"));
    }

    private String loginUser(String loginName, String password) throws Exception {
        String uri = "/login";
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri).param("loginName", loginName).param("password", password)
                .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        return getValuesForGivenKey(content, "token");
    }

    private void registerUser(String loginName, String email, String password) throws Exception {
        String uri = "/register";
        mvc.perform(MockMvcRequestBuilders.post(uri).param("loginName", loginName).param("password", password).param("email", email)
                .accept(MediaType.APPLICATION_JSON_VALUE));
    }

    public String getValuesForGivenKey(String jsonArrayStr, String key) throws Exception {
        JSONObject jsonObject = new JSONObject(jsonArrayStr);
        return jsonObject.getString(key);
    }

    @Test
    public void updateUserPasswordTest() throws Exception {
        // change the password to new password and login
        changePassword(TEST_USER_ID, TEST_PASSWORD, NEW_PASSWORD);
        login(NEW_PASSWORD);
        // Change the password back and re-login.
        changePassword(TEST_USER_ID, NEW_PASSWORD, TEST_PASSWORD);
        login(TEST_PASSWORD);
    }

    private void changePassword(String loginName, String oldPassword, String newPassword) throws Exception {
        String uri = "/update_user_password";
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri).param("loginName", loginName)
                .param("oldPassword", oldPassword).param("newPassword", newPassword).accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue("changePassword:Content length should be larger than zero",content.length() > 0);
        assertTrue("changePassword: result should be true",content.contains("\"result\":true"));
    }

    @Test
    public void getFileTransferSessionsForUserWithNoTransferSessionsTest() throws Exception {
        Faker faker = new Faker();

        // create a random user as sender
        String senderFirstName = faker.name().firstName() + faker.name().firstName();
        String senderEmail = faker.internet().emailAddress();
        String senderPassword = senderFirstName + '!';
        registerUser(senderFirstName, senderEmail, senderPassword);

        // login the sender and get token
        String senderToken = loginUser(senderEmail, senderPassword);

        String uri = "/get_file_transfer_sessions_for_user";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).param("token", senderToken).accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        assertTrue("Content length should be larger than 0", content.length() > 0);
        assertTrue("Should not have any errors", content.contains("\"errorCode\":\"NO_ERROR\""));
        assertTrue("Content error message should be empty",content.contains("\"errorMessage\":\"\""));
        assertTrue("Content value should be empty",content.contains("\"value\":[]"));
    }

    @Test
    public void isEmailRegisteredTestWhenEmaiIsRegistered() throws Exception {
        Faker faker = new Faker();

        // create a random user as sender
        String userFirstName = faker.name().firstName() + faker.name().firstName();
        String userEmail = faker.internet().emailAddress();
        String userPassword = userFirstName + '!';
        registerUser(userFirstName, userEmail, userPassword);

        String uri = "/handle_forgot_password_request";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).param("email", userEmail).accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        assertTrue("Content length should be larger than 0", content.length() > 0);
        assertTrue("Should not have any errors", content.contains("\"errorCode\":\"NO_ERROR\""));
        assertTrue("Content error message should be empty",content.contains("\"errorMessage\":\"\""));
        assertTrue("Content value should b true",content.contains("\"result\":true"));
    }
    
    @Test
    public void isEmailRegisteredTestWhenEmaiIsNotRegistered() throws Exception {
        Faker faker = new Faker();
        String userEmail = faker.internet().emailAddress();
        String uri = "/handle_forgot_password_request";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).param("email", userEmail).accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        assertTrue("Content length should be larger than 0", content.length() > 0);
        assertTrue("Should have error", content.contains("\"errorCode\":\"USER_NOT_FOUND\""));
        assertTrue("Content error message should be empty",content.contains("\"errorMessage\":\"User with given email address does not exist\""));
        assertTrue("Content value should be true",content.contains("\"result\":false"));
    }
    
    @Test
    public void validateResetPasswordTokenTestForValidToken() throws Exception {
        
        Faker faker = new Faker();

        // create a random user as sender
        String userFirstName = faker.name().firstName() + faker.name().firstName();
        String userEmail = faker.internet().emailAddress();
        String userPassword = userFirstName + '!';
        registerUser(userFirstName, userEmail, userPassword);

        String uri = "/handle_forgot_password_request";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).param("email", userEmail).accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        assertTrue("Content length should be larger than 0", content.length() > 0);
        assertTrue("Should not have any errors", content.contains("\"errorCode\":\"NO_ERROR\""));
        assertTrue("Content error message should be empty",content.contains("\"errorMessage\":\"\""));
        assertTrue("Content value should b true",content.contains("\"result\":true"));
        
        
        
        Optional<UserDTO> userContainer = userService.findUserByEmailAndStatus(userEmail, UserStatusType.active);
        
        
        String uri2 = "/validate_reset_password_token";
        ResultActions resultAction2 = mvc
                .perform(MockMvcRequestBuilders.post(uri2).param("token", userContainer.get().getResetToken()).accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction2.andExpect(ok);
        MvcResult mvcResult2 = resultAction2.andReturn();

        String content2 = mvcResult2.getResponse().getContentAsString();

        assertTrue("Content length should be larger than 0", content2.length() > 0);
        assertTrue("Should not have any errors", content2.contains("\"errorCode\":\"NO_ERROR\""));
        assertTrue("Content error message should be empty",content2.contains("\"errorMessage\":\"\""));
        assertTrue("Content value should be true",content2.contains("\"result\":true"));
    }
 
    @Test
    public void resetPasswordTest() throws Exception {
        
        Faker faker = new Faker();

        // create a random user as sender
        String userFirstName = faker.name().firstName() + faker.name().firstName();
        String userEmail = faker.internet().emailAddress();
        String userPassword = userFirstName + '!';
        registerUser(userFirstName, userEmail, userPassword);
        
        
        
        Optional<UserDTO> userContainer = userService.findUserByEmail(userEmail);
       
        
        
        String uri = "/handle_forgot_password_request";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).param("email", userEmail).accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        assertTrue("Content length should be larger than 0", content.length() > 0);
        assertTrue("Should not have any errors", content.contains("\"errorCode\":\"NO_ERROR\""));
        assertTrue("Content error message should be empty",content.contains("\"errorMessage\":\"\""));
        assertTrue("Content value should b true",content.contains("\"result\":true"));
        
        
        Optional<UserDTO> userContainer2 = userService.findUserByEmail(userEmail);
        
        
        String uri2 = "/reset_password";
        ResultActions resultAction2 = mvc
                .perform(MockMvcRequestBuilders.post(uri2).param("token", userContainer2.get().getResetToken()).param("loginName", userContainer2.get().getEmail()).param("newPassword", "passw0rd!").accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult2 = resultAction2.andReturn();

        String content2 = mvcResult2.getResponse().getContentAsString();

        assertTrue("Content length should be larger than 0", content2.length() > 0);
        assertTrue("Should not have any errors", content2.contains("\"errorCode\":\"NO_ERROR\""));
        assertTrue("Content error message should be empty",content2.contains("\"errorMessage\":\"\""));
        assertTrue("Content value should b true",content2.contains("\"result\":true"));
    }
    
    @Test
    public void resetPasswordTokenTestForInvalidToken() throws Exception {
        String uri = "/validate_reset_password_token";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).param("token", "1234567213").accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        assertTrue("Content length should be larger than 0", content.length() > 0);
        assertTrue("Should have error", content.contains("\"errorCode\":\"INVALID_RESET_PASSWORD_TOKEN\""));
        assertTrue("Content error message should be token is invalid",content.contains("\"errorMessage\":\"Token is invalid.\""));
        assertTrue("Content value should be false",content.contains("\"result\":false"));
    }
    
    
  
    
    @Test
    public void resetPasswordForInvalidPasswordTest() throws Exception {
        String uri = "/reset_password";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).param("token", "1234563456").param("loginName", "knaderi12").param("newPassword", "passw0rd!").accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        assertTrue("Content have error", content.contains("\"errorCode\":\"INVALID_RESET_PASSWORD_TOKEN\""));
        assertTrue("Content error message should be token is invalid",content.contains("\"errorMessage\":\"Token is invalid.\""));
        assertTrue("Content value should be false",content.contains("\"result\":false"));
    }
    
    
    @Test
    public void getFileTransferSessionsForUserWith1TransferSessionTest() throws Exception {
        // Faker faker = new Faker();

        // // create a random user as receiver
        // String receiverFirstName = faker.name().firstName() +
        // faker.name().firstName();
        // String receiverEmail = faker.internet().emailAddress();
        // String receiverPassword = receiverFirstName + '!';
        // registerUser(receiverFirstName, receiverEmail, receiverPassword);

        // // create a random user as sender
        // String senderFirstName = faker.name().firstName() + faker.name().firstName();
        // String senderEmail = faker.internet().emailAddress();
        // String senderPassword = senderFirstName + '!';
        // registerUser(senderFirstName, senderEmail, senderPassword);

        // // login the sender and get token
        // String senderToken = loginUser(senderEmail, senderPassword);

        // TODO: create a test with transfer session
        // currently we cannot start a FileTransferSession because we have no access to
        // gochain
        // we will implement this test once db_gateway is completed
    }

}
