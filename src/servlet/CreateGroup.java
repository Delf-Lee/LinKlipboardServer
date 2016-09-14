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

		// 1. ���� �� üũ
		if (LinKlipboardServer.isFull()) {
			respone = Integer.toString(LinKlipboard.ERROR_FULL_GROUP); // ����: ���� �ʰ�
		}
		else {
			// 2. �׷� �̸� üũ
			if (LinKlipboardServer.isExistGroup(groupName)) { // �׷� �̸� ��
				respone = Integer.toString(LinKlipboard.ERROR_DUPLICATED_GROUPNAME); // ����: �ߺ��� �׷� �̸�
			}
			else {
				ClientHandler cheif = new ClientHandler(request, groupName); // ����(���� ġ��) ����
				LinKlipboardGroup group = new LinKlipboardGroup(groupName, password, cheif); // �׷� ����
				
				// ����: �㰡�ڵ� + �г���
				respone = LinKlipboard.ACCESS_PERMIT + LinKlipboard.SEPARATOR + LinKlipboardGroup.DEFAULT_CHIEF_NAME; 
			}
		}
		PrintWriter out = response.getWriter();
		out.println(respone);
	}
}
