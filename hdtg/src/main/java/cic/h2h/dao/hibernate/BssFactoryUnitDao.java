package cic.h2h.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import entity.BssFactoryUnit;
import entity.Company;
@Repository("bssFactoryUnitDao")
public class BssFactoryUnitDao extends H2HBaseDao<BssFactoryUnit>{
	@SuppressWarnings("unchecked")
	public List<BssFactoryUnit> getFactoryByCompany(Company company) {
		@SuppressWarnings("deprecation")
		Criteria c = getCurrentSession().createCriteria(BssFactoryUnit.class);
		if (company != null)
			c.add(Restrictions.eq("company", company));
		return c.list();
	}
	
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public BssFactoryUnit getBssFactoryUnitByCode(String companyId, String code) {
		if (Formater.isNull(companyId) || Formater.isNull(code))
			return null;
		Criteria criteria = getCurrentSession().createCriteria(BssFactoryUnit.class);
		criteria.add(Restrictions.eq("company.id", companyId));
		criteria.add(Restrictions.eq("code", code));
		List<BssFactoryUnit> units = criteria.list();
		if (Formater.isNull(units))
			return null;
		return units.get(0);
	}
}
