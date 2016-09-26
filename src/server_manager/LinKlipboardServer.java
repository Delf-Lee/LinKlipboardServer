package server_manager;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import surpport.Directory;
import surpport.Logger;

public class LinKlipboardServer {

	private static Hashtable<String, LinKlipboardGroup> groups = new Hashtable<String, LinKlipboardGroup>(); // �ӽ� �׷� ���� 1
	private static Hashtable<String, ClientHandler> watingClients = new Hashtable<String, ClientHandler>();
	public static Logger logger = new Logger();

	public LinKlipboardServer() {
		System.out.println(" - Boot Server");
		Directory.createEmptyDirectory(LinKlipboard.FILE_DIR);
		System.out.println(" - Create directory to store files ");
	}

	/** ���ο� �׷��� ���� 
	 * @param newGroup ���� ������ �׷� */
	public synchronized static void createGroup(LinKlipboardGroup newGroup) {
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
	public synchronized static void joinGroup(String groupName, ClientHandler newClient) {
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

	/** @return ������ �����ϴ� �׷��� �� */
	public static int getGroupCnt() {
		return groups.size();
	}

	/** @param ������ ������ Ŭ���̾�Ʈ�� ip �ּ� 
	 * @return ���� ���� ������ Ŭ���̾�Ʈ�� �ߺ��� ip �ּҰ� �����ϸ� true �ƴϸ� false */
	public static boolean checkDuplicatedIpAddr(String ip) {

		if (groups.isEmpty())
			return false;

		Set<String> keys = groups.keySet();
		Iterator<String> groupName = keys.iterator();
		while (groupName.hasNext()) {
			String name = groupName.next();
			if (groups.get(name).isDuplicatedIpAddr(ip)) {
				return true;
			}
		}
		return false;
	}

	/**@param*/
	public static LinKlipboardGroup removeGroup(String group) {
		if (!group.isEmpty()) {
			return groups.remove(group);
		}
		return null;
	}
	
	public static void waitClient(ClientHandler client) {
		watingClients.put(client.getRemoteAddr(), client);
	}
	
	public static ClientHandler enterClient(String ip) {
		return watingClients.remove(ip);
	}
	
}
