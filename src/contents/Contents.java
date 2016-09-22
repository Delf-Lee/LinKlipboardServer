package contents;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.logging.Logger;

// 테스트
public abstract class Contents implements Serializable{

	protected String date;
	protected String sharer;
	protected int type;

	public Contents() {
		//date = Logger.now();
	}
	
	public Contents(String sharer) {
		this();
		this.sharer = sharer;
	}
	
	public void setSharer(String sharer) {
		this.sharer = sharer;
	}

	public String getSharer() {
		return sharer;
	}

	public String getDate() {
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