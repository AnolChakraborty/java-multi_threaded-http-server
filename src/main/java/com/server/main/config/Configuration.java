package com.server.main.config;

/**
 * This class is ued to load the server congiguration parameters and serve them
 * to the driver class.
 */

public class Configuration {

    private int port;
    private String webroot;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getWebroot() {
        return webroot;
    }

    public void setWebroot(String webroot) {
        this.webroot = webroot;
    }
}