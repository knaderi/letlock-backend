/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.controller;

import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.github.javafaker.Faker;
import com.landedexperts.letlock.filetransfer.backend.AbstractTest;
import com.landedexperts.letlock.filetransfer.backend.BackendTestConstants;
import com.landedexperts.letlock.filetransfer.backend.LetlockFiletransferBackendApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = LetlockFiletransferBackendApplication.class)
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
    @Before
    @Transactional
    public void setUp() throws Exception {
        super.setUp();
        Faker faker = new Faker();
        userLoginName = faker.name().firstName() + faker.name().lastName();
        userEmail = faker.internet().emailAddress();
        userPassword = userLoginName + '!';
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
        confirmSignUp(content, reset_token);
    }
    
    protected void createNorConfirmedUser()
            throws Exception, UnsupportedEncodingException {
        String content = registerUser();

        // test login fails at this point since reset-Token has not been confirmed.
        loginForInactiveUser();
 
    }
    

    private String registerUser() throws Exception, UnsupportedEncodingException {
        String uri = "/register";
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri).param("loginName", userLoginName)
                .param("email", userEmail).param("password", userPassword).accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertTrue("Json content is empty ", content.length() > 0);
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
        assertTrue("loginTest: content length should be larger than zero", content.length() > 0);
        assertTrue("loginTest: returnCode should be SUCCESS", content.contains("\"returnCode\":\"USER_NOT_CONFIRMED\""));
    }
    
    private String getPendingUserResetToken() throws Exception, UnsupportedEncodingException {
        String uri3 = "/get_user_object";
        ResultActions resultAction3 = mvc
                .perform(MockMvcRequestBuilders.post(uri3).param("email", userEmail).param("password", userPassword).accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction3.andExpect(ok);
        MvcResult mvcResult3 = resultAction3.andReturn();
        String content3 = mvcResult3.getResponse().getContentAsString();
        String reset_token = getValuesForGivenKey(content3, "resetToken", "");
        return reset_token;
    }
    
    
    private void confirmSignUp(String content, String reset_token) throws Exception, UnsupportedEncodingException {
        String uri2 = "/confirm_signup";
        ResultActions resultAction2 = mvc.perform(MockMvcRequestBuilders.post(uri2)
                .param("email", userEmail).param("resetToken", reset_token).accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction2.andExpect(ok);
        MvcResult mvcResult2 = resultAction2.andReturn();
        String content2 = mvcResult2.getResponse().getContentAsString();
        assertTrue("Json content is empty ", content2.length() > 0);
        assertForNoError("registerUser", content2);
        String userActive = getValuesForGivenKey(content2, "value", "result");
        assertTrue(userActive.contentEquals("true"));
    }

    protected void login() throws Exception {

        String uri = "/login";
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri).param("loginName", userLoginName)
                .param("password", userPassword).accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        token = getValuesForGivenKey(content, "token", "result");
        assertForNoError("login", content);
        assertTrue("loginTest: content length should be larger than zero", content.length() > 0);
        assertTrue("loginTest: returnCode should be SUCCESS", content.contains("\"returnCode\":\"SUCCESS\""));
    }

}
