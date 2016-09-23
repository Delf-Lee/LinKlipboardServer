package client_request;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import server_manager.ClientHandler;
import server_manager.LinKlipboardGroup;
import server_manager.LinKlipboardServer;

@WebServlet("/SettingRequest")
public class SettingRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SettingRequest() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String ipAddr = request.getRemoteAddr(); // ip 주소 추출
		
		String groupName = request.getParameter("groupName"); // 그룹 이름 추출
		String setting = request.getParameter("setting"); // 설정 종류 추출

		LinKlipboardGroup targetGroup = LinKlipboardServer.getGroup(groupName); // 그룹 객체 가져옴
		ClientHandler client = targetGroup.searchClient(ipAddr); // 그룹에서 클라이언트 특정
		
		String respond = null;
		PrintWriter out = response.getWriter();
		
		switch (setting) {
		case "nickname":
			String nickname = request.getParameter("nickname");
			client.setNickname(nickname);
			// 이미 히스토리에 박혀있는 닉네임은 어떡해?
			break;

		default:
			break;
		}
	}

}
