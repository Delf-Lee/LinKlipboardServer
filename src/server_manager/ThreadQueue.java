package server_manager;

import java.util.Vector;

import Transferer.Transfer;

public class ThreadQueue extends Thread {
	private Vector<Transfer> queue = new Vector<Transfer>();
	private boolean run = false;
	private final static int FIRST_THREAD = 0;

	public ThreadQueue(){
		// this.wait();
		
		this.start();
	}

	public void add(Transfer th) {
		queue.add(th);
		//	notify();
	}

	//	@Override
	//	public void run() {
	//		while (true) {
	//			try {
	//				wait();
	//				if (run == false && !queue.isEmpty()) {
	//					Transfer now = queue.remove(FIRST_THREAD);
	//					now.start();
	//					now.join();
	//				}
	//			} catch (InterruptedException e1) {
	//				e1.printStackTrace();
	//			}
	//		}
	//	}
	@Override
	public void run() {
		while (true) {
			if (run == false && !queue.isEmpty()) {
				Transfer now = queue.remove(FIRST_THREAD);
				now.start();
				run = true;
				while (now.isAlive()) {
				}
				run = false;
			}
		}
	}
}
