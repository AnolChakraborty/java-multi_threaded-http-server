package com.server.main.dashboard;

import com.server.main.sql.DatabaseSQL;

public class Dashboard {
    private String cookieData = null;
    private String responseCode = null;
    
    private DatabaseSQL databaseSQL = null;

    public Dashboard(String cookieData) {
        this.cookieData = cookieData;
        databaseSQL = new DatabaseSQL("studentDetailsDB");
    }

    public String getContent() {
        String content = databaseSQL.getStudentDetails(cookieData);
        if (content != null) {
            responseCode = "200 OK";
            return content;
        } else {
            responseCode = "500 Internal Server Error";
            return null;
        }
    }

    public String getResponseCode() {
        return responseCode;
    }
}
