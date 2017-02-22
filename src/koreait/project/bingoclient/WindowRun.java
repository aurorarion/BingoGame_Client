package koreait.project.bingoclient;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import lombok.Getter;
import lombok.Setter;
import packet.bingo.project.Packet;
import packet.bingo.project.PacketAnalyzer;
import packet.bingo.project.PacketSender;
import packet.bingo.project.PacketType;

@Getter
@Setter
public class WindowRun extends JFrame {

	private GameWindow game = new GameWindow(50);
	private LoginWindow login = new LoginWindow();
	private SignupWindow signup = new SignupWindow();
	private Packet packet;

	private int loginWidth = 500, loginHeight = 500;
	private int gameWidth = 700, gameHeight = 600;
	private int signupWidth = 300, signupHeight = 160;

	private Timer timer;

	public WindowRun() {
		setTitle("Game Login");
		setSize(loginWidth, loginHeight);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		add(login);

		// 회원가입
		login.getSignup().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ClientMain.connect(); // 서버 연결 시도
				if (ClientMain.socket == null) {
					JOptionPane.showMessageDialog(null, "서버연결 실패인한 오프라인 테스트 모드로 회원가입창에 진입합니다.");
				}
				setTitle("SignUP");
				setSize(signupWidth, signupHeight);
				setLocationRelativeTo(null);
				signup.setVisible(true);
				login.setVisible(false);
				add(signup);
				validate();
				login.getIdInput().setText("아이디");
				login.getPwInput().setText("패스워드");
			}
		});

		// 로그인버튼
		login.getLoginBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ClientMain.connect(); // 서버 연결 시도
				if (ClientMain.socket != null) {
					Packet packet = new Packet(PacketType.SIGN_IN, login.getIdInput().getText(),
							login.getPwInput().getText());
					PacketSender.getInstance().sendPacket(packet);
					// 유저 접속 정보요청
					timer = new Timer(2000, new UserChecker());
					timer.start();
				} else {
					// 오프라인 테스트
					JOptionPane.showMessageDialog(null, "서버연결 실패인한 오프라인 테스트 모드로 진입합니다.");
					PacketAnalyzer.getInstance().setLoginFlag(true);

					loginLevelCheck();
				}
			}
		});

		// 비밀번호필드 엔터
		login.getPwInput().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Packet packet = new Packet(PacketType.SIGN_IN, login.getIdInput().getText(),
						login.getPwInput().getText());
				PacketSender.getInstance().sendPacket(packet);
				// 유저 접속 정보요청
				timer = new Timer(2000, new UserChecker());
				timer.start();
			}
		});

		// 회원가입버튼
		signup.getSignupBtn().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int signupCheck;
				if (ClientMain.socket != null) {
					if (signup.getPwInput().getText().equals(signup.getPwCheckInput().getText())) {
						signupCheck = JOptionPane.showConfirmDialog(signup.getSignupBtn(), "회원가입 하시겠습니까?",
								"SingUp Success", JOptionPane.OK_OPTION);
						if (signupCheck == 0) {
							packet = new Packet(PacketType.SIGN_UP, signup.getIdInput().getText().trim(),
									signup.getPwInput().getText().trim());
							PacketSender.getInstance().sendPacket(packet);
							JOptionPane.showMessageDialog(signup.getSignupBtn(), "회원가입 성공");
						} else {
							JOptionPane.showMessageDialog(signup.getSignupBtn(), "회원가입 취소");
						}
						signup.getIdInput().setText("");
						signup.getPwInput().setText("");
						signup.getPwCheckInput().setText("");
						changeToLoginPanel();
					} else {
						JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다");
					}
				} else {
					// 오프라인 테스트
					if (signup.getPwInput().getText().equals(signup.getPwCheckInput().getText())) {
						signupCheck = JOptionPane.showConfirmDialog(signup.getSignupBtn(), "회원가입 하시겠습니까?",
								"SingUp Success", JOptionPane.OK_OPTION);
						if (signupCheck == 0) {
							JOptionPane.showMessageDialog(signup.getSignupBtn(), "회원가입 성공");
						} else {
							JOptionPane.showMessageDialog(signup.getSignupBtn(), "회원가입 취소");
						}
						signup.getIdInput().setText("");
						signup.getPwInput().setText("");
						signup.getPwCheckInput().setText("");
						changeToLoginPanel();
					} else {
						JOptionPane.showMessageDialog(null, "비밀번호가 일치하지 않습니다");
					}
				}
			}

		});
		setVisible(true);
	}

	private void changeToLoginPanel() {
		setTitle("Game Login");
		setSize(loginWidth, loginHeight);
		setLocationRelativeTo(null);
		signup.setVisible(false);
		login.setVisible(true);
		add(login);
		validate();
	}

	public void loginLevelCheck() {
		if (PacketAnalyzer.getInstance().isLoginFlag()) {
			String[] example = { "50", "100", "150" };
			String select = (String) JOptionPane.showInputDialog(login.getIdInput(), "난이도를 선택하세요",
					"Bingo Game ver 0.9 beta", JOptionPane.QUESTION_MESSAGE, null, example, "50");
			if (select != null) {
				game = new GameWindow(Integer.parseInt(select));
				setTitle("BingoGame v0.9 beta");
				setSize(gameWidth, gameHeight);
				setLocationRelativeTo(null);
				login.setVisible(false);
				add(game);
				validate();
			}
		} else {
			JOptionPane.showMessageDialog(null, "아이디 또는 비밀번호를 다시 확인해주세요");
		}
	}

	// 접속자 목록 리스트 요청 클래스
	private class UserChecker implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (ClientMain.socket != null) {
				Packet packet = new Packet(PacketType.REQUEST_USER_LIST, null, null);
				PacketSender.getInstance().sendPacket(packet);
			} else {
				JOptionPane.showMessageDialog(null, "접속이 끊겼습니다.");
			}
		}
	}
}