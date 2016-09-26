import java.awt.*;
import java.awt.event.*;

public class A extends Frame implements WindowListener {

	public A(String title) {
		super(title);
		this.setSize(300, 300);
		this.setLocation(300, 300);
		this.addWindowListener(this);
	}

	public static void main(String args[]) {
		A a = new A("이벤트 예제");
		a.setVisible(true);
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowClosing(WindowEvent e) {
		System.out.println("ggg");
		this.setVisible(false);
		System.exit(0);
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}
}