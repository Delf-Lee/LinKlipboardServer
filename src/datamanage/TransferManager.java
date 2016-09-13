package datamanage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import contents.Contents;
import contents.ImageContents;
import contents.StringContents;
import server_manager.ClientHandler;
import server_manager.LinKlipboard;
import server_manager.LinKlipboardGroup;

public class TransferManager extends Thread {

	private LinKlipboardGroup group;
	private ClientHandler client;
	private int type;

	private static final int port = 20;
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	private DataOutputStream dOut;
	private DataInputStream dIn;

	private int process; // ������ ������ �۾�
	private final static int RECEIVE = 1; // ���� �۾�
	private final static int SEND = 0; // �۽� �۾�

	public TransferManager(LinKlipboardGroup group, ClientHandler client, int dataType) {
		this.client = client;
		this.type = dataType;
		this.group = group;
	}

	/** Ŭ���̾�Ʈ�� ���Ͽ� ������ ��û�Ѵ�. */
	private void connectToSocket() {
		try {
			String ipAddr = client.getRemoteAddr();
			int portNum = client.getRemotePort();

			// ���� ���� ����
			socket = new Socket(ipAddr, portNum);

			// ��Ʈ�� ����
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** ������ �����ϴ� �����带 �����Ѵ�. ��Ȯ���� �����忡�� �����͸� �����ϴ� �κи� �����Ѵ�. */
	public void createReceiveThread() {
		process = RECEIVE;
		this.start();
	}

	public void createSendThread() {
		process = SEND;
		this.start();
	}

	/** �����͸� ���� �����ϴ� �κ� */
	private Contents receiveData() {
		try {
			switch (type) {
			case LinKlipboard.STRING_TYPE:
				return new StringContents(client.getNickname()).receiveData(in);

			case LinKlipboard.IMAGE_TYPE:
				return new ImageContents(client.getNickname()).receiveData(in);

			case LinKlipboard.FILE_TYPE:
				return null;
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**�����͸� ���� �����ϴ� �κ� */
	private void sendData() {
		try {
			Contents sendData = group.getLastContents();
			out.writeObject(sendData);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {

		switch (process) {
		case RECEIVE: // ����
			connectToSocket();
			Contents contents = receiveData();
			group.setLastContents(contents);
			break;

		case SEND: // �۽� 
			sendData();
			break;
		}
		try {
			in.close();
			socket.close();
		} catch (IOException e) {
		}
	}
}