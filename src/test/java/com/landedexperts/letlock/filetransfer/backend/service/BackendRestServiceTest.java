package com.landedexperts.letlock.filetransfer.backend.service;

import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.github.javafaker.Faker;
import com.landedexperts.letlock.filetransfer.backend.AbstractTest;
import com.landedexperts.letlock.filetransfer.backend.BackendTestConstants;

public class BackendRestServiceTest extends AbstractTest implements BackendTestConstants {
    String lineSeparator = System.getProperty("line.separator");

    private static ResultMatcher ok = MockMvcResultMatchers.status().isOk();

    String userEmail = "";
    String userLoginName = "";
    String userPassword = "";
    String resetToken = "";

    @Override
    @Before
    @Transactional
    public void setUp() throws Exception {
        super.setUp();
        Faker faker = new Faker();
        userLoginName = faker.name().firstName() + faker.name().lastName();
        userEmail = faker.internet().emailAddress();
        userPassword = userLoginName + '!';
        // registerUser(userLoginName, userEmail, userPassword);
    }

    @Test
    public void registerTest() throws Exception {
        String uri = "/register";
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri).param("loginName", userLoginName)
                .param("email", userEmail).param("password", userPassword).accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.length() > 0);
        assertTrue("registerTest: content length should be larger than zero", content.length() > 0);
        assertTrue("There should not be any error", content.contains("\"errorCode\":\"NO_ERROR"));

        registerUserAgain(uri, userLoginName, userEmail, userPassword);
    }

    @Test
    public void loginTest() throws Exception {
        Faker faker = new Faker();
        String senderLoginName = faker.name().firstName() + faker.name().lastName();

        String senderEmail = faker.internet().emailAddress();
        String senderPassword = senderLoginName + '!';
        registerUser(senderLoginName, senderEmail, senderPassword);

        String content = login(senderLoginName, senderPassword);
        assertTrue("loginTest: content length should be larger than zero", content.length() > 0);
        assertTrue("loginTest: errorCode should be NO_ERROR", content.contains("\"errorCode\":\"NO_ERROR\""));
    }

    @Test
    public void isLoginNameAvailableTest() throws Exception {
        Faker faker = new Faker();
        String senderLoginName = faker.name().firstName() + faker.name().lastName();
        String emailAdress = faker.internet().emailAddress();
        String senderPassword = senderLoginName + '!';

        registerUser(senderLoginName, emailAdress, senderPassword);

        String uri = "/user_is_login_name_available";
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri).param("loginName", senderLoginName)
                .param("password", senderPassword).accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue("isLoginNameAvailableTest: content should be larger than zero", content.length() > 0);
        assertTrue("isLoginNameAvailableTest: Content should have no error", content.contains("\"errorCode\":\"NO_ERROR\""));
        assertTrue("isLoginNameAvailableTest: result should be false", content.contains("\"result\":false"));
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
        assertTrue("logoutTestForGoodToken: length should be larger than zero", content.length() > 0);
        assertTrue("logoutTestForGoodToken: The error should be Login session  not found",
                content.contains("\"errorCode\":\"LOGIN_SESSION_NOT_FOUND\""));
        assertTrue("logoutTestForGoodToken: result should be false", content.contains("\"result\":false"));
    }

    @Test
    public void logoutTestForBadToken() throws Exception {
        String uri = "/logout";
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri).param("loginName", TEST_USER_ID)
                .param("token", "badToken").accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue("logoutTestForBadToken:content length should be larger than zero", content.length() > 0);
        assertTrue("logoutTestForBadToken: The error should be Login session  not found",
                content.contains("\"errorCode\":\"LOGIN_SESSION_NOT_FOUND\""));
        assertTrue("logoutTestForBadToken: result should be false", content.contains("\"result\":false"));
    }

    public String getValuesForGivenKey(String jsonArrayStr, String key) throws Exception {
        JSONObject jsonObject = new JSONObject(jsonArrayStr);
        return jsonObject.getString(key);
    }

    @Test
    public void updateUserPasswordTest() throws Exception {
        registerUser(userLoginName, userEmail, userPassword);
        // change the password to new password and login
        handleForgotPassword();
        login(userLoginName, userPassword);
        // Change the password back and re-login.
        changePassword(userLoginName, userPassword, NEW_PASSWORD);
        login(userLoginName, NEW_PASSWORD);
    }

    private void changePassword(String loginName, String oldPassword, String newPassword) throws Exception {
        String uri = "/update_user_password";
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri).param("loginName", loginName)
                .param("oldPassword", oldPassword).param("newPassword", newPassword).accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue("changePassword:Content length should be larger than zero", content.length() > 0);
        assertTrue("changePassword: result should be true", content.contains("\"result\":true"));
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
        assertTrue("Content error message should be empty", content.contains("\"errorMessage\":\"\""));
        assertTrue("Content value should be empty", content.contains("\"value\":[]"));
    }

    @Test
    public void handleForgotPasswordWhenEmaiIsRegistered() throws Exception {
        registerUser(userLoginName, userEmail, userPassword);
        String uri = "/handle_forgot_password";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).param("email", userEmail).param("resetToken", "")
                        .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue("Content length should be larger than 0", content.length() > 0);
        assertTrue("Should not have any errors", content.contains("\"errorCode\":\"NO_ERROR\""));
        assertTrue("Content error message should be empty", content.contains("\"errorMessage\":\"\""));
        assertTrue("Content value should b true", content.contains("\"result\":true"));
        //make sure reset token is created
        testtResetToken();
    }

    private void testtResetToken() throws Exception, UnsupportedEncodingException, JSONException {
        ResultActions resultAction2 = mvc
                .perform(MockMvcRequestBuilders.post("/get_user_object").param("email", userEmail)
                        .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction2.andExpect(ok);
        MvcResult mvcResult2 = resultAction2.andReturn();

        String content2 = mvcResult2.getResponse().getContentAsString();
        JSONObject jsonObject = new JSONObject(content2);
        resetToken = jsonObject.getString("resetToken");
        assertTrue(resetToken.length() > 0);
    }

    @Test
    public void validateResetPasswordTokenTestForValidToken() throws Exception {
        registerUser(userLoginName, userEmail, userPassword);
        handleForgotPassword();

        String uri2 = "/validate_reset_password_token";
        ResultActions resultAction2 = mvc
                .perform(MockMvcRequestBuilders.post(uri2).param("email", userEmail).param("token", resetToken)
                        .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction2.andExpect(ok);
        MvcResult mvcResult2 = resultAction2.andReturn();

        String content2 = mvcResult2.getResponse().getContentAsString();

        assertTrue("Content length should be larger than 0", content2.length() > 0);
        assertTrue("Should not have any errors", content2.contains("\"errorCode\":\"NO_ERROR\""));
        assertTrue("Content error message should be empty", content2.contains("\"errorMessage\":\"\""));
        assertTrue("Content value should be true", content2.contains("\"result\":true"));
    }

    @Test
    public void resetPasswordTest() throws Exception {
        registerUser(userLoginName, userEmail, userPassword);
        handleForgotPassword();

        String uri2 = "/reset_password";
        ResultActions resultAction2 = mvc.perform(MockMvcRequestBuilders.post(uri2).param("token", resetToken)
                .param("email", userEmail).param("newPassword", "passw0rd!")
                .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction2.andExpect(ok);
        MvcResult mvcResult2 = resultAction2.andReturn();

        String content2 = mvcResult2.getResponse().getContentAsString();

        assertTrue("Content length should be larger than 0", content2.length() > 0);
        assertTrue("Should not have any errors", content2.contains("\"errorCode\":\"NO_ERROR\""));
        assertTrue("Content error message should be empty", content2.contains("\"errorMessage\":\"\""));
        assertTrue("Content value should b true", content2.contains("\"result\":true"));
    }

    @Test
    public void resetPasswordTokenTestForInvalidToken() throws Exception {
        registerUser(userLoginName, userEmail, userPassword);
        handleForgotPassword();
        String uri2 = "/validate_reset_password_token";
        String wrongToken = "1234567213";
        ResultActions resultAction2 = mvc
                .perform(MockMvcRequestBuilders.post(uri2).param("email", userEmail).param("token", wrongToken)
                        .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction2.andExpect(ok);
        MvcResult mvcResult2 = resultAction2.andReturn();

        String content2 = mvcResult2.getResponse().getContentAsString();

        assertTrue("Content length should be larger than 0", content2.length() > 0);
        assertTrue("Should have error", content2.contains("\"errorCode\":\"USER_NOT_FOUND\""));
        assertTrue("Content error message should be token is invalid", content2.contains("\"errorMessage\":\"Invalid token or email\""));
        assertTrue("Content value should be false", content2.contains("\"result\":false"));
    }

    private void handleForgotPassword() throws Exception, UnsupportedEncodingException, JSONException {
        String uri = "/handle_forgot_password";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).param("email", userEmail).accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue("Content length should be larger than 0", content.length() > 0);
        assertTrue("Should not have any errors " + content, content.contains("\"errorCode\":\"NO_ERROR\""));
        assertTrue("Content error message should be empty", content.contains("\"errorMessage\":\"\""));
        assertTrue("Content value should b true", content.contains("\"result\":true"));
        //make sure reset token gets created.
        testtResetToken();
    }

    @Test
    public void resetPasswordForInvalidPasswordTest() throws Exception {
        registerUser(userLoginName, userEmail, userPassword);
        handleForgotPassword();
        String uri = "/reset_password";
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri).param("token", resetToken)
                .param("email", userEmail).param("newPassword", "")
                .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        assertTrue("Content have error", content.contains("\"errorCode\":\"INVALID_PASSWORD\""));
        assertTrue("Content error message should be token is invalid", content.contains("\"errorMessage\":\"Password is invalid\""));
        assertTrue("Content value should be false", content.contains("\"result\":false"));
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

    private void registerUserAgain(String uri, String senderLoginName, String senderEmail, String senderPassword)
            throws Exception, UnsupportedEncodingException {
        // try to resgister the user again should result in an error
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri).param("loginName", senderLoginName)
                .param("email", senderEmail).param("password", senderPassword).accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.length() > 0);
        assertTrue("registerTest: content length should be larger than zero", content.length() > 0);
        assertTrue("content should be USER_NAME_TAKEN", content.contains("\"errorCode\":\"USER_NAME_TAKEN"));
    }

    private String login(String senderLoginName, String password) throws Exception, UnsupportedEncodingException {
        String uri = "/login";
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri).param("loginName", senderLoginName)
                .param("password", password).accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        return content;
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
