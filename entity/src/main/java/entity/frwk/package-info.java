//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.09.09 at 05:16:55 PM ICT 
//

@XmlJavaTypeAdapters({
    @XmlJavaTypeAdapter(value = DateAdapter.class, type = Date.class),
    @XmlJavaTypeAdapter(value = HibernateAdapter.class, type = HibernateProxy.class)
})
package entity.frwk;

import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;

import org.hibernate.proxy.HibernateProxy;

import common.adapter.DateAdapter;
import common.adapter.HibernateAdapter;
