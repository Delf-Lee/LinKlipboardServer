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

/** 클라이언트가 요청시 서버가 파일을 전송하는 스레드 */
public class TmpFileSender extends Thread {

	private ServerSocket listener;
	private Socket socket;

	private FileOutputStream fos;
	private DataInputStream dis;

	private LinKlipboardGroup group; // 파일을 전송할 클라이언트가 속한 그룹
	private ClientHandler client; // 파일을 전송할 클라이언트

	private static final int BYTE_SIZE = 65536;

	/** @param group 파일을 전송할 클라이언트가 속한 그룹
	 * @param client 파일을 전송할 클라이언트 */
	public TmpFileSender(LinKlipboardGroup group, ClientHandler client) {
		this.group = group;
		this.client = client;

		this.start();
	}

	/** 소켓을 열고 클라이언트의 접속을 기다린다. */
	private void setConnection() {
		try {
			// 소켓 접속 설정
			listener = new ServerSocket(LinKlipboard.FTP_PORT);
			socket = listener.accept();

			dis = new DataInputStream(socket.getInputStream()); // 바이트 배열을 받기 위한 데이터스트림 생성

		} catch (IOException e) {
			System.out.println("접속 오류");
			e.printStackTrace();
		}
	}

	/** 열려있는 소켓을 모두 닫는다. */
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
		setConnection(); // 연결 설정
		/* 현재는 최신 데이터만 보내게 구현되어 있음
		 * 후에 히스토리의 데이터도 보내는 기능도 구현해야함.
		 * 
		 * 그 구현을 따로 클래스로 만들것인지 그냥 if 문으로 나눌 것인지는 아직 미정 */
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