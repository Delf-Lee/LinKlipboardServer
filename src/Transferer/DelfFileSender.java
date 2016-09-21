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

	// 스트림
	private FileOutputStream fos;
	private DataInputStream dis;

	@Override
	public void setConnection() {
		try {
			// 소켓 접속 설정
			listener = new ServerSocket(LinKlipboard.FTP_PORT);
			ready = true;
			socket = listener.accept();

			dis = new DataInputStream(socket.getInputStream()); // 바이트 배열을 받기 위한 데이터스트림 생성

		} catch (IOException e) {
			System.out.println("접속 오류");
			e.printStackTrace();
		}
	}

	@Override
	public void closeSocket() {
		FileContents sendContents = (FileContents) group.getLastContents(); // 그룹의 최신데이터를 가져온다.
		String sendFilePath = sendContents.getFilePath(); // 파일이 저장되어 있는 경로를 가져온다. 

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
