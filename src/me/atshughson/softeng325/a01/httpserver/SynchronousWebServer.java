package me.atshughson.softeng325.a01.httpserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class SynchronousWebServer implements WebServer {
	private static final int PORT = 9000;

	private ServerSocket serverSocket;

	public SynchronousWebServer() {
		serverSocket = null;

		try {
			serverSocket = new ServerSocket(PORT);
			System.out.println("Opening socket on port " + PORT);
		} catch (IOException e) {
			System.err.println("Could not open socket on port: " + PORT);
			System.exit(-1);
		}

		System.out.println("Type 'stop' to stop the server");
	}

	@Override
	public void start() throws IOException {
		try {
			while (true) {
				Socket clientSocket = serverSocket.accept();

				BufferedReader in = new BufferedReader(new InputStreamReader(
						clientSocket.getInputStream()));
				DataOutputStream out = new DataOutputStream(
						clientSocket.getOutputStream());

				// Get the incoming request and create a response.
				Request request = new Request(in);
				Response response = new Response(request);

				// Send the HTTP response.
				response.send(out);

				clientSocket.close();
			}
		} catch (SocketException e) {
			// Do nothing when accept() is interrupted.
		}
	}

	@Override
	public void stop() throws IOException {
		serverSocket.close();
	}

	@Override
	public void listen(InputStream in) {
		new Thread(new WebServerInputListener(this, in)).start();
	}

	public static void main(String[] args) throws IOException {
		WebServer ws = new SynchronousWebServer();
		ws.listen(System.in);
		ws.start();
	}
}
