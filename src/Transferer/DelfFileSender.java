package Transferer;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;

import contents.FileContents;
import server_manager.ClientHandler;
import server_manager.LinKlipboard;
import server_manager.LinKlipboardGroup;

public class DelfFileSender extends FileTransfer {

	public DelfFileSender(LinKlipboardGroup group, ClientHandler client) {
		super(group, client);
		this.start();
	}

	// ��Ʈ��
	private FileOutputStream fos;
	private DataInputStream dis;

	@Override
	public void setConnection() {
		try {
			// ���� ���� ����
			listener = new ServerSocket(LinKlipboard.FTP_PORT);
			ready = true;
			socket = listener.accept();

			dis = new DataInputStream(socket.getInputStream()); // ����Ʈ �迭�� �ޱ� ���� �����ͽ�Ʈ�� ����

		} catch (IOException e) {
			System.out.println("���� ����");
			e.printStackTrace();
		}
	}

	@Override
	public void closeSocket() {
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
