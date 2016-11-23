package contents;

import java.io.Serializable;

import server_manager.LinKlipboard;
import surpport.Logger;

// Å×½ºÆ®
public abstract class Contents implements Serializable{
	private static final long serialVersionUID = 4131370422438049456L;
	protected String date = "null";
	protected String sharer = "null";
	protected int type = LinKlipboard.NULL;
	protected int serialNo;

	public Contents() {
		setDate();
	}
	
	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}
	
	public Contents(String sharer) {
		this();
		this.sharer = sharer;
	}
	public Contents(String sharer, int type) {
		this(sharer);
		this.type = type;
	}
	
	public void setSharer(String sharer) {
		this.sharer = sharer;
	}
	
	public void setDate() {
		date = Logger.now();
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
	
	public void setType(int type) {
		this.type = type;
	}
}