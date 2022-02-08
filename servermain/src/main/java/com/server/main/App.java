package com.server.main;

import java.io.IOException;
import com.server.main.config.Configuration;
import com.server.main.config.ConfigurationManager;
import com.server.main.core.HttpConnectionListenerThread;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * Driver class for the server.
 *
 */
public class App {
    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(App.class);

        // System.out.println("Staring server............");
        logger.info("Starting server............");
        try {
            ConfigurationManager.getInstance().loadConfigurationFile(
                    "/home/ascrack/Documents/5th sem/projects/major-project/servermain/src/main/java/com/server/main/resources/http.json");
        } catch (IOException e) {
            // System.out.println("Configuratiom file missing or wrong path.");
            logger.info("Configuratiom file missing or wrong path.");
            e.printStackTrace();
        }
        Configuration conf = ConfigurationManager.getInstance().getCurrentConfiguration();
        // System.out.println("Port: " + conf.getPort());
        // System.out.println("Webroot: " + conf.getWebroot());
        logger.info("Port: " + conf.getPort());
        logger.info("Webroot: " + conf.getWebroot());

        HttpConnectionListenerThread listenerThread;
        try {
            listenerThread = new HttpConnectionListenerThread(conf.getPort(), conf.getWebroot());
            listenerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}