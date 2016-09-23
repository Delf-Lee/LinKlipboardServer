package datamanage;

import java.util.Hashtable;
import java.util.Vector;

import contents.Contents;
import server_manager.LinKlipboard;

public class History {
	private Hashtable<Integer, Contents> list2 = new Hashtable<Integer, Contents>(LinKlipboard.HISTORY_MAX * 2);
	private Vector<Contents> list = new Vector<Contents>(LinKlipboard.HISTORY_MAX * 2);
	private int serialNo = 0;

	public History() {
	}

	/** @param contents 히스토리에 삽입할 Contents 객체
	 * @return 삽입된 Contents serial number */
	public int addContents(Contents contents) {
		int next = nextIndex();
		// list2.put(next, contents);
		list2.replace(next, contents);
		return next;
	}

	/** @return 0부터 HISTORY_MAX * 2 까지 순환하는 정수 */
	private int nextIndex() {
		return (serialNo + 1 < LinKlipboard.HISTORY_MAX * 2) ? serialNo++ : 0;
	}

	private int getLastSerialNo() {
		return (serialNo + LinKlipboard.HISTORY_MAX);
	}

	/** @return 현재 객체가 가지고 있는 Vector<Contents>*/
	public Vector<Contents> getContentsVector() {
		Vector<Contents> contentsVector = null;
		Hashtable<Integer, Contents> tmp;
		synchronized (list2) {
			tmp = new Hashtable<Integer, Contents>(list2);
		}

		int cnt = serialNo, index = serialNo;
		if (serialNo > LinKlipboard.HISTORY_DEFAULT) {
			cnt = LinKlipboard.HISTORY_DEFAULT;
			index = serialNo - LinKlipboard.HISTORY_DEFAULT + 1;
		}
		for (int i = 0; i < cnt; i++) {
			contentsVector.add(i, tmp.get(index - i));
		}
		return contentsVector;
	}

	public int getNextSerialNo() {
		return (serialNo + 1);
	}

	public Contents getContents(int serial) {
		// return list.get(serial);
		return list2.get(serial);
	}

	public int size() {
		//return list.size();
		return list2.size();
	}

	public boolean isEmpty() {
		//return list.isEmpty();
		return list2.isEmpty();
	}

	public boolean isFull() {
		return (size() < LinKlipboard.HISTORY_MAX);
	}
}
