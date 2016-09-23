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
public class JoinGroupRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public JoinGroupRequest() {
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
					group.joinGroup(newClient); // �׷쿡 �߰�

					Transfer sender = new HisrotySender(group, newClient);
					respondMsg = sendRespond(sender);
					Logger.logJoinClient(newClient);
				}
			}
		}
		out.println(respondMsg);
	}

	/** �������� ������ ���� �� ���� ���� ��� */
	private String sendRespond(Transfer sender) {
		Timer timer = new Timer(5); // 5�� Ÿ�̸�
		while (!sender.isReady()) {
			if (!timer.isAlive()) {
				return Integer.toString(LinKlipboard.ERROR_SOCKET_CONNECTION);
			}
		}
		String response = LinKlipboard.READY_TO_TRANSFER + LinKlipboard.SEPARATOR; // �㰡 �ڵ�
		response += "nickname" + LinKlipboard.SEPARATOR + LinKlipboardGroup.DEFAULT_CREW_NAME; // �г���
		
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