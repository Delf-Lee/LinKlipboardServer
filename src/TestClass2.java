import java.util.Vector;

import javax.swing.JFrame;

import Transferer.Transfer;

public class TestClass2 extends JFrame {
	private ThreadQueue m = new ThreadQueue();

	public void run() {
		m.start();
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
		}

	}

	public static void main(String[] args) {
		new TestClass2().run();
	}
}

class ThreadQueue extends Thread {
	private Vector<Transfer> queue = new Vector<Transfer>();
	private boolean run = false;
	private final static int FIRST_THREAD = 0;

	public ThreadQueue() {

		try {
			this.wait();
			this.start();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void add(Transfer th) {
		queue.add(th);
		notify();
	}

	@Override
	public void run() {
		while (true) {
			try {
				wait();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
}
