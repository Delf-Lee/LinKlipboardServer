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
		// response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("�г��� ���� ��û***************************************************");
		String groupName = request.getParameter("groupName"); // �׷� �̸� ����
		String nickname = request.getParameter("nickname"); // ���� ���� ����
		String ip = request.getRemoteAddr();
		System.out.println("����� �г���: " + nickname);
		System.out.println(request.getRemotePort());

		//PrintWriter out = response.getWriter();
		LinKlipboardGroup group = LinKlipboardServer.getGroup(groupName); // �׷� ��ü ������
		// ClientHandler client = LinKlipboardServer.enterClient(ip);

		// �г����� ������ Ŭ���̾�Ʈ ����
		ClientHandler client = LinKlipboardServer.enterClient(ip); // �׷��
		if (client == null) {
			client = group.searchClient(ip); // �׷���
		}

		int respond = LinKlipboard.COMPLETE_APPLY;
		// �г��� �ߺ� Ȯ��
		if (group.isNicknameUsable(nickname)) {
			System.out.println("�г���------------------------------------- " + nickname);
			System.out.println(group);
			client.setNickname(nickname); // �г��� ����

			group.joinGroup(client); // �׷쿡 �߰�
			System.out.println(group.size());
			
			//if (group.isChief(client)) {
			//	System.out.println("���� �»�� ������");
			//out.println(LinKlipboard.COMPLETE_APPLY);
			//}
			
			group.notificateJoinClient(client); // �ٸ� Ŭ���̾�Ʈ���� ������ �˸�
			System.out.println("�̴ֵ����� �۽��Ѵ�!");
			Transfer sender = new HisrotySender(group, client); // ��ü �����丮 ������ ���� ������ ����
	//		respond = sendRespond(sender, client, group.createDefaultNickname()); // ������ ������ ������ ���� �� ���� ���� ���
			Logger.logJoinClient(client);
		}
		else { // �ߺ�
			respond = LinKlipboard.ERROR_DUPLICATED_NICKNAME;
			System.out.println("�ߺ�");
		}
		System.out.println("�г��� ����� Ŭ���̾�Ʈ���� ������ ���� ������ ����: " + respond);
		//out.println(respond);
		//out.close();
		
	}

	/** �������� ������ ���� �� ���� ���� ��� */
//	private int sendRespond(Transfer sender, ClientHandler client, String nickname) {
//		while (!sender.isReady()) {
//		}
//		System.out.println("���� ����" + LinKlipboard.COMPLETE_APPLY);
//		int response = LinKlipboard.COMPLETE_APPLY;
//		return response;
//	}
}