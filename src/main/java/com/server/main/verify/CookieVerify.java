package com.server.main.verify;

import java.time.LocalTime;

import com.server.main.sql.DatabaseSQL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.openhft.hashing.LongHashFunction;

public class CookieVerify {
    Logger logger = LoggerFactory.getLogger(CookieVerify.class);

    private String cookie = null;
    DatabaseSQL databaseSQL = null;

    public CookieVerify(String cookie) {
        this.cookie = cookie;
        databaseSQL = new DatabaseSQL("loginDB");
    }

    public CookieVerify() {
        databaseSQL = new DatabaseSQL("loginDB");
    }

    public String isVerified() {
        String roll = null;
        if ((roll = databaseSQL.getCookieData(cookie)) != null) {
            return roll;
        } else {
            return null;
        }
    }

    public String generateCookie(String roll) {
        String cookie = System.currentTimeMillis() + roll + LocalTime.now().toString() + Math.random();
        try {
            long hash = LongHashFunction.wy_3().hashChars(cookie);
            cookie = Long.toString(Math.abs(hash));

            if (databaseSQL.setCookieData(roll, cookie)) {
                return cookie;
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error("Cannot create cookie hash " + e);
            return null;
        }
    }

    public void deleteCookie(String roll) {
        databaseSQL.deleteCookieData(roll);
    }

}
