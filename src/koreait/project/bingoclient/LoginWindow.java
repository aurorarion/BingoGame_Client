package koreait.project.bingoclient;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import lombok.Getter;
import lombok.Setter;
import packet.bingo.project.Packet;
import packet.bingo.project.PacketSender;
import packet.bingo.project.PacketType;

@Getter
@Setter
public class LoginWindow extends JPanel implements MouseListener {

	private JPanel upPanel;
	private JPanel centerPanel;
	private JPanel downPanel;

	private JPanel loginPanel;
	private JPanel loginInputPanel;
	private JTextField idInput;
	private JPasswordField pwInput;
	private JButton loginBtn;

	private JLabel signup;
	private char tempEchoChar;

	public LoginWindow() {

		setLayout(new GridLayout(3, 1));

		Color backColor = new Color(47, 47, 47);
		Color foreColor = new Color(230, 230, 230);
		
		ImageIcon img = new ImageIcon("./src/images/bingo.png");

		upPanel = new JPanel() {
			@Override
			public void paint(Graphics g) {
				g.drawImage(img.getImage(), 10, 10, 470, 162, 0, 0, img.getIconWidth(), img.getIconHeight(), this);
			}
			
		};
		downPanel = new JPanel();

		centerPanel = new JPanel(new CardLayout(120, 30));

		loginPanel = new JPanel(new BorderLayout()); // 로그인 패널 선언
		loginInputPanel = new JPanel(new GridLayout(2, 1));
		idInput = new JTextField("아이디");
		idInput.setName("id");

		pwInput = new JPasswordField("비밀번호");
		tempEchoChar = pwInput.getEchoChar();
		pwInput.setEchoChar((char) 0);

		pwInput.setName("pw");
		loginBtn = new JButton("로그인");

		loginInputPanel.add(idInput);
		loginInputPanel.add(pwInput);

		signup = new JLabel("회원가입");

		loginPanel.add(loginBtn, BorderLayout.EAST);
		loginPanel.add(loginInputPanel);
		loginPanel.add(signup, BorderLayout.SOUTH);

		centerPanel.add(loginPanel);


		idInput.addMouseListener(this);
		pwInput.addMouseListener(this);

		idInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if(idInput.getText().equals("아이디")) {
					idInput.setText("");
				}
			}
		});
		
		pwInput.setFocusTraversalKeysEnabled(false);		// 탭키인식하게 하기
		pwInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_TAB) {
					pwInput.setEchoChar(tempEchoChar);
					pwInput.setText("");
				} else {
					pwInput.setEchoChar(tempEchoChar);
				}
			}
		});

		add(upPanel);
		add(centerPanel);
		add(downPanel);

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		String fieldName = ((JTextField) e.getSource()).getName();
		switch (fieldName) {
		case "id":
			if (idInput.getText().equals("아이디"))
				idInput.setText("");
			break;
		case "pw":
			pwInput.setEchoChar(tempEchoChar);
			pwInput.setText("");
		}

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
