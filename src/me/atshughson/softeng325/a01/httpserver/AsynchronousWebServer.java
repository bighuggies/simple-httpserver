package me.atshughson.softeng325.a01.httpserver;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/***
 * Boss thread of a threaded webserver which will spawn WebServerWorker threads
 * on each received HTTP request.
 * 
 * @author ahug048
 * 
 */
public class AsynchronousWebServer implements WebServer {
    private static final int SHUTDOWN_TIMEOUT = 3000; // In milliseconds
    private static final int PORT = 9000;

    private ServerSocket serverSocket;
    private ExecutorService threadPool;

    public AsynchronousWebServer() {
        serverSocket = null;

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Opening socket on port " + PORT);
        } catch (IOException e) {
            System.err.println("Could not open socket on port: " + PORT);
            System.exit(-1);
        }

        System.out.println("Type 'stop' to stop the server");

        threadPool = Executors.newCachedThreadPool();
    }

    @Override
    public void start() throws IOException {
        System.out
                .println("Listening for incoming connections on port " + PORT);

        try {
            while (true) {
                // Create a worker and pass it the client socket whenever a
                // request is received.
                WebServerWorker worker = new WebServerWorker(
                        serverSocket.accept());
                threadPool.execute(worker);
            }
        } catch (SocketException e) {
            // Do nothing when accept() is interrupted.
        }
    }

    /**
     * Shuts down the server, first attempting to finish ongoing requests.
     * 
     * @throws IOException
     */
    @Override
    public void stop() throws IOException {
        System.out.println("Shutting down server on port " + PORT);

        serverSocket.close();

        try {
            threadPool
                    .awaitTermination(SHUTDOWN_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            System.out
                    .println("Could not complete all submitted requests before shutdown: "
                            + e.getLocalizedMessage());
        }

        threadPool.shutdown();
    }

    @Override
    public void listen(InputStream in) {
        threadPool.execute(new WebServerInputListener(this, in));
    }

    public static void main(String[] args) throws IOException {
        WebServer ws = new AsynchronousWebServer();
        ws.listen(System.in);
        ws.start();
    }
}
