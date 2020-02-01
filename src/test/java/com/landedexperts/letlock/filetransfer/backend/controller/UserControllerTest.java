package com.landedexperts.letlock.filetransfer.backend.controller;

import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

public class UserControllerTest extends BaseControllerTest {

    @Override
    @Before
    @Transactional
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void registerTest() throws Exception {
        String uri = "/register";
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri).param("loginName", userLoginName)
                .param("email", userEmail).param("password", userPassword).accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("registerTest", content);
        registerUserAgain(uri, userLoginName, userEmail, userPassword);
    }


    @Test
    public void isLoginNameAvailableTest() throws Exception {
        registerUser();

        String uri = "/user_is_login_name_available";
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri).param("loginName", userLoginName)
                .param("password", userPassword).accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("isLoginNameAvailableTest", content);
    }

    @Test
    public void logoutTestForGoodToken() throws Exception {
        registerUser();
        login();

        String uri = "/logout";
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri).param("loginName", userLoginName)
                .param("token", "badToken").accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue("logoutTestForGoodToken: length should be larger than zero", content.length() > 0);
        assertTrue("logoutTestForGoodToken: The error should be Login session  not found",
                content.contains("\"returnCode\":\"LOGIN_SESSION_NOT_FOUND\""));
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
                content.contains("\"returnCode\":\"LOGIN_SESSION_NOT_FOUND\""));
        assertTrue("logoutTestForBadToken: result should be false", content.contains("\"result\":false"));
    }

    public String getValuesForGivenKey(String jsonArrayStr, String key) throws Exception {
        JSONObject jsonObject = new JSONObject(jsonArrayStr);
        return jsonObject.getString(key);
    }

    @Test
    public void updateUserPasswordTest() throws Exception {
        registerUser();
        // change the password to new password and login
        handleForgotPassword();
        login();
        // Change the password back and re-login.
        changePassword(userLoginName, userPassword, NEW_PASSWORD);
        userPassword = NEW_PASSWORD;
        login();
    }

    private void changePassword(String loginName, String oldPassword, String newPassword) throws Exception {
        String uri = "/update_user_password";
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri).param("loginName", loginName)
                .param("oldPassword", oldPassword).param("newPassword", newPassword).accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("changePassword", content);
    }

    @Test
    //TODO: fix this
    public void getFileTransferSessionsForUserWithNoTransferSessionsTest() throws Exception {
//        registerUser();
//
//        // login the sender and get token
//        login();
//
//        String uri = "/get_file_transfer_sessions_for_user";
//        ResultActions resultAction = mvc
//                .perform(MockMvcRequestBuilders.post(uri).param("token", token).accept(MediaType.APPLICATION_JSON_VALUE));
//
//        resultAction.andExpect(ok);
//        MvcResult mvcResult = resultAction.andReturn();
//
//        String content = mvcResult.getResponse().getContentAsString();
//
//        assertForNoError("getFileTransferSessionsForUserWithNoTransferSessionsTest", content);
//        assertTrue("Content value should be empty", content.contains("\"value\":[]"));
    }

    @Test
    public void handleForgotPasswordWhenEmaiIsRegistered() throws Exception {
        registerUser();
        String uri = "/handle_forgot_password";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).param("email", userEmail).param("resetToken", "")
                        .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("handleForgotPasswordWhenEmaiIsRegistered", content);
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
        registerUser();
        handleForgotPassword();

        String uri2 = "/validate_reset_password_token";
        ResultActions resultAction2 = mvc
                .perform(MockMvcRequestBuilders.post(uri2).param("email", userEmail).param("token", resetToken)
                        .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction2.andExpect(ok);
        MvcResult mvcResult2 = resultAction2.andReturn();

        String content2 = mvcResult2.getResponse().getContentAsString();

        assertForNoError("validateResetPasswordTokenTestForValidToken", content2);
    }

    @Test
    public void resetPasswordTest() throws Exception {
        registerUser();
        handleForgotPassword();

        String uri2 = "/reset_password";
        ResultActions resultAction2 = mvc.perform(MockMvcRequestBuilders.post(uri2).param("token", resetToken)
                .param("email", userEmail).param("newPassword", "passw0rd!")
                .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction2.andExpect(ok);
        MvcResult mvcResult2 = resultAction2.andReturn();

        String content2 = mvcResult2.getResponse().getContentAsString();

        assertForNoError("resetPasswordTest", content2);
    }

    @Test
    public void resetPasswordTokenTestForInvalidToken() throws Exception {
        registerUser();
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
        assertTrue("Should have error", content2.contains("\"returnCode\":\"USER_NOT_FOUND\""));
        assertTrue("Content error message should be token is invalid", content2.contains("\"returnMessage\":\"Invalid token or email\""));
    }

    private void handleForgotPassword() throws Exception, UnsupportedEncodingException, JSONException {
        String uri = "/handle_forgot_password";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).param("email", userEmail).accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("resetPasswordTest", content);
        //make sure reset token gets created.
        testtResetToken();
    }

    @Test
    public void resetPasswordForInvalidPasswordTest() throws Exception {
        registerUser();
        handleForgotPassword();
        String uri = "/reset_password";
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri).param("token", resetToken)
                .param("email", userEmail).param("newPassword", "")
                .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        assertTrue("Content have error", content.contains("\"returnCode\":\"INVALID_PASSWORD\""));
        assertTrue("Content error message should be token is invalid", content.contains("\"returnMessage\":\"Password is invalid\""));
        assertTrue("Content value should be false", content.contains("\"result\":false"));
    }





    private void registerUserAgain(String uri, String senderLoginName, String senderEmail, String senderPassword)
            throws Exception, UnsupportedEncodingException {
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri).param("loginName", senderLoginName)
                .param("email", senderEmail).param("password", senderPassword).accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.length() > 0);
        assertTrue("registerTest: content length should be larger than zero", content.length() > 0);
        assertTrue("content should be USER_NAME_TAKEN", content.contains("\"returnCode\":\"USER_NAME_TAKEN"));
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