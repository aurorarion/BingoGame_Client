package koreait.project.bingoclient;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

import lombok.Getter;
import lombok.Setter;
import packet.bingo.project.Packet;
import packet.bingo.project.PacketSender;
import packet.bingo.project.PacketType;

@Getter
@Setter
public class GameWindow extends JPanel implements ActionListener {

	// 왼쪽
	private JPanel leftPanel;

	private JPanel bingoPanel;
	private JButton[] btn;

	private JPanel bottomPanel;
	private JPanel btnPanel;
	private JButton readyBtn;
	private JButton randomBtn;
	private JButton bingoBtn;
	private Label showServerNum;

	// 오른쪽
	private JPanel rightPanel;
	private JPanel memberListPanel;
	private JScrollPane memberListJsp;

	private JPanel chatPanel;
	private JTextArea showChat;
	private JScrollPane chatShowJsp;

	private JPanel rightBottomPanel;
	private JScrollPane inputChatJsp;
	private JTextArea inputChat;
	private JButton sendBtn;

	private ArrayList<JCheckBox> memberBoxList;

	// 게임처리
	private int LEVEL;
	private int BSIZE = 25;
	private String clickBtnNum;

	private Image[] img;
	private int[] num;
	private Packet packet;

	private String strNum;
	private Timer timer;

	public GameWindow(int Level) {
		this.LEVEL = Level;
		setLayout(new BorderLayout());

		img = new Image[LEVEL];
		num = new int[LEVEL];

		for (int i = 0; i < num.length; i++) {
			img[i] = Toolkit.getDefaultToolkit().getImage(String.format("./src/images/num_%03d.jpg", i + 1));
			// img[i] =
			// Toolkit.getDefaultToolkit().getImage(String.format("//192.168.3.185/과제/1조/images/num_%03d.jpg",
			// i + 1));
			// GameWindow.class.getResource(String.format("./src/images/num_%03d.jpg",
			// i + 1));
			num[i] = i;
		}
		randomNum();

		Color backColor = new Color(47, 47, 47);
		Color foreColor = new Color(230, 230, 230);
		Font font = new Font("맑은 고딕", Font.BOLD, 20);

		// 왼쪽 패널 선언
		leftPanel = new JPanel(new BorderLayout());

		// 빙고패널
		bingoPanel = new JPanel(new GridLayout(5, 5));
		btn = new JButton[BSIZE];
		setBtnOnPanel(); // 버튼 장착
		leftPanel.add(bingoPanel);

		// 왼쪽 바닥 패널
		bottomPanel = new JPanel(new BorderLayout());
		showServerNum = new Label();
		readyBtn = new JButton("Ready");
		randomBtn = new JButton("Random");
		bingoBtn = new JButton("Bingo");
		btnPanel = new JPanel(new GridLayout(1, 3));

		showServerNum.setFont(new Font("맑은 고딕", Font.BOLD, 30));
		showServerNum.setForeground(foreColor);
		showServerNum.setAlignment(Label.CENTER);

		readyBtn.setBackground(backColor);
		readyBtn.setForeground(foreColor);
		readyBtn.setFont(font);
		randomBtn.setBackground(backColor);
		randomBtn.setForeground(foreColor);
		randomBtn.setFont(font);
		bingoBtn.setBackground(backColor);
		bingoBtn.setForeground(foreColor);
		bingoBtn.setFont(font);
		btnPanel.add(randomBtn);
		btnPanel.add(readyBtn);
		btnPanel.add(bingoBtn);

		bottomPanel.setBackground(backColor);
		bottomPanel.setPreferredSize(new Dimension(700, 93));
		bottomPanel.add(btnPanel, BorderLayout.WEST);
		bottomPanel.add(showServerNum, BorderLayout.CENTER);

		leftPanel.add(bottomPanel, BorderLayout.SOUTH);
		add(leftPanel);

		// 오른쪽패널 선언
		rightPanel = new JPanel();
		chatPanel = new JPanel();
		memberListPanel = new JPanel();
		memberListPanel.setLayout(new BoxLayout(memberListPanel, BoxLayout.Y_AXIS));
		memberBoxList = new ArrayList<>();

		rightBottomPanel = new JPanel();

		showChat = new JTextArea(1, 1);
		showChat.setLineWrap(true);
		showChat.setEnabled(false);
		showChat.setBackground(backColor);
		showChat.setForeground(foreColor);

		inputChat = new JTextArea(1, 1);
		inputChat.setLineWrap(true);
		inputChat.setBackground(backColor);
		inputChat.setForeground(foreColor);

		chatShowJsp = new JScrollPane(showChat);
		inputChatJsp = new JScrollPane(inputChat);
		memberListJsp = new JScrollPane(memberListPanel);

		sendBtn = new JButton("Send");

		// 오른쪽 패널 설정
		rightPanel.setPreferredSize(new Dimension(200, 470));
		rightPanel.setLayout(new BorderLayout());

		// 접속자 목록패널
		memberListPanel.setBackground(backColor);
		memberListJsp.setPreferredSize(new Dimension(200, 93));
		rightPanel.add(memberListJsp, BorderLayout.NORTH);

		// 채팅패널
		chatPanel.setLayout(new CardLayout());
		chatPanel.add(chatShowJsp);
		rightPanel.add(chatPanel);

		// 오른쪽 하단 입력패널 - 채팅입력패널

		rightBottomPanel.setLayout(new BorderLayout());
		rightBottomPanel.setPreferredSize(new Dimension(200, 93));

		// rightBottomPanel.add(chatInputPanel);
		inputChatJsp.setPreferredSize(new Dimension(100, 90));
		rightBottomPanel.add(inputChatJsp);
		rightBottomPanel.add(sendBtn, BorderLayout.EAST);
		rightPanel.add(rightBottomPanel, BorderLayout.SOUTH);

		// 프레임에 오른쪽 패널 올리기
		add(rightPanel, BorderLayout.EAST);

		// 버튼 리스너설정
		readyBtn.addActionListener(this);
		randomBtn.addActionListener(this);
		bingoBtn.addActionListener(this);
		sendBtn.addActionListener(this);

		// 채팅창 엔터키
		inputChat.addKeyListener(new KeyAdapter() {
			boolean enterDown = false, shiftDown = false;

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					shiftDown = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					enterDown = true;
				}
				if (shiftDown && enterDown) {
					inputChat.append("\n");
				} else if (e.getExtendedKeyCode() == KeyEvent.VK_ENTER) {
					String str = inputChat.getText().trim();
					if (str.length() > 0) {
						if (ClientMain.socket != null) {
							packet = new Packet(PacketType.MESSAGE, str, "");
							PacketSender.getInstance().sendPacket(packet);
						} else {
							// 오프라인 테스트
							if (showChat.getText().length() > 0) {
								showChat.append("\n" + str);
							} else {
								showChat.append(str);
							}
						}
						showChat.setCaretPosition(showChat.getDocument().getLength());
						inputChat.setText("");
					}
					e.consume();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					shiftDown = false;
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					enterDown = false;
				}
			}
		});

		setVisible(true);
	}

	public JButton getBtnByIdx(int idx) {
		return btn[idx];
	}

	public void createCheckBoxList(String memberStr, String ready) {
		memberBoxList.clear();
		String[] memList = memberStr.split(",");
		String[] readyList = ready.split(",");
		for (int i = 0; i < memList.length; i++) {
			memberBoxList.add(new JCheckBox());
			memberBoxList.get(i).setBackground(new Color(47, 47, 47));
			memberBoxList.get(i).setForeground(new Color(230, 230, 230));
			memberBoxList.get(i).setEnabled(false);
			if (readyList[i].equals("true")) {
				memberBoxList.get(i).setSelected(true);
			}
			memberBoxList.get(i).setText(memList[i]);
		}
	}

	public void gameStart() {
		readyBtn.setEnabled(false);
	}

	public void memberListRenew() {
		memberListPanel.removeAll();
		for (JCheckBox member : memberBoxList) {
			memberListPanel.add(member);
		}
		memberListPanel.repaint();
		memberListPanel.revalidate();
	}

	// 승리자 메시지
	public void win() {
		JOptionPane.showMessageDialog(showChat, "You Win!!");
	}

	// 패배자 메시지
	public void lose() {
		JOptionPane.showMessageDialog(showChat, "You lose!!");
	}

	private void randomNum() {
		for (int i = 0; i < 10000; i++) {
			int r = (int) (Math.random() * LEVEL - 1) + 1;
			int temp = num[0];
			num[0] = num[r];
			num[r] = temp;
		}
	}

	private void setBtnOnPanel() {
		for (int i = 0; i < btn.length; i++) {
			btn[i] = new JButton(new ImageIcon(img[num[i]]));
			btn[i].setName(num[i] + 1 + "");
			btn[i].addActionListener(this);
			bingoPanel.add(btn[i]);
		}
	}

	private boolean validateCheck(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) < '0' || str.charAt(i) > '9') {
				return true;
			}
		}
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().length() > 0) {
			switch (e.getActionCommand()) {
			case "Random":
				bingoPanel.removeAll();
				randomNum();
				setBtnOnPanel();
				bingoPanel.revalidate();
				break;
			case "Ready":
				// 설정한 빙고 넘버 스트링으로 저장후 ready버튼시 서버로 전송
				if (ClientMain.socket != null) {
					for (int i = 0; i < btn.length; i++) {
						btn[i].removeActionListener(this); // 버튼 숫자 커스텀 액션리스너 제거
						if (i > 0) {
							strNum = "," + btn[i].getName();
						} else {
							strNum = btn[i].getName();
						}
					}
					readyBtn.setEnabled(false);
					randomBtn.removeActionListener(this);
					packet = new Packet(PacketType.READY, strNum, null);
					PacketSender.getInstance().sendPacket(packet);
				}
				break;
			case "Bingo":
				int count = 0;
				int bingoCount = 0;
				// 가로빙고체크

				for (int i = 0; i < btn.length; i++) {
					if (!btn[i].isEnabled()) {
						count++;
						if (count == 5) {
							bingoCount++;
							count = 0;
						}
					} else {
						i += 4 - i % 5;
						count = 0;
					}
				}
				// 세로빙고체크

				for (int i = 0; i < btn.length; i++) {
					if (!btn[i * 5 - (24 * (i / 5))].isEnabled()) {
						count++;
						if (count == 5) {
							bingoCount++;
							count = 0;
						}
					} else {
						i += 4 - i % 5;
						count = 0;
					}
				}

				// 대각선 빙고
				for (int i = 0; i < 10; i++) {
					if (!btn[i % 5 * (i < 5 ? 6 : 4) + (i < 5 ? 0 : 4)].isEnabled()) {
						count++;
						if (count == 5) {
							bingoCount++;
							count = 0;
						}
					} else {
						i += 4 - i % 5;
						count = 0;
					}
				}

				if (bingoCount >= 5) {
					if (ClientMain.socket != null) {
						packet = new Packet(PacketType.BINGO_COMPLETE, "bingo complete", null);
						PacketSender.getInstance().sendPacket(packet);
					}
					JOptionPane.showMessageDialog(showChat, bingoCount + "빙고! 빙고완성");
				} else {
					JOptionPane.showMessageDialog(showChat, bingoCount + "빙고!");
				}
				break;
			case "Send":
				String str = inputChat.getText().trim();
				if (str.length() > 0) {
					if (ClientMain.socket != null) {
						packet = new Packet(PacketType.MESSAGE, str, "");
						PacketSender.getInstance().sendPacket(packet);
					} else {
						// 오프라인 테스트
						if (showChat.getText().length() > 0) {
							showChat.append("\n" + str);
						} else {
							showChat.append(str);
						}
					}
					showChat.setCaretPosition(showChat.getDocument().getLength());
					inputChat.setText("");
					inputChat.requestFocus();
				}
				break;
			}
			// 빙고 버튼 숫자 커스터마이즈
		} else {
			int original = 0; // 선택된 버튼 위치 저장
			String bingBtn = ((JButton) e.getSource()).getName();
			for (int i = 0; i < btn.length; i++) {
				if (btn[i].getName().equals(bingBtn)) {
					original = i;
					break;
				}
			}

			String select = (String) JOptionPane.showInputDialog(((JButton) e.getSource()), "원하는 숫자를 입력하세요", bingBtn);
			if (select != null) {
				if (validateCheck(select)) {
					JOptionPane.showMessageDialog(null, "잘못된 값을 입력하셨습니다.");
				} else if (Integer.parseInt(select) > LEVEL) {
					JOptionPane.showMessageDialog(null, LEVEL + "이하의 숫자를 입력하세요");
				} else {
					int selectNum = Integer.parseInt(select) - 1;
					for (int i = 0; i < num.length; i++) {
						if (num[i] == selectNum) {
							int temp = num[i];
							num[i] = num[original];
							num[original] = temp;
							break;
						}
					}
					bingoPanel.removeAll();
					setBtnOnPanel();
					bingoPanel.revalidate();
				}
			}
		}
	}
}