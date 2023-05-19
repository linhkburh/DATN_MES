package frwk.dao.hibernate;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class SearchDao<T> extends BaseDao<T> {
	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;

	@Override
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@PostConstruct
	public void init() {
		criteria = getCurrentSession().createCriteria(getModelClass());
	}

	private Criteria criteria;

	public Criteria getCriteria() {
		return criteria;
	}

	public void addRestriction(Criterion restriction) {
		criteria.add(restriction);
	}

	public void addRestriction(Order order) {
		criteria.addOrder(order);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<T> search() {
		return (ArrayList<T>) criteria.list();

	}

	public long count(Class<T> cls) throws Exception {
		criteria.setProjection(Projections.rowCount());
		return ((Long) criteria.uniqueResult()).intValue();
	}
}
