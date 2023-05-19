package common.util;

import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

public class HibernateUtil {

	public static Object getImplementation(Object proxy) {
		HibernateProxy hibernateProxy = (HibernateProxy) proxy;
		LazyInitializer initializer = hibernateProxy.getHibernateLazyInitializer();
		return initializer.getImplementation();
	}
}
