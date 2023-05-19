package cic.h2h.dao.hibernate;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import common.util.ResourceException;
import entity.Customer;

@Repository(value = "customerManageDao")
public class CustomerManageDao extends H2HBaseDao<Customer> {
	@SuppressWarnings("unchecked")
	public Customer getOnly(String cusCode, String cusName) throws ResourceException {
		Criteria c = getThreadSession().createCriteria(Customer.class);
		if (!Formater.isNull(cusCode)) {
			c.add(Restrictions.like("code", cusCode, MatchMode.ANYWHERE).ignoreCase());
		}
		if (!Formater.isNull(cusName)) {
			c.add(Restrictions.like("orgName", cusName, MatchMode.ANYWHERE).ignoreCase());
		}
		c.setMaxResults(2);
		List<Customer> tmp = c.list();
		if (tmp.size() == 1)
			return tmp.get(0);
		else if (tmp.size() == 0)
			return null;
		else
			throw new ResourceException("Káº¿t quáº£ tra vá»� kg duy nháº¥t");
	}

	@SuppressWarnings("unchecked")
	@Cacheable(value = "getCusbyCode", unless = "#result == null")
	public Customer getCusbyCode(String code) {
		Criteria c = getThreadSession().createCriteria(Customer.class);
		c.add(Restrictions.eq("code", code));
		c.setMaxResults(1);
		List<Customer> tmp = c.list();
		if (tmp.size() == 0)
			return null;
		else
			return tmp.get(0);
	}

	public void makeOrderCode(final Customer cus) {
//		Criteria c = getCurrentSession().createCriteria(Quotation.class).add(Restrictions.eq("customer.id", cus.getId()))
//				.add(Restrictions.sqlRestriction("EXTRACT(Year FROM QUOTATION_DATE)=EXTRACT(Year FROM SYSDATE)"))
//				.setProjection(Projections.rowCount());
//		int iNumOfOrderInYear = ((Long) c.uniqueResult()).intValue();
//		cus.setNewOrderCode(cus.getCode() + (iNumOfOrderInYear + 1));
		getCurrentSession().doWork(new Work() {
			public void execute(Connection connection) throws SQLException {
				CallableStatement call = connection.prepareCall("{ ? = call MES_EXE.makeOrderCode(?,?) }");
				call.registerOutParameter(1, Types.VARCHAR); // or whatever it is
				call.setString(2, cus.getId());
				call.setString(3, cus.getCode());
				call.execute();
				cus.setNewOrderCode(call.getString(1));
			}
		});
	}

	@Override
	public List<Customer> getAll() {
		return getCurrentSession().createCriteria(Customer.class).addOrder(Order.asc("code")).list();
	}
	@SuppressWarnings("unchecked")
	@Cacheable(value = "customerManageDao.getByType")
	public List<Customer> getByType(String type) {
		List<Customer> lstCus = new ArrayList<Customer>();
		Criteria criteria = getCurrentSession().createCriteria(Customer.class);
		if("isCustomer".equals(type))
			criteria.add(Restrictions.eq("isCustomer", Boolean.TRUE)).list();
		else if("isPartner".equals(type))
			criteria.add(Restrictions.eq("isPartner", Boolean.TRUE)).list();
		else 
			criteria.add(Restrictions.and(Restrictions.eq("isCustomer", Boolean.TRUE),Restrictions.eq("isPartner", Boolean.TRUE))).list();
		lstCus = criteria.list();
		return lstCus;
	}
}
