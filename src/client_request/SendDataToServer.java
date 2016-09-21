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

/** 클라이언트가 서버에게 데이터를 보낼 때, 이 서블릿이 호출되어 실행된다. */ 
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
		// 데이터 수신
		String groupName = request.getParameter("groupName");
		String fileName = request.getParameter("fileName");

		String ipAddr = request.getRemoteAddr();

		LinKlipboardGroup targetGroup = LinKlipboardServer.getGroup(groupName); // 그룹 객체 가져옴
		System.out.println("test:" + ipAddr);
		ClientHandler client = targetGroup.searchClient(ipAddr); // 그룹에서 클라이언트 특정
		
		PrintWriter out = response.getWriter();
		
		if(fileName != null) {
			
		}
		else {
			TransferManager receiver = new TransferManager(targetGroup, client);
			receiver.createReceiveThread(); // 수신 스레드 생성	
			sendRespond(receiver, out); // 응답 대기
		}
	}

	/** 서버에서 소켓이 열릴 때 까지 응답 대기 */
	private void sendRespond(TransferManager receiver, PrintWriter out) {
		//Timer timer = new Timer(5); // 5초 타이머

		while (!receiver.isConnected()) {
			//			if (!timer.isAlive()) {
			//				out.println(LinKlipboard.ERROR_SOCKET_CONNECTION); // 오류: 소켓 통신 오류
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

	/** 클라이언트가 응답이 없을 떄를 대비하여 일정 시간 대기한다. */
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
