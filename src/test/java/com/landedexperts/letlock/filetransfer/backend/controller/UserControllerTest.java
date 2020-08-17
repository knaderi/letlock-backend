/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.controller;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UserControllerTest extends BaseControllerTest {

    @Override
    @BeforeEach
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
    public void getUserProfileTest() throws Exception {
        createLoggedInActiveUser();
        login();
        String uri = "/user/get_user_profile";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.get(uri).param("token", token).accept(MediaType.APPLICATION_JSON_VALUE));
        
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("getUserProfileTest", content);
    }
    
    @Test
    public void getUserProfileTestForBadToken() throws Exception {
        createLoggedInActiveUser();
        login();
        String uri = "/user/get_user_profile";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.get(uri).param("token", "123").accept(MediaType.APPLICATION_JSON_VALUE));
        
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertForError("getUserProfileTest", content, "TOKEN_INVALID");
    }

    @Test
    public void isLoginNameAvailableTest() throws Exception {
        createLoggedInActiveUser();

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
        createLoggedInActiveUser();
        login();

        String uri = "/user/logout";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).param("token", token).accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(content.length() > 0, "logoutTestForGoodToken: length should be larger than zero");
        Assertions.assertTrue(
                content.contains("\"returnCode\":\"SUCCESS\""),
                "logoutTestForGoodToken: User should be logged out successfully.");
        Assertions.assertTrue(content.contains("\"value\":true"), "logoutTestForGoodToken: result should be true");
    }

    @Test
    public void logoutTestForBadToken() throws Exception {
        String uri = "/user/logout";
        ResultActions resultAction = mvc.perform(
                MockMvcRequestBuilders.post(uri).param("token", "badToken").accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(content.length() > 0, "logoutTestForBadToken:content length should be larger than zero");
        Assertions.assertTrue(
                content.contains("\"returnCode\":\"LOGIN_SESSION_NOT_FOUND\""),
                "logoutTestForBadToken: The error should be Login session  not found");
        Assertions.assertTrue(content.contains("\"value\":false"), "logoutTestForBadToken: result should be false");
    }

    public String getValuesForGivenKey(String jsonArrayStr, String key, String parent) throws Exception {
        JSONObject jsonObject = new JSONObject(jsonArrayStr);
        if (!StringUtils.isBlank(parent)) {
            return jsonObject.getJSONObject(parent).getString(key);
        }
        return jsonObject.getString(key);
    }

    public String getValuesForGivenKey(String jsonArrayStr, String key) throws Exception {
        return getValuesForGivenKey(jsonArrayStr, key, "");
    }

    @Test
    public void changeUserPasswordTest() throws Exception {
        createLoggedInActiveUser();
        // change the password to new password and login
        // handleForgotPassword();
        login();
        // Change the password back and re-login.
        changePassword(token, userLoginName, userEmail, userPassword, NEW_PASSWORD);
        userPassword = NEW_PASSWORD;
        login();
    }

    private void changePassword(String token, String loginName, String email, String oldPassword, String newPassword) throws Exception {
        String uri = "/user/change_password";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).param("token", token).param("loginName", loginName).param("email", email)
                        .param("oldPassword", oldPassword).param("newPassword", newPassword).accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("changePassword", content);
    }

    @Test
    // TODO: fix this
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
//        Assertions.assertTrue("Content value should be empty", content.contains("\"value\":[]"));
    }

    @Test
    public void handleForgotPasswordWhenEmaiIsRegistered() throws Exception {
        createLoggedInActiveUser();
        String uri = "/handle_forgot_password";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).param("email", userEmail)
                        .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("handleForgotPasswordWhenEmaiIsRegistered", content);
        // make sure reset token is created
        testtResetToken();
    }

    private void testtResetToken() throws Exception, UnsupportedEncodingException, JSONException {
        ResultActions resultAction2 = mvc
                .perform(MockMvcRequestBuilders.post("/get_reset_token").param("email", userEmail).param("password", userPassword)
                        .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction2.andExpect(ok);
        MvcResult mvcResult2 = resultAction2.andReturn();

        String content2 = mvcResult2.getResponse().getContentAsString();
        resetToken = getValuesForGivenKey(content2, "resetToken", "result");
        Assertions.assertTrue(resetToken.length() > 0);
    }

    @Test
    public void validateResetPasswordTokenTestForValidToken() throws Exception {
        createLoggedInActiveUser();
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
        createLoggedInActiveUser();
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
        createLoggedInActiveUser();
        handleForgotPassword();
        String uri2 = "/validate_reset_password_token";
        String wrongToken = "1234567213";
        ResultActions resultAction2 = mvc
                .perform(MockMvcRequestBuilders.post(uri2).param("email", userEmail).param("token", wrongToken)
                        .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction2.andExpect(ok);
        MvcResult mvcResult2 = resultAction2.andReturn();

        String content2 = mvcResult2.getResponse().getContentAsString();

        Assertions.assertTrue(content2.length() > 0, "Content length should be larger than 0");
        Assertions.assertTrue(content2.contains("\"returnCode\":\"USER_NOT_FOUND\""), "Should have error");
        Assertions.assertTrue(content2.contains("\"returnMessage\":\"Invalid token or email\""),
                "Content error message should be token is invalid");
    }

    private void handleForgotPassword() throws Exception, UnsupportedEncodingException, JSONException {
        String uri = "/handle_forgot_password";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri).param("email", userEmail).accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("resetPasswordTest", content);
        // make sure reset token gets created.
        testtResetToken();
    }

    @Test
    public void resetPasswordForInvalidPasswordTest() throws Exception {
        createLoggedInActiveUser();
        handleForgotPassword();
        String uri = "/reset_password";
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri).param("token", resetToken)
                .param("email", userEmail).param("newPassword", "")
                .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("\"returnCode\":\"INVALID_PASSWORD\""), "Content have error");
        Assertions.assertTrue(content.contains("\"returnMessage\":\"Password is invalid\""),
                "Content error message should be token is invalid");
        Assertions.assertTrue(content.contains("\"value\":false"), "Content value should be false");
    }

    @Test
    public void resendSignUpConfirmationEmailNeeded() throws Exception {
        createNorConfirmedUser();
        String uri = "/resend_signup_email";
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri)
                .param("loginId", userEmail).param("password", userPassword)
                .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("\"returnCode\":\"SUCCESS\""), "Content have error");
    }

    @Test
    public void resendSignUpConfirmationEmailWhenNotNeeded() throws Exception {
        createLoggedInActiveUser();
        String uri = "/resend_signup_email";
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri)
                .param("loginId", userEmail).param("password", userPassword)
                .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("\"returnCode\":\"NO_CONFIRMATION_NEEDED\""), "Content have error");
    }

    private void registerUserAgain(String uri, String senderLoginName, String senderEmail, String senderPassword)
            throws Exception, UnsupportedEncodingException {
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri).param("loginName", senderLoginName)
                .param("email", senderEmail).param("password", senderPassword).accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(content.length() > 0);
        Assertions.assertTrue(content.length() > 0, "registerTest: content length should be larger than zero");
        Assertions.assertTrue(content.contains("\"returnCode\":\"USER_NAME_TAKEN"), "content should be USER_NAME_TAKEN");
    }

    @Test
    public void testValidContactUsForm() throws Exception, UnsupportedEncodingException {
        String uri = "/user/message";
        ContactUsModel contactUsModel = new ContactUsModel();
        contactUsModel.setFirstName("firstName");
        contactUsModel.setLastName("lastName");
        contactUsModel.setEmail("test@test.com");
        contactUsModel.setPhone("604-345-1786");
        contactUsModel.setSubject("subject");
        contactUsModel.setUserMessage("This is user's message.");

        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(contactUsModel))
                .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        Assertions.assertTrue(content.contains("\"returnCode\":\"SUCCESS\""), "Content have error");
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
