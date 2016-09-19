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

	private int process; // ������ ������ �۾�
	private final static int RECEIVE = 1; // ���� �۾�
	private final static int SEND = 0; // �۽� �۾�

	private boolean connect = false;

	public TransferManager(LinKlipboardGroup group, ClientHandler client) {
		this.client = client;
		this.group = group;
	}

	/** Ŭ���̾�Ʈ�� ���Ͽ� ������ ��û�Ѵ�. */
	// private void connectToSocket() {
	// try {
	// String ipAddr = client.getRemoteAddr();
	// int portNum = client.getRemotePort();
	//
	// // ���� ���� ����
	// socket = new Socket(ipAddr, port);
	//
	// // ��Ʈ�� ����
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
		System.out.println("���� ����");
		try {
			// ���� ���� ����
			listener = new ServerSocket(port);
			connect = true;
			System.out.println("���� ����");
			socket = listener.accept();

			System.out.println("�����ߴ�");
			// ��Ʈ�� ����
			// out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());

		} catch (IOException e) {

//			try {
//				socket.close();
//				listener.close();
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}

			System.out.println("���� ����");
			e.printStackTrace();
		}
	}

	private void createSendSoecket() {
		System.out.println("���� ����");
		try {
			// ���� ���� ����
			listener = new ServerSocket(port);
			connect = true;
			System.out.println("���� ����");
			socket = listener.accept();

			System.out.println("�����ߴ�");
			// ��Ʈ�� ����
			out = new ObjectOutputStream(socket.getOutputStream());
			// in = new ObjectInputStream(socket.getInputStream());

		} catch (IOException e) {

//			try {
//				socket.close();
//				listener.close();
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}

			System.out.println("���� ����");
			e.printStackTrace();
		}
	}

	/**
	 * ������ �����ϴ� �����带 �����Ѵ�. ��Ȯ���� �����忡�� �����͸� �����ϴ� �κи� �����Ѵ�.
	 * 
	 * @param out
	 *            Ŭ���̾�Ʈ���� ������ ������ ��Ʈ��. ������ ���� �Ŀ� Ŭ���̾�Ʈ�� �����ؾ� �ϱ� ������ �����忡�� Ŭ���̾�Ʈ��
	 *            ������ ������ ��, ������ �����Ѵ�.
	 */
	public void createReceiveThread() {
		process = RECEIVE;
		this.start();
	}

	public void createSendThread() {
		process = SEND;
		this.start();
	}

	/** �����͸� ���� �����ϴ� �κ� */
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

	/** �����͸� ���� �����ϴ� �κ� */
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
			case RECEIVE: // ����
				createReceiveSokcet();
				Contents contents = receiveData();
				group.setLastContents(contents);
				in.close();
				break;

			case SEND: // �۽�
				createSendSoecket();
				sendData();
				out.close();
				break;
			}

			if(!listener.isClosed()) {
				System.out.println("listener ����");
				listener.close();
			}
			
			socket.close();
		} catch (IOException e) {
		}
	}

	public void forDebug(Contents contents) {
		StringContents str = (StringContents) contents;
		System.out.println("���� ���ڿ� =" + str.getString());
		System.out.println("���� ��� =" + str.getSharer());
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