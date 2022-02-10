package com.server.main.http;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.server.main.util.Json;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Verify {
    Logger logger = LoggerFactory.getLogger(Verify.class);
    private String payload;

    public boolean loginVerify(String payload) throws IOException {
        this.payload = payload;
        logger.info("JSON: " + payload);
        JsonNode conf = Json.parse(payload);
        String username = Json.fromJson(conf, "username");
        String password = Json.fromJson(conf, "password");
        logger.info("Username: " + username);
        logger.info("Password: " + password);
        
        if (username.equals("AEI/admin") && password.equals("admin")) {
            return true;
        } else {
            return false;
        }
    }
}
