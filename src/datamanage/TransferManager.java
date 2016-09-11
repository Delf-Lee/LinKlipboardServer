package datamanage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;

import server_manager.ClientHandler;
import server_manager.LinKlipboard;
import server_manager.LinKlipboardGroup;

public class TransferManager extends Thread {

	private LinKlipboardGroup group;
	private ClientHandler client;
	private int type;

	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	private int process;
	private final static int RECEIVE = 1;
	private final static int SEND = 0;

	public TransferManager(LinKlipboardGroup group, ClientHandler client, int dataType) {
		this.client = client;
		this.type = dataType;
		this.group = group;
	}

	/** Ŭ���̾�Ʈ�� ���Ͽ� ������ ��û�Ѵ�. */
	public void setConnection() {
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

	/***/

	/** ������ �����ϴ� �����带 �����Ѵ�. ��Ȯ���� �����忡�� �����͸� �����ϴ� �κи� �����Ѵ�. */
	public void createReceiveThread() {
		process = RECEIVE;
		this.start();
	}

	/** �����͸� ���� �����ϴ� �κ� */
	private Contents receiveData() {
		try {

			switch (type) {
			case LinKlipboard.STRING_TYPE:
				//String receiveStr = (String) in.readObject();
				//StringContents contensStr = new StringContents(client.getNickname(), receiveStr);
				return new StringContents(client.getNickname()).receiveData(in);

			case LinKlipboard.IMAGE_TYPE:
				//ImageIcon receiveImg = (ImageIcon) in.readObject();
				//ImageContents contensImg = new ImageContents(client.getNickname(), receiveImg);
				return new ImageContents(client.getNickname()).receiveData(in);

			case LinKlipboard.FILE_TYPE:
				return null;

			}
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**�����͸� ���� �����ϴ� �κ� */
	private void sendData() {

	}

	@Override
	public void run() {

		switch (process) {
		case RECEIVE: // ����
			setConnection();
			Contents contents = receiveData();
			group.setLastContents(contents);
			break;

		case SEND: // �۽� 
			sendData();
		}

		/* ���ϰ� ��Ʈ���� �ݴ� �κ� ���� */
	}
}