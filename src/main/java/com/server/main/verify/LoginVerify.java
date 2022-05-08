package com.server.main.verify;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.fasterxml.jackson.databind.JsonNode;
import com.server.main.sql.DatabaseSQL;
import com.server.main.util.Json;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginVerify {
    Logger logger = LoggerFactory.getLogger(LoginVerify.class);
    private String payload;
    private boolean isVerified = false;

    private String responseCode = null;

    private String status = null;
    private String message = null;
    private String token = null;

    private String sendPayload = "{\"status\":\"%s\",\"message\":\"%s\",\"token\":\"%s\"}";

    public LoginVerify(String payload) throws IOException, NoSuchAlgorithmException {
        this.payload = payload;
        logger.info("JSON: " + payload);
        JsonNode conf = Json.parse(payload);
        CookieVerify cookieVerify = new CookieVerify();
        DatabaseSQL databaseSQL = new DatabaseSQL("loginDB");
        String username = Json.fromJson(conf, "username");
        String password = Json.fromJson(conf, "password");
        logger.info("Username: " + username);
        logger.info("Password: " + password);

        password = DigestUtils.sha256Hex(password);

        String localPassword = databaseSQL.getPassword(username.toUpperCase());

        if (localPassword != null) {
            if (localPassword.equals(password)) {
                isVerified = true;
                status = "success";
                message = "Login Successful";
                token = cookieVerify.generateCookie(username).toString() + "; Max-Age=300";
                responseCode = "200 OK";
            } else if (localPassword != password) {
                isVerified = false;
                status = "error";
                message = "Login failed, Incorrect Password";
                responseCode = "401 Unauthorized";
            }
        } else {
            isVerified = false;
            status = "failure";
            message = "Login failed, Invalid Username";
            responseCode = "401 Unauthorized";
        }
    }

    public String setLoginSession() {
        return sendPayload = String.format(sendPayload, status, message, token);
    }

    public String getResponseCode() {
        return responseCode;
    }
}
