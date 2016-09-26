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

		String prtMsg = basicInfo(client); // 시간
		prtMsg += " create the group "; // 행위
		prtMsg += "(Group name: " + client.getGroupName(); // 그룹 이름 
		prtMsg += ", Total group: " + LinKlipboardServer.getGroupCnt() + ")"; // 현재 총 그룹 수

		System.out.println(prtMsg);
		addLogFile(prtMsg);
	}

	/** 클라이언트의 접속을 기록 
	 * @param client 접속한 클라이언트의 핸들러 */
	public static void logJoinClient(ClientHandler client) {
		Date now = new Date();
		String ipAddr = client.getRemoteAddr();
		int port = client.getRemotePort();

		String prtMsg = "[" + now() + "] "; // 시간
		prtMsg += ipAddr + ":" + port; // ip && port
		prtMsg += " join the group "; // 행위
		prtMsg += "(Group name: " + client.getGroupName() + ")"; // 그룹 이름 

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

	/** @return YYYY.MM.DD/HH:MM:SS 형식의 현재 시간 */
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
