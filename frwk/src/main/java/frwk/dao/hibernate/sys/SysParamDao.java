package frwk.dao.hibernate.sys;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import entity.frwk.SysParam;

@Repository(value = "sysParamDao")
public class SysParamDao extends SysDao<SysParam> {
	private static final Logger logger = Logger.getLogger(SysParamDao.class);
	@SuppressWarnings("deprecation")
	@Cacheable(value = "sysParamDao.getSysParamByCode", unless = "#result == null")
	public SysParam getSysParamByCode(String code) {
		return (SysParam) getCurrentSession().createCriteria(SysParam.class).add(Restrictions.eq("code", code)).uniqueResult();
	}
	@SuppressWarnings("unchecked")
	public List<SysParam> getByType(String dictType) {
		return (List<SysParam>) getThreadSession().createCriteria(SysParam.class)
				.add(Restrictions.eq("code", dictType)).list();
	}

}