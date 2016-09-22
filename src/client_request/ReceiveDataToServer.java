package client_request;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Transferer.ContentsSender;
import Transferer.FileSender;
import Transferer.Transfer;
import contents.Contents;
import contents.FileContents;
import server_manager.ClientHandler;
import server_manager.LinKlipboard;
import server_manager.LinKlipboardGroup;
import server_manager.LinKlipboardServer;

@WebServlet("/ReceiveDataToServer")
public class ReceiveDataToServer extends HttpServlet {
	//private static final long serialVersionUID = 1L;

	public ReceiveDataToServer() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 데이터 수신
		String groupName = request.getParameter("groupName");
		String ipAddr = request.getRemoteAddr();

		LinKlipboardGroup targetGroup = LinKlipboardServer.getGroup(groupName); // 그룹 객체 가져옴
		ClientHandler client = targetGroup.searchClient(ipAddr); // 그룹에서 클라이언트 특정

		Transfer sender;
		Contents contents = targetGroup.getLastContents();
		String fileName = "";

		if (targetGroup.getLastContents().getType() == LinKlipboard.FILE_TYPE) {
			sender = new FileSender(targetGroup, client);
			fileName = ((FileContents) contents).getFileName();
		}
		else {
			sender = new ContentsSender(targetGroup, client);
		}

		PrintWriter out = response.getWriter();
		sendRespond(sender, out, fileName); // 응답 대기
	}

	/** 서버에서 소켓이 열릴 때 까지 응답 대기 */
	public void sendRespond(Transfer receiver, PrintWriter out, String fileName) {
		Timer timer = new Timer(5); // 5초 타이머

		while (!receiver.isReady()) {
			if (!timer.isAlive()) {
				out.println(LinKlipboard.ERROR_SOCKET_CONNECTION); // 오류: 소켓 통신 오류
				return;
			}
		}
		System.out.println(LinKlipboard.READY_TO_TRANSFER + LinKlipboard.SEPARATOR + fileName);
		out.println(LinKlipboard.READY_TO_TRANSFER + LinKlipboard.SEPARATOR + fileName); // 데이터 전송 준비 ㅠ
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
