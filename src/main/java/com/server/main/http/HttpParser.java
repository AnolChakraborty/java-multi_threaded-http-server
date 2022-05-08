package com.server.main.http;

import java.io.IOException;
import java.io.InputStream;

/**
 * Class for parsing the http request
 */

public class HttpParser {

    final int SP = 0x20; // 32
    final int CR = 0x0D; // 13
    final int LF = 0x0A; // 10
    final int BRKT_close = 0x7D; // 125

    private int _byte;
    private int i = 0;

    private int indexOfUserAgent;
    private int indexOfCookie;

    private String method = null;
    private String requestPath = null;
    private String version = null;
    private String host = null;
    private String userAgent = null;
    private String cookie = null;
    private String jsonIbjectString = null;
    private String request = null;

    boolean isJsonObjectPresent = false;
    boolean hasCookie = false;

    String arrayOfString[] = new String[40];

    StringBuilder stringBuilder = new StringBuilder();

    InputStream inputStream = null;

    public HttpParser(InputStream inputStream) throws IOException {
        this.inputStream = inputStream;

        while ((_byte = this.inputStream.read()) != -1) {

            if (_byte == CR) { // if CR is received it checks if next byte is LF, if not then it appends the
                               // byte
                _byte = this.inputStream.read();

                if (_byte == LF) { // if next byte is LF then it checks for the next byte
                    _byte = this.inputStream.read();

                    if (_byte != CR) { // if next byte if not CR then it converts the previously stored buffer to an
                                       // array
                        arrayOfString[i] = stringBuilder.toString();
                        stringBuilder.delete(0, stringBuilder.length());

                        if (arrayOfString[i].contains("User-Agent:")) {
                            indexOfUserAgent = i;
                        }
                        if (arrayOfString[i].contains("Cookie:")) {
                            indexOfCookie = i;
                            hasCookie = true;
                        }
                        if (arrayOfString[i].contains("Content-Type: application/json")) {
                            isJsonObjectPresent = true;
                        }

                        i++;
                    } else { // if the byte is CR after the LF then it checks for json data and parses it
                             // else exists
                        if (isJsonObjectPresent) {
                            _byte = this.inputStream.read();
                            stringBuilder.delete(0, stringBuilder.length());
                            while ((_byte = this.inputStream.read()) != BRKT_close) {
                                stringBuilder.append((char) _byte);
                            }
                            stringBuilder.append((char) BRKT_close);
                            jsonIbjectString = stringBuilder.toString();
                            arrayOfString[i++] = stringBuilder.toString();
                        }
                        break; // breaks the loop if CR is received after CRLF
                    }
                    stringBuilder.append((char) _byte); // if the byte after the LF is not CR then it appends it
                }
            } else {
                stringBuilder.append((char) _byte); // if byte is not CR then it appends it
            }
            arrayOfString[i] = stringBuilder.toString(); // at last convert the remaining last line buffered to array[n]
            if (arrayOfString[i].contains("Cookie:")) {
                indexOfCookie = i;
                hasCookie = true;
            }
        }
    }

    public String getMethod() {
        // parse method
        method = arrayOfString[0].substring(0, arrayOfString[0].indexOf(" "));
        return method;
    }

    public String getRequestPath() {
        // parse request path
        requestPath = arrayOfString[0].substring(arrayOfString[0].indexOf(" ") + 1,
                arrayOfString[0].indexOf("HTTP") - 1);
        return requestPath;
    }

    public String getVersion() {
        // parse version
        version = arrayOfString[0].substring(arrayOfString[0].indexOf("HTTP"));
        return version;
    }

    public String getHost() {
        // parse host
        host = arrayOfString[1].substring(arrayOfString[1].indexOf("Host: ") + ("Host: ").length());
        return host;
    }

    public String getUserAgent() {
        // parse user agent
        userAgent = arrayOfString[indexOfUserAgent]
                .substring(arrayOfString[indexOfUserAgent].indexOf("User-Agent:") + ("User-Agent: ").length());
        return userAgent;
    }

    public String getRequest() {
        request = "\n";
        int i = 0;
        while (arrayOfString[i] != null) {
            request += arrayOfString[i] + "\n";
            i++;
        }
        return request.substring(0, request.length() - 1);

    }

    public String getCookie() {
        if (hasCookie) {
            cookie = arrayOfString[indexOfCookie]
                    .substring(arrayOfString[indexOfCookie].indexOf("Cookie:") + ("Cookie: ").length());
        } else {
            cookie = null;
        }
        return cookie;
    }

    public String getJsonObject() {
        return jsonIbjectString;
    }
}