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
	
	/** 소켓을 열고 클라이언트의 접속을 기다린다. */
	abstract public void setConnection();
	/** 열린 소켓을 닫는다. */
	abstract public void closeSocket();
	
	/** @return 연결이 준비되었으면 true, 아니면 false */
	public boolean isReady() {
		return ready;
	}
}
