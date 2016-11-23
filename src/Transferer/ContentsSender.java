package Transferer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import contents.Contents;
import server_manager.ClientHandler;
import server_manager.LinKlipboardGroup;

public class ContentsSender extends Transfer {
	// ��Ʈ��
	private ObjectOutputStream out;

	public ContentsSender(LinKlipboardGroup group, ClientHandler client) {
		super(group, client);
		//client.addThread(this);
		this.start();
	}

	@Override
	public void setConnection() {
		try {
			// ���� ���� ����
			System.out.println("���� �Ѹ� ip: " + client.getRemoteAddr());
			System.out.println(client.getRemoteAddr() + " " + client.getRemotePort() + 1);
			socket = new Socket(client.getRemoteAddr(), client.getRemotePort() + 1);
			//socket = listener.accept();
			// TODO: timeout
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
		System.out.println("���� ���� �Ϸ�");
		try {
			Contents sendData = group.getLastContents();
			out.writeObject(sendData);
			out.flush();
			System.out.println("���� ����");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		closeSocket();
	}
}