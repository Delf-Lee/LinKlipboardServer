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

/** 클라이언트가 서버에게 데이터를 보낼 때, 이 서블릿이 호출되어 실행된다.
 * 서버는 수신 스레드를 생성하여 클라이언트로부터 데이터를 받는다. */
@WebServlet("/SendDataToServer")
public class RequestReceive extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public RequestReceive() {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 데이터 수신
		String groupName = request.getParameter("groupName"); // 클라이언트의 그룹이름
		String fileName = request.getParameter("fileName"); // 클라이언트가 보낼 파일명
		String ipAddr = request.getRemoteAddr();
		
		
		LinKlipboardGroup targetGroup = LinKlipboardServer.getGroup(groupName); // 그룹 객체 가져옴
		ClientHandler client = targetGroup.searchClient(ipAddr); // 그룹에서 클라이언트 특정
		System.out.println(client.getNickname() + "이 fileName " + fileName + "를 서버에게 송신 시도");
		
		PrintWriter out = response.getWriter(); // 스트림 가져옴

		Transfer receiver; // 데이터 수신 스레드
		int sirialNo = targetGroup.getNextSerialNo();
		
		// 1. 파일 요청
		if (fileName != null) {
			if (!fileName.contains(".")) { // 오류: 파일이 아님
				out.println(LinKlipboard.ERROR_NOT_SUPPORTED);
			}
			receiver = new FileReceiver(targetGroup, client, fileName); // 파일 수신 스레드 생성
			sendRespond(receiver, out, sirialNo);
		}
		// 2. 문자열 or 이미지
		else {
			System.out.println("#########################################################");
			receiver = new ContentsReceiver(targetGroup, client); // 객체 수신 스레드 생성
			sendRespond(receiver, out, sirialNo); // 응답 대기
		}
	}

	/** 서버에서 소켓이 열릴 때 까지 응답 대기 */
	private void sendRespond(Transfer receiver, PrintWriter out, int serialNo) {
		String sendMsg = null;
		while (!receiver.isReady()) {
			System.out.println("준비 안됨");
		}
		sendMsg = LinKlipboard.READY_TO_TRANSFER + LinKlipboard.SEPARATOR;
		sendMsg += "serialNo" + LinKlipboard.SEPARATOR + serialNo;
		System.out.println("응답보냄: " + sendMsg);
		out.println(sendMsg);
	}
}
