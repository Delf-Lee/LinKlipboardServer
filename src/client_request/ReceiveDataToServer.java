package client_request;

import java.io.IOException;
import java.io.PrintWriter;

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
import sun.awt.image.ImageWatched.Link;

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
		
		TransferManager sender = new TransferManager(targetGroup, client);
		sender.createSendThread(); // ���� ������ ����
		
		PrintWriter out = response.getWriter();
		sendRespond(sender, out); // ���� ���
	}
	
	/** �������� ������ ���� �� ���� ���� ��� */
	public void sendRespond(TransferManager receiver, PrintWriter out) {
		//Timer timer = new Timer(5); // 5�� Ÿ�̸�
		
		while (!receiver.isConnected()) {
//			if (!timer.isAlive()) {
//				out.println(LinKlipboard.ERROR_SOCKET_CONNECTION); // ����: ���� ��� ����
//				return;
//			}
		}
		out.println(LinKlipboard.READY_TO_TRANSFER); // ������ ���� �غ� ��
	}
}
