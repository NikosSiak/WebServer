package app;

import java.io.IOException;
import web.WebServer;

public class App {
    public static void main(String[] args) {
        WebServer server = new WebServer("/");
        try {
            server.bind();
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
