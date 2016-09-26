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

/** 클라이언트가 컨텐츠를 요청할 시 호출된다.
 * 서버는 송신 스레드를 생성하여 클라이언트에게 Contents 객체를 전송한다.*/
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
		// 데이터 수신
		String groupName = request.getParameter("groupName"); // 클라이언트가 속한 그룹
		String serialNo = request.getParameter("seiralNo"); // 클라이언트가 요청한 히스토리의 시리얼넘버
		String ipAddr = request.getRemoteAddr(); // 클라이언트의 ip

		LinKlipboardGroup targetGroup = LinKlipboardServer.getGroup(groupName); // 그룹 객체 가져옴
		ClientHandler client = targetGroup.searchClient(ipAddr); // 그룹에서 클라이언트 특정

		Transfer sender = null; // 송신 스레드
		Contents contents = targetGroup.getLastContents(); // 전송할 객체
		String fileName = "";
		String responeMsg;

		PrintWriter out = response.getWriter();
		// 1. 최신 데이터 요청
		if (serialNo == null) {
			// 1-A. 파일
			switch (targetGroup.getLastContents().getType()) {
			case LinKlipboard.FILE_TYPE:
				sender = new FileSender(targetGroup, client); // 1-A. 파일 송신 스레드 생성
				fileName = ((FileContents) contents).getFileName(); // 클라이언트에게 알려줄 파일명
				break;

			// 1-B. 스트링 or 이미지
			case LinKlipboard.STRING_TYPE:
			case LinKlipboard.IMAGE_TYPE:
				sender = new ContentsSender(targetGroup, client); // 1-B. 객체 송신 스레드 생성
				break;
			default:
				responeMsg = Integer.toString(LinKlipboard.ERROR_NOT_SUPPORTED); // 지원하지 않는 형식
				out.print(responeMsg);
				return;
			}
		}
		// 2. 히스토리 요청
		else {
			sender = new HisrotySender(targetGroup, client, serialNo); // 2. 히스토리 송신 스래드 생성
		}

		sendRespond(sender, out, fileName); // 응답 대기
	}

	/** 서버에서 소켓이 열릴 때 까지 응답 대기 */
	public void sendRespond(Transfer receiver, PrintWriter out, String fileName) {
		while (!receiver.isReady()) {
		}
		out.println(LinKlipboard.READY_TO_TRANSFER + LinKlipboard.SEPARATOR + fileName); // 데이터 전송 준비
	}

}
