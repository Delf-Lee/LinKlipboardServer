package server_manager;

import java.net.Socket;

import javax.servlet.http.HttpServletRequest;

public class ClientHandler {

	private String nickName;
	private String groupName;
	protected String remoteAddr; // 클라이언트의 주소
	protected int remoteProt; // 클라이언트의 포트번호
	protected int localPort; // 서버의 포트 번호
	protected Socket socket; // 클라이언트와의 소켓

	//	protected LinKlipboardGroup myGroup;

	public ClientHandler(HttpServletRequest client, String groupName) {
		//this.client = client;
		if (client != null) {
			this.remoteAddr = client.getRemoteAddr();
			this.remoteProt = client.getRemotePort();
			this.localPort = client.getLocalPort();
			this.groupName = groupName;
		}
	}

	/** @param 변경할 클라이언트의 닉네임 */
	public void setNickname(String nickName) {
		this.nickName = nickName;
	}

	/** @return 클라이언트가 속한 그룹 이름*/
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getNickname() {
		return nickName;
	}

	public String getRemoteAddr() { // 클라이언트의 주소를 리턴
		return remoteAddr;
	}

	public int getRemotePort() { // 클라이언트의 포트 번호 리턴
		return remoteProt;
	}

	public int getLocalPort() { // 서버 포트 번호 리턴
		return localPort;
	}

	public void sendData() {

	}

	public void recieveData() {

	}
}
