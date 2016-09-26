import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class TestClass2 extends JFrame {
	public TestClass2() {
		setSize(100, 100);
		
		setVisible(true);
	}

	public static void main(String[] args) {
		TestClass2 a = new TestClass2();
		MsgWinow.confirm(a, "gg");
	}
}

class MsgWinow extends JOptionPane {

	public static void error(JFrame screen, String text) {
		showMessageDialog(screen, text, "오류", ERROR_MESSAGE);
	}

	public static boolean confirm(JFrame screen, String text) {
		int result = showConfirmDialog(screen, text, "확인", YES_NO_OPTION);
		if (result == CLOSED_OPTION) {
			return false;
		}
		else if (result == YES_OPTION) {
			return true;
		}
		else {
			return false;
		}
	}
}
