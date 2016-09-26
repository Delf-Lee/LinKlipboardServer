package server_manager;

import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import Transferer.ContentsSender;
import contents.Contents;
import contents.StringContents;
import datamanage.History;

public class LinKlipboardGroup {

	private String groupName;
	private String groupPassword;
	private int order = 0;

	private ClientHandler chief; // ����
	private Hashtable<String, ClientHandler> clients;
	//private Vector<ClientHandler> clients;
	private History history = new History(); // �� �׷��� �����丮
	private Contents lastContents; // �ֱ� ������

	public static final String DEFAULT_CHIEF_NAME = "Chief";
	public static final String DEFAULT_CREW_NAME = "Crew";

	private static String fileSaveDir = "C:\\LinKlipboardServer";

	/** ���ο� �׷��� ����
	 * @param groupName �� �׷��� �̸�
	 * @param password �� �׷쿡 �����ϱ� ���� �н�����
	 * @param chief �׷����� Ŭ���̾�Ʈ �ڵ鷯*/
	public LinKlipboardGroup(String groupName, String password, ClientHandler chief) {
		this.groupName = groupName;
		this.groupPassword = password;
		this.chief = chief;

		clients = new Hashtable<String, ClientHandler>(LinKlipboard.MAX_CLIENT - 1); // <ip, client>
		joinGroup(chief);
		chief.setNickname(DEFAULT_CHIEF_NAME);
		Directory.createEmptyDirectory(getFileDir());
		// createFileReceiveFolder();
		System.out.println("���� ����");
	}

	/** �� Ŭ���̾�Ʈ�� �׷쿡 �߰��Ѵ�. 
	 * @param newClient �׷쿡 �߰��� Ŭ���̾�Ʈ�� �ڵ鷯 
	 * @return �׷���� ���� MAX_CLIENT�̸��� ��� true �׷��� ������ false. */
	public synchronized void joinGroup(ClientHandler newClient) {
		// Ŭ���̾�Ʈ�� �߰�
		newClient.setOrder(order++);
		clients.put(newClient.getRemoteAddr(), newClient);
	}

	public String createDefaultNickname() {
		for (int i = 0;; i++) {
			if (!clients.containsKey(DEFAULT_CREW_NAME + i + 1)) {
				return (DEFAULT_CREW_NAME + i + 1);
			}
		}
	}

	/** ����ڰ� ���� ������ �г����� �׷� ������ �ߺ����� Ȯ���Ѵ�. 
	 * @return ��� ������ �г����̸� true �׷��� ������ false */
	public boolean isNicknameUsable(String nickname) {
		Set<String> ips = clients.keySet();
		Iterator<String> it = ips.iterator();
		while (it.hasNext()) {
			if (it.next().equals(nickname)) {
				return false;
			}
		}
		return true;
	}

	/** ���� �׷����� ������ ��������, ���� �׷����� ���Ѵ�.
	 * @return �׷��� �ٷ� ������ ������ Ŭ���̾�Ʈ�� �ڵ鷯 */
	public ClientHandler setChief() {
		ClientHandler nextCheif = null;
		int compareOrder = order;

		Set<String> c = clients.keySet();
		Iterator<String> it = c.iterator();

		while (it.hasNext()) {
			ClientHandler client = clients.get(it.next());
			if (client.getOrder() < compareOrder) {
				compareOrder = client.getOrder();
				nextCheif = client;
			}
		}
		return nextCheif;
	}

	/** �׷��� �׷����� ����/�����Ѵ�.
	 * @param chief �׷������� ������ Ŭ���̾�Ʈ �ڵ鷯*/
	public void setChief(ClientHandler chief) {
		this.chief = chief;
	}

	/** �׷쿡�� ������ �н����尡 Ŭ���̾�Ʈ�� �Է��� �н����� ��ġ�ϴ��� ���Ѵ�. 
	 * @param password ���� �н�����
	 * @return �н����尡 ��ġ�ϸ� true �ƴϸ� false */
	public boolean isPasswordCorrected(String password) {
		return groupPassword.equals(password);
	}

	/** @return �׷� �̸�*/
	public String getName() {
		return groupName;
	}

	/** @return ���� �׷���� �ִ� �׷�� �̻��̸� true �ƴϸ� false */
	public boolean isFull() {
		return (clients.size() > LinKlipboard.MAX_CLIENT);
	}

	/** �׷��� �ֽ� Contents�� �����Ѵ�. 
	 * @param contents ������ Contents ��ü */
	public synchronized void setLastContents(Contents contents) {
		this.lastContents = contents;
		setHistory(contents);
	}

	/** �׷��� �����丮�� Contents�� �߰��Ѵ�. 
	 * @param contents �����丮�� ���� �߰��� Contents ��ü */
	private void setHistory(Contents contents) {
		System.out.println("�����丮 ����");
		history.addContents(contents);
	}

	/** @return �׷��� �ֽ� Contents ��ü */
	public Contents getLastContents() {
		return lastContents;
	}

	/** @param ipAddr �׷� ���� �ִ� Ŭ���̾�Ʈ�� ip �ּ�
	 * @return ipAddr�� �ش��ϴ� Ŭ���̾�Ʈ �ڵ鷯 */
	public ClientHandler searchClient(String ipAddr) {
		return clients.get(ipAddr);
	}

	/** @param ipAddr �׷� ���� �ִ� Ŭ���̾�Ʈ�� ip �ּ� */
	public String getNickname(String ipAddr) {
		if (clients.containsKey(ipAddr)) {
			return clients.get(ipAddr).getNickname();
		}
		return null;
	}

	public boolean isDuplicatedIpAddr(String ip) {
		return clients.containsKey(ip);
	}

	/** @return �׷쿡�� ������ ���ϵ��� ����Ǿ� �ִ� ������ ��� ���ڿ�*/
	public String getFileDir() {
		return (fileSaveDir + "\\" + groupName);
	}

	/** @return �����丮 ���� �������� Vector */
	public Vector<Contents> getHistory() {
		return history.getContentsVector();
	}

	/** �˸� �۽� �����带 ���� */
	public synchronized void notificateExitClients(ClientHandler sender) {
		String exitClient = sender.getNickname();
		Contents tmp = new StringContents("exit:" + exitClient, LinKlipboard.NULL);
		new Notification(this, sender);
	}

	public synchronized void notificateJoinClient(ClientHandler sender) {
		System.out.println("���� �˸� �غ�");
		String joinClient = sender.getNickname();
		Contents tmp = new StringContents("join:" + joinClient, LinKlipboard.NULL);
		new Notification(this, sender);
	}
	
	public synchronized void notificateUpdate(ClientHandler sender) {
		new Notification(this, sender);
	}

	public int getNextSerialNo() {
		return history.getNextSerialNo();
	}

	public Contents getHistoryContents(int serialNo) {
		return history.getContents(serialNo);
	}

	/** �׷���� 0�� �Ǹ� ��ü�� �ı� */
	public void destroyGroup() {
		File fileReceiveFolder = new File(getFileDir()); // �׷� ���� ���� ��ü ������
		Directory.clearDirecrory(fileReceiveFolder); // ���� ����
	}

	public Vector<String> getClients() {
		Vector<String> clientsList = new Vector<String>();
		Set<String> ips = clients.keySet();
		Iterator<String> it = ips.iterator();
		while (it.hasNext()) {
			int i = 0;
			String nextClient = clients.get(it.next()).getNickname();
			clientsList.add(i++, nextClient);
		}
		return clientsList;
	}

	/** �׷쿡�� Ŭ���̾�Ʈ ����
	 * @return ������ Ŭ���̾�Ʈ �ڵ鷯 ��ü */
	public synchronized ClientHandler removeClient(String client) {
		ClientHandler removed = clients.remove(client);
		if (clients.isEmpty()) {
			LinKlipboardServer.removeGroup(groupName);
		}
		return removed;
	}

	/** �۽��ڸ� ������ �׷� ���� ��� Ŭ���̾�Ʈ���� �׷��� �ֽ� Contents ��ü�� ����
	 * @param sender ������ �����͸� ������ Ŭ���̾�Ʈ */
	class Notification extends Thread {

		private LinKlipboardGroup group;
		private ClientHandler sender;
		private int type;

		public Notification(LinKlipboardGroup group, ClientHandler sender) {
			this.group = group;
			this.sender = sender;
			start();
		}

		/* TODO: ����ȭ ������ �ذ����� ����.
		 * ����� �� ������ ���� ä �ٸ� ������ ������ ���ɼ��� ���� */
		@Override
		public void run() {
			switch (type) {
			case LinKlipboard.UPDATE_DATA:
				sendUpdateData();
				break;
//			case LinKlipboard.EXIT_CLITNT:
//				notificateExitClient();
//				break;
//
//			case LinKlipboard.JOIN_CLITNT:
//				notificateJoinClient();
//				break;

			default:
				break;
			}
		}

		/** �۽��ڸ� ������ �׷� ���� ��� Ŭ���̾�Ʈ���� �׷��� �ֽ� Contents ��ü�� ����
		 * @param sender ������ �����͸� ������ Ŭ���̾�Ʈ */
		private void sendUpdateData() {
			//Hashtable<String, ClientHandler> addressee = clients; // ���� �׷�� ���� ����
			Hashtable<String, ClientHandler> addressee = null;

			synchronized (clients) {
				addressee = new Hashtable<String, ClientHandler>(clients); // ���� �׷�� ���� ����
			}
			addressee.remove(sender.getRemoteAddr()); // �۽����� ���ܴ��

			Set<String> ipAddrs;
			synchronized (clients) {
				ipAddrs = clients.keySet(); // key(ip�ּ�)�� ����
			}
			Iterator<String> it = ipAddrs.iterator();
			while (it.hasNext()) { // �۽� ������ ����
				new ContentsSender(group, clients.get(it.next()));
			}
		}

//		private void notificateExitClient() {
//			Set<String> ipAddrs;
//			synchronized (clients) {
//				ipAddrs = clients.keySet(); // key(ip�ּ�)�� ����
//			}
//			Iterator<String> it = ipAddrs.iterator();
//			while (it.hasNext()) { // �۽� ������ ����
//				new NotificationSender(it.next(), sender.getNickname(), NotificationSender.EXIT);
//			}
//		}
//
//		private void notificateJoinClient() {
//			Set<String> ipAddrs;
//			synchronized (clients) {
//				ipAddrs = clients.keySet(); // key(ip�ּ�)�� ����
//			}
//			Iterator<String> it = ipAddrs.iterator();
//			while (it.hasNext()) { // �۽� ������ ����
//				new NotificationSender(it.next(), sender.getNickname(), NotificationSender.JOIN);
//			}
//		}
	}
}