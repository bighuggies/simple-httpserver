package me.atshughson.softeng325.a01.httpserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/***
 * Takes a client socket of a host which has made a request to the web server
 * and handles the request and response lifecycle of the HTTP transaction.
 * 
 * @author ahug048
 * 
 */
public class WebServerWorker extends Thread {
	private Socket socket;
	private BufferedReader in;
	private DataOutputStream out;

	/**
	 * Creates a WebServerWorker to handle the request/response lifecycle of the
	 * HTTP transaction with a host.
	 * 
	 * @param s
	 *            Client socket with which to communicate with the host.
	 * @throws IOException
	 */
	public WebServerWorker(Socket s) throws IOException {
		this.socket = s;
		this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		this.out = new DataOutputStream(s.getOutputStream());
	}

	/**
	 * Run whenever the thread is started. Gets the request from the
	 * clientSocket, creates a response and sends it back to the host which made
	 * the request.
	 */
	public void run() {
		try {
			Request request = new Request(in);
			Response response = new Response(request);

			if (!socket.isClosed()) {
				response.send(out);
			} else {
				System.out.println("Could not service request, socket closed.");
			}

			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
