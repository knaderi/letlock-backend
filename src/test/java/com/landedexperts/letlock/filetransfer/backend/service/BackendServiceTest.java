package com.landedexperts.letlock.filetransfer.backend.service;

import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.github.javafaker.Faker;

import com.landedexperts.letlock.filetransfer.backend.AbstractTest;
import com.landedexperts.letlock.filetransfer.backend.BackendTestConstants;

public class BackendServiceTest extends AbstractTest implements BackendTestConstants {
    String lineSeparator = System.getProperty("line.separator");
    private final Logger logger = LoggerFactory.getLogger(BackendServiceTest.class);

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
        assertTrue(content.contains("\"result\":false"));
        assertTrue(content.contains("\"errorCode\":\"USER_NAME_TAKEN"));
    }

    @Test
    public void loginTest() throws Exception {
        String testPassword = TEST_PASSWORD;
        String content = login(testPassword);
        assertTrue(content.length() > 0);
        assertTrue(content.contains("\"errorCode\":\"NO_ERROR\""));
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
        assertTrue(content.length() > 0);
        assertTrue(content.contains("\"errorCode\":\"NO_ERROR\""));
        assertTrue(content.contains("\"result\":false"));
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
        assertTrue(content.length() > 0);
        assertTrue(content.contains("\"errorCode\":\"LOGIN_SESSION_NOT_FOUND\""));
        assertTrue(content.contains("\"result\":false"));
    }

    @Test
    public void logoutTestForBadToken() throws Exception {
        String uri = "/logout";
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri).param("loginName", TEST_USER_ID)
                .param("token", "badToken").accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.length() > 0);
        assertTrue(content.contains("\"errorCode\":\"LOGIN_SESSION_NOT_FOUND\""));
        assertTrue(content.contains("\"result\":false"));
    }

    private String loginUser(String loginName, String password) throws Exception {
        String uri = "/login";
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri).param("loginName", loginName)
                .param("password", password).accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        return getValuesForGivenKey(content, "token");
    }

    private void registerUser(String loginName, String email, String password) throws Exception {
        String uri = "/register";
        mvc.perform(MockMvcRequestBuilders.post(uri).param("loginName", loginName).param("password", password)
                .param("email", email).accept(MediaType.APPLICATION_JSON_VALUE));
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
        ResultActions resultAction = mvc.perform(
                MockMvcRequestBuilders.post(uri).param("loginName", loginName).param("oldPassword", oldPassword)
                        .param("newPassword", newPassword).accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.length() > 0);
        assertTrue(content.contains("\"result\":true"));
    }

    @Test
    public void getFileTransferSessionsForUserWithNoTransferSessionsTest() throws Exception {
        Faker faker = new Faker();

        // create a random user as receiver
        String receiverFirstName = faker.name().firstName() + faker.name().firstName();
        String receiverEmail = faker.internet().emailAddress();
        String receiverPassword = receiverFirstName + '!';
        registerUser(receiverFirstName, receiverEmail, receiverPassword);

        // create a random user as sender
        String senderFirstName = faker.name().firstName() + faker.name().firstName();
        String senderEmail = faker.internet().emailAddress();
        String senderPassword = senderFirstName + '!';
        registerUser(senderFirstName, senderEmail, senderPassword);

        // login the sender and get token
        String senderToken = loginUser(senderEmail, senderPassword);

        String uri = "/get_file_transfer_sessions_for_user";
        ResultActions resultAction = mvc.perform(
                MockMvcRequestBuilders.post(uri).param("token", senderToken).accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        assertTrue(content.length() > 0);
        assertTrue(content.contains("\"errorCode\":\"NO_ERROR\""));
        assertTrue(content.contains("\"errorMessage\":\"\""));
        assertTrue(content.contains("\"value\":[]"));
    }

    resultAction.andExpect(ok);

    MvcResult mvcResult = resultAction.andReturn();

    String content = mvcResult.getResponse().getContentAsString();

    assertTrue(content.length() > 0);
        assertTrue(content.contains("\"errorCode\":\"NO_ERROR\""));
        assertTrue(content.contains("\"errorMessage\":\"\""));
        assertTrue(content.contains("\"value\":[]"));
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
