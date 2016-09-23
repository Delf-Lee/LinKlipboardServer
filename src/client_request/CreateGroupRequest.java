package client_request;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Transferer.HisrotySender;
import Transferer.Transfer;
import client_request.JoinGroupRequest.Timer;
import server_manager.ClientHandler;
import server_manager.LinKlipboard;
import server_manager.LinKlipboardGroup;
import server_manager.LinKlipboardServer;
import server_manager.Logger;

@WebServlet("/CreateGroup")
public class CreateGroupRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int GROUP_NAME = 0;
	private static final int PASSWORD = 1;

	public CreateGroupRequest() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String groupName = request.getParameter("groupName");
		String password = request.getParameter("password");

		String respondMsg = null;
		PrintWriter out = response.getWriter();

		// 중복 ip 체크
		//		if (LinKlipboardServer.checkDuplicatedIpAddr(request.getRemoteAddr())) {
		//			respond = Integer.toString(LinKlipboard.ERROR_DUPLICATED_IP);
		//			out.println(respond);
		//			System.out.println("중복ip");
		//			return;
		//		}

		// 1. 서버 수 체크
		if (LinKlipboardServer.isFull()) {
			respondMsg = Integer.toString(LinKlipboard.ERROR_FULL_GROUP); // 오류: 정원 초과
		}
		else {
			// 2. 그룹 이름 체크
			if (LinKlipboardServer.isExistGroup(groupName)) { // 그룹 이름 비교
				respondMsg = Integer.toString(LinKlipboard.ERROR_DUPLICATED_GROUPNAME); // 오류: 중복된 그룹 이름
			}
			else {
				ClientHandler cheif = new ClientHandler(request, groupName); // 방장(이하 치프) 생성
				LinKlipboardGroup group = new LinKlipboardGroup(groupName, password, cheif); // 그룹 생성
				LinKlipboardServer.createGroup(group);

				respondMsg = LinKlipboard.READY_TO_TRANSFER + LinKlipboard.SEPARATOR; // 허가 코드
				respondMsg += "nickname" + LinKlipboard.SEPARATOR + LinKlipboardGroup.DEFAULT_CHIEF_NAME; // 닉네임
				Logger.logCreateGroup(cheif);
			}
		}

		// 전송
		out.println(respondMsg);
	}
	/** 서버에서 소켓이 열릴 때 까지 응답 대기 */
	private String sendRespond(Transfer sender) {
		Timer timer = new Timer(5); // 5초 타이머
		while (!sender.isReady()) {
			if (!timer.isAlive()) {
				return Integer.toString(LinKlipboard.ERROR_SOCKET_CONNECTION);
			}
		}
		String response = LinKlipboard.READY_TO_TRANSFER + LinKlipboard.SEPARATOR; // 허가 코드
		response += "nickname" + LinKlipboard.SEPARATOR + LinKlipboardGroup.DEFAULT_CHIEF_NAME; // 닉네임
		
		return response;
	}

	/** 클라이언트가 응답이 없을 떄를 대비하여 일정 시간 대기한다. */
	class Timer extends Thread {
		private int time;

		public Timer(int time) {
			this.time = time;
			this.start();
		}

		@Override
		public void run() {
			for (int i = 0; i < time; i++) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return;
		}
	}
}
