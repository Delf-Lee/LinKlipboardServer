package Transferer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;

import com.sun.nio.sctp.Notification;

import contents.Contents;
import contents.ImageContents;
import contents.StringContents;
import server_manager.ClientHandler;
import server_manager.LinKlipboard;
import server_manager.LinKlipboardGroup;

public class ContentsReceiver extends Transfer {

	private ObjectInputStream in;

	public ContentsReceiver(LinKlipboardGroup group, ClientHandler client) {
		super(group, client);
		this.start();
	}

	@Override
	public void setConnection() {
		try {
			// ���� ���� ����
			listener = new ServerSocket(client.getRemotePort());
			ready = true;
			System.out.println("���� ���� / ���� ���");
			socket = listener.accept();

			System.out.println("�����ߴ�");
			// ��Ʈ�� ����
			in = new ObjectInputStream(socket.getInputStream());

		} catch (IOException e) {

			System.out.println("���� ����");
			e.printStackTrace();
		}
	}

	@Override
	public void closeSocket() {
		try {
			in.close();
			socket.close();
			listener.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		setConnection(); // ���� ����
		Contents contents = receiveData(); // ������ �ޱ�
		System.out.println(client.getNickname() + "���κ��� ������ ����");
		contents.setDate();
		contents.setSharer(client.getNickname());
		group.setLastContents(contents); // �ش� �׷쿡 ������ ����
		group.notificateUpdate(client); // �׷���� ��ο��� �˸� �۽�
		
		closeSocket(); // ���� �ݱ�
	}

	/** �����͸� ���� �����ϴ� �κ� */
	private Contents receiveData() {
		Contents contents = null;
		try {
			contents = (Contents) in.readObject();
			System.out.println("���� �������� Ÿ����: " + contents.getType());
			int type = contents.getType();
			switch (type) {
			case LinKlipboard.STRING_TYPE:
				contents = (StringContents) contents;
				StringContents tmp = (StringContents) contents;
				System.out.println(tmp.getString());
				break;

			case LinKlipboard.IMAGE_TYPE:
				contents = (ImageContents) contents;
				break;

			case LinKlipboard.FILE_TYPE:
				break;
			}

		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}

		contents.setSharer(client.getNickname());
		return contents;
	}

}
