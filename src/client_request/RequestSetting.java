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
import surpport.Logger;

@WebServlet("/ChangeSettingOfClient")
public class RequestSetting extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public RequestSetting() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String ipAddr = request.getRemoteAddr(); // ip 주소 추출

		System.out.println("닉네임 변경 요청");
		String groupName = request.getParameter("groupName"); // 그룹 이름 추출
		String nickname = request.getParameter("nickname"); // 설정 종류 추출
		String ip = request.getRemoteAddr();
		System.out.println("변경될 닉네임: " + nickname);

		PrintWriter out = response.getWriter();
		LinKlipboardGroup group = LinKlipboardServer.getGroup(groupName); // 그룹 객체 가져옴
		ClientHandler client = LinKlipboardServer.enterClient(ip);

		int respond = LinKlipboard.COMPLETE_APPLY;
		// 닉네임 중복 확인
		if (group.isNicknameUsable(nickname)) {

			client.setNickname(nickname); // 닉네임 변경
			System.out.println("닉네임 변경됨");
			group.joinGroup(client); // 그룹에 추가
			System.out.println("그룹에 추가");

			if (group.isChief(client)) {
				out.println(LinKlipboard.COMPLETE_APPLY);
				return;
			}
			else {
				group.notificateJoinClient(client); // 다른 클라이언트에게 접속을 알림
				Transfer sender = new HisrotySender(group, client); // 전체 히스토리 데이터 전송 스레드 생성
				respond = sendRespond(sender, client, group.createDefaultNickname()); // 스레드 내에서 소켓이 열릴 때 까지 응답 대기
				Logger.logJoinClient(client); // 로깅
			}
		}
		else {
			respond = LinKlipboard.ERROR_DUPLICATED_NICKNAME;
			System.out.println("중복");
		}

		out.println(respond);
	}

	/** 서버에서 소켓이 열릴 때 까지 응답 대기 */
	private int sendRespond(Transfer sender, ClientHandler client, String nickname) {
		Timer timer = new Timer(5); // 5초 타이머
		while (!sender.isReady()) {
			if (!timer.isAlive()) {
				// 응답 = 오류 코드
				return LinKlipboard.ERROR_SOCKET_CONNECTION;
			}
		}

		// 응답 = 허가코드 + 닉네임 + 포트번호
		int response = LinKlipboard.ACCESS_PERMIT;

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
