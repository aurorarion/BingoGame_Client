package koreait.project.bingoclient;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import lombok.Getter;
import packet.bingo.project.PacketAnalyzer;
import packet.bingo.project.PacketReceiver;
import packet.bingo.project.PacketSender;

@Getter
public class ClientMain {

	static Socket socket = null;
	static WindowRun window;

	public static void main(String[] args) {
		
		window = new WindowRun();

	}
	
	public static void connect() {
		try {
			socket = new Socket("192.168.3.226", 7000);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "서버 연결에 실패하였습니다.");
		}
		
		if (socket != null) {
			PacketSender.makeInstance(socket);
			PacketAnalyzer.getInstance().setCntWindow(window);
			PacketReceiver task = new PacketReceiver(socket);
			Thread thread = new Thread(task);
			thread.start();
		}
	}

}