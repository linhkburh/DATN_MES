package common.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

import common.util.XmlUtils;

public class HibernateAdapter extends XmlAdapter<String, HibernateProxy> {
    
    @Override
	public String marshal(HibernateProxy hibernateProxy) throws Exception {
    	LazyInitializer initializer =
    	        hibernateProxy.getHibernateLazyInitializer();
    	    Object obj = initializer.getImplementation();
    	    return XmlUtils.convertObjToXML(obj);
	}

	@Override
	public HibernateProxy unmarshal(String v) throws Exception {
		return null;
	}

}