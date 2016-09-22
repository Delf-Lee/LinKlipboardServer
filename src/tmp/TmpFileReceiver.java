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
//	private LinKlipboardGroup group; // 파일을 업데이트 할 그룹
//	private ClientHandler client; // 업데이트 한 클라이언트
//
//	// private static String fileReceiveDirBase = "C:\\Program Files\\LinKlipboardServer"; // 파일 저장 기본 경로 베이스
//	private String fileName;
//	private File receiveFile; // 받을 파일
//
//	private static final int FILE_NAME_ONLY = 0;
//	private static final int EXTENTON = 1;
//	private static final int BYTE_SIZE = 65536;
//
//	/** @param group 받은 파일을 업로드 할 그룹
//	 * @param client 파일을 업로드한 클라이언트의 핸들러
//	 * @param fileName 클라이언트가 보낸 파일의 파일명 */
//	public TmpFileReceiver(LinKlipboardGroup group, ClientHandler client, String fileName) {
//		this.group = group;
//		this.client = client;
//		this.fileName = fileName;
//
//		this.start();
//	}
//
//	/** 소켓을 열고 클라이언트의 접속을 기다린다. */
//	private void setConnection() {
//		try {
//			// 소켓 접속 설정
//			listener = new ServerSocket(LinKlipboard.FTP_PORT);
//			socket = listener.accept();
//
//			dis = new DataInputStream(socket.getInputStream()); // 바이트 배열을 받기 위한 데이터스트림 생성
//
//		} catch (IOException e) {
//			System.out.println("접속 오류");
//			e.printStackTrace();
//		}
//	}
//
//	/** 열려있는 소켓을 모두 닫는다. */
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
//	/** 파일이름 중복이라면 숫자를 덧붙인다. 
//	 * @return 중복된 파일명이 존재하지 않는다면 원래의 파일명 문자열. 중복된 파일이 존재한다면 기존 파일명에 숫자를 붙힌 문자열
//	 * 중복된 파일 뒤에 붙일 숫자의 의미는 이미 존재하는 중복된 이름의 파일 수 */
//	private String checkFileNameDuplicated() {
//
//		File dir = new File(group.getFileDir());
//		File[] innerFile = dir.listFiles(); // 폴더 내 존재하는 파일을 innerFile에 넣음
//		String[] splitedFileName = fileName.split(".");
//
//		if (!innerFile[0].getName().equals(fileName)) {
//			return fileName;
//		}
//
//		for (int i = 1;; i++) { // innerFile의 파일들 중 중복되는 파일 탐색
//			if (!innerFile[i].getName().equals(fileName)) {
//				return (splitedFileName[FILE_NAME_ONLY] + " (" + Integer.toString(i) + ")." + splitedFileName[EXTENTON]);
//			}
//		}
//	}
//
//	public void run() {
//		// 연결 설정
//		setConnection();
//
//		fileName = checkFileNameDuplicated(); // 중복된 파일명을 확인하고 중복됐다면 파일명에 숫자를 붙여 변경한다.
//		receiveFile = new File(group.getFileDir() + "\\" + fileName); // 새로운 파일명으로 새 파일 생성
//		try {
//			int byteSize = BYTE_SIZE;
//			byte[] ReceiveByteArrayToFile = new byte[byteSize]; // 바이트 배열 생성
//
//			fos = new FileOutputStream(receiveFile.getPath()); // 지정한 경로에 바이트 배열을 쓰기위한 파일 스트림 생성
//
//			int EndOfFile = 0;
//			while ((EndOfFile = dis.read(ReceiveByteArrayToFile)) != -1) {
//				fos.write(ReceiveByteArrayToFile, 0, EndOfFile);
//			}
//
//			closeSocket(); // 소켓 닫기
//
//		} catch (IOException e) {
//			closeSocket();
//			return;
//		}
//		Contents receiveContents = new FileContents(receiveFile); // FileContents 생성
//		group.setLastContents(receiveContents); // 그룹 공유 데이터 갱신
//		group.sendNotification(client); // 그룹원들 모두에게 알림 송신
//	}
//}