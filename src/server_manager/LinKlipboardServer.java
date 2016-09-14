package server_manager;

import java.util.Hashtable;

public class LinKlipboardServer {
	private static Hashtable<String, LinKlipboardGroup> groups = new Hashtable<String, LinKlipboardGroup>(); // �ӽ� �׷� ���� 1
	public static Logger logger = new Logger();

	/** ���ο� �׷��� ���� 
	 * @param newGroup ���� ������ �׷� */
	public static void createGroup(LinKlipboardGroup newGroup, ClientHandler newClient) {
		if (!isFull()) {
			groups.put(newGroup.getName(), newGroup);
		}
	}

	/** @return �׷� ���� MAX_GROUP �����̸� true, �ƴϸ� false */
	public static boolean isFull() {
		if (groups.size() < LinKlipboard.MAX_GROUP) { // �ִ� �׷� �� üũ
			return false;
		}
		return true;
	}

	/** ���� �׷쿡 Ŭ���̾�Ʈ ���� 
	 * @param groupName Ŭ���̾�Ʈ�� ������ ����ϴ� �׷��� �̸� 
	 * @param Ŭ���̾�Ʈ�� �ڵ鷯 */
	public static void joinGroup(String groupName, ClientHandler newClient) {
		/* �ߺ�ó�� �� ���� ó���� �������� */
		groups.get(groupName).joinGroup(newClient);
	}

	/** �ż� �Ϸ��� �׷��� �̸��� ������ �Ͱ� �ߺ��Ǵ��� Ȯ�� 
	 * @param groupName ���� �ż��Ϸ� �ϴ� �׷��� �̸� 
	 * @return �ߺ��� �̸��� ������ true �ƴϸ� false */
	public static boolean isExistGroup(String groupName) {
		return groups.containsKey(groupName);
	}

	/** @param groupName ��ȯ ���� �׷��̸� 
	 * @return groupName�� ��ġ�ϴ� LinKlipboardGroup ��ü ��ȯ*/
	public static LinKlipboardGroup getGroup(String groupName) {
		return groups.get(groupName);
	}

	public static int getGroupCnt() {
		return groups.size();
	}
}
