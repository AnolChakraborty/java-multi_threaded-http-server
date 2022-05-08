package com.server.main.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.server.main.dashboard.Dashboard;
import com.server.main.http.HttpParser;
import com.server.main.sql.DatabaseSQL;
import com.server.main.verify.CookieVerify;
import com.server.main.verify.LoginVerify;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that accept incoming connection from the master thread and processes
 * them (Child Thread)
 */

public class HttpConnectionWorkerThread extends Thread {

    Logger logger = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);

    private Socket socket = null;
    private HttpParser httpParser = null;

    private BufferedInputStream inputStream = null;
    private BufferedOutputStream outputStream = null;

    private String path = null;
    private File image = null;
    private String contentType = null;
    private String content = null;
    private long content_length = 0;
    private boolean isImage = false;
    boolean ajaxRequest = false;
    private final String CLRF = "\r\n";
    private String responseCode;

    private String responseBody = "HTTP/1.1 %s" + CLRF +
            "Content-Length: %d" + CLRF +
            "Content-Type: %s" + CLRF +
            "Date: %s" + CLRF +
            "Accept-Ranges: bytes" + CLRF +
            "Access-Control-Allow-Origin: *" + CLRF +
            "Conection: Keep-Alive" + CLRF +
            "Content-Encoding: UTF-8" + CLRF +
            "Server: Custom-JAVA-HTTP-Server" + CLRF +
            "X-Powered-By: JAVA" + CLRF;
    private String setCookie = "Set-Cookie: %s; Max-Age=%d" + CLRF;

    String requestPath = "C:\\AnolCH\\Documents\\5th sem\\projects\\major-project\\servermain\\src\\main\\java\\com\\server\\main\\pages";

    public HttpConnectionWorkerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            
/*****************************************************************************************/
            
            // inputStream = socket.getInputStream();    // Previously this
            
            inputStream = new BufferedInputStream(socket.getInputStream());    // Then changed to this
            
/*****************************************************************************************/
            
            // outputStream = socket.getOutputStream(); // Previously this
            
            outputStream = new BufferedOutputStream(socket.getOutputStream());    // Then changed to this

/*****************************************************************************************/

            httpParser = new HttpParser(inputStream);

            path = httpParser.getRequestPath().toLowerCase();
            logger.info("Path requested: " + path);
            // logger.info("useragent : " + httpParser.getUserAgent());

            switch (path) {
                case "/login/verify":
                    ajaxRequest = true;
                    if (httpParser.getJsonObject() != null) {
                        contentType = "application/json";
                        LoginVerify loginVerify = new LoginVerify(httpParser.getJsonObject());
                        responseCode = loginVerify.getResponseCode();
                        content = loginVerify.setLoginSession();
                    } else {
                        requestPath += "/403/index.html"; // Forbidden for the user
                        ajaxRequest = false;
                    }
                    break;
                case "/dashboard":
                    if (httpParser.getCookie() != null) {
                        CookieVerify cookieVerify = new CookieVerify(httpParser.getCookie());
                        if ((cookieVerify.isVerified()) != null) {
                            requestPath += "/dashboard/index.html";
                        } else {
                            requestPath += "/403/index.html"; // Forbidden for the user due to invalid cookie
                        }
                    } else {
                        requestPath += "/403/index.html"; // Forbidden for the user to access without cookie
                    }
                    break;
                case "/dashboard/getdata":
                    ajaxRequest = true;
                    if (httpParser.getCookie() != null) {
                        CookieVerify cookieVerify = new CookieVerify(httpParser.getCookie());
                        String cookieData = null;
                        if ((cookieData = cookieVerify.isVerified()) != null) {
                            Dashboard dashboardVerify = new Dashboard(cookieData); // cookieData is the roll number
                            if ((content = dashboardVerify.getContent()) != null) {
                                responseCode = dashboardVerify.getResponseCode();
                                contentType = "application/json";
                            } else {
                                content = "{\"status\":\"failure\",\"message\":\"Internal server error\"}";
                                contentType = "application/json";
                            }
                        } else {
                            requestPath += "/403/index.html"; // Forbidden for the user due to invalid cookie
                            ajaxRequest = false;
                        }
                    } else {
                        requestPath += "/403/index.html"; // Forbidden for the user to access the site without cookie
                        ajaxRequest = false;
                    }
                    break;
                case "/":
                    requestPath += "/home/index.html";
                    break;
                case "/home":
                    requestPath += "/home/index.html";
                    break;
                case "/about":
                    requestPath += "/about/index.html";
                    break;
                case "/login":
                    requestPath += "/login/index.html";
                    break;
                case "/logout":
                    requestPath += "/home/index.html";
                    if (httpParser.getCookie() != null) {
                        CookieVerify cookieVerify = new CookieVerify(httpParser.getCookie());
                        String cookieData = null;
                        if ((cookieData = cookieVerify.isVerified()) != null) {
                            cookieVerify.deleteCookie(cookieData);
                        }
                    }
                    setCookie = String.format(setCookie, "sessionID", 0);
                    responseBody += setCookie;
                    break;
                case "/signup":
                    requestPath += "/signup/index.html";
                    break;
                case "/favicon.ico":
                case "/favicon":
                    requestPath += "/favicon/favicon.png";
                    break;
                case "/404":
                    requestPath += "/404/index.html";
                    break;
                case "/403":
                    requestPath += "/403/index.html";
                    break;
                case "/500":
                case "/dashboard/internal_server_error":
                    requestPath += "/500/index.html";
                    break;
                default:
                    requestPath += path;
                    break;
            }

            if (path.startsWith("/profilepic/") && path.endsWith(".jpg")) {
                String newRequestPath = "C:\\AnolCH\\Documents\\5th sem\\projects\\major-project\\servermain\\src\\main\\java\\com\\server\\main\\pages";
                if (httpParser.getCookie() != null) {
                    CookieVerify cookieVerify = new CookieVerify(httpParser.getCookie());
                    String cookieData = null;
                    if ((cookieData = cookieVerify.isVerified()) != null) {
                        DatabaseSQL databaseSQL = new DatabaseSQL("studentDetailsDB");
                        if (!databaseSQL.imageViewerIsValid(path, cookieData)) {
                            logger.info("path " + path + " not served due to invalid session");
                            requestPath = newRequestPath + "/403/index.html";
                        }
                    } else {
                        requestPath = newRequestPath + "/403/index.html"; // Forbidden for the user to veiw others
                                                                          // profile picture
                    }
                } else {
                    requestPath = newRequestPath + "/403/index.html"; // Forbidden for the user to access the the images
                                                                      // without valid cookie
                }
            }

            if (!ajaxRequest) {
                if (new File(requestPath).exists()) {
                    if (requestPath.endsWith(".jpg") || requestPath.endsWith(".png") || requestPath.endsWith(".jpeg")) {
                        image = new File(requestPath);
                        content_length = image.length();
                        isImage = true;
                        responseCode = "200 OK";
                    } else {
                        content = Files.readString(Paths.get(requestPath), StandardCharsets.UTF_8);
                        content_length = content.length();
                        responseCode = "200 OK";
                    }
                } else {
                    logger.error("Invalid path requested: {}", path);
                    requestPath = "C:\\AnolCH\\Documents\\5th sem\\projects\\major-project\\servermain\\src\\main\\java\\com\\server\\main\\pages\\404\\index.html";
                    content = Files.readString(Paths.get(requestPath), StandardCharsets.UTF_8);
                    content_length = content.length();
                    contentType = "text/html";
                    responseCode = "404 Not Found";
                }
            }

            if (requestPath.endsWith(".html")) {
                contentType = "text/html";
            } else if (requestPath.endsWith(".css")) {
                contentType = "text/css";
            } else if (requestPath.endsWith(".js")) {
                contentType = "application/javascript";
            } else if (requestPath.endsWith(".png")) {
                contentType = "image/png";
            } else if (requestPath.endsWith(".jpg")) {
                contentType = "image/jpg";
            } else if (requestPath.endsWith(".jpeg")) {
                contentType = "image/jpeg";
            } else if (requestPath.endsWith(".ico")) {
                contentType = "image/x-icon";
            } else if (requestPath.endsWith(".json")) {
                contentType = "application/json";
            } else if (requestPath.endsWith(".xml")) {
                contentType = "application/xml";
            } else if (requestPath.endsWith(".pdf")) {
                contentType = "application/pdf";
            } else if (requestPath.endsWith(".svg")) {
                contentType = "i5mage/svg+xml";
            } else if (requestPath.endsWith(".txt")) {
                contentType = "text/plain";
            } else if (requestPath.endsWith(".woff")) {
                contentType = "application/font-woff";
            } else if (requestPath.endsWith(".woff2")) {
                contentType = "application/font-woff2";
            } else if (requestPath.endsWith(".ttf")) {
                contentType = "application/font-ttf";
            } else if (requestPath.endsWith(".eot")) {
                contentType = "application/vnd.ms-fontobject";
            } else if (requestPath.endsWith(".otf")) {
                contentType = "application/font-otf";
            } else if (requestPath.endsWith(".svg")) {
                contentType = "image/svg+xml";
            }

            String date = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").format(new Date());
            if (isImage) {
                responseBody = String.format(responseBody, responseCode, content_length, contentType, date);
                responseBody += CLRF;
                outputStream.write(responseBody.getBytes());
                Files.copy(image.toPath(), outputStream);
                outputStream.write((CLRF + CLRF).getBytes());
                // sleep(800);
            } else {
                responseBody = String.format(responseBody, responseCode, content.length(), contentType, date);
                responseBody += CLRF;
                responseBody += content;
                outputStream.write(responseBody.getBytes());
                // sleep(400);

            }
            outputStream.flush();
            logger.info("Connection closed from: " + socket.getInetAddress().getHostAddress());

        } catch (Exception e) {
            logger.error("Error at worker thread while processing request: {}", e.getMessage());
            e.printStackTrace();
        } finally {

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }

    }
}
