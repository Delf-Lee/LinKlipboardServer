package client_request;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Iterator;

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

@WebServlet("/CreateGroup")
public class CreateGroup extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int GROUP_NAME = 0;
	private static final int PASSWORD = 1;

	public CreateGroup() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String groupName = request.getParameter("groupName");
		String password = request.getParameter("password");

		String respond = null;
		PrintWriter out = response.getWriter();

		// �ߺ� ip üũ
//		if (LinKlipboardServer.checkDuplicatedIpAddr(request.getRemoteAddr())) {
//			respond = Integer.toString(LinKlipboard.ERROR_DUPLICATED_IP);
//			out.println(respond);
//			System.out.println("�ߺ�ip");
//			return;
//		}

		// 1. ���� �� üũ
		if (LinKlipboardServer.isFull()) {
			respond = Integer.toString(LinKlipboard.ERROR_FULL_GROUP); // ����: ���� �ʰ�
		}
		else {
			// 2. �׷� �̸� üũ
			if (LinKlipboardServer.isExistGroup(groupName)) { // �׷� �̸� ��
				respond = Integer.toString(LinKlipboard.ERROR_DUPLICATED_GROUPNAME); // ����: �ߺ��� �׷� �̸�
			}
			else {
				System.out.println("���� ���� ��, ipüũ: " + request.getRemoteAddr());
				ClientHandler cheif = new ClientHandler(request, groupName); // ����(���� ġ��) ����
				System.out.println("���� �ڵ鷯 ����, �ڵ鷯 ���� ipüũ: " + cheif.getRemoteAddr());
				LinKlipboardGroup group = new LinKlipboardGroup(groupName, password, cheif); // �׷� ����
				LinKlipboardServer.createGroup(group);

				// ����: �㰡�ڵ� + �г���
				respond = LinKlipboard.ACCESS_PERMIT + LinKlipboard.SEPARATOR + LinKlipboardGroup.DEFAULT_CHIEF_NAME;
				Logger.logCreateGroup(cheif);
			}
		}

		// ����
		out.println(respond);
	}
}
