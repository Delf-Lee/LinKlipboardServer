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
			// ���� ���� ����
			listener = new ServerSocket(portNum);
			socket = listener.accept();

			ipAddr = socket.getInetAddress().getHostAddress();

			System.out.println("������ ������ �ּ�: " + socket.getInetAddress().getHostAddress());

			// ��Ʈ�� ����
			objectOutput = new ObjectOutputStream(socket.getOutputStream());
			objectInput = new ObjectInputStream(socket.getInputStream());

			objectOutput.writeObject(new String("ss")); // ��ü ����
			objectOutput.flush();
			System.out.println("���ۿϷ�");

		} catch (IOException e) {

			try {
				listener.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			System.out.println("���� ����");
			e.printStackTrace();
		}
	}
}
