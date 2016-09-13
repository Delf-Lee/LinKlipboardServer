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
		// ���� ����
		String groupName = request.getParameter("groupName");
		String password = request.getParameter("password");
		char[] pw = Password.readPassword(request.getInputStream());
				

		int respone = LinKlipboard.NULL;

		if (!LinKlipboardServer.isExistGroup(groupName)) { // �׷� �̸� ��
			respone = LinKlipboard.ERROR_NO_MATCHED_GROUPNAME;
		}
		else {
			LinKlipboardGroup group = LinKlipboardServer.getGroup(groupName); // �׷� ��ü ������
			if (group.isPasswordCorrected(password)) { // �׷��� �н����� ��ġ Ȯ��
				respone = LinKlipboard.ERROR_PASSWORD_INCORRECT;
			}
			else {
				ClientHandler newClient = new ClientHandler(request, groupName); // Ŭ���̾�Ʈ ����
				group.joinGroup(newClient); // �׷쿡 �߰�
				respone = LinKlipboard.ACCESS_PERMIT;
			}
		}

		PrintWriter out = response.getWriter();
		out.println(respone);
	}
}