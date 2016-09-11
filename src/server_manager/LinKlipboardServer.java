package server_manager;

import java.util.Hashtable;
import java.util.Vector;

public class LinKlipboardServer {
	private static Hashtable<String, LinKlipboardGroup> tmpGroups; // 임시 그룹 모임 1
	private static Vector<LinKlipboardGroup> groups = new Vector<LinKlipboardGroup>(LinKlipboard.MAX_GROUP); // 임시 그룹 모임 2

	/** 새로운 그룹을 생성 
	 * @param newGroup 새로 생성할 그룹 */
	public static void createGroup(LinKlipboardGroup newGroup, ClientHandler newClient) {
		if (groups.size() < LinKlipboard.MAX_GROUP) { // 최대 그룹 수 체크
			tmpGroups.put(newGroup.getName(), newGroup);
		}
	}

	/** 기존 그룹에 클라이언트 입장 
	 * @param groupName 클라이언트가 입장을 희망하는 그룹의 이름 
	 * @param 클라이언트의 핸들러 */
	public static void joinGroup(String groupName, ClientHandler newClient) {
		/* 중복처리 및 보안 처리는 서블릿에서 */
		tmpGroups.get(groupName).joinGroup(newClient);
	}

	/** 신설 하려는 그룹의 이름이 기존의 것과 중복되는지 확인 
	 * @param groupName 새로 신설하려 하는 그룹의 이름 
	 * @return 중복된 이름이 있으면 true 아니면 false */
	public static boolean isExistGroup(String groupName) {
		return tmpGroups.containsKey(groupName);
	}

	/** @param groupName 반환 받을 그룹이름 
	 * @return groupName과 일치하는 LinKlipboardGroup 객체 반환*/
	public static LinKlipboardGroup getGroup(String groupName) {
		return tmpGroups.get(groupName);
	}

}
