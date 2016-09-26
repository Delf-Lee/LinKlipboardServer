package Transferer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;

import com.sun.nio.sctp.Notification;

import contents.Contents;
import contents.ImageContents;
import contents.StringContents;
import server_manager.ClientHandler;
import server_manager.LinKlipboard;
import server_manager.LinKlipboardGroup;

public class ContentsReceiver extends Transfer {

	private ObjectInputStream in;

	public ContentsReceiver(LinKlipboardGroup group, ClientHandler client) {
		super(group, client);
		this.start();
	}

	@Override
	public void setConnection() {
		try {
			// 소켓 접속 설정
			listener = new ServerSocket(client.getRemotePort());
			ready = true;
			System.out.println("응답 보냄 / 접속 대기");
			socket = listener.accept();

			System.out.println("접속했다");
			// 스트림 설정
			in = new ObjectInputStream(socket.getInputStream());

		} catch (IOException e) {

			System.out.println("접속 오류");
			e.printStackTrace();
		}
	}

	@Override
	public void closeSocket() {
		try {
			in.close();
			socket.close();
			listener.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		setConnection(); // 연결 설정
		Contents contents = receiveData(); // 데이터 받기
		System.out.println(client.getNickname() + "으로부터 컨텐츠 수신");
		contents.setDate();
		contents.setSharer(client.getNickname());
		group.setLastContents(contents); // 해당 그룹에 데이터 저장
		group.notificateUpdate(client); // 그룹원들 모두에게 알림 송신
		
		closeSocket(); // 소켓 닫기
	}

	/** 데이터를 직접 수신하는 부분 */
	private Contents receiveData() {
		Contents contents = null;
		try {
			contents = (Contents) in.readObject();
			System.out.println("받은 데이터의 타입은: " + contents.getType());
			int type = contents.getType();
			switch (type) {
			case LinKlipboard.STRING_TYPE:
				contents = (StringContents) contents;
				StringContents tmp = (StringContents) contents;
				System.out.println(tmp.getString());
				break;

			case LinKlipboard.IMAGE_TYPE:
				contents = (ImageContents) contents;
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

}
