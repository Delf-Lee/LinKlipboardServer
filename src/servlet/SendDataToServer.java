package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import contents.Contents;
import datamanage.TransferManager;
import server_manager.ClientHandler;
import server_manager.LinKlipboardGroup;
import server_manager.LinKlipboardServer;

@WebServlet("/SendDataToServer")
public class SendDataToServer extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SendDataToServer() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 데이터 수신
		String groupName = request.getParameter("groupName");
		String nickname = request.getParameter("nickname");
		int type = Integer.parseInt(request.getParameter("type"));
		String ipAddr = request.getRemoteAddr();

		LinKlipboardGroup targetGroup = LinKlipboardServer.getGroup(groupName); // 그룹 객체 가져옴
		ClientHandler client = targetGroup.searchClient(nickname); // 그룹에서 클라이언트 특정
		new TransferManager(targetGroup, client, type).createReceiveThread(); // 수신 스레드 생성
	}
}
