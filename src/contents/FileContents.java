package contents;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import server_manager.LinKlipboard;

public class FileContents extends Contents implements Serializable {
	private String filePath;

	public FileContents() {
		super();
	}
	
	public FileContents(String sharer, String path) {
		super(sharer);
		type = LinKlipboard.FILE_TYPE;
		this.filePath = path;
	}

	public String getfilePath() {
		return filePath;
	}

	@Override
	public Contents receiveData(ObjectInputStream in) throws ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		return null;
	}
}