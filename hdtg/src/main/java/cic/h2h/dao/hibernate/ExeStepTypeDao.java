package cic.h2h.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import entity.ExeStepType;

@Repository(value = "exeStepTypeDao")
public class ExeStepTypeDao extends H2HBaseDao<ExeStepType> {
	@SuppressWarnings("unchecked")
	@Override
	public List<ExeStepType> getAll() {
		return getCurrentSession().createCriteria(ExeStepType.class).addOrder(Order.asc("name")).list();
	}

	public ExeStepType getByCode(String code) {
		return (ExeStepType) getCurrentSession().createCriteria(ExeStepType.class).add(Restrictions.eq("code", code)).uniqueResult();
	}
}
