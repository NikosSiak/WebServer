package web;

/**
 * <h1>HTTP Header</h1> This class represents a http header.
 * 
 * @author NikosSiak
 * @version 1.0
 */
public class HttpHeader {

    private String method;
    private String host;
    private String filePath;
    private boolean keepAlive;

    /**
     * Constructs a HttpHeader object with given values.
     * 
     * @param method    The http method of the request or response
     * @param host      Specifies the domain name of the server (for virtual
     *                  hosting)
     * @param filePath  The path to the file that the client requested
     * @param keepAlive Type of connection
     * @since 1.0
     */
    public HttpHeader(String method, String host, String filePath, String keepAlive) {
        this.method = method;
        this.host = host;
        if (!this.host.endsWith("/")) {
            this.host += "/";
        }
        this.filePath = filePath;
        if (!this.filePath.endsWith("/")) {
            this.filePath += "/";
        }
        this.keepAlive = keepAlive.equals("keep-alive");
    }

    /**
     * @return The http method of this header
     * @since 1.0
     */
    public String getMethod() {
        return this.method;
    }

    /**
     * @param method The http method of this header
     * @since 1.0
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * @return The domain name of the server
     * @since 1.0
     */
    public String getHost() {
        return this.host;
    }

    /**
     * @param host The domain name of the server
     * @since 1.0
     */
    public void setHost(String host) {
        this.host = host;
        if (!this.host.endsWith("/")) {
            this.host += "/";
        }
    }

    /**
     * @return The path to the file that is being requested
     * @since 1.0
     */
    public String getFilePath() {
        return this.filePath;
    }

    /**
     * @param filePath The path to the file that is being requested
     * @since 1.0
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
        if (!this.filePath.endsWith("/")) {
            this.filePath += "/";
        }
    }

    /**
     * @return The type of http connection
     * @since 1.0
     */
    public boolean getKeepAlive() {
        return this.keepAlive;
    }

    /**
     * @param keepAlive The type of http connection
     * @since 1.0
     */
    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }
    
}