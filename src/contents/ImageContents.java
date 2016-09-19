package contents;

import java.awt.Image;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import javax.swing.ImageIcon;

import server_manager.LinKlipboard;

public class ImageContents extends Contents implements Serializable {
	private ImageIcon imageData;

	public ImageContents() {
		super();
		type = LinKlipboard.IMAGE_TYPE;
	}
	
	public ImageContents(String sharer) {
		this();
	}
	
	public ImageContents(ImageIcon image) {
		this();
		this.imageData = image;
	}
	
	public ImageContents(Image image) {
		this();
		this.imageData = new ImageIcon(image);
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