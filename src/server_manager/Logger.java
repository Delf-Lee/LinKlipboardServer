package server_manager;

import java.util.Calendar;
import java.util.Date;

import contents.Contents;

public class Logger {

	public final static int SEND = 50;
	public final static int RECEIVE = 51;

	public final static int CREATE_GROUP = 60;
	public final static int DESTROYD_GROUP = 61;
	public final static int JOIN_CLIENT = 62;
	public final static int EXIT_CLIENT = 63;

	public static void accessClient() {
		/* 접속 기록과 거부 사유 */
	}

	public static void logCreateGroup(ClientHandler client) {
		String ipAddr = client.getRemoteAddr();
		int port = client.getRemotePort();
		Calendar now = Calendar.getInstance();

		String prtMsg = "[" + now() + "] "; // 시간
		prtMsg += ipAddr + ":" + port; // ip && port
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
	private static String now() {
		Calendar cal = Calendar.getInstance();
		String year = Integer.toString(cal.get(Calendar.YEAR));
		String month = Integer.toString(cal.get(Calendar.MONTH));
		String date = Integer.toString(cal.get(Calendar.DATE));
		String hour = Integer.toString(cal.get(Calendar.HOUR_OF_DAY));
		String minute = Integer.toString(cal.get(Calendar.MINUTE));
		String sec = Integer.toString(cal.get(Calendar.SECOND));

		return year + "." + month + "." + date + "/" + hour + ":" + minute + ":" + sec;
	}
}
