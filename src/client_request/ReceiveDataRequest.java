package client_request;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Transferer.ContentsReceiver;
import Transferer.FileReceiver;
import Transferer.Transfer;
import server_manager.ClientHandler;
import server_manager.LinKlipboard;
import server_manager.LinKlipboardGroup;
import server_manager.LinKlipboardServer;

/** Ŭ���̾�Ʈ�� �������� �����͸� ���� ��, �� ������ ȣ��Ǿ� ����ȴ�. */
@WebServlet("/SendDataToServer")
public class ReceiveDataRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ReceiveDataRequest() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ������ ����
		String groupName = request.getParameter("groupName");
		String fileName = request.getParameter("fileName");
		System.out.println("- client trying uploading... group name; " + groupName + ", file name: " + fileName);
		String ipAddr = request.getRemoteAddr();
		System.out.println("��û�� Ŭ���̾�Ʈ�� ip: " + ipAddr);
		LinKlipboardGroup targetGroup = LinKlipboardServer.getGroup(groupName); // �׷� ��ü ������
		ClientHandler client = targetGroup.searchClient(ipAddr); // �׷쿡�� Ŭ���̾�Ʈ Ư��
		System.out.println("ip�� ������� ã�� �ڵ鷯 ���� Ȯ�� / �ڵ鷯 �ȿ� ip: " + client.getRemoteAddr());
		PrintWriter out = response.getWriter(); // ��Ʈ�� ������

		Transfer receiver; // �����͸� ���� ������

		if (fileName != null) { // ���� �����Ͱ� ������ ���
			if(!fileName.contains(".")) {
				out.println(LinKlipboard.ERROR_NOT_SUPPORTED);
			}
			receiver = new FileReceiver(targetGroup, client, fileName);
			sendRespond(receiver, out);
		}
		else { // ���� �����Ͱ� �������� ���
			receiver = new ContentsReceiver(targetGroup, client);
			sendRespond(receiver, out); // ���� ���
		}
	}

	/** �������� ������ ���� �� ���� ���� ��� */
	private void sendRespond(Transfer receiver, PrintWriter out) {
		Timer timer = new Timer(5); // 5�� Ÿ�̸�

		while (!receiver.isReady()) {
			if (!timer.isAlive()) {
				out.println(LinKlipboard.ERROR_SOCKET_CONNECTION); // ����: ���� ��� ����
				return;
			}
		}
		out.println(LinKlipboard.READY_TO_TRANSFER);
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
