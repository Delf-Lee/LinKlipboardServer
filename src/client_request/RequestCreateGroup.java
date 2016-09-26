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

		// 중복 ip 체크
		//		if (LinKlipboardServer.checkDuplicatedIpAddr(request.getRemoteAddr())) {
		//			respond = Integer.toString(LinKlipboard.ERROR_DUPLICATED_IP);
		//			out.println(respond);
		//			System.out.println("중복ip");
		//			return;
		//		}

		// 1. 서버 수 체크
		if (LinKlipboardServer.isFull()) {
			respondMsg = Integer.toString(LinKlipboard.ERROR_FULL_GROUP); // 오류: 정원 초과
		}
		else {
			// 2. 그룹 이름 체크
			if (LinKlipboardServer.isExistGroup(groupName)) { // 그룹 이름 비교
				respondMsg = Integer.toString(LinKlipboard.ERROR_DUPLICATED_GROUPNAME); // 오류: 중복된 그룹 이름
			}
			else {
				ClientHandler cheif = new ClientHandler(request, groupName); // 방장(이하 치프) 생성
				LinKlipboardGroup group = new LinKlipboardGroup(groupName, password, cheif); // 그룹 생성
				LinKlipboardServer.createGroup(group);

				// 응답 = 허가코드 + 닉네임 + 포트번호
				respondMsg = LinKlipboard.ACCESS_PERMIT + LinKlipboard.SEPARATOR; // 허가 코드
				respondMsg += "nickname" + LinKlipboard.SEPARATOR + LinKlipboardGroup.DEFAULT_CHIEF_NAME + LinKlipboard.SEPARATOR; // 닉네임
				respondMsg += "portNum" + LinKlipboard.SEPARATOR + cheif.getRemotePort(); // 포트번호
				
				respondMsg = new String(respondMsg.getBytes("utf-8"), "utf-8");
				System.out.println(Charset.defaultCharset());
				//System.out.println("utf-8 -> euc-kr        : " + new String(word.getBytes("utf-8"), "euc-kr"));
				//respondMsg = convert(respondMsg);
				
				Logger.logCreateGroup(cheif); // 로깅
			}
		}
		// 전송
		out.println(respondMsg);
	}
}