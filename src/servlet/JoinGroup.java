package servlet;

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
import sun.security.util.Password;

@WebServlet("/joinClient")
public class JoinGroup extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public JoinGroup() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 정보 받음
		String groupName = request.getParameter("groupName");
		String password = request.getParameter("password");
		char[] pw = Password.readPassword(request.getInputStream());

		String respone = null;

		// 1. 그룹 찾기
		if (!LinKlipboardServer.isExistGroup(groupName)) {
			respone = Integer.toString(LinKlipboard.ERROR_NO_MATCHED_GROUPNAME); // 오류: 그룹이름 매핑 불가
		}
		else {
			LinKlipboardGroup group = LinKlipboardServer.getGroup(groupName); // 그룹 객체 가져옴
			// 2. 인원 체크
			if (group.isFull()) {
				respone = Integer.toString(LinKlipboard.ERROR_FULL_CLIENT); // 오류: 정원 초과
			}
			else {
				// 3. 패스워드 체크
				if (!group.isPasswordCorrected(password)) { // 그룹의 패스워드 일치 확인
					respone = Integer.toString(LinKlipboard.ERROR_PASSWORD_INCORRECT); // 오류: 패스워드 불일치
				}
				else {
					ClientHandler newClient = new ClientHandler(request, groupName); // 클라이언트 생성
					group.joinGroup(newClient); // 그룹에 추가
					// 응답: 허가코드 + 기본 닉네임
					respone = LinKlipboard.ACCESS_PERMIT + LinKlipboard.SEPARATOR + newClient.getNickname();
					Logger.accessClient(Logger.JOIN_GROUP, newClient);
				}
			}
		}

		PrintWriter out = response.getWriter();
		out.println(respone);
	}
}