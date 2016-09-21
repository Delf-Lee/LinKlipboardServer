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

	// 스트림
	private DataOutputStream dos;
	private FileInputStream fis;

	@Override
	public void setConnection() {
		try {
			// 소켓 접속 설정
			listener = new ServerSocket(LinKlipboard.FTP_PORT);
			ready = true;
			socket = listener.accept();

			dos = new DataOutputStream(socket.getOutputStream()); // 바이트 배열을 보내기 위한 데이터스트림 생성
			
		} catch (IOException e) {
			System.out.println("접속 오류");
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
		
		FileContents sendContents = (FileContents) group.getLastContents(); // 그룹의 최신데이터를 가져온다.
		File sendFile = new File(sendContents.getFilePath()); // 파일이 저장되어 있는 경로를 가져온다. 

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
