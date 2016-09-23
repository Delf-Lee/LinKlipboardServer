package datamanage;

import java.util.Vector;

import contents.Contents;
import server_manager.LinKlipboard;

public class History {
	private Vector<Contents> list;
	private int index = 0;
	private boolean defaultIndex = true;

	public History() {
		list = new Vector<Contents>(LinKlipboard.HISTORY_MAX * 2);
	}

	/** @param contents �����丮�� ������ Contents ��ü*/
	public void setIndex(Contents contents) {
		// contents.setIndex(index);
		list.add(nextIndex(), contents);
	}

	/** @return 0���� HISTORY_MAX * 2 ���� ��ȯ�ϴ� ���� */
	private int nextIndex() {
		if (index + 1 < LinKlipboard.HISTORY_MAX * 2) {
			if (index < LinKlipboard.HISTORY_MAX) {
				defaultIndex = false;
			}
			return index++;
		}
		defaultIndex = true;
		return 0;
	}

	/** @return ���� ��ü�� ������ �ִ� Vector<Contents>*/
	public Vector<Contents> getContentsVector() {
		return list;
	}

	public Contents getContents(int index) {
		if (defaultIndex == true) {
			return list.get(index);
		}
		else {
			return list.get(index + LinKlipboard.HISTORY_MAX);
		}
	}

	public int size() {
		return list.size();
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public boolean isFull() {
		return (size() < LinKlipboard.HISTORY_MAX);
	}
}
