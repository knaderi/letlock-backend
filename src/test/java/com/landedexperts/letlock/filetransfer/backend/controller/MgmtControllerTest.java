/*******************************************************************************
 * Copyright (C) Landed Experts Technologies Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Proprietary and confidential
 *  Written by Kazem Naderi - 2019
 ******************************************************************************/
package com.landedexperts.letlock.filetransfer.backend.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import com.landedexperts.letlock.filetransfer.backend.session.AppSettingsManager;

public class MgmtControllerTest extends BaseControllerTest {

    @Autowired
    MgmtMapper mgmtMapper;
    List<String> jsonKeyValues = new ArrayList<String>();

    @Override
    @BeforeEach
    @Transactional
    public void setUp() throws Exception {
        super.setUp();
    }
    @Autowired
    private AppSettingsManager appsSettingsManager;
    
    @Test
    public void getIsFreeSignUPCreditForEmail() throws Exception{
        appsSettingsManager.loadAppsSettings();
        boolean isFreeSignup = appsSettingsManager.isFreeSignUpCreditForapps();
        loginAsSystem();
        String updateSettingURL = "/setting/update_apps_setting";
        String isFreeSignUPCreditResponse = "";
        if(isFreeSignup) {
           ResultActions resultAction = mvc
                   .perform(MockMvcRequestBuilders.post(updateSettingURL).param("token", token).param("key", "signup_free_credit").param("value","false").param("app", "all_apps")
                           .accept(MediaType.APPLICATION_JSON_VALUE));
           resultAction.andExpect(ok);
           MvcResult mvcResult = resultAction.andReturn();
           mvcResult.getResponse().getContentAsString();
           isFreeSignUPCreditResponse = isFreeSignUpCredit();
           assertJsonForKeyValue("getIsFreeSignUPCreditForEmail", isFreeSignUPCreditResponse, "result", "true", "equalsTo");
           appsSettingsManager.loadAppsSettings();
           isFreeSignUPCreditResponse = isFreeSignUpCredit();
           assertJsonForKeyValue("getIsFreeSignUPCreditForEmail", isFreeSignUPCreditResponse, "result", "false", "equalsTo");
           assertFalse(appsSettingsManager.isFreeSignUpCreditForapps());
        }else {
            ResultActions resultAction = mvc
                    .perform(MockMvcRequestBuilders.post(updateSettingURL).param("token", token).param("key", "signup_free_credit").param("value","true").param("app", "all_apps")
                            .accept(MediaType.APPLICATION_JSON_VALUE));
            resultAction.andExpect(ok);
            MvcResult mvcResult = resultAction.andReturn();
            mvcResult.getResponse().getContentAsString();
            isFreeSignUPCreditResponse = isFreeSignUpCredit();
            assertJsonForKeyValue("getIsFreeSignUPCreditForEmail", isFreeSignUPCreditResponse, "result", "false", "equalsTo");
            appsSettingsManager.loadAppsSettings();
            isFreeSignUPCreditResponse = isFreeSignUpCredit();
            assertJsonForKeyValue("getIsFreeSignUPCreditForEmail", isFreeSignUPCreditResponse, "result", "true", "equalsTo");
            assertTrue(appsSettingsManager.isFreeSignUpCreditForapps());
        }
    }

    @Test
    public void testIsFreeSignUpCreditForWrongAppName() throws Exception {
        String uri = "/setting/is_free_signup_credit_for_app";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.get(uri).param("appName", "sfdsafds")
                        .accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertForError("isFreeSignUpCredit", content, "APP_SETTING_NOT_FOUND");
    }

    @Test
    public void testIsFreeSignUpCreditFound() throws Exception {
        String content = isFreeSignUpCredit();
        assertForError("isFreeSignUpCredit", content, "SUCCESS");
    }

    private String isFreeSignUpCredit() throws Exception, UnsupportedEncodingException {
        String uri = "/setting/is_free_signup_credit";
        ResultActions resultAction = mvc
                .perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE));

        resultAction.andExpect(ok);
        MvcResult mvcResult = resultAction.andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        return content;
    }

    @Test
    public void testGetAllSettings() {
        AppsSettingsVO[] settings = mgmtMapper.readSettings();
        assertNotNull(settings);
        assertTrue(settings.length >0);
    }
    
    //TODO: The api is not integrated or working
    
    @Test
    public void testAddRedeemCode() throws Exception{
//        String uri = "/setting/add_redeem_code";
//        String loginUrl = "/login";
//        ResultActions resultAction = mvc.perform(MockMvcRequestBuilders.post(loginUrl).param("loginName", "system")
//                .param("password", "Be to rabti naderh!").accept(MediaType.APPLICATION_JSON_VALUE));
//
//        resultAction.andExpect(ok);
//        MvcResult mvcResult = resultAction.andReturn();
//
//        String content = mvcResult.getResponse().getContentAsString();
//        token = getValuesForGivenKey(content, "token", "result");
//        
//        
//        ResultActions addReddemCodeAction = mvc.perform(
//                MockMvcRequestBuilders.post(uri).param("token",token)
//                        .param("packageId","3")
//                        .param("redeemCode",UUID.randomUUID().toString())
//                        .param("partnerName","AppSumo")
//                        .param("action","SIGNUP")
//                        .param("validUntil","2021-04-30 00:00:00.00")
//                        .param("discountValue","100")
//                        .param("discountUnit","percent")
//                        .accept(MediaType.APPLICATION_JSON_VALUE));
//
//        addReddemCodeAction.andExpect(ok);
//        MvcResult mvcAddReddemResult = addReddemCodeAction.andReturn();
//
//        String addRedeemContent = mvcAddReddemResult.getResponse().getContentAsString();
//        assertForNoError("testAddRedeemCode", content);

    }
    
    //Temporary solution to generate redeemcodes for  appsumo
    
    //USE this only when creating redeemcodes
    //@Test
    public void generatePromotionStmtForAppsumo() throws Exception{
        generateRedeemCodesArtifacts("http://www.letlock.io", 10000, "prd");
        generateRedeemCodesArtifacts("http://letlockweb-dev.s3-website-us-west-2.amazonaws.com", 10000,"dev");
        generateRedeemCodesArtifacts("http://letlockweb-qa.s3-website-us-west-2.amazonaws.com", 10000,"qa");
        
    }
    

    public void generateRedeemCodesArtifacts(String host, int numberOfRedeemCodes, String env) throws Exception{
        StringBuffer stmts = new StringBuffer();
        StringBuffer redeemCodesList = new StringBuffer();
        redeemCodesList.append("AppSumo Code,").append("Redeem URL").append("\r\n");
        for (int i=0; i<numberOfRedeemCodes; i++) {
            String redeemCode = UUID.randomUUID().toString();
            stmts.append("INSERT INTO product.package_discount(\r\n" + 
                    "    package_id, code, partner_name, redeem_on_action, valid_until, discount_value, discount_unit, redeemed)" + 
                    "    VALUES (3,'" + redeemCode + "', 'AppSumo','SIGNUP','2021-04-30 00:00:00.00',100, 'percent', false);").append("\r\n");
            
            redeemCodesList.append(redeemCode).append(",").append(host).append("/download/?signUp=true&partnerName=AppSumo&code=").append(redeemCode).append("\r\n");
            
        }

        String redeemCodesInsertStmts = "./redeemcodes/"+env+"-redeemCodes-stmts.sql";
        String redeemCodesCSVFile = "./redeemcodes/" +env+"-AppSumo-LetLock-RedeemCodes.csv";
        printToFile(stmts, redeemCodesInsertStmts);
        printToFile(redeemCodesList, redeemCodesCSVFile);
    }

    private void printToFile(StringBuffer stmts, String redeemCodesInsertStmts) throws IOException {
        BufferedWriter bwr = new BufferedWriter(new FileWriter(new File(redeemCodesInsertStmts)));
        
        //write contents of StringBuffer to a file
        bwr.write(stmts.toString());
        
        //flush the stream
        bwr.flush();
        
        //close the stream
        bwr.close();
    }

}
