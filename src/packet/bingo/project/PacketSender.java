package packet.bingo.project;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class PacketSender {

	OutputStream os;
	ObjectOutputStream writer;

	private static PacketSender instance;

	private PacketSender(Socket socket) {
		try {
			os = socket.getOutputStream();
			writer = new ObjectOutputStream(os);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("[!] Unable to get output stream.");
		}

		System.out.println("[!] PacketSender instance is ready!");
	}

	public static void makeInstance(Socket socket) {
		instance = new PacketSender(socket);
	}

	public static PacketSender getInstance() {
		return instance;
	}

	public boolean sendPacket(Packet packet) {
		boolean ret = false;
		try {
			writer.writeObject(packet);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("[!] Unable to send a packet to server. Check out internet environment.");
		}
		return ret;
	}

}
