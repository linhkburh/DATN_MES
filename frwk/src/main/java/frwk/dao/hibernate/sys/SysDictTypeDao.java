package frwk.dao.hibernate.sys;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import entity.frwk.SysDictType;
@Repository(value = "sysDictTypeDao")
public class SysDictTypeDao extends SysDao<SysDictType> {
	
	
	public SysDictType getSysDictTypeByCode(String code) {
		Criteria c = getCurrentSession().createCriteria(SysDictType.class);
		c.add(Restrictions.eq("code", code));
		
		return (SysDictType) c.uniqueResult();
		
	}
}
