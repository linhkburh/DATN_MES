package frwk.dao.hibernate;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ThreadLocalSessionContext;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.annotation.Transactional;

import common.util.Formater;
import common.util.JsonUtils;
import common.util.ReflectionUtils;
import common.util.ReflectionUtils.ReconcileObj;
import entity.frwk.SysUsers;
import entity.frwk.UserLog;
import frwk.controller.SearchParam;
import frwk.utils.ApplicationContext;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

@SuppressWarnings("restriction")
@Transactional
public abstract class BaseDao<T> {

	private static final Logger logger = Logger.getLogger(BaseDao.class);

	@Autowired
	private HttpSession session;

	@Autowired
	MessageSource messageSource;

	private Class<T> modelClass;

	private ArrayList<Order> orders;

	private Criteria criteria;

	private SearchParam searchParam;

	@Autowired
	private DataSource dataSource;

	public void addOrder(Order od) {
		if (orders == null)
			orders = new ArrayList<Order>(Arrays.asList(od));
		else
			orders.add(od);

	}

	// Tim kiem
	public void addRestriction(Criterion restriction) {
		criteria.add(restriction);

	}

	public void clear() {
		getCurrentSession().clear();
	}

	public void createAlias(String associationPath, String alias) {
		this.criteria.createAlias(associationPath, alias);
		
	}

	@SuppressWarnings("unchecked")
	public BaseDao<T> createCriteria() throws Exception {
		Constructor<T> daoConstructor = (Constructor<T>) this.getClass().getConstructor();
		BaseDao<T> dao = (BaseDao<T>) daoConstructor.newInstance();
		dao.criteria = getCurrentSession().createCriteria(dao.getModelClass());
		return dao;
	}

	public void del(T entity) throws Exception {
		getCurrentSession().delete(entity);
		UserLog log = new UserLog(getSessionUser());
		log.setAction("del_" + entity.getClass().getName());
		if(entity instanceof HibernateProxy) {
			LazyInitializer initializer = ((HibernateProxy) entity).getHibernateLazyInitializer();
			Object pojo =  initializer.getImplementation();
			log.setRecordId(String.valueOf(
					getClassMetadata(pojo.getClass()).getIdentifier(pojo, (SessionImplementor) getCurrentSession())));
		}else {
			log.setRecordId(String.valueOf(
					getClassMetadata(entity.getClass()).getIdentifier(entity, (SessionImplementor) getCurrentSession())));
		}
		try {
			log.setDetail(JsonUtils.writeToString(entity));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		getCurrentSession().save(log);

	}

	public void endCurrentSession() {
		try {
			Session sess = getCurrentSession();
			if (sess.getTransaction().getStatus() == TransactionStatus.ACTIVE)
				sess.getTransaction().commit();

		} catch (Exception ex1) {
			logger.error(ex1.getMessage(), ex1);
			Session sess = getCurrentSession();
			if (sess.getTransaction().getStatus() == TransactionStatus.ACTIVE)
				sess.getTransaction().rollback();
			throw ex1;
		} finally {
			getCurrentSession().close();
		}
		getCurrentSession().beginTransaction();
	}

	public void endTheadSession(Exception ex) throws Exception {
		Session sess = ThreadLocalSessionContext.unbind(getSessionFactory());
		if (sess == null || sess.getTransaction().getStatus() != TransactionStatus.ACTIVE)
			return;
		if (ex == null) {
			try {
				sess.getTransaction().commit();
			} catch (Exception e) {
				sess.getTransaction().rollback();
				throw e;
			}
		} else
			sess.getTransaction().rollback();
	}
	public void evict(Object object) {
		getCurrentSession().evict(object);

	}

	/**
	 * Kiem tra phan tu co ton tai trong danh sach
	 * 
	 * @param chkItem
	 * @param path
	 * @param chkSourcs
	 * @return
	 * @throws Exception
	 */
	public Object find(Object chkItem, List<?> chkSourcs) throws Exception {
		if (Formater.isNull(chkSourcs))
			return null;
		Class<?> modelClss = chkItem.getClass().isAssignableFrom(HibernateProxy.class) ? chkSourcs.get(0).getClass()
				: chkItem.getClass();
		String identifierPropertyName = getIdentifierPropertyName(modelClss);
		Field f = ReflectionUtils.getField(modelClss, identifierPropertyName);
		if (!f.isAccessible())
			f.setAccessible(true);
		for (Object item : chkSourcs) {
			if (f.get(item) == null)
				continue;
			Serializable itemVal = (Serializable) f.get(item);
			Serializable chkItemVal = (Serializable) f.get(chkItem);
			if (itemVal.equals(chkItemVal))
				return item;
		}
		return null;
	}

	public void flush() {
		getCurrentSession().flush();
	}

	public <V> V get(Class<V> c, Serializable recordId) {
		return (V) getObject(c, recordId);
	}

	public T get(Serializable recordId) {
		return getCurrentSession().get(modelClass, recordId);
	}

	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		return getCurrentSession().createCriteria(modelClass).list();
	}

	public ClassMetadata getClassMetadata(Class<?> clss) {
		return getSessionFactory().getClassMetadata(clss);
	}

	public Connection getConnection() {
		return DataSourceUtils.getConnection(dataSource);
	}

	public Criteria getCriteria() {
		return criteria;
	}

	public Session getCurrentSession() {
		Session sess = getSessionFactory().getCurrentSession();
		if (sess.getTransaction().getStatus() != TransactionStatus.ACTIVE)
			sess.beginTransaction();
		return sess;

	}

	public Serializable getId(Object object) {
		return getClassMetadata(object.getClass()).getIdentifier(object, (SessionImplementor) getCurrentSession());
	}

	public String getIdentifierPropertyName(Class<?> modelClss) throws Exception {
		ClassMetadata hibernateClsInf = getClassMetadata(modelClss);
		if (hibernateClsInf == null)
			return null;
		return hibernateClsInf.getIdentifierPropertyName();
	}

	@SuppressWarnings("unchecked")
	public Class<T> getModelClass() {
		if (modelClass == null)
			modelClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass())
					.getActualTypeArguments()[0];
		return modelClass;
	}

	public <V> V getObject(Class<V> c, Serializable recordId) {
		return getCurrentSession().get(c, recordId);
	}

	/**
	 * Get object by thead session
	 * 
	 * @param c        c object class
	 * @param recordId object id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T getObject(T o) throws Exception {
		if (o == null)
			throw new Exception("Object is null");
		ClassMetadata tmp = getClassMetadata(o.getClass());
		if (tmp == null)
			return o;
		Serializable identifier = tmp.getIdentifier(o, (SessionImplementor) getCurrentSession());
		return (T) getCurrentSession().get(o.getClass(), identifier);
	}

	public T getPoJo(Serializable recordId) {
		T rs = null;
		Session s = openNewSession();
		Transaction tx = null;
		try {
			tx = s.beginTransaction();
			rs = s.get(modelClass, recordId);
			tx.commit();
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			throw ex;
		} finally {
			s.close();
		}
		return rs;
	}

	public SearchParam getSearchParam() {
		return searchParam;
	}

	public abstract SessionFactory getSessionFactory();

	public SysUsers getSessionUser() {
		ApplicationContext appContext = (ApplicationContext) session
				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
		return (SysUsers) appContext.getAttribute(ApplicationContext.USER);
	}

	@SuppressWarnings("unchecked")
	public Method getSetterParent(Class<?> clsChild, Class<?> clsParent) throws Exception {
		ClassMetadata hibernateClssChild = getClassMetadata(clsChild);
		if (hibernateClssChild == null)
			return null;
		String[] propNames = hibernateClssChild.getPropertyNames();
		for (String propName : propNames) {
			org.hibernate.type.Type propType = hibernateClssChild.getPropertyType(propName);
			if (propType.getReturnedClass().isAssignableFrom(clsParent)) {
				Field f = clsChild.getDeclaredField(propName);
				return ReflectionUtils.getSetterMethod(clsChild, f);
			}
		}
		return null;
	}
	@Deprecated
	/**
	 * This method is no longer acceptable to get CurrentSession.
	 * <p> Use {@link getCurrentSession()} instead.
	 * @return
	 */
	public Session getThreadSession() {
		return getCurrentSession();
	}
	@SuppressWarnings("unchecked")
	@PostConstruct
	private void init() {
		modelClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	public T load(Serializable recordId) {
		return getCurrentSession().load(modelClass, recordId);
	}

	public void load(T o) {
		if(o instanceof HibernateProxy)
			return;
		Serializable id = getId(o);
		if(getCurrentSession().contains(o))
			return;
		getCurrentSession().load(o, id);
	}

	public Session openNewSession() {
		return getSessionFactory().openSession();

	}

	public void refresh(T o) {
		getCurrentSession().refresh(o);
	}

	public void save(T object) throws Exception {
		// ghi log
		UserLog log = new UserLog(getSessionUser());
		ClassMetadata m = getClassMetadata(object.getClass());
		Serializable id = m.getIdentifier(object, (SessionImplementor) getCurrentSession());

		if (id == null || "".equals(id) || "0".equals(id.toString())) {
			getCurrentSession().save(object);
			log.setAction("insert_" + object.getClass().getName());

		} else {
			getCurrentSession().merge(object);
			log.setAction("edit_" + object.getClass().getName());
		}

		log.setRecordId(String.valueOf(m.getIdentifier(object, (SessionImplementor) getCurrentSession())));
		// Ghi log co the loi do object pojo, kg du thong tin de serialize doi voi cac thuoc tinh tinh toan
		try {
			log.setDetail(JsonUtils.writeToString(object));
			getCurrentSession().save(log);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	public ArrayList<T> search() {
		// Tham so tim kiem
		if (getSearchParam() != null) {
			criteria.setFirstResult(getSearchParam().getBeginIndex().intValue());
			criteria.setMaxResults(getSearchParam().getPageSize());
		}
		// Order
		if (this.orders != null) {
			for (Order order : this.orders)
				criteria.addOrder(order);
		}
		criteria.setProjection(null);
		return (ArrayList<T>) criteria.list();

	}

	public void setSearchParam(SearchParam searchParam) {
		this.searchParam = searchParam;
	}

	/**
	 * Dong bo 2 danh sach: pojo-> proxy
	 * 
	 * @param <V>          Kieu cua danh sach can update
	 * 
	 * @param <T>          Kieu object
	 * @param proxy:       Persistence object (old in db - object chua du lieu DB)
	 * @param lstFieldName Ten danh sach can update
	 * @param pojo:        Object du lieu tren giao dien
	 * @param reconcileObj Neu khac null, dong bo du lieu cac truong primitive giua cac phan tu tuong ung cua 2 danh
	 *                     sach
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <V> void updateListByPath(T proxy, String lstFieldName, T pojo, ReflectionUtils.ReconcileObj<V> reconcileObj)
			throws Exception {
		if (Formater.isNull(lstFieldName))
			return;
		Field f = ReflectionUtils.getField(pojo.getClass(), lstFieldName);
		if (!f.isAccessible())
			f.setAccessible(true);
		sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl t1 = (sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl) f
				.getGenericType();
		Type[] fieldType = t1.getActualTypeArguments();
		Class<?> objClss = (Class<?>) fieldType[0];
		Method fGetter = ReflectionUtils.getGeter(pojo.getClass(), f);
		List lstProxyObject = (List) fGetter.invoke(proxy);
		List lstPojoObject = (List) f.get(pojo);
		Method setParent = getSetterParent(objClss, pojo.getClass());
		ClassMetadata pojoItemClassMetadata = getClassMetadata(objClss);

		// Them, sua
		for (Object pojoItem : lstPojoObject) {
			boolean add = pojoItemClassMetadata.getIdentifier(pojoItem,
					(SessionImplementor) getCurrentSession()) == null;
			if (add) {
				lstProxyObject.add(pojoItem);
			} else {
				Object proxyItem = find(pojoItem, lstProxyObject);
				if (reconcileObj != null)
					reconcileObj.reconcile((V) proxyItem, (V) pojoItem, null);
			}
			setParent.invoke(pojoItem, proxy);
		}

		// Xoa
		List lstDels = new ArrayList();
		// lstProxyObject hien tai da bao gom cac object duoc them
		for (Object proxyItem : lstProxyObject) {
			if (!getCurrentSession().contains(proxyItem)) {
				getCurrentSession().save(proxyItem);
				continue;
			}
			Object pojoItem = find(proxyItem, lstPojoObject);
			if (pojoItem == null)
				lstDels.add(proxyItem);
		}
		lstProxyObject.removeAll(lstDels);
		for (Object del : lstDels)
			getCurrentSession().delete(del);

	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <V> void updatePojoList(Object proxy, String lstFieldName, Object pojo, ReconcileObj<V> reconcileObj)
			throws Exception {
		if (Formater.isNull(lstFieldName))
			return;
		Field f = ReflectionUtils.getField(pojo.getClass(), lstFieldName);
		if (!f.isAccessible())
			f.setAccessible(true);
		ParameterizedTypeImpl t1 = (ParameterizedTypeImpl) f.getGenericType();
		Type[] fieldType = t1.getActualTypeArguments();
		Class<?> objClss = (Class<?>) fieldType[0];
		Method fGetter = ReflectionUtils.getGeter(pojo.getClass(), f);
		List lstProxyObject = (List) fGetter.invoke(proxy);
		List lstPojoObject = (List) f.get(pojo);
		ClassMetadata pojoItemClassMetadata = getClassMetadata(objClss);
		Method setParent = getSetterParent(objClss, pojo.getClass());
		// Them, sua
		for (Object pojoItem : lstPojoObject) {
			Serializable id = null;
			try {
				id = pojoItemClassMetadata.getIdentifier(pojoItem, (SessionImplementor) getCurrentSession());
			} catch (Exception e) {
				logger.info(String.format("objClss: %s, lstFieldName: %s",
						new Object[] { objClss.getCanonicalName(), lstFieldName }));
				logger.error(e.getMessage(), e);
			}
			// Them moi
			if (id == null) {
				if (setParent != null)
					setParent.invoke(pojoItem, pojo);
				getCurrentSession().persist(pojoItem);
				continue;
			}

			Object proxyItem = find(pojoItem, lstProxyObject);
			// Xoa mot phan
			if (proxyItem == null) {
				lstProxyObject.remove(proxyItem);
				getCurrentSession().delete(proxyItem);
			} else {
				if (reconcileObj != null)
					reconcileObj.reconcile((V) proxyItem, (V) pojoItem, null);
			}
		}
		// Xoa phan con lai
		List lstDels = new ArrayList();
		for (Object proxyItem : lstProxyObject) {
			Object pojoItem = find(proxyItem, lstPojoObject);
			if (pojoItem == null) {
				lstDels.add(proxyItem);
			}

		}
		for (Object proxyItem : lstDels) {
			lstProxyObject.remove(proxyItem);
			getCurrentSession().delete(proxyItem);
		}

	}
}
