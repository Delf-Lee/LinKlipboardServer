package server_manager;

import java.util.Hashtable;
import java.util.Vector;

import org.omg.PortableInterceptor.ClientRequestInfoOperations;

import datamanage.Contents;
import datamanage.History;

public class LinKlipboardGroup {

	private String groupName;
	private String groupPassword;

	private ClientHandler chief; // ����
	private Hashtable<String, ClientHandler> clients;
	//private Vector<ClientHandler> clients;
	private History history; // �� �׷��� �����丮
	private Contents lastContents; // �ֱ� ������

	public static final String DEFAULT_CHIEF_NAME = "�׷���";
	public static final String DEFAULT_CREW_NAME = "�׷��";

	/** ���ο� �׷��� ����
	 * @param groupName �� �׷��� �̸�
	 * @param password �� �׷쿡 �����ϱ� ���� �н�����
	 * @param chief �׷����� Ŭ���̾�Ʈ �ڵ鷯*/
	public LinKlipboardGroup(String groupName, String password, ClientHandler chief) {
		this.groupName = groupName;
		this.groupPassword = password;
		this.chief = chief;

		//clients = new Vector<ClientHandler>(LinKlipboard.MAX_CLIENT - 1);
		clients = new Hashtable<String, ClientHandler>(LinKlipboard.MAX_CLIENT - 1);
		joinGroup(chief);
		chief.setNickname(DEFAULT_CHIEF_NAME);
	}

	/** �� Ŭ���̾�Ʈ�� �׷쿡 �߰��Ѵ�. 
	 * @param newClient �׷쿡 �߰��� Ŭ���̾�Ʈ�� �ڵ鷯 */
	public void joinGroup(ClientHandler newClient) {
		// Ŭ���̾�Ʈ�� �߰�
		setDefaultNickname(newClient);
		if (clients.size() < LinKlipboard.MAX_CLIENT) {
			//clients.add(newClient);
			clients.put(newClient.getNickname(), newClient);
		}
	}

	/** �׷� ������ �� Ŭ���̾�Ʈ���� �ߺ����� ���� �⺻ �г����� �ο��Ѵ�. 
	 * @param newClient  ������ Ŭ���̾�Ʈ�� �ڵ鷯 */
	public void setDefaultNickname(ClientHandler newClient) {
		//		for (int i = 0; i < clients.size(); i++) {
		//			if (!newClient.getNickname().equals(DEFAULT_CREW_NAME + i + 1)) {
		//				newClient.setNickname(DEFAULT_CREW_NAME + i + 1);
		//				break;
		//			}
		//		}
		for (int i = 0; i < clients.size(); i++) {
			if (!clients.containsKey(DEFAULT_CREW_NAME + i + 1)) {
				newClient.setNickname(DEFAULT_CREW_NAME + i + 1);
				break;
			}
		}
	}

	/** ����ڰ� ���� ������ �г����� �׷� ������ �ߺ����� Ȯ���Ѵ�. 
	 * @return ��� ������ �г����̸� true �׷��� ������ false */
	public boolean checkNickname(String nickname) {
		return !(clients.containsKey(nickname));
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

	public void distributeData(int senderIndex, Contents data) {
		// �ٸ� Ŭ���̾�Ʈ���� �����͸� �Ѹ� 
	}

	public void reciveData() {
		// �����͸� �޾Ƽ� �����丮�� ����
	}

	/** @return �׷� �̸�*/
	public String getName() {
		return groupName;
	}

	public void sendNotification(ClientHandler sender /*�Ǵ� Ŭ���̾�Ʈ�� �ε���*/) {
		// sender�� ������ ��� Ŭ���̾�Ʈ���� �˸� ����
	}

	/** @return ���� �׷���� �ִ� �׷�� �̻��̸� true �ƴϸ� false */
	public boolean isFull() {
		return (clients.size() > LinKlipboard.MAX_CLIENT);
	}

	public void setLastContents(Contents contents) {
		this.lastContents = contents;
		setHistory();
	}

	private void setHistory() {

	}

	public ClientHandler searchClient(String nickname) {
		return clients.get(nickname);
	}

	class Notification extends Thread {
		public void run() {
			//  ��� Ŭ���̾�Ʈ���� �˸� �۽�
		}
	}
}