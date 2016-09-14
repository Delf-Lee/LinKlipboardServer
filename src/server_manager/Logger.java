package server_manager;

import java.util.Date;

import contents.Contents;

public class Logger {

	public final static int SEND = 50;
	public final static int RECEIVE = 51;

	public final static int CREATE_GROUP = 60;
	public final static int JOIN_GROUP = 61;
	public final static int DESTROYD_GROUP = 62;

	public static void accessClient(int access, ClientHandler client) {

		Date now = new Date();
		String ipAddr = client.getRemoteAddr();
		int port = client.getRemotePort();
		String status = null;
		switch (access) {
		case CREATE_GROUP:
			status = " create the group";
			break; //

		case JOIN_GROUP:
			status = " join the group";
			break;
		}

		String prtMsg = "[" + now + "] " + ipAddr + ":" + port + status + "(Group count: " + LinKlipboardServer.getGroupCnt() + ")";
		System.out.println(prtMsg);
	}

	public static void transferData(int transfer, Contents data) {

	}

	public static void addLogFile(String log) {

	}
}
