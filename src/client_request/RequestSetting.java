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
		String ipAddr = request.getRemoteAddr(); // ip �ּ� ����
		
		System.out.println("�г��� ���� ��û");
		String groupName = request.getParameter("groupName"); // �׷� �̸� ����
		String nickname = request.getParameter("nickname"); // ���� ���� ����
		System.out.println("����� �г���: " + nickname);
		LinKlipboardGroup targetGroup = LinKlipboardServer.getGroup(groupName); // �׷� ��ü ������
		ClientHandler client = targetGroup.searchClient(ipAddr); // �׷쿡�� Ŭ���̾�Ʈ Ư��
		
		int respond = LinKlipboard.COMPLETE_APPLY;
		// �г��� �ߺ� Ȯ��
		if (targetGroup.isNicknameUsable(nickname)) {
			client.setNickname(nickname); // �г��� ����
			System.out.println("�г��� �����");
		}
		else {
			respond = LinKlipboard.ERROR_DUPLICATED_NICKNAME;
			System.out.println("�ߺ�");
		}
		
		PrintWriter out = response.getWriter();
		out.println(respond);
	}
}