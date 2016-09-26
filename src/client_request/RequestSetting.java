package client_request;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import server_manager.ClientHandler;
import server_manager.LinKlipboard;
import server_manager.LinKlipboardGroup;
import server_manager.LinKlipboardServer;

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
		System.out.println("변경될 닉네임: " + nickname);
		LinKlipboardGroup targetGroup = LinKlipboardServer.getGroup(groupName); // 그룹 객체 가져옴
		ClientHandler client = targetGroup.searchClient(ipAddr); // 그룹에서 클라이언트 특정
		
		int respond = LinKlipboard.COMPLETE_APPLY;
		// 닉네임 중복 확인
		if (targetGroup.isNicknameUsable(nickname)) {
			client.setNickname(nickname); // 닉네임 변경
			System.out.println("닉네임 변경됨");
		}
		else {
			respond = LinKlipboard.ERROR_DUPLICATED_NICKNAME;
			System.out.println("중복");
		}
		
		PrintWriter out = response.getWriter();
		out.println(respond);
	}
}