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
import server_manager.ClientHandler;
import server_manager.LinKlipboard;
import server_manager.LinKlipboardGroup;
import server_manager.LinKlipboardServer;
import server_manager.Logger;

@WebServlet("/JoinGroup")
public class RequestJoinGroup extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public RequestJoinGroup() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Hrer is joinGroup servlet URL").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 정보 받음s
		String groupName = request.getParameter("groupName");
		String password = request.getParameter("password");

		String respondMsg = null;
		PrintWriter out = response.getWriter();

		// 1. 그룹 찾기
		if (!LinKlipboardServer.isExistGroup(groupName)) {
			respondMsg = Integer.toString(LinKlipboard.ERROR_NO_MATCHED_GROUPNAME); // 오류: 그룹이름 매핑 불가
		}
		else {
			LinKlipboardGroup group = LinKlipboardServer.getGroup(groupName); // 그룹 객체 가져옴
			// 2. 인원 체크
			if (group.isFull()) {
				respondMsg = Integer.toString(LinKlipboard.ERROR_FULL_CLIENT); // 오류: 정원 초과
			}
			else {
				// 3. 패스워드 체크
				if (!group.isPasswordCorrected(password)) { // 그룹의 패스워드 일치 확인
					respondMsg = Integer.toString(LinKlipboard.ERROR_PASSWORD_INCORRECT); // 오류: 패스워드 불일치
				}
				else {
					ClientHandler newClient = new ClientHandler(request, groupName); // 클라이언트 생성
					group.notificateJoinClient(newClient); // 다른 클라이언트에게 접속을 알림
					group.joinGroup(newClient); // 그룹에 추가

					Transfer sender = new HisrotySender(group, newClient); // 전체 히스토리 데이터 전송 스레드 생성
					respondMsg = sendRespond(sender, newClient,group.createDefaultNickname()); // 스레드 내에서 소켓이 열릴 때 까지 응답 대기
					Logger.logJoinClient(newClient); // 로깅
				}
			}
		}
		out.println(respondMsg); // 응답
	}

	/** 서버에서 소켓이 열릴 때 까지 응답 대기 */
	private String sendRespond(Transfer sender, ClientHandler client, String nickname) {
		Timer timer = new Timer(5); // 5초 타이머
		while (!sender.isReady()) {
			if (!timer.isAlive()) {
				// 응답 = 오류 코드
				return Integer.toString(LinKlipboard.ERROR_SOCKET_CONNECTION);
			}
		}
		
		// 응답 = 허가코드 + 닉네임 + 포트번호
		String response = LinKlipboard.ACCESS_PERMIT + LinKlipboard.SEPARATOR; // 허가 코드
		response += "nickname" + LinKlipboard.SEPARATOR  + nickname + LinKlipboard.SEPARATOR; // 닉네임
		response += "portNum" + LinKlipboard.SEPARATOR + client.getRemotePort(); // 포트번호
		
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