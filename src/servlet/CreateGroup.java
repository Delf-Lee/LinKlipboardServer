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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String groupName = request.getParameter("groupName");
		String password = request.getParameter("password");

		int respone = LinKlipboard.NULL;

		if (LinKlipboardServer.isExistGroup(groupName)) { // 그룹 이름 비교
			respone = LinKlipboard.ERROR_DUPLICATED_GROUPNAME;
		}
		else {
			ClientHandler cheif = new ClientHandler(request, groupName); // 방장(이하 치프) 생성
			LinKlipboardGroup group = new LinKlipboardGroup(groupName, password, cheif); // 그룹 생성
			respone = LinKlipboard.ACCESS_PERMIT;
		}

		PrintWriter out = response.getWriter();
		out.println(respone);
	}
}
