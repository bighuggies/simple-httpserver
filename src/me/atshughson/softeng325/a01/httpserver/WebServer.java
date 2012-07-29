package me.atshughson.softeng325.a01.httpserver;

import java.io.IOException;
import java.io.InputStream;

public interface WebServer {
    /**
     * Opens the server to incoming connections over the socket, allowing it to
     * serve HTTP requests.
     * 
     * @throws IOException
     */
    public void start() throws IOException;

    /**
     * Shuts down the server.
     */
    public void stop() throws IOException;

    /**
     * Listen for commands from an input stream.
     * 
     * @param in
     *            The input stream which commands will come from.
     */
    public void listen(InputStream in);

}
