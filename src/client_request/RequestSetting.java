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
import surpport.Logger;

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
		String ip = request.getRemoteAddr();
		System.out.println("����� �г���: " + nickname);

		PrintWriter out = response.getWriter();
		LinKlipboardGroup group = LinKlipboardServer.getGroup(groupName); // �׷� ��ü ������
		ClientHandler client = LinKlipboardServer.enterClient(ip);

		int respond = LinKlipboard.COMPLETE_APPLY;
		// �г��� �ߺ� Ȯ��
		if (group.isNicknameUsable(nickname)) {

			client.setNickname(nickname); // �г��� ����
			System.out.println("�г��� �����");
			group.joinGroup(client); // �׷쿡 �߰�
			System.out.println("�׷쿡 �߰�");

			if (group.isChief(client)) {
				out.println(LinKlipboard.COMPLETE_APPLY);
				return;
			}
			else {
				group.notificateJoinClient(client); // �ٸ� Ŭ���̾�Ʈ���� ������ �˸�
				Transfer sender = new HisrotySender(group, client); // ��ü �����丮 ������ ���� ������ ����
				respond = sendRespond(sender, client, group.createDefaultNickname()); // ������ ������ ������ ���� �� ���� ���� ���
				Logger.logJoinClient(client); // �α�
			}
		}
		else {
			respond = LinKlipboard.ERROR_DUPLICATED_NICKNAME;
			System.out.println("�ߺ�");
		}

		out.println(respond);
	}

	/** �������� ������ ���� �� ���� ���� ��� */
	private int sendRespond(Transfer sender, ClientHandler client, String nickname) {
		Timer timer = new Timer(5); // 5�� Ÿ�̸�
		while (!sender.isReady()) {
			if (!timer.isAlive()) {
				// ���� = ���� �ڵ�
				return LinKlipboard.ERROR_SOCKET_CONNECTION;
			}
		}

		// ���� = �㰡�ڵ� + �г��� + ��Ʈ��ȣ
		int response = LinKlipboard.ACCESS_PERMIT;

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
