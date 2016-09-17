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

@WebServlet("/JoinGroup")
public class JoinGroup extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public JoinGroup() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Hrer is joinGroup servlet URL").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ���� ����
		String[] info = request.getParameter("info").split(":");
		String groupName = info[0];
		String password = info[1];

		String respone = null;

		// 1. �׷� ã��
		if (!LinKlipboardServer.isExistGroup(groupName)) {
			respone = Integer.toString(LinKlipboard.ERROR_NO_MATCHED_GROUPNAME); // ����: �׷��̸� ���� �Ұ�
		}
		else {
			LinKlipboardGroup group = LinKlipboardServer.getGroup(groupName); // �׷� ��ü ������
			// 2. �ο� üũ
			if (group.isFull()) {
				respone = Integer.toString(LinKlipboard.ERROR_FULL_CLIENT); // ����: ���� �ʰ�
			}
			else {
				// 3. �н����� üũ
				if (!group.isPasswordCorrected(password)) { // �׷��� �н����� ��ġ Ȯ��
					respone = Integer.toString(LinKlipboard.ERROR_PASSWORD_INCORRECT); // ����: �н����� ����ġ
				}
				else {
					ClientHandler newClient = new ClientHandler(request, groupName); // Ŭ���̾�Ʈ ����
					group.joinGroup(newClient); // �׷쿡 �߰�
					// ����: �㰡�ڵ� + �⺻ �г���
					respone = LinKlipboard.ACCESS_PERMIT + LinKlipboard.SEPARATOR + newClient.getNickname();
					Logger.logJoinClient(newClient);
				}
			}
		}

		PrintWriter out = response.getWriter();
		out.println(respone);
	}
}