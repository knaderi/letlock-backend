package com.landedexperts.letlock.filetransfer.backend;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = LetlockFiletransferBackendApplication.class)
@WebAppConfiguration
public abstract class AbstractTest {
    protected MockMvc mvc;
    @Autowired
    WebApplicationContext webApplicationContext;

    protected void setUp() throws Exception {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }

    @BeforeClass
    public static void setSystemProperty() {
        // Do not change this as this may cause braking the build in remote dev
        String activeProfile = "local";
        String mvnCommandLineArgs = System.getenv().get("MAVEN_CMD_LINE_ARGS");
        if (!StringUtils.isEmpty(mvnCommandLineArgs)) {
            int index = mvnCommandLineArgs.indexOf("-Dspring.profiles.active=");
            activeProfile = mvnCommandLineArgs.substring(index + 25);
        }
        System.getProperties().setProperty("spring.profiles.active", activeProfile);

    }

    protected void assertForNoError(String functionName, String content) throws Exception {
        assertTrue("functionName - content length should be larger than zero", content.length() > 0);
        assertJsonForKeyValue(functionName, content, "returnMessage", "", "equalsTo");
        assertJsonForKeyValue(functionName, content, "returnCode", "SUCCESS", "equalsTo");
        if (content.contains("\"result\":")) {
            assertJsonForKeyValue(functionName, content, "result", "", "notEmpty");
        }
    }

    protected void assertHasValueForKey(String key, String jsonString, List<String> list) throws Exception {
        JsonParser p = new JsonParser();
        JsonElement jsonElement = p.parse(jsonString);
        check(key, jsonElement, list);
        assertTrue(list.size() > 0);
    }

    protected void assertContentForKeyValueLargerThanZero(String testName, String content, String keyName) throws Exception {
        assertJsonForKeyValue(testName, content, keyName, "0", "greaterThan");
    }

    protected void assertJsonForKeyValue(String testName, String content, String keyName, String expectedKeyValue, String operator)
            throws Exception {
        String actualValueForKey = getValuesForGivenKey(content, keyName);
        String failureMessageComparison = "";
        if (operator.equals("equalsTo")) {
            failureMessageComparison = "is not equals to";
        } else if (operator.equals("lessThan")) {
            failureMessageComparison = "is not less than";
        } else if (operator.equals("greaterThan")) {
            failureMessageComparison = "is not greater than";
        } else if (operator.equals("notEmpty")) {
            failureMessageComparison = "is empty";
        }

        String failureMessage = String.format(testName
                + " - The actual value of "
                + keyName
                + " is: "
                + actualValueForKey
                + ", and "
                + failureMessageComparison
                + " expectedValue:  "
                + expectedKeyValue, testName, keyName, actualValueForKey, expectedKeyValue);
        if (operator.equals("equalsTo")) {
            assertTrue(failureMessage, actualValueForKey.equals(expectedKeyValue));
        } else if (operator.equals("lessThan")) {
            assertTrue(failureMessage, Integer.valueOf(actualValueForKey) < Integer.valueOf(expectedKeyValue));
        } else if (operator.equals("greaterThan")) {
            assertTrue(failureMessage, Integer.valueOf(actualValueForKey) > Integer.valueOf(expectedKeyValue));
        } else if (operator.equals("notEmpty")) {
            assertFalse(failureMessage, StringUtils.isEmpty(actualValueForKey));
        }
    }

    protected String getValuesForGivenKey(String jsonArrayStr, String key) throws Exception {
        return this.getValuesForGivenKey(jsonArrayStr, key, "");
    }

    protected String getValuesForGivenKey(String jsonArrayStr, String key, String parent) throws Exception {
        JSONObject jsonObject = new JSONObject(jsonArrayStr);
        if (StringUtils.isEmpty(parent)) {
            return jsonObject.getString(key);
        } else {
            return jsonObject.getJSONObject(parent).getString(key);
        }
    }

    protected static void check(String key, String jsonString, List<String> list) {
        JsonParser p = new JsonParser();
        JsonElement jsonElement = p.parse(jsonString);
        check(key, jsonElement, list);
    }

    private static void check(String key, JsonElement jsonElement, List<String> list) {
        if (jsonElement.isJsonArray()) {
            for (JsonElement jsonElement1 : jsonElement.getAsJsonArray()) {
                check(key, jsonElement1, list);
            }
        } else {
            if (jsonElement.isJsonObject()) {
                Set<Map.Entry<String, JsonElement>> entrySet = jsonElement
                        .getAsJsonObject().entrySet();
                for (Map.Entry<String, JsonElement> entry : entrySet) {
                    String key1 = entry.getKey();
                    if (key1.equals(key)) {
                        list.add(entry.getValue().toString());
                    }
                    check(key, entry.getValue(), list);
                }
            } else {
                if (jsonElement.toString().equals(key)) {
                    list.add(jsonElement.toString());
                }
            }
        }
    }
}