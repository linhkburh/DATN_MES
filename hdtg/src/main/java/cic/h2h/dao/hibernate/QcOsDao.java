package cic.h2h.dao.hibernate;

import java.io.Serializable;

import org.hibernate.criterion.Restrictions;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.stereotype.Repository;

import entity.QcOs;
import entity.QuotationItem;

@Repository("qcOsDao")
public class QcOsDao extends H2HBaseDao<QcOs> {
	@SuppressWarnings("deprecation")
	public QcOs getByQiID(QuotationItem qi) {
		return (QcOs) getThreadSession().createCriteria(QcOs.class).add(Restrictions.eq("quotationItem", qi));
	}
}