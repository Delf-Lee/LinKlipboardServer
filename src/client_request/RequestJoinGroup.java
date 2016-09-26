package client_request;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Transferer.HisrotySender;
import Transferer.Transfer;
import server_manager.ClientHandler;
import server_manager.LinKlipboard;
import server_manager.LinKlipboardGroup;
import server_manager.LinKlipboardServer;
import server_manager.Logger;

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
		// ���� ����s
		String groupName = request.getParameter("groupName");
		String password = request.getParameter("password");

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
				respondMsg = Integer.toString(LinKlipboard.ERROR_FULL_CLIENT); // ����: ���� �ʰ�
			}
			else {
				// 3. �н����� üũ
				if (!group.isPasswordCorrected(password)) { // �׷��� �н����� ��ġ Ȯ��
					respondMsg = Integer.toString(LinKlipboard.ERROR_PASSWORD_INCORRECT); // ����: �н����� ����ġ
				}
				else {
					ClientHandler newClient = new ClientHandler(request, groupName); // Ŭ���̾�Ʈ ����
					group.notificateJoinClient(newClient); // �ٸ� Ŭ���̾�Ʈ���� ������ �˸�
					group.joinGroup(newClient); // �׷쿡 �߰�

					Transfer sender = new HisrotySender(group, newClient); // ��ü �����丮 ������ ���� ������ ����
					respondMsg = sendRespond(sender, newClient,group.createDefaultNickname()); // ������ ������ ������ ���� �� ���� ���� ���
					Logger.logJoinClient(newClient); // �α�
				}
			}
		}
		out.println(respondMsg); // ����
	}

	/** �������� ������ ���� �� ���� ���� ��� */
	private String sendRespond(Transfer sender, ClientHandler client, String nickname) {
		Timer timer = new Timer(5); // 5�� Ÿ�̸�
		while (!sender.isReady()) {
			if (!timer.isAlive()) {
				// ���� = ���� �ڵ�
				return Integer.toString(LinKlipboard.ERROR_SOCKET_CONNECTION);
			}
		}
		
		// ���� = �㰡�ڵ� + �г��� + ��Ʈ��ȣ
		String response = LinKlipboard.ACCESS_PERMIT + LinKlipboard.SEPARATOR; // �㰡 �ڵ�
		response += "nickname" + LinKlipboard.SEPARATOR  + nickname + LinKlipboard.SEPARATOR; // �г���
		response += "portNum" + LinKlipboard.SEPARATOR + client.getRemotePort(); // ��Ʈ��ȣ
		
		return response;
	}

	/** Ŭ���̾�Ʈ�� ������ ���� ���� ����Ͽ� ���� �ð� ����Ѵ�. */
	class Timer extends Thread {
		private int time;

		public Timer(int time) {
			this.time = time;
			this.start();
		}

		@Override
		public void run() {
			for (int i = 0; i < time; i++) {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return;
		}
	}
}