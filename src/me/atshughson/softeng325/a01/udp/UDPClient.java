package me.atshughson.softeng325.a01.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient {
	public static void main(String[] args) throws InterruptedException {
		try {
			InetAddress serverAddress = InetAddress.getByName(args[0]);
			int serverPort = Integer.parseInt(args[1]);
			int numPackets = Integer.parseInt(args[2]);

			DatagramSocket socket = new DatagramSocket();

			for (int i = 0; i < numPackets; i++) {
				byte[] data = (Integer.toString(i)).getBytes();
				DatagramPacket request = new DatagramPacket(data, data.length,
						serverAddress, serverPort);

				socket.send(request);
			}

			socket.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}