package com.landedexperts.letlock.filetransfer.backend.service;

import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.landedexperts.letlock.filetransfer.backend.AbstractTest;
import com.landedexperts.letlock.filetransfer.backend.BackendTestConstants;

public class BackendServiceTest extends AbstractTest implements BackendTestConstants {


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
        loginUser();
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

    private String loginUser() throws Exception {
        String uri = "/login";
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri).param("loginName", TEST_USER_ID)
                .param("password", TEST_PASSWORD).accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        return getValuesForGivenKey(content, "token");
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
        assertTrue(content.length() > 0);
        assertTrue(content.contains("\"result\":true"));
    }


}
