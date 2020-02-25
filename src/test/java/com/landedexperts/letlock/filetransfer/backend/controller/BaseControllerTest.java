package com.landedexperts.letlock.filetransfer.backend.controller;

import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
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
import com.landedexperts.letlock.filetransfer.backend.database.LetLockPGDataSource;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = LetlockFiletransferBackendApplication.class)
@WebAppConfiguration
public abstract class BaseControllerTest extends AbstractTest implements BackendTestConstants{

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
    
    protected void registerUser()
            throws Exception, UnsupportedEncodingException {
        String uri = "/register";
        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(uri).param("loginName", userLoginName)
                .param("email", userEmail).param("password", userPassword).accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertTrue("Json content is empty ", content.length() > 0);        
        assertForNoError("registerUser", content);
        userId = getValuesForGivenKey(content, "id", "result");
        
        
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
