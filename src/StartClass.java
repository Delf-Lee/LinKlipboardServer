import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import server_manager.LinKlipboardServer;

public class StartClass implements ServletContextListener {
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		LinKlipboardServer tmp = new LinKlipboardServer();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		;
	}
}
