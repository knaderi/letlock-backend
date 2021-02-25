package com.landedexperts.letlock.filetransfer.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.landedexperts.letlock.filetransfer.backend.session.TwoFAManager;

import static com.landedexperts.letlock.filetransfer.backend.BackendConstants.TEMP_TOKEN_PREFIX;
import com.landedexperts.letlock.filetransfer.backend.utils.ResponseCode;

public class TwoFAControllerTest extends BaseControllerTest {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
        createLoggedInActiveUser();
        login();
    }
    
    @Test
    public void TwoFALoginTest() throws Exception {
        updateSettings(true);
        logout();
        twoFALogin();
        
        Map<String, String> params = new HashMap<String, String>();
        params.put("sendTo", userEmail);
        params.put("type","email");
        String content = makeMockRequest("/2fa_login_code", "get", params, true);
        assertForNoError("get_2fa_login_code", content);
        String code = TwoFAManager.getInstance().generateCode(token);
        
        // test for invalid code
        content = verifyCode("123");
        Assertions.assertEquals(ResponseCode.CODE_INVALID.name(),
                getValuesForGivenKey(content, "returnCode"));
        Assertions.assertEquals("2", getValuesForGivenKey(content, "attemptsAvailable", "result"),
                "Should have 2 attempts left");
        
        // test for valid code
        content = verifyCode(code);
        assertForNoError("verifyCode", content);
        token = getValuesForGivenKey(content, "token", "result");
        
        // Disable 2FA settings to check that user is logged in 
        //      and authorized to perform account settings change
        updateSettings(false);
        
    }
    
    private void updateSettings(Boolean enable) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("password", userPassword);
        params.put("email", userEmail);
        params.put("enabled", enable.toString());
        params.put("phoneNumber", "");
        String content = makeMockRequest("/2fa_settings", "put", params, true);
        assertForNoError("enable2FA", content);
    }
    
    private void logout() throws Exception {
        String content = makeMockRequest("/user/logout", "post", new HashMap<String, String>(), true);
        assertForNoError("logout", content);

    }
    
    private void twoFALogin() throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("loginName", userLoginName);
        params.put("password", userPassword);
        String content = makeMockRequest("/login", "post", params, true);
        Assertions.assertEquals("true",
                getValuesForGivenKey(content, "twoFARequired", "result"),
                "Should require the second authentication step");
        token = getValuesForGivenKey(content, "token", "result");
        Assertions.assertTrue(
                token.startsWith(TEMP_TOKEN_PREFIX),
                "User session should have a TEMP token");
    }
    
    private String verifyCode(String code) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("code", code);
        return makeMockRequest("/2fa_login_code", "put", params, true);
    }
}
