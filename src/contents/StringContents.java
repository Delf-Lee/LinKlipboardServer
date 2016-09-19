package contents;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import server_manager.LinKlipboard;

public class StringContents extends Contents implements Serializable {
	private String stringData;
	
	public StringContents() {
		super();
	}

	public StringContents(String data) {
		super();
		stringData = data;
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