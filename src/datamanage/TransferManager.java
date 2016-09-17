package datamanage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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
	private ServerSocket listener;
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	private DataOutputStream dout;
	private DataInputStream din;

	private PrintWriter pout;

	private int process; // ������ ������ �۾�
	private final static int RECEIVE = 1; // ���� �۾�
	private final static int SEND = 0; // �۽� �۾�

	private boolean connect = false;

	public TransferManager(LinKlipboardGroup group, ClientHandler client, int dataType) {
		this.client = client;
		this.type = dataType;
		this.group = group;
	}

	/** Ŭ���̾�Ʈ�� ���Ͽ� ������ ��û�Ѵ�. */
	//	private void connectToSocket() {
	//		try {
	//			String ipAddr = client.getRemoteAddr();
	//			int portNum = client.getRemotePort();
	//
	//			// ���� ���� ����
	//			socket = new Socket(ipAddr, port);
	//
	//			// ��Ʈ�� ����
	//			out = new ObjectOutputStream(socket.getOutputStream());
	//			in = new ObjectInputStream(socket.getInputStream());
	//
	//		} catch (UnknownHostException e) {
	//			e.printStackTrace();
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//	}

	private void createSoecket() {
		System.out.println("���� ����");
		try {
			// ���� ���� ����
			listener = new ServerSocket(port);
			connect = true;
			System.out.println("���� ����");
			socket = listener.accept();

			System.out.println("�����ߴ�");
			// ��Ʈ�� ����
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

		} catch (IOException e) {

			//			try {
			//				socket.close();
			//				listener.close();
			//			} catch (IOException e1) {
			//				e1.printStackTrace();
			//			}

			System.out.println("���� ����");
			e.printStackTrace();
		}
	}

	/** ������ �����ϴ� �����带 �����Ѵ�. ��Ȯ���� �����忡�� �����͸� �����ϴ� �κи� �����Ѵ�. 
	 * @param out Ŭ���̾�Ʈ���� ������ ������ ��Ʈ��.
	 * ������ ���� �Ŀ� Ŭ���̾�Ʈ�� �����ؾ� �ϱ� ������ �����忡�� Ŭ���̾�Ʈ�� ������ ������ ��, ������ �����Ѵ�.*/
	public void createReceiveThread(PrintWriter out) {
		System.out.println("���ú� ������ �����?");
		process = RECEIVE;
		this.pout = out;
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
			createSoecket();
			Contents contents = receiveData();
			forDebug(contents);
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

	public void forDebug(Contents contents) {
		StringContents str = (StringContents) contents;
		System.out.println("���� ���ڿ� =" + str.getString());
	}
	
	public boolean isConnected() {
		return connect;
	}
}