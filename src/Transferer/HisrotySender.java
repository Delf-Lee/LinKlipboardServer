package Transferer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.Vector;

import contents.Contents;
import server_manager.ClientHandler;
import server_manager.LinKlipboard;
import server_manager.LinKlipboardGroup;

public class HisrotySender extends Transfer {
	// ��Ʈ��
	private ObjectOutputStream out;
	private String ipAddr;

	public HisrotySender(LinKlipboardGroup group, ClientHandler client) {
		super(group, client);
		this.start();
	}

	@Override
	public void setConnection() {
		try {
			// ���� ���� ����
			listener = new ServerSocket(LinKlipboard.FTP_PORT);
			ready = true;
			socket = listener.accept();

			out = new ObjectOutputStream(socket.getOutputStream());

		} catch (IOException e) {
			System.out.println("���� ����");
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
			Vector<Contents> sendData = group.getHistory(); // ���� �׷��� �����丮 ������ ����
			Vector<Contents> copySendData;
			synchronized (sendData) {
				copySendData = new Vector<Contents>(sendData); // ���� 
			}
			out.writeObject(copySendData);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
