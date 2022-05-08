package com.server.main.config;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.server.main.util.Json;

/**
 * This class loads the server json configuration file and parses them
 * accordingly.
 */

public class ConfigurationManager {
    private static ConfigurationManager myConfigurationManager;
    private static Configuration myCurrentConfiguration;

    private ConfigurationManager() {
    }

    public static ConfigurationManager getInstance() {
        if (myConfigurationManager == null) {
            myConfigurationManager = new ConfigurationManager();
        }
        return myConfigurationManager;
    }

    /*
     * This method is used to load a configuration file provided by the path.
     */
    public void loadConfigurationFile(String filePath) throws IOException {
        FileReader filereader = null;
        try {
            filereader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            throw new HttpConfigurationException(e);
        }
        StringBuffer sb = new StringBuffer();
        int i;
        try {
            while ((i = filereader.read()) != -1) {
                sb.append((char) i);
            }

        } catch (IOException e) {
            throw new HttpConfigurationException(e);

        }
        filereader.close();
        JsonNode conf = null;
        try {

            conf = Json.parse(sb.toString());
        } catch (IOException e) {
            throw new HttpConfigurationException("Error Parsing the configuration file", e);
        }
        try {

            myCurrentConfiguration = Json.fromJson(conf, Configuration.class);
        } catch (IOException e) {
            throw new HttpConfigurationException("Error Parsing the configuration file, internal", e);
        }
    }

    /*
     * Returns the current loaded configuration.
     */
    public Configuration getCurrentConfiguration() {
        if (myConfigurationManager == null) {
            throw new HttpConfigurationException("No Current Configuration Set");
        }
        return myCurrentConfiguration;
    }

}
