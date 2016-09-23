package contents;

import java.io.File;
import java.io.Serializable;

import server_manager.LinKlipboard;

public class FileContents extends Contents implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1559329719938703224L;
	private String fileName;
	private String filePath;
	private long fileSize;

	public FileContents() {
		super();
		type = LinKlipboard.FILE_TYPE;
		//createSendFile();
	}
	
	public FileContents(File file) {
		this();
		fileName = file.getName();
		fileSize = file.length();
		filePath = file.getPath();
	}
	
	/** ���� ������ �̸��� ��ȯ */
	public String getFileName() {
		return this.fileName;
	}
	
	public String getFilePath() {
		return this.filePath;
	}
	
	/** ���� ������ ũ�⸦ ��ȯ */
	public long getFileSize() {
		return this.fileSize;
	}
	
	public void initFilePath() {
		this.filePath = null;
	}
	
}