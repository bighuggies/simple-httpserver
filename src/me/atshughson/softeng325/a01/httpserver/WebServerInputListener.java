package me.atshughson.softeng325.a01.httpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class WebServerInputListener implements Runnable {

    private WebServer _webServer;
    private BufferedReader _input;

    /**
     * Listens for input on an input stream and passes commands to a WebServer
     * instance.
     * 
     * @param w
     *            The webserver to which the commands will be passed.
     * @param in
     *            The input stream on which to listen for commands.
     */
    public WebServerInputListener(WebServer w, InputStream in) {
        _webServer = w;
        _input = new BufferedReader(new InputStreamReader(in));
    }

    @Override
    public void run() {
        try {
            String line = "";

            // We only care about the stop command.
            while (!(line = _input.readLine()).equals("stop")) {
                System.out.println("Didn't understand command: " + line);
                System.out.println("Type 'stop' to stop the server.");
            }

            _webServer.stop();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
