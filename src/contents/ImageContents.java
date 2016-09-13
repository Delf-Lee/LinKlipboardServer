package contents;

import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.ImageIcon;

import server_manager.LinKlipboard;

public class ImageContents extends Contents {
	private ImageIcon imageData;

	public ImageContents() {
		super("");
	}
	
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