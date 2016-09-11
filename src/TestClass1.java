import java.util.Hashtable;

public class TestClass1 {
	public static void main(String[] args) {
		Hashtable<String, String> test = new Hashtable<String, String>();
		
		test.put("test1", "test_1");
		
		if(test.containsKey("test2")) {
			System.out.println("return true");
		}
		else {
			System.out.println("return false");
		}
		
	}
}
