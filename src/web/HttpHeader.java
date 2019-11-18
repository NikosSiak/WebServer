package web;

/**
 * <h1>HTTP Header</h1> This class represents a http header.
 * 
 * @author NikosSiak
 * @version 1.0.1
 */
public class HttpHeader {

    private String method;
    private String host;
    private String filePath;
    private boolean keepAlive;
    private String returnCode;
    private String date;

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
        this.filePath = filePath;
        this.keepAlive = keepAlive.equals("keep-alive");
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (this.method.contains("HTTP")) {
            stringBuilder.append(this.method).append(" ");
            stringBuilder.append(this.returnCode).append("\r\n");
            stringBuilder.append(this.date).append("\r\n");
            stringBuilder.append("\r\n");
        }
        return stringBuilder.toString();
    }

    /**
     * Empty constructor
     * 
     * @since 1.0.1
     */
    public HttpHeader() {
        
    }

    /**
     * @return The http return code of this header
     * @since 1.0.1
     */
    public String getReturnCode() {
        return returnCode;
    }

    /**
     * @param returnCode The http return code of this header
     * @since 1.0.1
     */
    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    /**
     * @return The string representation of the date this header was created
     * @since 1.0.1
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date The string representation of the date this header was created
     * @since 1.0.1
     */
    public void setDate(String date) {
        this.date = date;
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