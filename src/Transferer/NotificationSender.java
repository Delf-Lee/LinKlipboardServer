package Transferer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import contents.Contents;
import contents.StringContents;
import server_manager.ClientHandler;
import server_manager.LinKlipboard;
import server_manager.LinKlipboardGroup;

public class NotificationSender extends Transfer {
	private ObjectOutputStream out;
	private Contents clientInfo;
	private String nickname;
//	public static final int JOIN = 0;
//	public static final int EXIT = 1;
	
	public NotificationSender(LinKlipboardGroup group, ClientHandler client, int state, String nickname) {
		super(group, client);
		this.nickname = nickname;
		clientInfo = createClientInfo(state);
		this.start();
	}
	
//	public NotificationSender(String ip, String nickname, int state) {
//
//	}
	
		@Override
		public void setConnection() {
			try {
				// 소켓 접속 설정
				System.out.println(client.getRemotePort() + 1);
				
				socket = new Socket(client.getRemoteAddr(), client.getRemotePort() + 1);
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
			System.out.println(clientInfo.getSharer() + "---------------");
			System.out.println(clientInfo.getType() + "---------------");
			System.out.println(out);
			System.out.println(clientInfo);
			out.writeObject(clientInfo);
			out.flush();
			// TODO: 응답받고 예외처리
		} catch (IOException e) {
			e.printStackTrace();
		}
		closeSocket();
	}
	private Contents createClientInfo(int state) {
		System.out.println("클라이언트 정보 객체 생성------------------------------------");
		String name = null;
		if(state == LinKlipboard.JOIN_CLITNT) {
			name = "join;" + nickname;
		}
		else if(state == LinKlipboard.EXIT_CLITNT) {
			name = "exit;" + nickname;
		}
		System.out.println("클라이언트 들에게 뿌린 닉네임: " + nickname);
		Contents tmp = new StringContents();
		tmp.setSharer(name);
		tmp.setType(LinKlipboard.NULL);
		return tmp;
	}
}