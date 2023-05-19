package cic.h2h.dao.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import entity.WorkPro;

@Repository(value = "workProDao")
public class WorkProDao extends H2HBaseDao<WorkPro>{
	private static Logger log = Logger.getLogger(WorkProDao.class);
	
	public WorkPro getWorkProByQuotationItemExeId(String quotationItemExeId) {
		WorkPro workPro = null;
		try {
			Criteria criteria = getCurrentSession().createCriteria(WorkPro.class);
			criteria.add(Restrictions.eq("quotationItemExe.id", quotationItemExeId));
			workPro = (WorkPro) criteria.uniqueResult();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return workPro;
	}
}
