/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.StringUtils;


public class InstallerControllerTest extends BaseControllerTest {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }
    
    @Test
    public void testDownloadWindowsInstaller() throws Exception {
        String uri = "/installer/windows";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.get(uri)
                        .accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        Assertions.assertFalse(StringUtils.isEmpty(getValuesForGivenKey(content,"result")),"Installer download link is empty");
    }
    
    @Test
    public void testResetInstallersCache() throws Exception {
        String uri = "/installer";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.put(uri)
                        .accept(MediaType.APPLICATION_JSON_VALUE));
        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertForNoError("testResetInstallersCache", content);
    }

}
