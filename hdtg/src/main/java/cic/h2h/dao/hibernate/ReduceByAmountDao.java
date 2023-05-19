package cic.h2h.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.cfg.CreateKeySecondPass;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import entity.MaterialType;
import entity.ReduceByAmount;

@Repository(value = "reduceByAmountDao")
public class ReduceByAmountDao extends H2HBaseDao<ReduceByAmount> {

	public ReduceByAmount getByQuality(Long quality) {
		// getCurrentSession().createCriteria(ReduceByAmount.class).add(Restrictions.e)
		Criteria c = getCurrentSession().createCriteria(ReduceByAmount.class);
		// Lon hon hoac bang min
		c.add(Restrictions.or(Restrictions.isNull("amountFrom"), Restrictions.le("amountFrom", quality)));
		// Nho hon hoac bang max
		c.add(Restrictions.or(Restrictions.isNull("amountTo"), Restrictions.ge("amountTo", quality)));
		List tmp = c.list();
		if(tmp.size()==1)
			return (ReduceByAmount) tmp.get(0);
		return null;
	}
}
