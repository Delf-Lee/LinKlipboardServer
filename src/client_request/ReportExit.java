package client_request;

import java.io.IOException;
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
	private static final long serialVersionUID = 1L;
       
    public ReportExit() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String groupName = request.getParameter("groupName");
		String ipAddr = request.getRemoteAddr();
		
		LinKlipboardGroup targetGroup = LinKlipboardServer.getGroup(groupName); // 그룹 객체 가져옴
		ClientHandler client = targetGroup.removeClient(ipAddr); // 클라이언트 삭제하고 객체 가져옴
		
		targetGroup.notificateExitClients(client);
	}

}
