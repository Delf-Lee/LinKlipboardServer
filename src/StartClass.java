import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class StartClass implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("�̻���");		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
	}
}