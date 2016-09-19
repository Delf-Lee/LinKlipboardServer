package Transferer;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.ImageIcon;

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

	private int process; // 수행할 스레드 작업
	private final static int RECEIVE = 1; // 수신 작업
	private final static int SEND = 0; // 송신 작업

	private boolean connect = false;

	public TransferManager(LinKlipboardGroup group, ClientHandler client) {
		this.client = client;
		this.group = group;
	}

	/** 클라이언트의 소켓에 연결을 요청한다. */
	// private void connectToSocket() {
	// try {
	// String ipAddr = client.getRemoteAddr();
	// int portNum = client.getRemotePort();
	//
	// // 소켓 접속 설정
	// socket = new Socket(ipAddr, port);
	//
	// // 스트림 설정
	// out = new ObjectOutputStream(socket.getOutputStream());
	// in = new ObjectInputStream(socket.getInputStream());
	//
	// } catch (UnknownHostException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }

	private void createReceiveSokcet() {
		System.out.println("소켓 연다");
		try {
			// 소켓 접속 설정
			listener = new ServerSocket(port);
			connect = true;
			System.out.println("응답 보냄");
			socket = listener.accept();

			System.out.println("접속했다");
			// 스트림 설정
			// out = new ObjectOutputStream(socket.getOutputStream());
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

	private void createSendSoecket() {
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
			// in = new ObjectInputStream(socket.getInputStream());

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

	/**
	 * 데이터 수신하는 스레드를 생성한다. 정확히는 스레드에서 데이터를 수신하는 부분만 실행한다.
	 * 
	 * @param out
	 *            클라이언트에게 응답을 전송할 스트림. 소켓이 열린 후에 클라이언트가 접속해야 하기 때문에 스레드에서 클라이언트의
	 *            접속이 감지될 때, 응답을 시행한다.
	 */
	public void createReceiveThread() {
		process = RECEIVE;
		this.start();
	}

	public void createSendThread() {
		process = SEND;
		this.start();
	}

	/** 데이터를 직접 수신하는 부분 */
	private Contents receiveData() {
		Contents contents = null;
		try {
			contents = (Contents) in.readObject();
			int type = contents.getType();
			switch (type) {
			case LinKlipboard.STRING_TYPE:
				contents = (StringContents) contents;

				StringContents tmp = (StringContents) contents;
				System.out.println(tmp.getString());
				break;

			case LinKlipboard.IMAGE_TYPE:
				contents = (ImageContents) contents;

				ImageContents tmp1 = (ImageContents) contents;

				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
				c.setContents(new ImageTransferable(tmp1.getImage().getImage()), null);
				break;

			case LinKlipboard.FILE_TYPE:
				break;
			}

		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}

		contents.setSharer(client.getNickname());
		return contents;
	}

	/** 데이터를 직접 전송하는 부분 */
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
		try {
			switch (process) {
			case RECEIVE: // 수신
				createReceiveSokcet();
				Contents contents = receiveData();
				group.setLastContents(contents);
				in.close();
				break;

			case SEND: // 송신
				createSendSoecket();
				sendData();
				out.close();
				break;
			}

			if(!listener.isClosed()) {
				System.out.println("listener 닫음");
				listener.close();
			}
			
			socket.close();
		} catch (IOException e) {
		}
	}

	public void forDebug(Contents contents) {
		StringContents str = (StringContents) contents;
		System.out.println("받은 문자열 =" + str.getString());
		System.out.println("보낸 사람 =" + str.getSharer());
	}

	public boolean isConnected() {
		return connect;
	}

	static class ImageTransferable implements Transferable {
		private Image image;

		public ImageTransferable(Image image) {
			this.image = image;
		}

		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
			if (isDataFlavorSupported(flavor)) {
				return image;
			} else {
				throw new UnsupportedFlavorException(flavor);
			}
		}

		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return flavor == DataFlavor.imageFlavor;
		}

		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { DataFlavor.imageFlavor };
		}
	}
}