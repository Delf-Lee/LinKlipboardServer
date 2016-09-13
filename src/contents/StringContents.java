package contents;

import java.io.IOException;
import java.io.ObjectInputStream;

import server_manager.LinKlipboard;

public class StringContents extends Contents {
	private String stringData;
	
	public StringContents() {
		super("");
	}

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

