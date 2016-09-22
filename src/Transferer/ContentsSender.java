package Transferer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import contents.Contents;
import server_manager.ClientHandler;
import server_manager.LinKlipboard;
import server_manager.LinKlipboardGroup;

public class ContentsSender extends Transfer {
	// ��Ʈ��
	private ObjectOutputStream out;
	private String ipAddr;
	public ContentsSender(LinKlipboardGroup group, ClientHandler client) {
		super(group, client);
		this.start();
	}
	
	public ContentsSender(LinKlipboardGroup group, String ip) {
		super(group, null);
		this.ipAddr = ip;
		this.start();
	}

	@Override
	public void setConnection() {
		try {
			// ���� ���� ����
			socket = new Socket(ipAddr, LinKlipboard.FTP_PORT);
			// ��Ʈ�� ����
			out = new ObjectOutputStream(socket.getOutputStream());

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void closeSocket() {
		try {
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		setConnection();

		try {
			Contents sendData = group.getLastContents();
			out.writeObject(sendData);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
