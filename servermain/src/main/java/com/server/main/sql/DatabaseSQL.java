package com.server.main.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseSQL {

    Logger logger = LoggerFactory.getLogger(DatabaseSQL.class);

    private Statement statement = null;
    private Connection connection = null;

    public DatabaseSQL() {
    }

    public DatabaseSQL(String dbName) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/LocalHostLogin", "root",
                    "root");

            statement = connection.createStatement();
        } catch (Exception e) {
            logger.error("Cannot connect to database " + e);
        }
    }

    public String getCookieData(String cookie) {
        String query = "SELECT _roll FROM loginDB WHERE _cookie = '" + cookie + "'";
        try {
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return resultSet.getString("_roll");
            } else {
                return null;
            }
        } catch (SQLException e) {
            logger.error("Cannot fetch user roll based on cookie " + e);
            return null;
        }
    }

    public boolean setCookieData(String roll, String cookie) {
        String query = "update loginDB set _cookie = '" + cookie + "' where _roll = '" + roll + "'";
        try {
            statement.executeUpdate(query);
            return true;
        } catch (SQLException e) {
            logger.error("Cannot set cookie " + e);
            return false;
        }
    }

    public void deleteCookieData(String roll) {
        String query = "update loginDB set _cookie = '' where roll = '" + roll + "'";
        try {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            logger.error("Cannot delete cookie " + e);
        }
    }

    public String getPassword(String username) {
        String query = "SELECT _password FROM loginDB WHERE _roll = '" + username + "'";
        try {
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                return resultSet.getString("_password");
            } else {
                return null;
            }
        } catch (SQLException e) {
            logger.error("Cannot fetch password based on username " + e);
            return null;
        }
    }

    public String getStudentDetails(String cookieData) {
        String query = "SELECT * FROM studentDetailsDB WHERE _roll = '" + cookieData + "'";

        String name = null;
        String roll = null;
        String branch = null;
        String semester = null;
        String email = null;
        String phone = null;
        String address = null;
        String profilePic = null;
        String payload = "{\"status\":\"success\",\"name\":\"%s\",\"roll\":\"%s\",\"branch\":\"%s\",\"semester\":\"%s\",\"email\":\"%s\",\"phone\":\"%s\",\"address\":\"%s\",\"profilePic\":\"%s\"}";
        try {
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                name = resultSet.getString("_name");
                roll = resultSet.getString("_roll");
                branch = resultSet.getString("_branch");
                semester = resultSet.getString("_semester");
                email = resultSet.getString("_email");
                phone = resultSet.getString("_phone");
                address = resultSet.getString("_address");
                profilePic = resultSet.getString("_profilePic");
                profilePic = "/profilePic/" + profilePic ;
                return String.format(payload, name, roll, branch, semester, email, phone, address, profilePic);
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error("Cannot fetch student details based on cookieData (roll number) " + e);
            return null;
        }
    }

    public boolean imageViewerIsValid(String path, String cookieData) {
        String query = "SELECT _profilePic FROM studentDetailsDB WHERE _roll = '" + cookieData + "'";
        try {
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                String profilePic = "/profilepic/" + resultSet.getString("_profilePic");
                if (profilePic.equals(path)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e) {
            logger.error("Cannot fetch profile pic based on cookieData (roll number) " + e);
            return false;
        }
    }

}
