package packet.bingo.project;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

public class PacketReceiver implements Runnable {

	private Socket socket;

	public PacketReceiver(Socket socket) {
		this.socket = socket;
		System.out.println("[!] PacketReceiver instance is ready.");
	}

	@Override
	public void run() {
		while (true) {
			try (InputStream is = socket.getInputStream(); ObjectInputStream reader = new ObjectInputStream(is)) {
				Packet packet;
				while ((packet = (Packet) reader.readObject()) != null) {
					PacketAnalyzer.getInstance().analyzePacket(packet);
				}
			} catch (SocketException e) {
				e.printStackTrace();
				System.out.println("[!] Socket is closed.");
				break;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				System.out.println("[!] Please define Packet class.");
				break;
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("[!] Unable to get input stream.");
				break;
			}
		}
		System.out.println("[!] Thread is going to be terminated.");
	}

}
