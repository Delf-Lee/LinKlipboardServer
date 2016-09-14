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

@WebServlet("/CreateGroup")
public class CreateGroup extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public CreateGroup() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String groupName = request.getParameter("groupName");
		String password = request.getParameter("password");

		String respone = null;

		// 1. 서버 수 체크
		if (LinKlipboardServer.isFull()) {
			respone = Integer.toString(LinKlipboard.ERROR_FULL_GROUP); // 오류: 정원 초과
		}
		else {
			// 2. 그룹 이름 체크
			if (LinKlipboardServer.isExistGroup(groupName)) { // 그룹 이름 비교
				respone = Integer.toString(LinKlipboard.ERROR_DUPLICATED_GROUPNAME); // 오류: 중복된 그룹 이름
			}
			else {
				ClientHandler cheif = new ClientHandler(request, groupName); // 방장(이하 치프) 생성
				LinKlipboardGroup group = new LinKlipboardGroup(groupName, password, cheif); // 그룹 생성
				
				// 응답: 허가코드 + 닉네임
				respone = LinKlipboard.ACCESS_PERMIT + LinKlipboard.SEPARATOR + LinKlipboardGroup.DEFAULT_CHIEF_NAME; 
			}
		}
		PrintWriter out = response.getWriter();
		out.println(respone);
	}
}
