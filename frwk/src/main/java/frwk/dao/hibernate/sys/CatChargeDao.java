package frwk.dao.hibernate.sys;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import entity.frwk.CatCharge;

@Repository(value = "catChargeDao")
public class CatChargeDao extends SysDao<CatCharge> {
	private static final Logger logger = Logger.getLogger(CatChargeDao.class);

	public CatCharge getById(String id) {

		return (CatCharge) getThreadSession().get(CatCharge.class, id);
	}

	public CatCharge getSysParamByCode(String code) {
		return (CatCharge) getCurrentSession().createCriteria(CatCharge.class).add(Restrictions.eq("code", code)).uniqueResult();
	}
	
	

	@SuppressWarnings("unchecked")
	public List<CatCharge> getByType(String dictType) {
		return (List<CatCharge>) getThreadSession().createCriteria(CatCharge.class)
				.add(Restrictions.eq("code", dictType)).list();
	}

}