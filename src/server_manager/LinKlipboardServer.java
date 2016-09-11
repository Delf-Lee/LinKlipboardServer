package server_manager;

import java.util.Hashtable;
import java.util.Vector;

public class LinKlipboardServer {
	private static Hashtable<String, LinKlipboardGroup> tmpGroups; // �ӽ� �׷� ���� 1
	private static Vector<LinKlipboardGroup> groups = new Vector<LinKlipboardGroup>(LinKlipboard.MAX_GROUP); // �ӽ� �׷� ���� 2

	/** ���ο� �׷��� ���� 
	 * @param newGroup ���� ������ �׷� */
	public static void createGroup(LinKlipboardGroup newGroup, ClientHandler newClient) {
		if (groups.size() < LinKlipboard.MAX_GROUP) { // �ִ� �׷� �� üũ
			tmpGroups.put(newGroup.getName(), newGroup);
		}
	}

	/** ���� �׷쿡 Ŭ���̾�Ʈ ���� 
	 * @param groupName Ŭ���̾�Ʈ�� ������ ����ϴ� �׷��� �̸� 
	 * @param Ŭ���̾�Ʈ�� �ڵ鷯 */
	public static void joinGroup(String groupName, ClientHandler newClient) {
		/* �ߺ�ó�� �� ���� ó���� �������� */
		tmpGroups.get(groupName).joinGroup(newClient);
	}

	/** �ż� �Ϸ��� �׷��� �̸��� ������ �Ͱ� �ߺ��Ǵ��� Ȯ�� 
	 * @param groupName ���� �ż��Ϸ� �ϴ� �׷��� �̸� 
	 * @return �ߺ��� �̸��� ������ true �ƴϸ� false */
	public static boolean isExistGroup(String groupName) {
		return tmpGroups.containsKey(groupName);
	}

	/** @param groupName ��ȯ ���� �׷��̸� 
	 * @return groupName�� ��ġ�ϴ� LinKlipboardGroup ��ü ��ȯ*/
	public static LinKlipboardGroup getGroup(String groupName) {
		return tmpGroups.get(groupName);
	}

}
