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
		String ipAddr = request.getRemoteAddr(); // ip �ּ� ����
		
		String groupName = request.getParameter("groupName"); // �׷� �̸� ����
		String setting = request.getParameter("setting"); // ���� ���� ����

		LinKlipboardGroup targetGroup = LinKlipboardServer.getGroup(groupName); // �׷� ��ü ������
		ClientHandler client = targetGroup.searchClient(ipAddr); // �׷쿡�� Ŭ���̾�Ʈ Ư��
		
		String respond = null;
		PrintWriter out = response.getWriter();
		
		switch (setting) {
		case "nickname":
			String nickname = request.getParameter("nickname");
			client.setNickname(nickname);
			// �̹� �����丮�� �����ִ� �г����� ���?
			break;

		default:
			break;
		}
	}

}
