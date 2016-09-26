package client_request;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Transferer.ContentsSender;
import Transferer.FileSender;
import Transferer.HisrotySender;
import Transferer.Transfer;
import contents.Contents;
import contents.FileContents;
import server_manager.ClientHandler;
import server_manager.LinKlipboard;
import server_manager.LinKlipboardGroup;
import server_manager.LinKlipboardServer;

/** Ŭ���̾�Ʈ�� �������� ��û�� �� ȣ��ȴ�.
 * ������ �۽� �����带 �����Ͽ� Ŭ���̾�Ʈ���� Contents ��ü�� �����Ѵ�.*/
@WebServlet("/ReceiveDataToServer")
public class RequestClipboardData extends HttpServlet {
	// private static final long serialVersionUID = 1L;

	public RequestClipboardData() {
		super();
	}

	@Override
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		response.setContentType("text/plain;charset=UTF-8");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ������ ����
		String groupName = request.getParameter("groupName"); // Ŭ���̾�Ʈ�� ���� �׷�
		String serialNo = request.getParameter("seiralNo"); // Ŭ���̾�Ʈ�� ��û�� �����丮�� �ø���ѹ�
		String ipAddr = request.getRemoteAddr(); // Ŭ���̾�Ʈ�� ip

		LinKlipboardGroup targetGroup = LinKlipboardServer.getGroup(groupName); // �׷� ��ü ������
		ClientHandler client = targetGroup.searchClient(ipAddr); // �׷쿡�� Ŭ���̾�Ʈ Ư��

		Transfer sender = null; // �۽� ������
		Contents contents = targetGroup.getLastContents(); // ������ ��ü
		String fileName = "";
		String responeMsg;

		PrintWriter out = response.getWriter();
		// 1. �ֽ� ������ ��û
		if (serialNo == null) {
			// 1-A. ����
			switch (targetGroup.getLastContents().getType()) {
			case LinKlipboard.FILE_TYPE:
				sender = new FileSender(targetGroup, client); // 1-A. ���� �۽� ������ ����
				fileName = ((FileContents) contents).getFileName(); // Ŭ���̾�Ʈ���� �˷��� ���ϸ�
				break;

			// 1-B. ��Ʈ�� or �̹���
			case LinKlipboard.STRING_TYPE:
			case LinKlipboard.IMAGE_TYPE:
				sender = new ContentsSender(targetGroup, client); // 1-B. ��ü �۽� ������ ����
				break;
			default:
				responeMsg = Integer.toString(LinKlipboard.ERROR_NOT_SUPPORTED); // �������� �ʴ� ����
				out.print(responeMsg);
				return;
			}
		}
		// 2. �����丮 ��û
		else {
			sender = new HisrotySender(targetGroup, client, serialNo); // 2. �����丮 �۽� ������ ����
		}

		sendRespond(sender, out, fileName); // ���� ���
	}

	/** �������� ������ ���� �� ���� ���� ��� */
	public void sendRespond(Transfer receiver, PrintWriter out, String fileName) {
		while (!receiver.isReady()) {
		}
		out.println(LinKlipboard.READY_TO_TRANSFER + LinKlipboard.SEPARATOR + fileName); // ������ ���� �غ�
	}

}
