package client_request;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Transferer.TransferManager;
import server_manager.ClientHandler;
import server_manager.LinKlipboard;
import server_manager.LinKlipboardGroup;
import server_manager.LinKlipboardServer;

/** Ŭ���̾�Ʈ�� �������� �����͸� ���� ��, �� ������ ȣ��Ǿ� ����ȴ�. */ 
@WebServlet("/SendDataToServer")
public class SendDataToServer extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final int IS_READY = 0;
	private static final int FILE_NAME = 1;

	public SendDataToServer() {
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

		String ipAddr = request.getRemoteAddr();

		LinKlipboardGroup targetGroup = LinKlipboardServer.getGroup(groupName); // �׷� ��ü ������
		System.out.println("test:" + ipAddr);
		ClientHandler client = targetGroup.searchClient(ipAddr); // �׷쿡�� Ŭ���̾�Ʈ Ư��
		
		PrintWriter out = response.getWriter();
		
		if(fileName != null) {
			
		}
		else {
			TransferManager receiver = new TransferManager(targetGroup, client);
			receiver.createReceiveThread(); // ���� ������ ����	
			sendRespond(receiver, out); // ���� ���
		}
	}

	/** �������� ������ ���� �� ���� ���� ��� */
	private void sendRespond(TransferManager receiver, PrintWriter out) {
		//Timer timer = new Timer(5); // 5�� Ÿ�̸�

		while (!receiver.isConnected()) {
			//			if (!timer.isAlive()) {
			//				out.println(LinKlipboard.ERROR_SOCKET_CONNECTION); // ����: ���� ��� ����
			//				return;
			//			}
		}
		out.println(LinKlipboard.READY_TO_TRANSFER);
	}

	private boolean isClientReady() {
		return true;
	}
	
	private String getFileName(String repone) {
		String[] info = repone.split(";");
		try {
			return info[FILE_NAME];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
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
