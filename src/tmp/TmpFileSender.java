package tmp;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import contents.FileContents;
import server_manager.ClientHandler;
import server_manager.LinKlipboard;
import server_manager.LinKlipboardGroup;

/** Ŭ���̾�Ʈ�� ��û�� ������ ������ �����ϴ� ������ */
public class TmpFileSender extends Thread {

	private ServerSocket listener;
	private Socket socket;

	private FileOutputStream fos;
	private DataInputStream dis;

	private LinKlipboardGroup group; // ������ ������ Ŭ���̾�Ʈ�� ���� �׷�
	private ClientHandler client; // ������ ������ Ŭ���̾�Ʈ

	private static final int BYTE_SIZE = 65536;

	/** @param group ������ ������ Ŭ���̾�Ʈ�� ���� �׷�
	 * @param client ������ ������ Ŭ���̾�Ʈ */
	public TmpFileSender(LinKlipboardGroup group, ClientHandler client) {
		this.group = group;
		this.client = client;

		this.start();
	}

	/** ������ ���� Ŭ���̾�Ʈ�� ������ ��ٸ���. */
	private void setConnection() {
		try {
			// ���� ���� ����
			listener = new ServerSocket(LinKlipboard.FTP_PORT);
			socket = listener.accept();

			dis = new DataInputStream(socket.getInputStream()); // ����Ʈ �迭�� �ޱ� ���� �����ͽ�Ʈ�� ����

		} catch (IOException e) {
			System.out.println("���� ����");
			e.printStackTrace();
		}
	}

	/** �����ִ� ������ ��� �ݴ´�. */
	private void closeSocket() {
		try {
			dis.close();
			fos.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 
	
	@Override
	public void run() {
		setConnection(); // ���� ����
		/* ����� �ֽ� �����͸� ������ �����Ǿ� ����
		 * �Ŀ� �����丮�� �����͵� ������ ��ɵ� �����ؾ���.
		 * 
		 * �� ������ ���� Ŭ������ ��������� �׳� if ������ ���� �������� ���� ���� */
		FileContents sendContents = (FileContents) group.getLastContents(); // �׷��� �ֽŵ����͸� �����´�.
		String sendFilePath = sendContents.getFilePath(); // ������ ����Ǿ� �ִ� ��θ� �����´�. 

		try {
			byte[] ReceiveByteArrayToFile = new byte[BYTE_SIZE];

			fos = new FileOutputStream(sendFilePath);

			int EndOfFile = 0;
			while ((EndOfFile = dis.read(ReceiveByteArrayToFile)) != -1) {
				fos.write(ReceiveByteArrayToFile, 0, EndOfFile);
			}
			
			closeSocket();

		} catch (IOException e) {
			closeSocket();
			return;
		}
	}
}