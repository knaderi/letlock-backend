/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Irina Soboleva - 2020
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.controller;

import java.io.UnsupportedEncodingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

public class ContactControllerTest extends BaseControllerTest{
    protected String contactUserName = "";
    protected String contactLabel = "MyFirstContact";
    
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        createLoggedInActiveUser();
        contactUserName = userLoginName;
        setUpUserName();
        createLoggedInActiveUser();
        login();
    }
    
    @Test
    public void addContactTest() throws Exception {
        addContact();
    }
    
    private void addContact() throws Exception, UnsupportedEncodingException {
        String uri = "/contacts";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.post(uri)
                        .header("Authorization", "Bearer " + token)
                        .param("contactUserName", contactUserName)
                        .param("contactLabel", contactLabel)
                        .accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("addContactTest", content);
    }
    
    @Test
    public void listContactsTest() throws Exception {
        String content = listContacts();
        assertForNoError("listContactsTest", content);
        Assertions.assertEquals("[]", getValuesForGivenKey(content,"result"), "The contact list should be empty");
        addContact();
        content = listContacts();
        Assertions.assertTrue(content.contains("\"contactUserName\":\"" + contactUserName + "\""), "The list should contain a contact with the given name");
    }
    
    private String listContacts() throws Exception, UnsupportedEncodingException {
        String uri = "/contacts";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.get(uri)
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON_VALUE));
        MvcResult mvcResult = resultAction.andReturn();
        return mvcResult.getResponse().getContentAsString();
    }
    
    @Test
    public void updateContactTest() throws Exception {
        addContact();
        String uri = "/contacts";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.put(uri)
                        .header("Authorization", "Bearer " + token)
                        .param("contactUserName", contactUserName)
                        .param("contactLabel", "NewLabel")
                        .accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("updateContactTest", content);
        String listContent = listContacts();
        Assertions.assertTrue(listContent.contains("\"contactLabel\":\"NewLabel\""), "The list should contain a contact with the updated label");       
    }
}
