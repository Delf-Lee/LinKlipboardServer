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
		// response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("닉네임 변경 요청***************************************************");
		String groupName = request.getParameter("groupName"); // 그룹 이름 추출
		String nickname = request.getParameter("nickname"); // 설정 종류 추출
		String ip = request.getRemoteAddr();
		System.out.println("변경될 닉네임: " + nickname);
		System.out.println(request.getRemotePort());

		//PrintWriter out = response.getWriter();
		LinKlipboardGroup group = LinKlipboardServer.getGroup(groupName); // 그룹 객체 가져옴
		// ClientHandler client = LinKlipboardServer.enterClient(ip);

		// 닉네임을 설정할 클라이언트 지정
		ClientHandler client = LinKlipboardServer.enterClient(ip); // 그룹원
		if (client == null) {
			client = group.searchClient(ip); // 그룹장
		}

		int respond = LinKlipboard.COMPLETE_APPLY;
		// 닉네임 중복 확인
		if (group.isNicknameUsable(nickname)) {
			System.out.println("닉네임------------------------------------- " + nickname);
			System.out.println(group);
			client.setNickname(nickname); // 닉네임 변경

			group.joinGroup(client); // 그룹에 추가
			System.out.println(group.size());
			
			//if (group.isChief(client)) {
			//	System.out.println("지금 온사람 방장임");
			//out.println(LinKlipboard.COMPLETE_APPLY);
			//}
			
			group.notificateJoinClient(client); // 다른 클라이언트에게 접속을 알림
			System.out.println("이닛데이터 송신한다!");
			Transfer sender = new HisrotySender(group, client); // 전체 히스토리 데이터 전송 스레드 생성
	//		respond = sendRespond(sender, client, group.createDefaultNickname()); // 스레드 내에서 소켓이 열릴 때 까지 응답 대기
			Logger.logJoinClient(client);
		}
		else { // 중복
			respond = LinKlipboard.ERROR_DUPLICATED_NICKNAME;
			System.out.println("중복");
		}
		System.out.println("닉네임 변경시 클라이언트에게 다음과 같은 응답을 보냄: " + respond);
		//out.println(respond);
		//out.close();
		
	}

	/** 서버에서 소켓이 열릴 때 까지 응답 대기 */
//	private int sendRespond(Transfer sender, ClientHandler client, String nickname) {
//		while (!sender.isReady()) {
//		}
//		System.out.println("응답 보냄" + LinKlipboard.COMPLETE_APPLY);
//		int response = LinKlipboard.COMPLETE_APPLY;
//		return response;
//	}
}