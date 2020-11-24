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
        Assertions.assertTrue(content.contains("\"returnMessage\":\"The user password provide is wrong\""),
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
    public void utilTest() throws Exception {
        String[] array = {"matest37016@yopmail.com"
                ,"matest50705@yopmail.com"
                ,"matest51694@yopmail.com"
                ,"matest99700@yopmail.com"
                ,"matest53193@yopmail.com"
                ,"matest91587@yopmail.com"
                ,"matest88588@yopmail.com"
                ,"matest90100@yopmail.com"
                ,"matest40091@yopmail.com"
                ,"matest11902@yopmail.com"
                ,"matest58788@yopmail.com"
                ,"matest2432@yopmail.com"
                ,"matest39922@yopmail.com"
                ,"matest55790@yopmail.com"
                ,"matest95841@yopmail.com"
                ,"matest5887@yopmail.com"
                ,"matest34052@yopmail.com"
                ,"matest86120@yopmail.com"
                ,"matest54678@yopmail.com"
                ,"matest38851@yopmail.com"
                ,"matest74721@yopmail.com"
                ,"matest52121@yopmail.com"
                ,"matest35559@yopmail.com"
                ,"matest5397@yopmail.com"
                ,"matest42653@yopmail.com"
                ,"matest50871@yopmail.com"
                ,"matest24555@yopmail.com"
                ,"matest35843@yopmail.com"
                ,"matest51418@yopmail.com"
                ,"matest67984@yopmail.com"
                ,"matest41034@yopmail.com"
                ,"matest69132@yopmail.com"
                ,"matest45002@yopmail.com"
                ,"matest6733@yopmail.com"
                ,"matest66264@yopmail.com"
                ,"matest30191@yopmail.com"
                ,"matest87375@yopmail.com"
                ,"matest40707@yopmail.com"
                ,"matest43286@yopmail.com"
                ,"matest17335@yopmail.com"
                ,"matest90561@yopmail.com"
                ,"matest2793@yopmail.com"
                ,"matest51396@yopmail.com"
                ,"matest55646@yopmail.com"
                ,"matest22925@yopmail.com"
                ,"matest45698@yopmail.com"
                ,"matest60733@yopmail.com"
                ,"matest52765@yopmail.com"
                ,"matest43867@yopmail.com"
                ,"matest40979@yopmail.com"
                ,"matest99515@yopmail.com"
                ,"matest10751@yopmail.com"
                ,"matest65401@yopmail.com"
                ,"matest7272@yopmail.com"
                ,"matest13467@yopmail.com"
                ,"matest88946@yopmail.com"
                ,"matest30180@yopmail.com"
                ,"matest47414@yopmail.com"
                ,"matest15527@yopmail.com"
                ,"matest22910@yopmail.com"
                ,"matest90535@yopmail.com"
                ,"matest39606@yopmail.com"
                ,"matest33840@yopmail.com"
                ,"matest52256@yopmail.com"
                ,"matest25633@yopmail.com"
                ,"matest36606@yopmail.com"
                ,"matest95284@yopmail.com"
                ,"matest59525@yopmail.com"
                ,"matest34368@yopmail.com"
                ,"matest27857@yopmail.com"
                ,"matest23326@yopmail.com"
                ,"matest12063@yopmail.com"
                ,"matest49828@yopmail.com"
                ,"matest46931@yopmail.com"
                ,"matest79616@yopmail.com"
                ,"matest99509@yopmail.com"
                ,"matest36890@yopmail.com"
                ,"matest2827@yopmail.com"
                ,"matest19174@yopmail.com"
                ,"matest81436@yopmail.com"
                ,"matest63946@yopmail.com"
                ,"matest88460@yopmail.com"
                ,"matest62711@yopmail.com"
                ,"matest58559@yopmail.com"
                ,"matest3122@yopmail.com"
                ,"matest15644@yopmail.com"
                ,"matest40037@yopmail.com"
                ,"matest76381@yopmail.com"
                ,"matest27933@yopmail.com"
                ,"matest12240@yopmail.com"
                ,"matest51682@yopmail.com"
                ,"matest76434@yopmail.com"
                ,"matest47586@yopmail.com"
                ,"matest53806@yopmail.com"
                ,"matest41941@yopmail.com"
                ,"matest54518@yopmail.com"
                ,"matest18796@yopmail.com"
                ,"matest25163@yopmail.com"
                ,"matest72195@yopmail.com"


};
        for (String value: array) {
            System.out.println("with signedup_accounts AS (\r\n" + 
                    "Select reset_token from users.users where email = crypt('" + value + "', email))\r\n" + 
                    "UPDATE users.users\r\n" + 
                    "                SET reset_token = null, status='active'\r\n" + 
                    "                WHERE (email  = CRYPT('" + value + "', email)) and status='pending' and reset_token = (select reset_token from  signedup_accounts)\r\n" + 
                    "\r\n" + 
                    ";");
        }
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
