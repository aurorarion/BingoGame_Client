package koreait.project.bingoclient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import lombok.Getter;
import packet.bingo.project.Packet;
import packet.bingo.project.PacketAnalyzer;
import packet.bingo.project.PacketSender;
import packet.bingo.project.PacketType;

@Getter
public class BingoBtnAction implements ActionListener {

	private static BingoBtnAction instance = new BingoBtnAction();
	private GameWindow window;
	private int idx = -1;
	private Packet packet;

	private BingoBtnAction() {

	}

	public static BingoBtnAction getInstance() {
		return instance;
	}

	public void setBtnAction(GameWindow window, String receiveNum) {
		this.window = window;
		if (idx != -1) {
			window.getBtnByIdx(idx).removeActionListener(this);
		}
		for (int i = 0; i < window.getBtn().length; i++) {
			if (window.getBtnByIdx(i).getName().trim().equals(receiveNum)) {
				window.getBtnByIdx(i).addActionListener(this);
				idx = i;
				break;
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		window.getBtnByIdx(idx).setEnabled(false);
		String sendNum = ((JButton) e.getSource()).getName();
		if (ClientMain.socket != null) {
			packet = new Packet(PacketType.CLICKED_NUM, sendNum, "");
			PacketSender.getInstance().sendPacket(packet);
		}
	}
}
