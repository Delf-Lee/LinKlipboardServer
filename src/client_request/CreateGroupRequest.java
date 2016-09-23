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
import client_request.JoinGroupRequest.Timer;
import server_manager.ClientHandler;
import server_manager.LinKlipboard;
import server_manager.LinKlipboardGroup;
import server_manager.LinKlipboardServer;
import server_manager.Logger;

@WebServlet("/CreateGroup")
public class CreateGroupRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final int GROUP_NAME = 0;
	private static final int PASSWORD = 1;

	public CreateGroupRequest() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String groupName = request.getParameter("groupName");
		String password = request.getParameter("password");

		String respondMsg = null;
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
			respondMsg = Integer.toString(LinKlipboard.ERROR_FULL_GROUP); // ����: ���� �ʰ�
		}
		else {
			// 2. �׷� �̸� üũ
			if (LinKlipboardServer.isExistGroup(groupName)) { // �׷� �̸� ��
				respondMsg = Integer.toString(LinKlipboard.ERROR_DUPLICATED_GROUPNAME); // ����: �ߺ��� �׷� �̸�
			}
			else {
				ClientHandler cheif = new ClientHandler(request, groupName); // ����(���� ġ��) ����
				LinKlipboardGroup group = new LinKlipboardGroup(groupName, password, cheif); // �׷� ����
				LinKlipboardServer.createGroup(group);

				respondMsg = LinKlipboard.READY_TO_TRANSFER + LinKlipboard.SEPARATOR; // �㰡 �ڵ�
				respondMsg += "nickname" + LinKlipboard.SEPARATOR + LinKlipboardGroup.DEFAULT_CHIEF_NAME; // �г���
				Logger.logCreateGroup(cheif);
			}
		}

		// ����
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
		response += "nickname" + LinKlipboard.SEPARATOR + LinKlipboardGroup.DEFAULT_CHIEF_NAME; // �г���
		
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
