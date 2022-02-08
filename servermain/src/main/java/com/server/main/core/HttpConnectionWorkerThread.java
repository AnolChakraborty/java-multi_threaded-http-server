package com.server.main.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.server.main.http.HttpParser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that accept incoming connection from the master thread and processes
 * them (Child Thread)
 */

public class HttpConnectionWorkerThread extends Thread {

    Logger logger = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);

    private Socket socket = null;
    private OutputStream outputStream = null;
    private InputStream inputStream = null;

    String requestPath = "/home/ascrack/Documents/5th sem/projects/major-project/servermain/src/main/java/com/server/main/pages";
    final String CLRF = "\r\n";
    String content = null;
    String contentType = null;
    File image = null;
    long content_length = 0;
    boolean flag = false;

    public HttpConnectionWorkerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            HttpParser httpParser = new HttpParser(inputStream);

            String path = httpParser.getRequestPath();

            // logger.info(httpParser.getUserAgent());
            logger.info("Path requested {} ", path);
            // logger.info(httpParser.getRequest());

            /*
             * Checks which path is being requested by the server to load the specific
             * webpage files
             */
            switch (path) {
                case "/":
                    requestPath += "/home/index.html";
                    break;
                case "/home":
                    requestPath += "/home/index.html";
                    break;
                case "/about":
                    requestPath += "/about/index.html";
                    break;
                case "/contact":
                    requestPath += "/contact/index.html";
                    break;
                case "/login":
                    requestPath += "/login/index.html";
                    break;
                case "/signup":
                    requestPath += "/signup/index.html";
                    break;
                case "/404":
                    requestPath += "/404/index.html";
                    break;
                default:
                    requestPath += path;
                    break;
            }

            /*
             * Checks if the requested file or directory is valid or not if valid then
             * processes accordingly
             */
            if (new File(requestPath).exists()) {

                /*
                 * Checks if the requested file is an image file or any text file and processes
                 * accordingly
                 */
                if (requestPath.endsWith(".jpg") || requestPath.endsWith(".png") || requestPath.endsWith(".jpeg")) {
                    image = new File(requestPath);
                    content_length = image.length();
                    contentType = "Accept-Ranges: bytes" + CLRF + "Content-Type: image/png";
                    flag = true;
                } else {
                    content = Files.readString(Paths.get(requestPath), StandardCharsets.UTF_8);
                }
            }

            /*
             * If the requested file path is invalid or is not permitted to veiw or doesnot
             * exist, then the 404 error page gets served to the browser
             */
            else {
                logger.error("Invalid path requested: " + path);
                requestPath = "/home/ascrack/Documents/5th sem/projects/major-project/servermain/src/main/java/com/server/main/pages/404/index.html";
                content = Files.readString(Paths.get(requestPath),
                        StandardCharsets.UTF_8);
            }

            /*
             * After the content is loaded, the response is sent to the browser and the
             * sending process is determined by the file type, either is image or text
             */
            if (flag == false) { // If flag is false, then the content is text and is sent to the browser
                String response = "HTTP/1.1 200 OK" + CLRF +
                        contentType + CLRF +
                        "Content-Length: " + content.length() + CLRF +
                        CLRF +
                        content +
                        CLRF + CLRF;

                outputStream.write(response.getBytes());

            } else { // If flag is true, then the content is image and is sent to the browser
                String response = "HTTP/1.1 200 OK" + CLRF +
                        contentType + CLRF +
                        "Content-Length: " + content_length + CLRF +
                        CLRF;
                outputStream.write(response.getBytes());

                Files.copy(image.toPath(), outputStream);

                response = CLRF + CLRF;
                outputStream.write(response.getBytes());

            }

            /*
             * BUG_FIX Solution (a) Adding the delay in the thread.
             * (Works in Chrome, Firefox, Firedragon, Edge)
             * 
             * Making the connection delayed by few hundred miliseconds makes the webpage
             * served to the user in proper manner else due to some bug the files are not
             * interpreted by the browsers connection or the server is unable to send the
             * file, the bug is still unknown.
             * 
             * BUG_FIX: The bug is fixed by (either of these temporary solution)-
             * (a) Adding the delay in the thread.
             * (b) By not closing the inputstream/outputstream/socket connection.
             * 
             * N.B.: Use either one of the solutions at a time for implementation
             */
            sleep(100);

            outputStream.flush();

            logger.info("Connection closed from: " + socket.getInetAddress().getHostAddress());

        } catch (Exception e) {

            /*
             * If any exception occurs while the thread(child thread) accepts or processes
             * the request, then the connection is closed and the error is
             * logged.
             */
            logger.error("Error while processing request: " + e.getMessage());
            e.printStackTrace();
        }

        /*
         * BUG_FIX Solution (b) By not closing the inputstream/outputstream/socket connection.
         * (Works in Chrome only, doesnot work in any other browser)
         * 
         * If we do not close the inputstream/outputstream/socket connection(comment out
         * the code code and try), then the page is well served to the browser with all
         * the components loaded and making the webpage functional and interactive, but
         * the bug is unknown & this patch only works with Chrome
         * 
         * BUG_FIX: The bug is fixed by (either of these temporary solution)-
         * (a) Adding the delay in the thread.
         * (b) By not closing the inputstream/outputstream/socket connection.
         * 
         * N.B.: Use either one of the solutions at a time for implementation
         */
        finally { // Closes the inputstream, outputstream & socket connection
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }

            }
            if (outputStream != null) {
                try {
                    outputStream.close();
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
