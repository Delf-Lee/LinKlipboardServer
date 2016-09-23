package client_request;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Transferer.ContentsReceiver;
import Transferer.FileReceiver;
import Transferer.Transfer;
import server_manager.ClientHandler;
import server_manager.LinKlipboard;
import server_manager.LinKlipboardGroup;
import server_manager.LinKlipboardServer;

/** 클라이언트가 서버에게 데이터를 보낼 때, 이 서블릿이 호출되어 실행된다. */
@WebServlet("/SendDataToServer")
public class ReceiveDataRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ReceiveDataRequest() {
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
		System.out.println("- client trying uploading... group name; " + groupName + ", file name: " + fileName);
		String ipAddr = request.getRemoteAddr();
		System.out.println("요청한 클라이언트의 ip: " + ipAddr);
		LinKlipboardGroup targetGroup = LinKlipboardServer.getGroup(groupName); // 그룹 객체 가져옴
		ClientHandler client = targetGroup.searchClient(ipAddr); // 그룹에서 클라이언트 특정
		System.out.println("ip를 기반으로 찾은 핸들러 내용 확인 / 핸들러 안에 ip: " + client.getRemoteAddr());
		PrintWriter out = response.getWriter(); // 스트림 가져옴

		Transfer receiver; // 데이터를 받을 스레드

		if (fileName != null) { // 받을 데이터가 파일인 경우
			if(!fileName.contains(".")) {
				out.println(LinKlipboard.ERROR_NOT_SUPPORTED);
			}
			receiver = new FileReceiver(targetGroup, client, fileName);
			sendRespond(receiver, out);
		}
		else { // 받을 데이터가 컨텐츠인 경우
			receiver = new ContentsReceiver(targetGroup, client);
			sendRespond(receiver, out); // 응답 대기
		}
	}

	/** 서버에서 소켓이 열릴 때 까지 응답 대기 */
	private void sendRespond(Transfer receiver, PrintWriter out) {
		Timer timer = new Timer(5); // 5초 타이머

		while (!receiver.isReady()) {
			if (!timer.isAlive()) {
				out.println(LinKlipboard.ERROR_SOCKET_CONNECTION); // 오류: 소켓 통신 오류
				return;
			}
		}
		out.println(LinKlipboard.READY_TO_TRANSFER);
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
