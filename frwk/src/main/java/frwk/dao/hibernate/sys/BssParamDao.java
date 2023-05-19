package frwk.dao.hibernate.sys;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import entity.frwk.BssParam;

@Repository(value = "bssParamDao")
public class BssParamDao extends SysDao<BssParam> {
	private static final Logger logger = Logger.getLogger(BssParamDao.class);

	public BssParam getById(String id) {

		return (BssParam) getThreadSession().get(BssParam.class, id);
	}

	public BssParam getSysParamByCode(String code) {
		return (BssParam) getCurrentSession().createCriteria(BssParam.class).add(Restrictions.eq("code", code)).uniqueResult();
	}
	
	

	public List<BssParam> getByType(String dictType) {
		return (List<BssParam>) getThreadSession().createCriteria(BssParam.class)
				.add(Restrictions.eq("code", dictType)).list();
	}

}