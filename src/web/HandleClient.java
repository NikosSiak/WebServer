package web;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * <h1>Handle Client</h1> Responds to requests made by a client.
 * 
 * @author NikosSiak
 * @version 1.0.1
 */
class HandleClient implements Runnable {

    private Socket clientSocket;
    private String rootFolder;

    private final String METHOD = "Method";
    private final String FILE_PATH = "File-Path";
    private final String HTTP_VERSION = "Http-Version";
    private final String HOST = "Host";
    private final String CONNECTION = "Connection";
    private final String GET = "GET";
    private final String HTTP = "HTTP/1.1";

    private final String OK = "200 OK";
    private final String NOT_FOUND = "404 Not Found";

    /**
     * Creates a HandleClient object that handles the client connected to the
     * specified {@link java.net.Socket Socket}.
     * 
     * @param clientSocket The socket in which the client is connected
     * @param rootFolder   The root folder in which the hosted websites are stored
     * @since 1.0
     */
    public HandleClient(Socket clientSocket, String rootFolder) {
        this.clientSocket = clientSocket;
        this.rootFolder = rootFolder;
        if (!this.rootFolder.endsWith("/")) {
            this.rootFolder += "/";
        }
    }

    /**
     * Retrieves the request of the client and creates a HTTP object representing
     * it.
     * 
     * @param in The {@link java.io.InputStream} of the clientSocket
     * @return A HTTP object that represents the client's request
     * @throws IOException If an I/O error occurs
     * @since 1.0.1
     */
    HttpHeader getRequest(InputStream in) throws IOException {
        BufferedReader socketReader = new BufferedReader(new InputStreamReader(in));
        Map<String, String> headerFields = new HashMap<String, String>();
        String line = socketReader.readLine();
        boolean isFirstLine = true;

        while (!line.equals("")) {
            if (isFirstLine) {
                String[] firstLine = line.split(" ");
                headerFields.put(METHOD, firstLine[0]);
                headerFields.put(FILE_PATH, firstLine[1]);
                headerFields.put(HTTP_VERSION, firstLine[2]);
                isFirstLine = false;
            } else {
                String[] splitLine = line.split(":");
                headerFields.put(splitLine[0].trim(), splitLine[1].trim());
            }
            line = socketReader.readLine();
        }

        return new HttpHeader(headerFields.get(METHOD), headerFields.get(HOST), headerFields.get(FILE_PATH),
                headerFields.get(CONNECTION));
    }

    /**
     * Creates a http response for GET requests
     * 
     * @param request The GET request from the client
     * @return The response message
     * @since 1.0.1
     */
    private String handleGetRequest(HttpHeader request) {
        HttpHeader responseHeader = new HttpHeader();
        String path = rootFolder + request.getHost() + request.getFilePath();
        if (request.getFilePath().endsWith("/")) {
            path += "index.html";
        }
        String fileContents = "";

        // TODO: add a check if the file is text or binary
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            responseHeader.setReturnCode(OK);

            try {
                StringBuilder stringBuilder = new StringBuilder();
                String line = reader.readLine();
                while (line != null) {
                    stringBuilder.append(line).append("\n");
                    line = reader.readLine();
                }
                fileContents = stringBuilder.toString();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                reader.close();
            }
        } catch (FileNotFoundException e) {
            responseHeader.setReturnCode(NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
        }

        responseHeader.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss")));
        responseHeader.setMethod(HTTP);

        String message = responseHeader.toString();
        if (!fileContents.isEmpty()) {
            message += fileContents;
        }
        return message;
    }

    /**
     * Creates a http response for a request. Currently only GET request are implemented.
     * 
     * @param request The header of the clients request
     * @return The response that needs to be send to the client
     * @since 1.0.1
     */
    String handleRequest(HttpHeader request) {
        // HttpHeader response = new HttpHeader();
        String message;
        switch (request.getMethod()) {
        case GET:
            message = handleGetRequest(request);
            break;
        default:
            throw new UnsupportedOperationException("Method not supported yet");
        }
        return message;
    }

    /**
     * Sends the response to the client.
     * 
     * @param out      The {@link java.io.OutputStream} of the client
     * @param response The response that will be sent to the client
     * @throws IOException
     * @since 1.0.1
     */
    void sendResponse(OutputStream out, String response) throws IOException {
        out.write(response.getBytes());
    }

    /**
     * Gets the request from the client and sends a respond.
     * 
     * @since 1.0.1
     */
    @Override
    public void run() {
        try {
            InputStream socketIn = this.clientSocket.getInputStream();
            OutputStream socketOut = this.clientSocket.getOutputStream();
            try {
                HttpHeader httpRequest = getRequest(socketIn);
                String message = handleRequest(httpRequest);
                sendResponse(socketOut, message);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                socketIn.close();
                socketOut.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
