package server_manager;

import java.net.Socket;

import javax.servlet.http.HttpServletRequest;

import Transferer.Transfer;

public class ClientHandler {

	private String nickName;
	private String groupName;
	protected String remoteAddr; // Ŭ���̾�Ʈ�� �ּ�
	protected int remoteProt; // Ŭ���̾�Ʈ�� ��Ʈ��ȣ
	protected int localPort; // ������ ��Ʈ ��ȣ
	protected Socket socket; // Ŭ���̾�Ʈ���� ����
	private ThreadQueue queue = new ThreadQueue();

	private int order;

	public ClientHandler(HttpServletRequest client, String groupName) {
		//this.client = client;
		if (client != null) {
			this.remoteAddr = client.getRemoteAddr();
			this.remoteProt = client.getRemotePort();
			this.localPort = client.getLocalPort();
			this.groupName = groupName;
		} 
		else {
			this.remoteAddr = "localhost";
			this.remoteProt = 77777;
			//this.localPort = client.getLocalPort();
			this.groupName = groupName;
		}
	}

	/** @param nickname ������ Ŭ���̾�Ʈ�� �г��� */
	public void setNickname(String nickname) {
		this.nickName = nickname;
	}

	/** @return Ŭ���̾�Ʈ�� ���� �׷� �̸�*/
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setOrder(int order) {
		this.order = order;
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

	public int getOrder() {
		return order;
	}

	public boolean equals(ClientHandler client) {
		return remoteAddr.equals(client.remoteAddr);
	}
	
	public void addThread(Transfer th) {
		queue.add(th);
	}
}
