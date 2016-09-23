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

		String respond = null;
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
			respond = Integer.toString(LinKlipboard.ERROR_FULL_GROUP); // 오류: 정원 초과
		}
		else {
			// 2. 그룹 이름 체크
			if (LinKlipboardServer.isExistGroup(groupName)) { // 그룹 이름 비교
				respond = Integer.toString(LinKlipboard.ERROR_DUPLICATED_GROUPNAME); // 오류: 중복된 그룹 이름
			}
			else {
				ClientHandler cheif = new ClientHandler(request, groupName); // 방장(이하 치프) 생성
				LinKlipboardGroup group = new LinKlipboardGroup(groupName, password, cheif); // 그룹 생성
				LinKlipboardServer.createGroup(group);

				// 응답: 허가코드 + 닉네임
				respond = LinKlipboard.ACCESS_PERMIT + LinKlipboard.SEPARATOR + LinKlipboardGroup.DEFAULT_CHIEF_NAME;
				Logger.logCreateGroup(cheif);
			}
		}

		// 전송
		out.println(respond);
	}
}
