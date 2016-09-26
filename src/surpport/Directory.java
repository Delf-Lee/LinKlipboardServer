package surpport;

import java.io.File;

public class Directory {
	public synchronized static File createEmptyDirectory(String dir) {
		File directory = new File(dir);

		if (!directory.exists()) {
			directory.mkdir(); // 폴더 생성
		}
		else {
			clearDirecrory(directory);
		}
		return directory;
	}
	
	public synchronized static File createDirectory(String dir) {
		File directory = new File(dir);
		if (!directory.exists()) {
			directory.mkdir();
		}
		return directory;
	}


	public synchronized static void clearDirecrory(File dir) {
		File[] innerFile = dir.listFiles();
		for (File file : innerFile) {
			if (file.isDirectory()) {
				clearDirecrory(file);
			}
			file.delete();
		}
	}
}
