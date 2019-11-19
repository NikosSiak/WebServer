package web;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * <h1>Handle Client</h1> Responds to requests made by a client.
 * 
 * @author NikosSiak
 * @version 1.1
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

    private final String TEXT_HTML = "text/html";
    private final String UTF8 = "utf-8";

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
     * @return The response message as bytes
     * @since 1.0.1 Text files only
     * @since 1.1 Binary file support
     */
    private byte[] handleGetRequest(HttpHeader request) {
        HttpHeader responseHeader = new HttpHeader();
        String path = rootFolder + request.getHost() + request.getFilePath();
        if (request.getFilePath().endsWith("/")) {
            path += "index.html";
        }

        byte[] fileContents = null;
        try {
            InputStream reader = new FileInputStream(path);
            responseHeader.setReturnCode(OK);

            try {
                fileContents = Files.readAllBytes(Paths.get(path));
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
        if (path.endsWith(".html")) {
            responseHeader.setContentType(TEXT_HTML);
            responseHeader.setCharset(UTF8);
        }

        byte[] header = responseHeader.toString().getBytes();
        if (fileContents != null) {
            byte[] message = new byte[header.length + fileContents.length];
            System.arraycopy(header, 0, message, 0, header.length);
            System.arraycopy(fileContents, 0, message, header.length, fileContents.length);

            return message;
        }
        return header;
    }

    /**
     * Creates a http response for a request. Currently only GET request are implemented.
     * 
     * @param request The header of the clients request
     * @return The response that needs to be send to the client
     * @since 1.0.1
     */
    byte[] handleRequest(HttpHeader request) {
        byte[] message;
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
    void sendResponse(OutputStream out, byte[] response) throws IOException {
        out.write(response);
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
                byte[] message = handleRequest(httpRequest);
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
