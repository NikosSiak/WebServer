package web;

import java.net.Socket;

/**
 * <h1>Handle Client</h1>
 * Responds to requests made by a client
 * @author NikosSiak
 * @version 1.0
 */
class HandleClient implements Runnable {

    private Socket clientSocket;
    private String rootFolder;

    /**
     * Creates a HandleClient object that handles the client connected to the specified {@link java.net.Socket Socket}
     * 
     * @param clientSocket The socket in which the client is connected
     * @param rootFolder   The root folder in which the hosted websites are stored
     * @since 1.0
     */
    public HandleClient(Socket clientSocket, String rootFolder) {
        this.clientSocket = clientSocket;
        this.rootFolder = rootFolder;
    }

    @Override
    public void run() {
        
    }
}
