package com.server.main.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that listens for incoming connections to the socket and creates a new
 * child thread for each (Master Thread)
 */
public class HttpConnectionListenerThread extends Thread {

    Logger logger = LoggerFactory.getLogger(HttpConnectionListenerThread.class);

    private int port;
    private String webroot;
    private ServerSocket serverSocket;

    public HttpConnectionListenerThread(int port, String webroot) throws IOException {
        this.port = port;
        this.setWebroot(webroot);
        this.serverSocket = new ServerSocket(this.port);
    }

    public void setWebroot(String webroot) {
        this.webroot = webroot;
    }

    @Override
    public void run() {

        try {
            while (serverSocket.isBound() && !serverSocket.isClosed()) {

                Socket socket = serverSocket.accept();

                logger.info("Connection accepted from: " + socket.getInetAddress().getHostAddress());

                HttpConnectionWorkerThread httpConnectionWorkerThread = new HttpConnectionWorkerThread(socket);
                httpConnectionWorkerThread.start();
            }

        } catch (IOException e) {
            logger.error("Problem with setting socket: " + e.getMessage());
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                }
            }
        }

    }
}