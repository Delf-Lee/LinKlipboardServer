package client_request;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import server_manager.ClientHandler;
import server_manager.LinKlipboard;
import server_manager.LinKlipboardGroup;
import server_manager.LinKlipboardServer;
import surpport.Logger;

@WebServlet("/CreateGroup")
public class RequestCreateGroup extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public RequestCreateGroup() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ClientHandler cheif = new ClientHandler(request, "");
		Logger.logCreateGroup(cheif);
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

				// ���� = �㰡�ڵ� + �г��� + ��Ʈ��ȣ
				respondMsg = LinKlipboard.ACCESS_PERMIT + LinKlipboard.SEPARATOR; // �㰡 �ڵ�
				respondMsg += "nickname" + LinKlipboard.SEPARATOR + LinKlipboardGroup.DEFAULT_CHIEF_NAME + LinKlipboard.SEPARATOR; // �г���
				respondMsg += "portNum" + LinKlipboard.SEPARATOR + cheif.getRemotePort(); // ��Ʈ��ȣ
				
				respondMsg = new String(respondMsg.getBytes("utf-8"), "utf-8");
				System.out.println(Charset.defaultCharset());
				//System.out.println("utf-8 -> euc-kr        : " + new String(word.getBytes("utf-8"), "euc-kr"));
				//respondMsg = convert(respondMsg);
				
				Logger.logCreateGroup(cheif); // �α�
			}
		}
		// ����
		out.println(respondMsg);
	}
}