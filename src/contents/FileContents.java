package contents;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import server_manager.LinKlipboard;

public class FileContents extends Contents implements Serializable {
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
	
}