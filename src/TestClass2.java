import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class TestClass2 {

	public static void main(String[] args) {
		ServerSocket listener = null;
		Socket socket = null;

		String ipAddr;
		int portNum = 5957;

		ObjectOutputStream objectOutput;
		ObjectInputStream objectInput;

		try {
			System.out.println("Ŭ���̾�Ʈ ���� ���� �����");
			// ���� ���� ����

			socket = new Socket("localhost", portNum);

			// ��Ʈ�� ����
			objectOutput = new ObjectOutputStream(socket.getOutputStream());
			objectInput = new ObjectInputStream(socket.getInputStream());

			String receiveData = (String) objectInput.readObject();
			System.out.println("���� ������ : " + receiveData);

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
