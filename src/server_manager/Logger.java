package server_manager;

import java.util.Date;

import contents.Contents;

public class Logger {

	public final static int SEND = 50;
	public final static int RECEIVE = 51;

	public final static int CREATE_GROUP = 60;
	public final static int DESTROYD_GROUP = 61;
	public final static int JOIN_CLIENT = 62;
	public final static int EXIT_CLIENT = 63;

	
	public static void logCreateGroup(String groupName, ClientHandler client) {
		Date now = new Date();
		String ipAddr = client.getRemoteAddr();
		int port = client.getRemotePort();
		
		String prtMsg = "[" + now + "] " + ipAddr + ":" + port + " create the group.(Group name: " + groupName + ", Total group: " + LinKlipboardServer.getGroupCnt() + ")";
		System.out.println(prtMsg);
	}
	
	public static void logJoinClient(String groupName, ClientHandler client) {
		Date now = new Date();
		String ipAddr = client.getRemoteAddr();
		int port = client.getRemotePort();
		
		String prtMsg = "[" + now + "] " + ipAddr + ":" + port + " join the group.(Group name: " + groupName + ", Total group: " + LinKlipboardServer.getGroupCnt() + ")";
		System.out.println(prtMsg);
	}

	public static void transferData(int transfer, Contents data) {

	}

	public static void addLogFile(String log) {

	}
}
