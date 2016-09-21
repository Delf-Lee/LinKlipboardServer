package client_request;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Transferer.ContentsSender;
import Transferer.FileSender;
import Transferer.Transfer;
import contents.Contents;
import contents.FileContents;
import server_manager.ClientHandler;
import server_manager.LinKlipboard;
import server_manager.LinKlipboardGroup;
import server_manager.LinKlipboardServer;

@WebServlet("/ReceiveDataToServer")
public class ReceiveDataToServer extends HttpServlet {
	//private static final long serialVersionUID = 1L;

	public ReceiveDataToServer() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ������ ����
		String groupName = request.getParameter("groupName");
		String ipAddr = request.getRemoteAddr();

		LinKlipboardGroup targetGroup = LinKlipboardServer.getGroup(groupName); // �׷� ��ü ������
		ClientHandler client = targetGroup.searchClient(ipAddr); // �׷쿡�� Ŭ���̾�Ʈ Ư��

		Transfer sender;
		Contents contents = targetGroup.getLastContents();
		String fileName = "";

		if (targetGroup.getLastContents().getType() == LinKlipboard.FILE_TYPE) {
			sender = new FileSender(targetGroup, client);
			fileName = ((FileContents) contents).getFileName();
		}
		else {
			sender = new ContentsSender(targetGroup, client);
		}

		PrintWriter out = response.getWriter();
		sendRespond(sender, out, fileName); // ���� ���
	}

	/** �������� ������ ���� �� ���� ���� ��� */
	public void sendRespond(Transfer receiver, PrintWriter out, String fileName) {
		Timer timer = new Timer(5); // 5�� Ÿ�̸�

		while (!receiver.isReady()) {
			if (!timer.isAlive()) {
				out.println(LinKlipboard.ERROR_SOCKET_CONNECTION); // ����: ���� ��� ����
				return;
			}
		}
		System.out.println(LinKlipboard.READY_TO_TRANSFER + LinKlipboard.SEPARATOR + fileName);
		out.println(LinKlipboard.READY_TO_TRANSFER + LinKlipboard.SEPARATOR + fileName); // ������ ���� �غ� ��
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
