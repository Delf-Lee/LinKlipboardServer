package contents;

import java.io.Serializable;

import server_manager.LinKlipboard;

public class StringContents extends Contents implements Serializable {
	private static final long serialVersionUID = 8197233165098147502L;
	private String stringData;
	
	public StringContents() {
		super();
	}
	public StringContents(String sharer, int type) {
		super(sharer, type);
	}

	public StringContents(String data) {
		this();
		stringData = data;
		type = LinKlipboard.STRING_TYPE;
	}

	public StringContents(String sharer, String data) {
		this(sharer);
		type = LinKlipboard.STRING_TYPE;
		this.stringData = data;
	}

	public String getString() {
		return stringData;
	}
}