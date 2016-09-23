package contents;

import java.io.Serializable;

import server_manager.Logger;

// Å×½ºÆ®
public abstract class Contents implements Serializable{

	protected String date;
	protected String sharer;
	protected int type;

	public Contents() {
		setDate();
	}
	
	public Contents(String sharer) {
		this();
		this.sharer = sharer;
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
}