package com.landedexperts.letlock.filetransfer.backend.session;

import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.tomcat.util.json.JSONParser;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;


public class AuthenticationManager {
    static final String SETTINGS_FILE_NAME = "src/main/resources/auth/auth-settings.json";
    
    private static AuthenticationManager singleInstance = null;

    public static AuthenticationManager getInstance() {
        if (singleInstance == null) {
            singleInstance = new AuthenticationManager();
        }
        return singleInstance;
    }
    
    private List<String> openEndpoints = new ArrayList<String>();
    
    private List<String> adminEndpoints = new ArrayList<String>();

    private AuthenticationManager() {
        readSettings();
    }
    
    @SuppressWarnings("unchecked")
    private void readSettings() {
        try {
            //ToDo: move the settings to DB
            LinkedHashMap<String, Object> authSettings = new JSONParser(new FileReader(SETTINGS_FILE_NAME)).parseObject();
            openEndpoints = (List<String>) authSettings.get("openEndpoints");
            adminEndpoints = (List<String>) authSettings.get("adminEndpoints");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ;
    }
    
    public List<String> getOpenEndpoints() {
        return openEndpoints;
    }

    public List<String> getAdminEndpoints() {
        return adminEndpoints;
    }

}
