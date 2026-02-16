package com.vignesh.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public final class JsonDataReader {

    private static final Logger logger = LogManager.getLogger(JsonDataReader.class);
    private static final Gson GSON = new Gson();

//    private JsonDataReader() {
//    }

    public static JsonObject readJsonFile(String resourcePath) {
        InputStream is = JsonDataReader.class.getClassLoader().getResourceAsStream(resourcePath);
        if (is == null) {
            throw new RuntimeException("JSON file not found: " + resourcePath);
        }
        JsonObject json = JsonParser.parseReader(
                new InputStreamReader(is, StandardCharsets.UTF_8)).getAsJsonObject();
        logger.info("Loaded JSON data from: {}", resourcePath);
        return json;
    }

    public static List<Map<String, String>> readTestData(String resourcePath) {
        InputStream is = JsonDataReader.class.getClassLoader().getResourceAsStream(resourcePath);
        if (is == null) {
            throw new RuntimeException("JSON file not found: " + resourcePath);
        }
        Type listType = new TypeToken<List<Map<String, String>>>() {}.getType();
        List<Map<String, String>> data = GSON.fromJson(
                new InputStreamReader(is, StandardCharsets.UTF_8), listType);
        logger.info("Loaded {} test data entries from: {}", data.size(), resourcePath);
        return data;
    }

//    public static <T> T readAs(String resourcePath, Class<T> clazz) {
//        InputStream is = JsonDataReader.class.getClassLoader().getResourceAsStream(resourcePath);
//        if (is == null) {
//            throw new RuntimeException("JSON file not found: " + resourcePath);
//        }
//        return GSON.fromJson(new InputStreamReader(is, StandardCharsets.UTF_8), clazz);
//    }

    public static Object[][] toDataProviderArray(String resourcePath) {
        List<Map<String, String>> data = readTestData(resourcePath);
        Object[][] result = new Object[data.size()][1];
        for (int i = 0; i < data.size(); i++) {
            result[i][0] = data.get(i);
        }
        return result;
    }
}
