package client_request;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import server_manager.ClientHandler;
import server_manager.LinKlipboardGroup;
import server_manager.LinKlipboardServer;

@WebServlet("/ReportExit")
public class ReportExit extends HttpServlet {
       
    public ReportExit() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String groupName = request.getParameter("groupName");
		String ipAddr = request.getRemoteAddr();
		System.out.println(groupName + "�� " + ipAddr + "�� ����");
		
		LinKlipboardGroup targetGroup = LinKlipboardServer.getGroup(groupName); // �׷� ��ü ������
		ClientHandler client = targetGroup.removeClient(ipAddr); // Ŭ���̾�Ʈ �����ϰ� ��ü ������
		
		targetGroup.notificateExitClients(client);
		
		PrintWriter out = response.getWriter();
		out.println("");
	}

}
