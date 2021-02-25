/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.controller;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.github.javafaker.Faker;
import com.landedexperts.letlock.filetransfer.backend.AbstractTest;
import com.landedexperts.letlock.filetransfer.backend.BackendTestConstants;
import com.landedexperts.letlock.filetransfer.backend.LetlockFiletransferBackendApplication;
import com.landedexperts.letlock.filetransfer.backend.utils.LoginNameValidator;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes=LetlockFiletransferBackendApplication.class)
@WebAppConfiguration
public abstract class BaseControllerTest extends AbstractTest implements BackendTestConstants {

    protected String lineSeparator = System.getProperty("line.separator");

    protected static ResultMatcher ok = MockMvcResultMatchers.status().isOk();

    protected String userEmail = "";
    protected String userLoginName = "";
    protected String userPassword = "";
    protected String resetToken = "";
    protected String userId = "";
    protected String token = "";

    @Override
    @BeforeEach
    @Transactional
    public void setUp() throws Exception {
        super.setUp();
        setUpUserName();
    }

    /**
     * This method is a helper function to create a user, activate it and login as
     * the new user to allow the test run for the new user.
     * 
     * @throws Exception
     * @throws UnsupportedEncodingException
     */
    protected void createLoggedInActiveUser()
            throws Exception, UnsupportedEncodingException {
        String content = registerUser();

        // test login fails at this point since reset-Token has not been confirmed.
        loginForInactiveUser();
        // no get reset token for signup confirmation to mimick what user would do
        // through email.
        String reset_token = getPendingUserResetToken();
        // Mimik user clicking on signup confirmation to enable his account and change
        // account status from pending to active
        confirmSignUp(reset_token);
    }
    
    protected void createNorConfirmedUser()
            throws Exception, UnsupportedEncodingException {
        String content = registerUser();

        // test login fails at this point since reset-Token has not been confirmed.
        loginForInactiveUser();
 
    }
    
    protected void setUpUserName()
            throws Exception, UnsupportedEncodingException {
        Faker faker = new Faker();
        boolean validLoginName = false;
        while (!validLoginName) {
            userLoginName = faker.name().firstName() + faker.name().lastName();
            if(LoginNameValidator.isValid(userLoginName)) {
                validLoginName = true;
            }
        }               
        userEmail = faker.internet().emailAddress();
        userPassword = userLoginName + '!';
 
    }
    

    private String registerUser() throws Exception, UnsupportedEncodingException {
        String uri = "/register";
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri).param("loginName", userLoginName)
                .param("email", userEmail).param("password", userPassword).accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertTrue(content.length() > 0, "Json content is empty ");
        assertForNoError("registerUser", content);
        userId = getValuesForGivenKey(content, "id", "result");
        return content;
    }

    private void loginForInactiveUser() throws Exception {

        String uri = "/login";
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri).param("loginName", userLoginName)
                .param("password", userPassword).accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        token = getValuesForGivenKey(content, "token", "result");
        Assertions.assertTrue(content.length() > 0, "loginTest: content length should be larger than zero");
        Assertions.assertTrue(content.contains("\"returnCode\":\"USER_NOT_CONFIRMED\""), "loginTest: returnCode should be SUCCESS");
    }
    
    void loginAsSystem() throws Exception {

        String uri = "/login";
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri).param("loginName", "System")
                .param("password", "Be to rabti naderh!").accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        token = getValuesForGivenKey(content, "token", "result");
        Assertions.assertTrue(content.length() > 0, "loginTest: content length should be larger than zero");
    }
    
    private String getPendingUserResetToken() throws Exception, UnsupportedEncodingException {
        String uri3 = "/get_reset_token";
        ResultActions resultAction3 = mvc
                .perform(MockMvcRequestBuilders.post(uri3).param("email", userEmail).param("password", userPassword).accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction3.andExpect(ok);
        MvcResult mvcResult3 = resultAction3.andReturn();
        String content3 = mvcResult3.getResponse().getContentAsString();
        String reset_token = getValuesForGivenKey(content3, "resetToken", "result");
        return reset_token;
    }
    
    
    private void confirmSignUp(String reset_token) throws Exception, UnsupportedEncodingException {
        String uri2 = "/confirm_signup";
        ResultActions resultAction2 = mvc.perform(MockMvcRequestBuilders.post(uri2)
                .param("email", userEmail).param("resetToken", reset_token).accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction2.andExpect(ok);
        MvcResult mvcResult2 = resultAction2.andReturn();
        String content2 = mvcResult2.getResponse().getContentAsString();
        Assertions.assertTrue(content2.length() > 0, "Json content is empty ");
        assertForNoError("registerUser", content2);
        String userActive = getValuesForGivenKey(content2, "value", "result");
        Assertions.assertTrue(userActive.contentEquals("true"));
    }

    protected void login() throws Exception {

      loginAsUser(userLoginName, userPassword);
    }
    
    protected void loginAsUser(String userName, String password) throws Exception {

        String uri = "/login";
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri).param("loginName", userName)
                .param("password", password).accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        token = getValuesForGivenKey(content, "token", "result");
        assertForNoError("login", content);
        Assertions.assertTrue(content.length() > 0, "loginTest: content length should be larger than zero");
        Assertions.assertTrue(content.contains("\"returnCode\":\"SUCCESS\""), "loginTest: returnCode should be SUCCESS");
    }
    
    public String makeMockRequest(String uri, String method, Map<String,String> params, Boolean expectOk
            ) throws Exception {
        MockHttpServletRequestBuilder req;
        switch (method) {
        case "post":
            req = MockMvcRequestBuilders.post(uri);
            break;
        case "get":
            req = MockMvcRequestBuilders.get(uri);
            break;
        case "put":
            req = MockMvcRequestBuilders.put(uri);
            break;
        case "delete":
            req = MockMvcRequestBuilders.delete(uri);
            break;
        default: 
            throw new Exception("Unknown method");
        };
        req.header("Authorization", "Bearer " + token);
        params.forEach((k,v) -> req.param(k, v));
        req.accept(MediaType.APPLICATION_JSON_VALUE);
        ResultActions resultAction = mvc.perform(req);
        if (expectOk) {
            resultAction.andExpect(ok);
        }
        MvcResult mvcResult = resultAction.andReturn();
        return mvcResult.getResponse().getContentAsString();
    }


}
