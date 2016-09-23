package Transferer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.Vector;

import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;

import contents.Contents;
import server_manager.ClientHandler;
import server_manager.LinKlipboard;
import server_manager.LinKlipboardGroup;
import sun.print.resources.serviceui;

public class HisrotySender extends Transfer {
	// ��Ʈ��
	private ObjectOutputStream out;
	private String ipAddr;

	private int serialNo = LinKlipboard.NULL;

	public HisrotySender(LinKlipboardGroup group, ClientHandler client) {
		super(group, client);
		this.start();
	}

	public HisrotySender(LinKlipboardGroup group, ClientHandler client, String serialNo) {
		this(group, client);
		this.serialNo = Integer.parseInt(serialNo);
	}

	@Override
	public void setConnection() {
		try {
			// ���� ���� ����
			listener = new ServerSocket(LinKlipboard.FTP_PORT);
			ready = true;
			socket = listener.accept();

			out = new ObjectOutputStream(socket.getOutputStream());

		} catch (IOException e) {
			System.out.println("���� ����");
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
		if (serialNo == LinKlipboard.NULL) {
			try {
				Vector<Contents> sendData = group.getHistory(); // ���� �׷��� �����丮 ������ ����
				Vector<Contents> copySendData;
				synchronized (sendData) {
					copySendData = new Vector<Contents>(sendData); // ����
				}
				out.writeObject(copySendData);
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Contents cotents= group.getHistoryContents(serialNo);
			try {
				out.writeObject(cotents);
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
