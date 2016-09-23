package Transferer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.Vector;

import contents.Contents;
import server_manager.ClientHandler;
import server_manager.LinKlipboard;
import server_manager.LinKlipboardGroup;

public class HisrotySender extends Transfer {
	// 스트림
	private ObjectOutputStream out;
	private String ipAddr;

	public HisrotySender(LinKlipboardGroup group, ClientHandler client) {
		super(group, client);
		this.start();
	}

	@Override
	public void setConnection() {
		try {
			// 소켓 접속 설정
			listener = new ServerSocket(LinKlipboard.FTP_PORT);
			ready = true;
			socket = listener.accept();

			out = new ObjectOutputStream(socket.getOutputStream());

		} catch (IOException e) {
			System.out.println("접속 오류");
			e.printStackTrace();
		}
	}

	@Override
	public void closeSocket() {
		try {
			out.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		setConnection();

		try {
			Vector<Contents> sendData = group.getHistory(); // 현재 그룹의 히스토리 컨텐츠 추출
			Vector<Contents> copySendData;
			synchronized (sendData) {
				copySendData = new Vector<Contents>(sendData); // 복사 
			}
			out.writeObject(copySendData);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
