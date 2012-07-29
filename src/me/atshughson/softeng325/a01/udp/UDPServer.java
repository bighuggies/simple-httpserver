package me.atshughson.softeng325.a01.udp;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class UDPServer {
	private static final int PORT = 9000;
	private static final int BUF_SIZE = 8;
	private static final int TIMEOUT = 100; // timeout in milliseconds

	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		DatagramSocket socket = new DatagramSocket(PORT);
		InetAddress serverHost = InetAddress.getLocalHost();

		socket.setSoTimeout(TIMEOUT);

		System.out.println("Server address: " + serverHost.getHostAddress()
				+ ", " + PORT);

		DatagramPacket packet = new DatagramPacket(new byte[BUF_SIZE], BUF_SIZE);

		int count = 0;
		int outOfOrder = 0;
		int prev = 0;
		int curr = 0;

		while (true) {
			try {
				socket.receive(packet);

				ByteArrayInputStream inputByteStream = new ByteArrayInputStream(
						packet.getData());
				DataInputStream inputDataStream = new DataInputStream(
						inputByteStream);
				curr = inputDataStream.readInt();

				// System.out.println(curr);

				if (curr - prev < 0) {
					outOfOrder++;
				}

				count++;
			} catch (SocketTimeoutException e) {
				if (count > 0) {
					System.out.println(count + " packets received.");
					System.out.println(outOfOrder + " packets out of order.");
					count = 0;
					outOfOrder = 0;
				}
			}
		}
	}
}