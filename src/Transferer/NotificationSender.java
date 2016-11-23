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
				// ���� ���� ����
				System.out.println(client.getRemotePort() + 1);
				
				socket = new Socket(client.getRemoteAddr(), client.getRemotePort() + 1);
				// ��Ʈ�� ����
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
			// TODO: ����ް� ����ó��
		} catch (IOException e) {
			e.printStackTrace();
		}
		closeSocket();
	}
	private Contents createClientInfo(int state) {
		System.out.println("Ŭ���̾�Ʈ ���� ��ü ����------------------------------------");
		String name = null;
		if(state == LinKlipboard.JOIN_CLITNT) {
			name = "join;" + nickname;
		}
		else if(state == LinKlipboard.EXIT_CLITNT) {
			name = "exit;" + nickname;
		}
		System.out.println("Ŭ���̾�Ʈ �鿡�� �Ѹ� �г���: " + nickname);
		Contents tmp = new StringContents();
		tmp.setSharer(name);
		tmp.setType(LinKlipboard.NULL);
		return tmp;
	}
}