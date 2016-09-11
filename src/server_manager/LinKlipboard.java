package server_manager;

public class LinKlipboard {

	public static final int MAX_GROUP = 10; // 최대로 생성할 수 있는 그룹 수
	public static final int MAX_CLIENT = 10; // 한 그룹에 최대로 입장할 수 있는 클라이언트 수

	public final static int NULL = -1;
	public final static int ACCESS_PERMIT = 100; // 접송 승인
	public final static int ERROR_DUPLICATED_GROUPNAME = 200; // 중복된 그룹 이름
	public final static int ERROR_NO_MATCHED_GROUPNAME = 201; // 해당 이름의 그룹 없음
	public final static int ERROR_PASSWORD_INCORRECT = 202; // 패스워드 불일치
	public final static int ERROR_SOCKET_CONNECTION = 203; // 소켓 연결 오류
	public final static int ERROR_DATA_TRANSFER = 204; // 데이터 송수신 오류
	public final static int ERROR_TRYCATCH = 205;

	public final static int STRING_TYPE = 10;
	public final static int IMAGE_TYPE = 11;
	public final static int FILE_TYPE = 12;
}
