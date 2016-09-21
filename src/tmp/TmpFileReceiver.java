package tmp;
//package Transferer;
//
//import java.io.DataInputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.net.ServerSocket;
//import java.net.Socket;
//
//import contents.Contents;
//import contents.FileContents;
//import server_manager.ClientHandler;
//import server_manager.LinKlipboard;
//import server_manager.LinKlipboardGroup;
//
//public class TmpFileReceiver extends Thread {
//	private FileOutputStream fos;
//	private DataInputStream dis;
//
//	private ServerSocket listener;
//	private Socket socket;
//
//	private LinKlipboardGroup group; // ������ ������Ʈ �� �׷�
//	private ClientHandler client; // ������Ʈ �� Ŭ���̾�Ʈ
//
//	// private static String fileReceiveDirBase = "C:\\Program Files\\LinKlipboardServer"; // ���� ���� �⺻ ��� ���̽�
//	private String fileName;
//	private File receiveFile; // ���� ����
//
//	private static final int FILE_NAME_ONLY = 0;
//	private static final int EXTENTON = 1;
//	private static final int BYTE_SIZE = 65536;
//
//	/** @param group ���� ������ ���ε� �� �׷�
//	 * @param client ������ ���ε��� Ŭ���̾�Ʈ�� �ڵ鷯
//	 * @param fileName Ŭ���̾�Ʈ�� ���� ������ ���ϸ� */
//	public TmpFileReceiver(LinKlipboardGroup group, ClientHandler client, String fileName) {
//		this.group = group;
//		this.client = client;
//		this.fileName = fileName;
//
//		this.start();
//	}
//
//	/** ������ ���� Ŭ���̾�Ʈ�� ������ ��ٸ���. */
//	private void setConnection() {
//		try {
//			// ���� ���� ����
//			listener = new ServerSocket(LinKlipboard.FTP_PORT);
//			socket = listener.accept();
//
//			dis = new DataInputStream(socket.getInputStream()); // ����Ʈ �迭�� �ޱ� ���� �����ͽ�Ʈ�� ����
//
//		} catch (IOException e) {
//			System.out.println("���� ����");
//			e.printStackTrace();
//		}
//	}
//
//	/** �����ִ� ������ ��� �ݴ´�. */
//	private void closeSocket() {
//		try {
//			dis.close();
//			fos.close();
//			socket.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	/** �����̸� �ߺ��̶�� ���ڸ� �����δ�. 
//	 * @return �ߺ��� ���ϸ��� �������� �ʴ´ٸ� ������ ���ϸ� ���ڿ�. �ߺ��� ������ �����Ѵٸ� ���� ���ϸ� ���ڸ� ���� ���ڿ�
//	 * �ߺ��� ���� �ڿ� ���� ������ �ǹ̴� �̹� �����ϴ� �ߺ��� �̸��� ���� �� */
//	private String checkFileNameDuplicated() {
//
//		File dir = new File(group.getFileDir());
//		File[] innerFile = dir.listFiles(); // ���� �� �����ϴ� ������ innerFile�� ����
//		String[] splitedFileName = fileName.split(".");
//
//		if (!innerFile[0].getName().equals(fileName)) {
//			return fileName;
//		}
//
//		for (int i = 1;; i++) { // innerFile�� ���ϵ� �� �ߺ��Ǵ� ���� Ž��
//			if (!innerFile[i].getName().equals(fileName)) {
//				return (splitedFileName[FILE_NAME_ONLY] + " (" + Integer.toString(i) + ")." + splitedFileName[EXTENTON]);
//			}
//		}
//	}
//
//	public void run() {
//		// ���� ����
//		setConnection();
//
//		fileName = checkFileNameDuplicated(); // �ߺ��� ���ϸ��� Ȯ���ϰ� �ߺ��ƴٸ� ���ϸ� ���ڸ� �ٿ� �����Ѵ�.
//		receiveFile = new File(group.getFileDir() + "\\" + fileName); // ���ο� ���ϸ����� �� ���� ����
//		try {
//			int byteSize = BYTE_SIZE;
//			byte[] ReceiveByteArrayToFile = new byte[byteSize]; // ����Ʈ �迭 ����
//
//			fos = new FileOutputStream(receiveFile.getPath()); // ������ ��ο� ����Ʈ �迭�� �������� ���� ��Ʈ�� ����
//
//			int EndOfFile = 0;
//			while ((EndOfFile = dis.read(ReceiveByteArrayToFile)) != -1) {
//				fos.write(ReceiveByteArrayToFile, 0, EndOfFile);
//			}
//
//			closeSocket(); // ���� �ݱ�
//
//		} catch (IOException e) {
//			closeSocket();
//			return;
//		}
//		Contents receiveContents = new FileContents(receiveFile); // FileContents ����
//		group.setLastContents(receiveContents); // �׷� ���� ������ ����
//		group.sendNotification(client); // �׷���� ��ο��� �˸� �۽�
//	}
//}