package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import datamanage.TransferManager;
import server_manager.ClientHandler;
import server_manager.LinKlipboard;
import server_manager.LinKlipboardGroup;
import server_manager.LinKlipboardServer;

@WebServlet("/SendDataToServer")
public class SendDataToServer extends HttpServlet {
	private static final long serialVersionUID = 1L;

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
		String tmp = request.getParameter("type");

		int type = Integer.parseInt(request.getParameter("type"));
		String ipAddr = request.getRemoteAddr();

		LinKlipboardGroup targetGroup = LinKlipboardServer.getGroup(groupName); // �׷� ��ü ������
		ClientHandler client = targetGroup.searchClient(ipAddr); // �׷쿡�� Ŭ���̾�Ʈ Ư��
		TransferManager receiver = new TransferManager(targetGroup, client, type);
		receiver.createReceiveThread(response.getWriter()); // ���� ������ ����
		PrintWriter out = response.getWriter();
		sendRespond(receiver, out);
	}

	public void sendRespond(TransferManager receiver, PrintWriter out) {
		while (!receiver.isConnected()) {
			System.out.println("test loop");
		}
		out.println(LinKlipboard.READY_TO_TRANSFER);
	}
}
