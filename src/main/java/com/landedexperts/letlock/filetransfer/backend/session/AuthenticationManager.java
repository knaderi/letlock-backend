package com.landedexperts.letlock.filetransfer.backend.session;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.apache.tomcat.util.json.JSONParser;
import static com.landedexperts.letlock.filetransfer.backend.BackendConstants.AUTH_SETTINGS_FILE_NAME;

public class AuthenticationManager {

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
            LinkedHashMap<String, Object> authSettings = getSettings();
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
    
    private LinkedHashMap<String, Object> getSettings() throws Exception{
        LinkedHashMap<String, Object> authSettings = new LinkedHashMap<String, Object>();
        URL url = Resources.getResource(AUTH_SETTINGS_FILE_NAME);
        String contentStr = Resources.toString(url, Charsets.UTF_8);
        authSettings = new JSONParser(contentStr).parseObject();
        return authSettings;

    }
    
}
