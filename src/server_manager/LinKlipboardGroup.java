package server_manager;

import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import Transferer.ContentsSender;
import contents.Contents;
import contents.StringContents;
import datamanage.History;

public class LinKlipboardGroup {

	private String groupName;
	private String groupPassword;
	private int order = 0;

	private ClientHandler chief; // 방장
	private Hashtable<String, ClientHandler> clients;
	//private Vector<ClientHandler> clients;
	private History history = new History(); // 이 그룹의 히스토리
	private Contents lastContents; // 최근 컨텐츠

	public static final String DEFAULT_CHIEF_NAME = "Chief";
	public static final String DEFAULT_CREW_NAME = "Crew";

	private static String fileSaveDir = "C:\\LinKlipboardServer";

	/** 새로운 그룹을 생성
	 * @param groupName 이 그룹의 이름
	 * @param password 이 그룹에 접속하기 위한 패스워드
	 * @param chief 그룹장의 클라이언트 핸들러*/
	public LinKlipboardGroup(String groupName, String password, ClientHandler chief) {
		this.groupName = groupName;
		this.groupPassword = password;
		this.chief = chief;

		clients = new Hashtable<String, ClientHandler>(LinKlipboard.MAX_CLIENT - 1); // <ip, client>
		joinGroup(chief);
		chief.setNickname(DEFAULT_CHIEF_NAME);
		Directory.createEmptyDirectory(getFileDir());
		// createFileReceiveFolder();
		System.out.println("폴더 생성");
	}

	/** 새 클라이언트를 그룹에 추가한다. 
	 * @param newClient 그룹에 추가할 클라이언트의 핸들러 
	 * @return 그룹원의 수가 MAX_CLIENT미만일 경우 true 그렇지 않으면 false. */
	public synchronized void joinGroup(ClientHandler newClient) {
		// 클라이언트를 추가
		newClient.setOrder(order++);
		clients.put(newClient.getRemoteAddr(), newClient);
	}

	public String createDefaultNickname() {
		for (int i = 0;; i++) {
			if (!clients.containsKey(DEFAULT_CREW_NAME + i + 1)) {
				return (DEFAULT_CREW_NAME + i + 1);
			}
		}
	}

	/** 사용자가 새로 설정한 닉네임이 그룹 내에서 중복인지 확인한다. 
	 * @return 사용 가능한 닉네임이면 true 그렇지 않으면 false */
	public boolean isNicknameUsable(String nickname) {
		Set<String> ips = clients.keySet();
		Iterator<String> it = ips.iterator();
		while (it.hasNext()) {
			if (it.next().equals(nickname)) {
				return false;
			}
		}
		return true;
	}

	/** 현재 그룹장의 연결이 끊어지면, 다음 그룹장을 정한다.
	 * @return 그룹장 바로 다음에 접속한 클라이언트의 핸들러 */
	public ClientHandler setChief() {
		ClientHandler nextCheif = null;
		int compareOrder = order;

		Set<String> c = clients.keySet();
		Iterator<String> it = c.iterator();

		while (it.hasNext()) {
			ClientHandler client = clients.get(it.next());
			if (client.getOrder() < compareOrder) {
				compareOrder = client.getOrder();
				nextCheif = client;
			}
		}
		return nextCheif;
	}

	/** 그룹의 그룹장을 설정/변경한다.
	 * @param chief 그룹장으로 지정할 클라이언트 핸들러*/
	public void setChief(ClientHandler chief) {
		this.chief = chief;
	}

	/** 그룹에서 설정된 패스워드가 클라이언트가 입력한 패스워와 일치하는지 비교한다. 
	 * @param password 비교할 패스워드
	 * @return 패스워드가 일치하면 true 아니면 false */
	public boolean isPasswordCorrected(String password) {
		return groupPassword.equals(password);
	}

	/** @return 그룹 이름*/
	public String getName() {
		return groupName;
	}

	/** @return 현재 그룹원이 최대 그룹원 이상이면 true 아니면 false */
	public boolean isFull() {
		return (clients.size() > LinKlipboard.MAX_CLIENT);
	}

	/** 그룹의 최신 Contents를 갱신한다. 
	 * @param contents 갱신할 Contents 객체 */
	public synchronized void setLastContents(Contents contents) {
		this.lastContents = contents;
		setHistory(contents);
	}

	/** 그룹의 히스토리에 Contents를 추가한다. 
	 * @param contents 히스토리에 새로 추가할 Contents 객체 */
	private void setHistory(Contents contents) {
		System.out.println("히스토리 세팅");
		history.addContents(contents);
	}

	/** @return 그룹의 최신 Contents 객체 */
	public Contents getLastContents() {
		return lastContents;
	}

	/** @param ipAddr 그룹 내에 있는 클라이언트의 ip 주소
	 * @return ipAddr에 해당하는 클라이언트 핸들러 */
	public ClientHandler searchClient(String ipAddr) {
		return clients.get(ipAddr);
	}

	/** @param ipAddr 그룹 내에 있는 클라이언트의 ip 주소 */
	public String getNickname(String ipAddr) {
		if (clients.containsKey(ipAddr)) {
			return clients.get(ipAddr).getNickname();
		}
		return null;
	}

	public boolean isDuplicatedIpAddr(String ip) {
		return clients.containsKey(ip);
	}

	/** @return 그룹에서 공유한 파일들이 저장되어 있는 폴더의 경로 문자열*/
	public String getFileDir() {
		return (fileSaveDir + "\\" + groupName);
	}

	/** @return 히스토리 내에 컨턴츠의 Vector */
	public Vector<Contents> getHistory() {
		return history.getContentsVector();
	}

	/** 알림 송신 스레드를 생성 */
	public synchronized void notificateExitClients(ClientHandler sender) {
		String exitClient = sender.getNickname();
		Contents tmp = new StringContents("exit:" + exitClient, LinKlipboard.NULL);
		new Notification(this, sender);
	}

	public synchronized void notificateJoinClient(ClientHandler sender) {
		System.out.println("접속 알림 준비");
		String joinClient = sender.getNickname();
		Contents tmp = new StringContents("join:" + joinClient, LinKlipboard.NULL);
		new Notification(this, sender);
	}
	
	public synchronized void notificateUpdate(ClientHandler sender) {
		new Notification(this, sender);
	}

	public int getNextSerialNo() {
		return history.getNextSerialNo();
	}

	public Contents getHistoryContents(int serialNo) {
		return history.getContents(serialNo);
	}

	/** 그룹원이 0가 되면 객체을 파괴 */
	public void destroyGroup() {
		File fileReceiveFolder = new File(getFileDir()); // 그룹 공유 폴더 객체 가져옴
		Directory.clearDirecrory(fileReceiveFolder); // 폴더 삭제
	}

	public Vector<String> getClients() {
		Vector<String> clientsList = new Vector<String>();
		Set<String> ips = clients.keySet();
		Iterator<String> it = ips.iterator();
		while (it.hasNext()) {
			int i = 0;
			String nextClient = clients.get(it.next()).getNickname();
			clientsList.add(i++, nextClient);
		}
		return clientsList;
	}

	/** 그룹에서 클라이언트 삭제
	 * @return 삭제된 클라이언트 핸들러 객체 */
	public synchronized ClientHandler removeClient(String client) {
		ClientHandler removed = clients.remove(client);
		if (clients.isEmpty()) {
			LinKlipboardServer.removeGroup(groupName);
		}
		return removed;
	}

	/** 송신자를 제외한 그룹 내의 모든 클라이언트에게 그룹의 최신 Contents 객체를 전송
	 * @param sender 서버에 데이터를 전송한 클라이언트 */
	class Notification extends Thread {

		private LinKlipboardGroup group;
		private ClientHandler sender;
		private int type;

		public Notification(LinKlipboardGroup group, ClientHandler sender) {
			this.group = group;
			this.sender = sender;
			start();
		}

		/* TODO: 동기화 문제를 해결하지 못함.
		 * 통신이 다 끝나지 않은 채 다른 연결을 수립할 가능성이 있음 */
		@Override
		public void run() {
			switch (type) {
			case LinKlipboard.UPDATE_DATA:
				sendUpdateData();
				break;
//			case LinKlipboard.EXIT_CLITNT:
//				notificateExitClient();
//				break;
//
//			case LinKlipboard.JOIN_CLITNT:
//				notificateJoinClient();
//				break;

			default:
				break;
			}
		}

		/** 송신자를 제외한 그룹 내의 모든 클라이언트에게 그룹의 최신 Contents 객체를 전송
		 * @param sender 서버에 데이터를 전송한 클라이언트 */
		private void sendUpdateData() {
			//Hashtable<String, ClientHandler> addressee = clients; // 현재 그룹원 정보 복사
			Hashtable<String, ClientHandler> addressee = null;

			synchronized (clients) {
				addressee = new Hashtable<String, ClientHandler>(clients); // 현재 그룹원 정보 복사
			}
			addressee.remove(sender.getRemoteAddr()); // 송신인은 제외대상

			Set<String> ipAddrs;
			synchronized (clients) {
				ipAddrs = clients.keySet(); // key(ip주소)만 추출
			}
			Iterator<String> it = ipAddrs.iterator();
			while (it.hasNext()) { // 송신 스레드 생성
				new ContentsSender(group, clients.get(it.next()));
			}
		}

//		private void notificateExitClient() {
//			Set<String> ipAddrs;
//			synchronized (clients) {
//				ipAddrs = clients.keySet(); // key(ip주소)만 추출
//			}
//			Iterator<String> it = ipAddrs.iterator();
//			while (it.hasNext()) { // 송신 스레드 생성
//				new NotificationSender(it.next(), sender.getNickname(), NotificationSender.EXIT);
//			}
//		}
//
//		private void notificateJoinClient() {
//			Set<String> ipAddrs;
//			synchronized (clients) {
//				ipAddrs = clients.keySet(); // key(ip주소)만 추출
//			}
//			Iterator<String> it = ipAddrs.iterator();
//			while (it.hasNext()) { // 송신 스레드 생성
//				new NotificationSender(it.next(), sender.getNickname(), NotificationSender.JOIN);
//			}
//		}
	}
}