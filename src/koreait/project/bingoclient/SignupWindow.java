package koreait.project.bingoclient;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import lombok.Getter;
import lombok.Setter;
import packet.bingo.project.Packet;
import packet.bingo.project.PacketAnalyzer;
import packet.bingo.project.PacketSender;
import packet.bingo.project.PacketType;

@Getter
@Setter
public class SignupWindow extends JPanel {

	private JPanel allInputPanel;
	private JPanel idPanel;
	private JTextField idInput;
	private JLabel idLabel;

	private JPanel pwPanel;
	private JPasswordField pwInput;
	private JLabel pwLabel;

	private JPanel pwCheckPanel;
	private JPasswordField pwCheckInput;
	private JLabel pwCheckLabel;

	private JButton idCheck;
	private JButton signupBtn;

	private Packet packet;

	public SignupWindow() {

		setLayout(new BorderLayout());

		allInputPanel = new JPanel();
		allInputPanel.setLayout(new BoxLayout(allInputPanel, BoxLayout.Y_AXIS));

		idPanel = new JPanel();
		idPanel.setLayout(new BoxLayout(idPanel, BoxLayout.X_AXIS));
		idLabel = new JLabel("              아이디 : ");
		idInput = new JTextField();
		idPanel.add(idLabel);
		idPanel.add(idInput);
		idCheck = new JButton("중복체크");
		idPanel.add(idCheck);
		idPanel.setPreferredSize(new Dimension(300, 30));
		allInputPanel.add(idPanel);

		pwPanel = new JPanel();
		pwPanel.setLayout(new BoxLayout(pwPanel, BoxLayout.X_AXIS));
		pwLabel = new JLabel("          패스워드 : ");
		pwInput = new JPasswordField();
		pwPanel.add(pwLabel);
		pwPanel.add(pwInput);
		pwPanel.setPreferredSize(new Dimension(300, 30));
		allInputPanel.add(pwPanel);

		pwCheckPanel = new JPanel();
		pwCheckPanel.setLayout(new BoxLayout(pwCheckPanel, BoxLayout.X_AXIS));
		pwCheckLabel = new JLabel("패스워드 확인 : ");
		pwCheckInput = new JPasswordField();
		pwCheckPanel.add(pwCheckLabel);
		pwCheckPanel.add(pwCheckInput);
		pwCheckInput.setPreferredSize(new Dimension(300, 30));
		allInputPanel.add(pwCheckPanel);

		signupBtn = new JButton("회원가입");

		add(allInputPanel, BorderLayout.NORTH);
		add(signupBtn, BorderLayout.SOUTH);

		signupBtn.setEnabled(false);

		idInput.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				idInput.setText("");
				signupBtn.setEnabled(false);
			}
		});
		
		
		// 회원가입 - 아이디체크
		idCheck.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (ClientMain.socket != null) {
					packet = new Packet(PacketType.DUPLICATION_CHECK, idInput.getText().trim(), null);
					PacketSender.getInstance().sendPacket(packet);
				} else {
//					오프라인 테스트
					PacketAnalyzer.getInstance().setIdflag(false);	// true - 중복, false - 가능
					if (PacketAnalyzer.getInstance().isIdflag()) {
						JOptionPane.showMessageDialog(null, "이미 사용중인 아이디 입니다.");
					} else {
						signupBtn.setEnabled(true);
					}
				}
			}
		});

	}
}