package client_request;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.synth.SynthSeparatorUI;

import com.sun.prism.paint.Gradient;

import Transferer.ContentsReceiver;
import Transferer.FileReceiver;
import Transferer.Transfer;
import server_manager.ClientHandler;
import server_manager.LinKlipboard;
import server_manager.LinKlipboardGroup;
import server_manager.LinKlipboardServer;

/** Ŭ���̾�Ʈ�� �������� �����͸� ���� ��, �� ������ ȣ��Ǿ� ����ȴ�.
 * ������ ���� �����带 �����Ͽ� Ŭ���̾�Ʈ�κ��� �����͸� �޴´�. */
@WebServlet("/SendDataToServer")
public class RequestReceive extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public RequestReceive() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ������ ����
		String groupName = request.getParameter("groupName"); // Ŭ���̾�Ʈ�� �׷��̸�
		String fileName = request.getParameter("fileName"); // Ŭ���̾�Ʈ�� ���� ���ϸ�
		String ipAddr = request.getRemoteAddr();
		
		
		LinKlipboardGroup targetGroup = LinKlipboardServer.getGroup(groupName); // �׷� ��ü ������
		ClientHandler client = targetGroup.searchClient(ipAddr); // �׷쿡�� Ŭ���̾�Ʈ Ư��
		System.out.println(client.getNickname() + "�� fileName " + fileName + "�� �������� �۽� �õ�");
		
		PrintWriter out = response.getWriter(); // ��Ʈ�� ������

		Transfer receiver; // ������ ���� ������
		int sirialNo = targetGroup.getNextSerialNo();
		
		// 1. ���� ��û
		if (fileName != null) {
			if (!fileName.contains(".")) { // ����: ������ �ƴ�
				out.println(LinKlipboard.ERROR_NOT_SUPPORTED);
			}
			receiver = new FileReceiver(targetGroup, client, fileName); // ���� ���� ������ ����
			sendRespond(receiver, out, sirialNo);
		}
		// 2. ���ڿ� or �̹���
		else {
			System.out.println("#########################################################");
			receiver = new ContentsReceiver(targetGroup, client); // ��ü ���� ������ ����
			sendRespond(receiver, out, sirialNo); // ���� ���
		}
	}

	/** �������� ������ ���� �� ���� ���� ��� */
	private void sendRespond(Transfer receiver, PrintWriter out, int serialNo) {
		String sendMsg = null;
		while (!receiver.isReady()) {
			System.out.println("�غ� �ȵ�");
		}
		sendMsg = LinKlipboard.READY_TO_TRANSFER + LinKlipboard.SEPARATOR;
		sendMsg += "serialNo" + LinKlipboard.SEPARATOR + serialNo;
		System.out.println("���亸��: " + sendMsg);
		out.println(sendMsg);
	}
}
