package Transferer;

import java.net.ServerSocket;
import java.net.Socket;

import server_manager.ClientHandler;
import server_manager.LinKlipboardGroup;

abstract public class Transfer extends Thread {
	
	protected ServerSocket listener;
	protected Socket socket;

	protected LinKlipboardGroup group;
	protected ClientHandler client;

	protected static final int BYTE_SIZE = 65536;
	protected boolean ready = false;

	public Transfer(LinKlipboardGroup group, ClientHandler client) {
		this.group = group;
		this.client = client;
	}
	
	/** ������ ���� Ŭ���̾�Ʈ�� ������ ��ٸ���. */
	abstract public void setConnection();
	/** ���� ������ �ݴ´�. */
	abstract public void closeSocket();
	
	/** @return ������ �غ�Ǿ����� true, �ƴϸ� false */
	public boolean isReady() {
		return ready;
	}
}
