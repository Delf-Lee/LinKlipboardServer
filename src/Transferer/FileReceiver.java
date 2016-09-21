package Transferer;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;

import contents.Contents;
import contents.FileContents;
import server_manager.ClientHandler;
import server_manager.LinKlipboard;
import server_manager.LinKlipboardGroup;

public class FileReceiver extends Transfer {

	// 스트림
	private FileOutputStream fos;
	private DataInputStream dis;

	// 파일 정보
	private File receiveFile; // 받을 파일
	private String fileName; // 받을 파일 이름
	
	private static final int FILE_NAME_ONLY = 0;
	private static final int EXTENTON = 1;

	public FileReceiver(LinKlipboardGroup group, ClientHandler client, String fileName) {
		super(group, client);
		this.fileName = fileName;
		this.start();
	}

	/** 클라이언트와 연결할 소켓을 생성하고 접속을 대기한다. */
	@Override
	public void setConnection() {
		try {
			// 소켓 접속 설정
			System.out.println(" - 소켓 생성 중");
			listener = new ServerSocket(LinKlipboard.FTP_PORT);
			System.out.println(" - 연결 준비 완료 / 접속 대기 중");
			ready = true;
			socket = listener.accept();
			System.out.println(" - 클라이언트 접속");

			dis = new DataInputStream(socket.getInputStream()); // 바이트 배열을 받기 위한 데이터스트림 생성

		} catch (IOException e) {
			System.out.println("접속 오류");
			e.printStackTrace();
		}
	}

	/** 연결된 스트림과 소켓을 닫는다. */
	@Override
	public void closeSocket() {
		try {
			dis.close();
			fos.close();
			socket.close();
			listener.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		// 연결 설정
		setConnection();

		// fileName = checkFileNameDuplicated(); // 중복된 파일명을 확인하고 중복됐다면 파일명에 숫자를 붙여 변경한다.
		receiveFile = new File(group.getFileDir() + "\\" + fileName); // 새로운 파일명으로 새 파일 생성
		try {
			int byteSize = BYTE_SIZE;
			byte[] ReceiveByteArrayToFile = new byte[byteSize]; // 바이트 배열 생성

			fos = new FileOutputStream(receiveFile.getPath()); // 지정한 경로에 바이트 배열을 쓰기위한 파일 스트림 생성

			// 파일 전송
			int EndOfFile = 0;
			System.out.println(" - 파일 수신 시도");
			while ((EndOfFile = dis.read(ReceiveByteArrayToFile)) != -1) {
				fos.write(ReceiveByteArrayToFile, 0, EndOfFile);
			}

			closeSocket(); // 소켓 닫기
			System.out.println("소켓 닫음");

		} catch (IOException e) {
			closeSocket();
			return;
		}
		Contents receiveContents = new FileContents(receiveFile); // FileContents 생성
		group.setLastContents(receiveContents); // 그룹 공유 데이터 갱신
		group.sendNotification(client); // 그룹원들 모두에게 알림 송신
	}

	/** 파일이름 중복이라면 숫자를 덧붙인다. 
	 * @return 중복된 파일명이 존재하지 않는다면 원래의 파일명 문자열. 중복된 파일이 존재한다면 기존 파일명에 숫자를 붙힌 문자열
	 * 중복된 파일 뒤에 붙일 숫자의 의미는 이미 존재하는 중복된 이름의 파일 수 */
	private String checkFileNameDuplicated() {

		File dir = new File(group.getFileDir());
		File[] innerFile = dir.listFiles(); // 폴더 내 존재하는 파일을 innerFile에 넣음
		String[] splitedFileName = fileName.split(".");

		if (!innerFile[0].getName().equals(fileName)) {
			return fileName;
		}

		for (int i = 1;; i++) { // innerFile의 파일들 중 중복되는 파일 탐색
			if (!innerFile[i].getName().equals(fileName)) {
				return (splitedFileName[FILE_NAME_ONLY] + " (" + Integer.toString(i) + ")." + splitedFileName[EXTENTON]);
			}
		}
	}
}
