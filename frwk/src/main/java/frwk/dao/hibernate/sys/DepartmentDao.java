package frwk.dao.hibernate.sys;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import entity.Company;
import entity.Department;

@Repository(value = "departmentDao")
public class DepartmentDao extends SysDao<Department> {

	@SuppressWarnings("unchecked")
	public List<Department> getAllByCompanyOrderAscName(String id) {
		return getCurrentSession().createCriteria(Department.class).add(Restrictions.eq("company.id", id)).addOrder(Order.asc("name")).list();
	}
	
	public Department getDepartmentByCompanyAndCode(String id, String code) {
		Criteria c = getCurrentSession().createCriteria(Department.class);
		c.add(Restrictions.eq("code", code));
		c.add(Restrictions.eq("company.id", id));
		return (Department) c.uniqueResult();
	}
}
