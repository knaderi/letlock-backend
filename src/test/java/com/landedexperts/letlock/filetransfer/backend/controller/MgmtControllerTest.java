/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.landedexperts.letlock.filetransfer.backend.database.mybatis.mapper.MgmtMapper;
import com.landedexperts.letlock.filetransfer.backend.database.mybatis.vo.AppsSettingsVO;

public class MgmtControllerTest extends BaseControllerTest {

    @Autowired
    MgmtMapper mgmtMapper;

    @Override
    @BeforeEach
    @Transactional
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testIsFreeSignUpCreditForWrongAppName() throws Exception {
        String uri = "/setting/is_free_signup_credit";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.get(uri).param("token", token).param("appName", "client_web_app1")
                        .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertForError("isFreeSignUpCredit", content, "APP_SETTING_NOT_FOUND");
    }

    @Test
    public void testIsFreeSignUpCreditFound() throws Exception {
        String uri = "/setting/is_free_signup_credit";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.get(uri).param("token", token).param("appName", "client_web_app")
                        .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertForError("isFreeSignUpCredit", content, "SUCCESS");
    }

    @Test
    public void testGetAllSettings() {
        AppsSettingsVO[] settings = mgmtMapper.readSettings();
        assertNotNull(settings);
        assertTrue(settings.length >0);
    }

}
