package packet.bingo.project;

import javax.swing.JOptionPane;

import koreait.project.bingoclient.BingoBtnAction;
import koreait.project.bingoclient.WindowRun;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PacketAnalyzer {

	private static PacketAnalyzer instance = new PacketAnalyzer();
	private WindowRun window;
	private boolean loginFlag = false;
	private boolean idflag = false;

	private PacketAnalyzer() {
	}

	public static PacketAnalyzer getInstance() {
		return instance;
	}

	public void setCntWindow(WindowRun window) {
		this.window = window;
	}

	public void analyzePacket(Packet packet) {
		switch (packet.getProtocol()) {
		case MESSAGE:
			String str = packet.getMainField().trim();
			if (window.getGame().getShowChat().getText().length() > 0) {
				window.getGame().getShowChat().append("\n" + str);
			} else {
				window.getGame().getShowChat().append(str);
			}
			window.getGame().getShowChat().setCaretPosition(window.getGame().getShowChat().getDocument().getLength());
			break;
		case MESSAGE_TO:
			break;
		case SIGN_IN:
			// System.out.println(packet.getMainField());
			if (packet.getMainField().equals("true")) {
				loginFlag = true;
			}
			window.loginLevelCheck();
			break;
		case BINGO_NUM:
			window.getGame().getShowServerNum().setText(packet.getMainField());
			BingoBtnAction.getInstance().setBtnAction(window.getGame(), packet.getMainField());
			break;
		case CLICKED_NUM:
			break;
		case READY:
			window.getGame().gameStart();
			break;
		case DUPLICATION_CHECK:
			System.out.println(packet.getMainField());
			if (packet.getMainField().equals("false")) {
				idflag = true;
			}
			if (idflag) {
				window.getSignup().getSignupBtn().setEnabled(true);
			} else {
				JOptionPane.showMessageDialog(null, "이미 사용중인 아이디 입니다.");
			}
			break;
		case REQUEST_USER_LIST:
			window.getGame().createCheckBoxList(packet.getMainField(), packet.getSubField());
			window.getGame().memberListRenew();
			break;
		case BINGO_COMPLETE:
			window.getGame().lose(); // 패배메세지
			System.exit(0);
			break;
		case WIN:
			window.getGame().win(); // 승리메세지
			break;
		default:
			System.out.println("Wrong protocol");
		}
	}
}