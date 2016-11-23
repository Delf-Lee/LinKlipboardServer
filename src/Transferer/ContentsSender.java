package Transferer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import contents.Contents;
import server_manager.ClientHandler;
import server_manager.LinKlipboardGroup;

public class ContentsSender extends Transfer {
	// 스트림
	private ObjectOutputStream out;

	public ContentsSender(LinKlipboardGroup group, ClientHandler client) {
		super(group, client);
		//client.addThread(this);
		this.start();
	}

	@Override
	public void setConnection() {
		try {
			// 소켓 접속 설정
			System.out.println("이제 뿌릴 ip: " + client.getRemoteAddr());
			System.out.println(client.getRemoteAddr() + " " + client.getRemotePort() + 1);
			socket = new Socket(client.getRemoteAddr(), client.getRemotePort() + 1);
			//socket = listener.accept();
			// TODO: timeout
			// 스트림 설정
			out = new ObjectOutputStream(socket.getOutputStream());

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
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
		System.out.println("연결 설정 완료");
		try {
			Contents sendData = group.getLastContents();
			out.writeObject(sendData);
			out.flush();
			System.out.println("정보 받음");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		closeSocket();
	}
}