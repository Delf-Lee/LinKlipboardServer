package Transferer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.Iterator;
import java.util.Vector;

import contents.Contents;
import datamanage.ClientInitData;
import server_manager.ClientHandler;
import server_manager.LinKlipboard;
import server_manager.LinKlipboardGroup;

public class HisrotySender extends Transfer {
	// ��Ʈ��
	private ObjectOutputStream out;
	private String ipAddr;

	private int serialNo = LinKlipboard.NULL;

	public HisrotySender(LinKlipboardGroup group, ClientHandler client) {
		super(group, client);
		//client.addThread(this);
		this.start();
	}

	public HisrotySender(LinKlipboardGroup group, ClientHandler client, String serialNo) {
		this(group, client);
		this.serialNo = Integer.parseInt(serialNo);
		// client.addThread(this);
		start();
	}

	@Override
	public void setConnection() {
		try {
			// ���� ���� ����
			System.out.println(client.getRemotePort());
			listener = new ServerSocket(client.getRemotePort());
			ready = true;
			System.out.println("���� ���� ��� ��");
			socket = listener.accept();
			System.out.println("����");

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
		if (serialNo == LinKlipboard.NULL) {
			System.out.println("��ü �����丮 ����");
			try {
				System.out.println(group.size());
				ClientInitData sendInitData = new ClientInitData(group); // ������ Contents Vector
				out.writeObject(sendInitData);
				out.flush();
				System.out.println("���� �Ϸ�"); // debug
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			Contents cotents = group.getHistoryContents(serialNo);
			try {
				out.writeObject(cotents);
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		closeSocket();
	}
}
