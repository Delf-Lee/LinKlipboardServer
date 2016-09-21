package server_manager;

import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

import org.omg.PortableInterceptor.ClientRequestInfoOperations;

import com.sun.jndi.url.corbaname.corbanameURLContextFactory;
import com.sun.security.ntlm.Client;

import contents.Contents;
import datamanage.History;

public class LinKlipboardGroup {

	private String groupName;
	private String groupPassword;

	private ClientHandler chief; // 방장
	private Hashtable<String, ClientHandler> clients;
	//private Vector<ClientHandler> clients;
	private History history; // 이 그룹의 히스토리
	private Contents lastContents; // 최근 컨텐츠

	public static final String DEFAULT_CHIEF_NAME = "Chief";
	public static final String DEFAULT_CREW_NAME = "Crew";

	private static String fileSaveDir = "C:\\Program Files\\LinKlipboardServer";

	/** 새로운 그룹을 생성
	 * @param groupName 이 그룹의 이름
	 * @param password 이 그룹에 접속하기 위한 패스워드
	 * @param chief 그룹장의 클라이언트 핸들러*/
	public LinKlipboardGroup(String groupName, String password, ClientHandler chief) {
		this.groupName = groupName;
		this.groupPassword = password;
		this.chief = chief;

		//clients = new Vector<ClientHandler>(LinKlipboard.MAX_CLIENT - 1);
		clients = new Hashtable<String, ClientHandler>(LinKlipboard.MAX_CLIENT - 1); // <ip, client>
		joinGroup(chief);
		chief.setNickname(DEFAULT_CHIEF_NAME);
		createFileReceiveFolder();
	}

	/** 새 클라이언트를 그룹에 추가한다. 
	 * @param newClient 그룹에 추가할 클라이언트의 핸들러 
	 * @return 그룹원의 수가 MAX_CLIENT미만일 경우 true 그렇지 않으면 false. */
	public void joinGroup(ClientHandler newClient) {
		// 클라이언트를 추가
		setDefaultNickname(newClient);
		clients.put(newClient.getRemoteAddr(), newClient);
	}

	/** 그룹 내에서 각 클라이언트에게 중복되지 않은 기본 닉네임을 부여한다. 
	 * @param newClient  설정할 클라이언트의 핸들러 */
	public void setDefaultNickname(ClientHandler newClient) {
		for (int i = 0;; i++) {
			if (!clients.containsKey(DEFAULT_CREW_NAME + i + 1)) {
				newClient.setNickname(DEFAULT_CREW_NAME + i + 1);
				return;
			}
		}
	}

	/** 사용자가 새로 설정한 닉네임이 그룹 내에서 중복인지 확인한다. 
	 * @return 사용 가능한 닉네임이면 true 그렇지 않으면 false */
	public boolean checkNickname(String nickname) {
		return !(clients.containsKey(nickname));
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

	public void distributeData(int senderIndex, Contents data) {
		// 다른 클라이언트에게 데이터를 뿌림 
	}

	public void reciveData() {
		// 데이터를 받아서 히스토리에 저장
	}

	/** @return 그룹 이름*/
	public String getName() {
		return groupName;
	}

	public void sendNotification(ClientHandler sender /*또는 클라이언트의 인덱스*/) {
		// sender를 제외한 모든 클라이언트에게 알림 전송
	}

	/** @return 현재 그룹원이 최대 그룹원 이상이면 true 아니면 false */
	public boolean isFull() {
		return (clients.size() > LinKlipboard.MAX_CLIENT);
	}

	public void setLastContents(Contents contents) {
		this.lastContents = contents;
		setHistory();
	}

	private void setHistory() {

	}

	public Contents getLastContents() {
		return lastContents;
	}

	public ClientHandler searchClient(String ipAddr) {
		return clients.get(ipAddr);
	}

	public String getNickname(String ipAddr) {
		if (clients.containsKey(ipAddr)) {
			return clients.get(ipAddr).getNickname();
		}
		return null;
	}

	public boolean isDuplicatedIpAddr(String ip) {
		return clients.containsKey(ip);
	}

	/** 그룹에서 공유될 파일을 저장할 폴더를 생성한다. */
	private void createFileReceiveFolder() {
		File fileReceiveFolder = new File(getFileDir());

		if (!fileReceiveFolder.exists()) {
			fileReceiveFolder.mkdir(); // 폴더 생성
		}
		else {
			initDir(fileReceiveFolder);
		}
	}

	public String getFileDir() {
		return (fileSaveDir + "\\" + groupName);
	}

	private void initDir(File d) {
		File dir = new File(fileSaveDir);
		File[] innerFile = dir.listFiles(); // 폴더 내 존재하는 파일을 innerFile에 넣음
		for (File file : innerFile) { // innerFile의 크기만큼 for문을 돌면서
			file.delete(); // 파일 삭제
			System.out.println("C:\\Program Files\\LinKlipboard폴더 안의 파일 삭제");
		}
		// Dir안에 파일이 하나만 있는 경우에 사용 가능
		// innerFile[0].delete();  
	}

	public void destroyGroup() {
		File fileReceiveFolder = new File(getFileDir()); // 그룹 공유 폴더 객체 가져옴
		initDir(fileReceiveFolder); // 폴더 내 파일 모두 삭제
		fileReceiveFolder.delete(); // 폴더 삭제
	}

	class Notification extends Thread {
		public void run() {
			//  모든 클라이언트에게 알림 송신
		}
	}
}