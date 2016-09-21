package Transferer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;

import contents.Contents;
import server_manager.ClientHandler;
import server_manager.LinKlipboard;
import server_manager.LinKlipboardGroup;

public class ContentsSender extends Transfer {
	// 스트림
	private ObjectOutputStream out;

	public ContentsSender(LinKlipboardGroup group, ClientHandler client) {
		super(group, client);
		this.start();
	}

	@Override
	public void setConnection() {
		// TODO: 구현
	}

	@Override
	public void closeSocket() {
		try {
			out.close();
			socket.close();
			listener.close();
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
