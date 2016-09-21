package Transferer;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;

import contents.FileContents;
import server_manager.ClientHandler;
import server_manager.LinKlipboard;
import server_manager.LinKlipboardGroup;

public class FileSender extends Transfer {

	public FileSender(LinKlipboardGroup group, ClientHandler client) {
		super(group, client);
		this.start();
	}

	// ��Ʈ��
	private DataOutputStream dos;
	private FileInputStream fis;

	@Override
	public void setConnection() {
		try {
			// ���� ���� ����
			listener = new ServerSocket(LinKlipboard.FTP_PORT);
			ready = true;
			socket = listener.accept();

			dos = new DataOutputStream(socket.getOutputStream()); // ����Ʈ �迭�� ������ ���� �����ͽ�Ʈ�� ����
			
		} catch (IOException e) {
			System.out.println("���� ����");
			e.printStackTrace();
		}
	}

	@Override
	public void closeSocket() {
		try {
			dos.close();
			fis.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		setConnection();
		
		FileContents sendContents = (FileContents) group.getLastContents(); // �׷��� �ֽŵ����͸� �����´�.
		File sendFile = new File(sendContents.getFilePath()); // ������ ����Ǿ� �ִ� ��θ� �����´�. 

		try {
			byte[] ReceiveByteArrayToFile = new byte[BYTE_SIZE];

			fis = new FileInputStream(sendFile);

			int EndOfFile = 0;
			while ((EndOfFile = fis.read(ReceiveByteArrayToFile)) != -1) {
				dos.write(ReceiveByteArrayToFile, 0, EndOfFile);
			}

			closeSocket();

		} catch (IOException e) {
			closeSocket();
			return;
		}
	}

}
