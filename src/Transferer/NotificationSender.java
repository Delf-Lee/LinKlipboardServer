//package Transferer;
//
//import java.io.BufferedWriter;
//import java.io.IOException;
//import java.io.OutputStreamWriter;
//import java.net.Socket;
//import java.net.UnknownHostException;
//
//import server_manager.ClientHandler;
//import server_manager.LinKlipboard;
//import server_manager.LinKlipboardGroup;
//
//public class NotificationSender extends Transfer {
//	BufferedWriter out ;
//	private String nickname;
//	private String ip;
//	
//	private int state;
//	public static final int JOIN = 0;
//	public static final int EXIT = 1;
//	
//	public NotificationSender(LinKlipboardGroup group, ClientHandler client) {
//		super(group, client);
//	}
//	
//	public NotificationSender(String ip, String nickname, int state) {
//		this(null, null);
//		this.ip = ip;
//		this.state = state;
//		this.nickname = nickname; 
//		this.start();
//	}
//	
//		@Override
//		public void setConnection() {
//			try {
//				// 소켓 접속 설정
//				socket = new Socket(ip, client.getRemotePort());
//				// 스트림 설정
//				 out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//
//			} catch (UnknownHostException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//		@Override
//		public void closeSocket() {
//			try {
//				out.close();
//				socket.close();
//				listener.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	
//	@Override
//	public void run() {
//		String sendMsg = null;
//		try {
//			switch (state) {
//			case JOIN:
//				sendMsg = "join"+ LinKlipboard.SEPARATOR + nickname;
//				break;
//			case EXIT:
//				sendMsg = "exit" + LinKlipboard.SEPARATOR + nickname;
//			default:
//				break;
//			}
//			out.write(nickname);
//			out.flush();
//			
//			// TODO: 응답받고 예외처리
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//}