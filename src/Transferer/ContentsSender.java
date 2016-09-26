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
	private String ipAddr;
	
	public ContentsSender(LinKlipboardGroup group, ClientHandler client) {
		super(group, client);
		this.start();
	}

	@Override
	public void setConnection() {
		try {
			// 소켓 접속 설정
			//System.out.println(/*client.getNickname() + */"의 소켓 연결 설정(" + client.getLocalPort() + "/" + client.getRemotePort() + ")");
			socket = new Socket(ipAddr, client.getRemotePort());
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

		try {
			Contents sendData = group.getLastContents();
			out.writeObject(sendData);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}