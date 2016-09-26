package datamanage;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import contents.Contents;
import server_manager.LinKlipboard;

public class History {
	private Hashtable<Integer, Contents> list2 = new Hashtable<Integer, Contents>(LinKlipboard.HISTORY_MAX * 2);
	//private Vector<Contents> list = new Vector<Contents>(LinKlipboard.HISTORY_MAX * 2);
	private int serialNo = 0;

	public History() {
	}

	/** @param contents �����丮�� ������ Contents ��ü,
	 * @return ���Ե� Contents serial number */
	public int addContents(Contents contents) {
		// int next = serialNo;
		if (list2.put(serialNo, contents) != null) {
			list2.replace(serialNo, contents); // �̰� ��������;
		}
		return serialNo++;
	}

	//	/** @return 0���� HISTORY_MAX * 2 ���� ��ȯ�ϴ� ���� */
	//	private int nextIndex() {
	//		return (serialNo + 1 < LinKlipboard.HISTORY_MAX * 2) ? serialNo++ : 0;
	//	}

	private int getLastSerialNo() {
		return (serialNo + LinKlipboard.HISTORY_MAX);
	}

	/** @return ���� ��ü�� ������ �ִ� Vector<Contents>*/
	public Vector<Contents> getContentsVector() {
		Vector<Contents> contentsVector = new Vector<Contents>(); // ��ȯ�� Vecter�� �����丮
		Hashtable<Integer, Contents> tmp; // �����ϴµ� ����� �ӽ� Hashtable�� �����丮(���� �ӽ� �����丮)

		synchronized (list2) {
			tmp = new Hashtable<Integer, Contents>(list2); // �ӽ� �����丮�� �ӽ� �����丮 �� ����
		}

		int cnt = serialNo, index = 0;

		if (serialNo > LinKlipboard.HISTORY_DEFAULT) {
			cnt = LinKlipboard.HISTORY_DEFAULT;
			index = (serialNo - 1) - LinKlipboard.HISTORY_MAX + 1;
		}

		for (int i = 0; i < cnt;) {
			Contents insertContenst = tmp.get(index + i);
			if (insertContenst != null) {
				contentsVector.add(i++, insertContenst);
			}
		}
		
		Iterator<Contents> it1 = contentsVector.iterator();
		while (it1.hasNext()) {
			System.out.println("cvȮ��" + it1.next().getType());
		}
		return contentsVector;
	}

	public int getNextSerialNo() {
		return (serialNo);
		//return (serialNo + 1);
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
	public Hashtable<Integer, Contents> getHistory() {
		return this.list2;
	}
}
