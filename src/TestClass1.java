import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TestClass1 {
	public static void main(String[] args) {
		ServerSocket listener = null;
		Socket socket = null;

		String ipAddr;
		int portNum = 5957;

		ObjectOutputStream objectOutput;
		ObjectInputStream objectInput;
		try {
			// 소켓 접속 설정
			listener = new ServerSocket(portNum);
			socket = listener.accept();

			ipAddr = socket.getInetAddress().getHostAddress();

			System.out.println("접속한 아이피 주소: " + socket.getInetAddress().getHostAddress());

			// 스트림 설정
			objectOutput = new ObjectOutputStream(socket.getOutputStream());
			objectInput = new ObjectInputStream(socket.getInputStream());

			objectOutput.writeObject(new String("ss")); // 객체 전송
			objectOutput.flush();
			System.out.println("전송완료");

		} catch (IOException e) {

			try {
				listener.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			System.out.println("접속 오류");
			e.printStackTrace();
		}
	}
}
