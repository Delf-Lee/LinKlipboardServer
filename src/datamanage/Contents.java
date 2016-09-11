package datamanage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;

import javax.swing.ImageIcon;

import server_manager.LinKlipboard;

public abstract class Contents {

	protected Date date;
	protected String sharer;
	protected int type;

	public Contents(String sharer) {
		date = new Date();
		this.sharer = sharer;
	}

	public String getSharer() {
		return sharer;
	}

	public Date getDate() {
		return date;
	}

	public int getType() {
		return type;
	}

	/** 데이터를 수신 
	 * @param in 수신 받을 스트림 
	 * @return 데이터를 저장하고 자기 자신을 반환 */
	abstract public Contents receiveData(ObjectInputStream in) throws ClassNotFoundException, IOException;

}

class StringContents extends Contents {
	private String stringData;

	public StringContents(String sharer) {
		super(sharer);
		type = LinKlipboard.STRING_TYPE;
	}

	public StringContents(String sharer, String data) {
		super(sharer);
		type = LinKlipboard.STRING_TYPE;
		this.stringData = data;
	}

	public String getString() {
		return stringData;
	}

	@Override
	public Contents receiveData(ObjectInputStream in) throws ClassNotFoundException, IOException {
		stringData = (String) in.readObject();
		return this;
	}
}

class ImageContents extends Contents {
	private ImageIcon imageData;

	public ImageContents(String sharer) {
		super(sharer);
		type = LinKlipboard.IMAGE_TYPE;
	}

	public ImageContents(String sharer, ImageIcon data) {
		super(sharer);
		type = LinKlipboard.IMAGE_TYPE;
		this.imageData = data;
	}

	public ImageIcon getImage() {
		return imageData;
	}

	@Override
	public Contents receiveData(ObjectInputStream in) throws ClassNotFoundException, IOException {
		imageData = (ImageIcon) in.readObject();
		return this;
	}
}

class FileContents extends Contents {
	private String filePath;

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
