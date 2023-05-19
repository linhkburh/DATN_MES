package cic.h2h.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import entity.QuotationItemFinish;

@Repository(value = "quotationItemFinishDao")
public class QuotationItemFinishDao extends H2HBaseDao<QuotationItemFinish>{
	public QuotationItemFinish getById(String id) throws Exception {
		return (QuotationItemFinish) getCurrentSession().get(QuotationItemFinish.class, id);
	}
	
	
	public List<QuotationItemFinish> getListByThreadSession(List<String> lstId) {
		List<QuotationItemFinish> finishs = new ArrayList<QuotationItemFinish>();
		Criteria criteria = getCurrentSession().createCriteria(QuotationItemFinish.class);
		criteria.add(Restrictions.in("id", lstId));
		finishs = criteria.list();
		return finishs;
	}
}
