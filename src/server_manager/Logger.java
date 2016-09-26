package server_manager;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import contents.Contents;

public class Logger {

	public final static int SEND = 50;
	public final static int RECEIVE = 51;

	public final static int TRY_ACCESS = 60;
	public final static int CREATE_GROUP = 61;
	public final static int DESTROYD_GROUP = 62;
	public final static int JOIN_CLIENT = 63;
	public final static int EXIT_CLIENT = 64;

	private static Calendar now;
	public static void accessClient(ClientHandler client) {
		String prtMsg = basicInfo(client);
		prtMsg += " try access.";
		
		System.out.println(prtMsg);
		addLogFile(prtMsg);
	}
	
	public static void resultAccess(ClientHandler client, int result) {
		String ipAddr = client.getRemoteAddr();
		String prtMsg = basicInfo(client);
		
		switch (result) {
		case LinKlipboard.ACCESS_PERMIT:
			prtMsg += " access success.";
			break;
		default:
			break;
		}
	}
	

	public static void logCreateGroup(ClientHandler client) {
		String ipAddr = client.getRemoteAddr();
		int port = client.getRemotePort();
		Calendar now = Calendar.getInstance();

		String prtMsg = basicInfo(client); // �ð�
		prtMsg += " create the group "; // ����
		prtMsg += "(Group name: " + client.getGroupName(); // �׷� �̸� 
		prtMsg += ", Total group: " + LinKlipboardServer.getGroupCnt() + ")"; // ���� �� �׷� ��

		System.out.println(prtMsg);
		addLogFile(prtMsg);
	}

	/** Ŭ���̾�Ʈ�� ������ ��� 
	 * @param client ������ Ŭ���̾�Ʈ�� �ڵ鷯 */
	public static void logJoinClient(ClientHandler client) {
		Date now = new Date();
		String ipAddr = client.getRemoteAddr();
		int port = client.getRemotePort();

		String prtMsg = "[" + now() + "] "; // �ð�
		prtMsg += ipAddr + ":" + port; // ip && port
		prtMsg += " join the group "; // ����
		prtMsg += "(Group name: " + client.getGroupName() + ")"; // �׷� �̸� 

		System.out.println(prtMsg);
		addLogFile(prtMsg);
	}

	public static void logExitClient(ClientHandler client) {

	}

	public static void destroyClient(ClientHandler client) {

	}

	public static void transferData(int transfer, Contents data) {

	}

	private static void addLogFile(String log) {

	}

	/** @return YYYY.MM.DD/HH:MM:SS ������ ���� �ð� */
	public static String now() {
		Calendar cal = Calendar.getInstance();
		String year = Integer.toString(cal.get(Calendar.YEAR));
		String month = Integer.toString(cal.get(Calendar.MONTH));
		String date = Integer.toString(cal.get(Calendar.DATE));
		String hour = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
		String minute = Integer.toString(cal.get(Calendar.MINUTE));
		String sec = Integer.toString(cal.get(Calendar.SECOND));

		return year + "." + month + "." + date + "/" + hour + ":" + minute + ":" + sec;
	}

	private static String basicInfo(ClientHandler client) {
		String info = "[" + now() + "] " + client.getRemoteAddr() + ":" + client.getRemotePort();
		return info;
	}
}
