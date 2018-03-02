package bp;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class AppContext {
	public static final ClassPathXmlApplicationContext CONTEXT = new ClassPathXmlApplicationContext("beans.xml");
}
