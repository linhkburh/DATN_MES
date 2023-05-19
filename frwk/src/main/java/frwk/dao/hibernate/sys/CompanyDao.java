package frwk.dao.hibernate.sys;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import entity.Company;

@Repository(value = "companyDao")
public class CompanyDao extends SysDao<Company>{
	@SuppressWarnings("unchecked")
	@Cacheable(value = "companyDao.getAllOrderAstName", unless = "#result == null")
	public List<Company> getAllOrderAstName() {
		return getCurrentSession().createCriteria(Company.class).addOrder(Order.asc("name")).list();
	}

	
	public Company getCompanyByCode(String code) {
		Criteria c = getCurrentSession().createCriteria(Company.class);
		c.add(Restrictions.eq("code", code));
		return (Company) c.uniqueResult();
	}
}
