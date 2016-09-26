package server_manager;

public class LinKlipboard {

	public static final int MAX_GROUP = 10; // �ִ�� ������ �� �ִ� �׷� ��
	public static final int MAX_CLIENT = 10; // �� �׷쿡 �ִ�� ������ �� �ִ� Ŭ���̾�Ʈ ��

	public final static int NULL = -1;
	public final static int ACCESS_PERMIT = 200; // ���� ����
	public final static int READY_TO_TRANSFER = 201; // ���� �غ� ��
	public final static int COMPLETE_APPLY = 202; //

	public final static int ERROR_DUPLICATED_GROUPNAME = 400; // �ߺ��� �׷� �̸�
	public final static int ERROR_NO_MATCHED_GROUPNAME = 401; // �ش� �̸��� �׷� ����
	public final static int ERROR_PASSWORD_INCORRECT = 402; // �н����� ����ġ
	public final static int ERROR_SOCKET_CONNECTION = 403; // ���� ���� ����
	public final static int ERROR_DATA_TRANSFER = 404; // ������ �ۼ��� ����
	public final static int ERROR_FULL_GROUP = 405; // ���� ���� �׷� �ʰ�
	public final static int ERROR_FULL_CLIENT = 406; // ���� ���� Ŭ���̾�Ʈ �ʰ�
	public final static int ERROR_TRYCATCH = 407;
	public final static int ERROR_DUPLICATED_IP = 408;
	public final static int ERROR_DUPLICATED_NICKNAME = 409;
	public final static int ERROR_NOT_SUPPORTED = 410;

	public final static int STRING_TYPE = 10;
	public final static int IMAGE_TYPE = 11;
	public final static int FILE_TYPE = 12;
	public final static String SEPARATOR = ";";
	public final static String FILE_DIR = "C:\\LinKlipboardServer";

	public final static int HISTORY_DEFAULT = 10;
	public final static int HISTORY_MAX = 50;

	public static final int UPDATE_DATA = 0;
	public static final int EXIT_CLITNT = 1;
	public static final int JOIN_CLITNT = 2;

}