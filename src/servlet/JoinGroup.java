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
				

		int respone = LinKlipboard.NULL;

		if (!LinKlipboardServer.isExistGroup(groupName)) { // 그룹 이름 비교
			respone = LinKlipboard.ERROR_NO_MATCHED_GROUPNAME;
		}
		else {
			LinKlipboardGroup group = LinKlipboardServer.getGroup(groupName); // 그룹 객체 가져옴
			if (group.isPasswordCorrected(password)) { // 그룹의 패스워드 일치 확인
				respone = LinKlipboard.ERROR_PASSWORD_INCORRECT;
			}
			else {
				ClientHandler newClient = new ClientHandler(request, groupName); // 클라이언트 생성
				group.joinGroup(newClient); // 그룹에 추가
				respone = LinKlipboard.ACCESS_PERMIT;
			}
		}

		PrintWriter out = response.getWriter();
		out.println(respone);
	}
}