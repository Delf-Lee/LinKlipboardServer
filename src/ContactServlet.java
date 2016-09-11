import java.io.*;
import java.net.*;

public class ContactServlet {
	public static void main(String[] args) {

		try {
			System.out.println("Ω√¿€");
			URL url = new URL("http://localhost:8080/LinKlipboardServerProject/DoLogin");
			URLConnection conn = url.openConnection();
			conn.setDoOutput(false);

			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
			out.write("username=¿ÃªÛ»∆\r\n");
			out.write("password=delf\r\n");
			out.flush();
			out.close();
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String response;
			while ((response = in.readLine()) != null) {
				System.out.println(response);
			}
			in.close();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
			// a real program would need to handle this exception
		} catch (IOException ex) {
			ex.printStackTrace();
			// a real program would need to handle this exception
		}
	}
}