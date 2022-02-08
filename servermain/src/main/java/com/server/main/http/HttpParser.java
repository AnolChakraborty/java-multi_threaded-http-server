package com.server.main.http;

import java.io.IOException;
import java.io.InputStream;

import com.server.main.App;

/**
 * Class for parsing the http request
 */

public class HttpParser {

    final int SP = 0x20; // 32
    final int CR = 0x0D; // 13
    final int LF = 0x0A; // 10

    private int _byte;
    private int i = 0;

    private int indexOfUserAgent;
    // private int indexOfCookie;
    // private int indexOfHost;

    private String method = null;
    private String requestPath = null;
    private String version = null;
    private String host = null;
    private String userAgent = null;
    // private String cookie = null;

    String arrayOfString[] = new String[50];

    StringBuilder stringBuilder = new StringBuilder();

    InputStream inputStream = null;

    public HttpParser(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;
        while ((_byte = this.inputStream.read()) >= 0) {
            // System.out.println((char)_byte + " " + _byte);
            if (_byte == CR) {
                _byte = this.inputStream.read();
                if (_byte == LF) {
                    _byte = this.inputStream.read();
                    if (_byte != CR) {
                        arrayOfString[i] = stringBuilder.toString();
                        stringBuilder.delete(0, stringBuilder.length());
                        i++;
                    } else {
                        break;
                    }
                    stringBuilder.append((char) _byte);
                }
            } else {
                stringBuilder.append((char) _byte);
            }
        }

        for (int j = 0; j < arrayOfString.length; j++) {
            if (arrayOfString[j] != null) {
                if (arrayOfString[j].contains("User-Agent")) {
                    indexOfUserAgent = j;
                }
            }
        }
    }

    public String getMethod() {
        // parse method
        String method = arrayOfString[0].substring(0, arrayOfString[0].indexOf(" "));
        return method;
    }

    public String getRequestPath() {
        // parse request path
        String requestPath = arrayOfString[0].substring(arrayOfString[0].indexOf(" ") + 1,
                arrayOfString[0].indexOf("HTTP") - 1);
        return requestPath;
    }

    public String getVersion() {
        // parse version
        String version = arrayOfString[0].substring(arrayOfString[0].indexOf("HTTP"));
        return version;
    }

    public String getHost() {
        // parse host
        String host = arrayOfString[1].substring(arrayOfString[1].indexOf("Host: ") + ("Host: ").length());
        return host;
    }

    public String getUserAgent() {
        // parse user agent
        String userAgent = arrayOfString[indexOfUserAgent]
                .substring(arrayOfString[indexOfUserAgent].indexOf("User-Agent:") + ("User-Agent: ").length());
        return userAgent;
    }

    public String getRequest() {
        String request = "\n";
        int i = 0;
        while (arrayOfString[i] != null) {
            request += arrayOfString[i] + "\n";
            i++;
        }
        return request;

    }

    // public String getCookie() {
    // // parse cookie
    // String cookie =
    // arrayOfString[indexOfCookie].substring(arrayOfString[indexOfCookie].indexOf("Cookie:
    // ") + ("Cookie: ").length());
    // return cookie;
    // }
}
