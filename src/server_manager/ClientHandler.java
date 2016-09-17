package server_manager;

import java.net.Socket;

import javax.servlet.http.HttpServletRequest;

public class ClientHandler {

	private String nickName;
	private String groupName;
	protected String remoteAddr; // Ŭ���̾�Ʈ�� �ּ�
	protected int remoteProt; // Ŭ���̾�Ʈ�� ��Ʈ��ȣ
	protected int localPort; // ������ ��Ʈ ��ȣ
	protected Socket socket; // Ŭ���̾�Ʈ���� ����

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

	/** @param ������ Ŭ���̾�Ʈ�� �г��� */
	public void setNickname(String nickName) {
		this.nickName = nickName;
	}

	/** @return Ŭ���̾�Ʈ�� ���� �׷� �̸�*/
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getNickname() {
		return nickName;
	}

	public String getRemoteAddr() { // Ŭ���̾�Ʈ�� �ּҸ� ����
		return remoteAddr;
	}

	public int getRemotePort() { // Ŭ���̾�Ʈ�� ��Ʈ ��ȣ ����
		return remoteProt;
	}

	public int getLocalPort() { // ���� ��Ʈ ��ȣ ����
		return localPort;
	}

	public void sendData() {

	}

	public void recieveData() {

	}
}
