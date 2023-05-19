package frwk.dao.hibernate.sys;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import entity.frwk.SysSecurity;

@Repository(value = "sysSecurityDao")
public class SysSecurityDao extends SysDao<SysSecurity> {

	public SysSecurity findByCodeAndActive(String code, boolean active) {
		Criteria criteria = getCurrentSession().createCriteria(SysSecurity.class).add(Restrictions.eq("code", code))
				.add(Restrictions.eq("active", active));
		return (SysSecurity) criteria.uniqueResult();
	}
}
