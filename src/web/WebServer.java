package web;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * <h1>Web Server</h1> 
 * The WebServer class implements a simple web server
 * 
 * @author NikosSiak
 * @version 1.0
 */
public class WebServer {

    private ServerSocket serverSocket;
    private int port;
    private String rootFolder;

    /**
     * Creates a WebServer object that uses port 80 and the specified root folder
     * 
     * @param rootFolder The root folder in which the hosted websites are stored
     * @since 1.0
     */
    public WebServer(String rootFolder) {
        this.port = 80;
        this.rootFolder = rootFolder;
    }

    /**
     * Creates a WebServer object with the specified port and rootFolder
     * 
     * @param port       The port in which the server will run
     * @param rootFolder The root folder in which the hosted websites are stored
     * @since 1.0
     */
    public WebServer(int port, String rootFolder) {
        this.port = port;
        this.rootFolder = rootFolder;
    }

    /**
     * Binds the specified port
     * 
     * @throws IOException
     * @since 1.0
     */
    public void bind() throws IOException {
        this.serverSocket = new ServerSocket(this.port);
    }

    /**
     * Starts the server and waits for new connections
     * 
     * @since 1.0
     */
    public void run() {
        Socket clientSocket;
        Thread handler;
        if (this.serverSocket == null) {
            System.err.println("First bind to a port");
            return;
        }
        while (true) {
            try {
                clientSocket = serverSocket.accept();
                handler = new Thread(new HandleClient(clientSocket, this.rootFolder));
                handler.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
