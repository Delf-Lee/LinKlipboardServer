package datamanage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import contents.Contents;
import contents.ImageContents;
import contents.StringContents;
import server_manager.ClientHandler;
import server_manager.LinKlipboard;
import server_manager.LinKlipboardGroup;

public class TransferManager extends Thread {

	private LinKlipboardGroup group;
	private ClientHandler client;
	private int type;

	private static final int port = 20;
	private ServerSocket listener;
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;

	private DataOutputStream dout;
	private DataInputStream din;

	private PrintWriter pout;

	private int process; // 수행할 스레드 작업
	private final static int RECEIVE = 1; // 수신 작업
	private final static int SEND = 0; // 송신 작업

	private boolean connect = false;

	public TransferManager(LinKlipboardGroup group, ClientHandler client, int dataType) {
		this.client = client;
		this.type = dataType;
		this.group = group;
	}

	/** 클라이언트의 소켓에 연결을 요청한다. */
	//	private void connectToSocket() {
	//		try {
	//			String ipAddr = client.getRemoteAddr();
	//			int portNum = client.getRemotePort();
	//
	//			// 소켓 접속 설정
	//			socket = new Socket(ipAddr, port);
	//
	//			// 스트림 설정
	//			out = new ObjectOutputStream(socket.getOutputStream());
	//			in = new ObjectInputStream(socket.getInputStream());
	//
	//		} catch (UnknownHostException e) {
	//			e.printStackTrace();
	//		} catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//	}

	private void createSoecket() {
		System.out.println("소켓 연다");
		try {
			// 소켓 접속 설정
			listener = new ServerSocket(port);
			connect = true;
			System.out.println("응답 보냄");
			socket = listener.accept();

			System.out.println("접속했다");
			// 스트림 설정
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

		} catch (IOException e) {

			//			try {
			//				socket.close();
			//				listener.close();
			//			} catch (IOException e1) {
			//				e1.printStackTrace();
			//			}

			System.out.println("접속 오류");
			e.printStackTrace();
		}
	}

	/** 데이터 수신하는 스레드를 생성한다. 정확히는 스레드에서 데이터를 수신하는 부분만 실행한다. 
	 * @param out 클라이언트에게 응답을 전송할 스트림.
	 * 소켓이 열린 후에 클라이언트가 접속해야 하기 때문에 스레드에서 클라이언트의 접속이 감지될 때, 응답을 시행한다.*/
	public void createReceiveThread(PrintWriter out) {
		System.out.println("리시브 스레드 만든다?");
		process = RECEIVE;
		this.pout = out;
		this.start();
	}

	public void createSendThread() {
		process = SEND;
		this.start();
	}

	/** 데이터를 직접 수신하는 부분 */
	private Contents receiveData() {
		try {
			switch (type) {
			case LinKlipboard.STRING_TYPE:
				return new StringContents(client.getNickname()).receiveData(in);

			case LinKlipboard.IMAGE_TYPE:
				return new ImageContents(client.getNickname()).receiveData(in);

			case LinKlipboard.FILE_TYPE:
				return null;
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**데이터를 직접 전송하는 부분 */
	private void sendData() {
		try {
			Contents sendData = group.getLastContents();
			out.writeObject(sendData);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {

		switch (process) {
		case RECEIVE: // 수신
			createSoecket();
			Contents contents = receiveData();
			forDebug(contents);
			group.setLastContents(contents);
			break;

		case SEND: // 송신 
			sendData();
			break;
		}
		try {
			in.close();
			socket.close();
		} catch (IOException e) {
		}
	}

	public void forDebug(Contents contents) {
		StringContents str = (StringContents) contents;
		System.out.println("받은 문자열 =" + str.getString());
	}
	
	public boolean isConnected() {
		return connect;
	}
}