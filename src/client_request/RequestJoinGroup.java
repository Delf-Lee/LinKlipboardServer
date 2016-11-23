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

@WebServlet("/JoinGroup")
public class RequestJoinGroup extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public RequestJoinGroup() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Hrer is joinGroup servlet URL").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ���� ����
		String groupName = request.getParameter("groupName");
		String password = request.getParameter("password");
		System.out.println(groupName + ", " + password);
		String respondMsg = null;
		PrintWriter out = response.getWriter();

		// 1. �׷� ã��
		if (!LinKlipboardServer.isExistGroup(groupName)) {
			respondMsg = Integer.toString(LinKlipboard.ERROR_NO_MATCHED_GROUPNAME); // ����: �׷��̸� ���� �Ұ�
		}
		else {
			LinKlipboardGroup group = LinKlipboardServer.getGroup(groupName); // �׷� ��ü ������
			// 2. �ο� üũ
			if (group.isFull()) {
				respondMsg = Integer.toString(LinKlipboard.ERROR_FULL_CLIENT); // ����: ���� �ʰ�\
				
			}
			else {
				// 3. �н����� üũ
				if (!group.isPasswordCorrected(password)) { // �׷��� �н����� ��ġ Ȯ��
					respondMsg = Integer.toString(LinKlipboard.ERROR_PASSWORD_INCORRECT); // ����: �н����� ����ġ
				}
				else {
					ClientHandler newClient = new ClientHandler(request, groupName); // Ŭ���̾�Ʈ ����
					LinKlipboardServer.waitClient(newClient); // ��⿭�� Ŭ���̾�Ʈ �߰�
					System.out.println("����");
					respondMsg = LinKlipboard.ACCESS_PERMIT + LinKlipboard.SEPARATOR; // �㰡 �ڵ�
					respondMsg += "nickname" + LinKlipboard.SEPARATOR + group.createDefaultNickname() + LinKlipboard.SEPARATOR; // �г���
					respondMsg += "portNum" + LinKlipboard.SEPARATOR + newClient.getRemotePort(); // ��Ʈ��ȣ
				}
			}
		}
		out.println(respondMsg); // ����
	}

}