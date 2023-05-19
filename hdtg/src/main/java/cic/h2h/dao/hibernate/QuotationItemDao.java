package cic.h2h.dao.hibernate;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import common.util.ReflectionUtils;
import common.util.ReflectionUtils.ReconcileObj;
import common.util.ResourceException;
import entity.AstMachine;
import entity.Company;
import entity.DLVPackageDetail;
import entity.Quotation;
import entity.QuotationItem;
import entity.QuotationItemExe;
import entity.WorkOrder;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysDictParamDao;
import frwk.dao.hibernate.sys.SysUsersDao;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

@SuppressWarnings("unchecked")
@Repository(value = "quotationItemDao")
public class QuotationItemDao extends H2HBaseDao<QuotationItem> {
	private static final Logger logger = Logger.getLogger(BaseDao.class);
	@Autowired
	private SysUsersDao sysUsersDao;
	@Autowired
	private SysDictParamDao sysDictParamDao;
	@Autowired
	private BssFactoryUnitDao bssFactoryUnitDao;

	public QuotationItem loadOnly(String id) {
		Object[] o = (Object[]) getCurrentSession().createNativeQuery(
				"select id,code,manage_code,ac_id,singgle,factory_unit,work_order_number from quotation_item where id = :id")
				.addScalar("id", new StringType()).addScalar("code", new StringType())
				.addScalar("manage_code", new StringType()).addScalar("ac_id", new StringType())
				.addScalar("singgle", new StringType()).addScalar("factory_unit", new StringType())
				.addScalar("work_order_number", new StringType()).setParameter("id", id).uniqueResult();
		QuotationItem qi = new QuotationItem();
		qi.setId((String) o[0]);
		qi.setCode((String) o[1]);
		qi.setManageCode((String) o[2]);
		if (o[3] != null)
			qi.setAcId(sysUsersDao.load((String) o[3]));
		if (o[4] != null)
			qi.setSiggle(sysDictParamDao.load((String) o[4]));
		if (o[5] != null)
			qi.setFactoryUnit(bssFactoryUnitDao.load((String) o[5]));
		qi.setWorkOderNumber((String) o[6]);
		return qi;
	}

	@SuppressWarnings("deprecation")
	public List<String> getLstIdByManagerCode(String managerCode) {
		Criteria cr = getCurrentSession().createCriteria(QuotationItem.class).add(Restrictions.eq("code", managerCode))
				.setProjection(Projections.projectionList().add(Projections.property("id"), "id"))
				.setResultTransformer(Transformers.aliasToBean(QuotationItem.class));
		List<String> strings = new ArrayList<String>();
		if (!Formater.isNull(cr.list())) {
			List<QuotationItem> items = cr.list();
			for (QuotationItem item : items) {
				strings.add(item.getId());
			}
		}
		return strings;
	}

	@Override
	public void del(QuotationItem item) throws Exception {
		getCurrentSession().delete(item);
	}

	@Override
	public void save(QuotationItem item) throws Exception {
		if (Formater.isNull(item.getId())) {
			if (getEquals(item) != null)
				throw new ResourceException(String.format("Mã quản lý %s đã tồn tại", item.getManageCode()));
			super.save(item);
		} else {
			QuotationItem itemInDb = quotationItemDao.get(QuotationItem.class, item.getId());
			updatePojoList(itemInDb, "quotationItemAllExeList", item, null);
			// Cac thong tin khong thay doi
			item.setCompany(itemInDb.getCompany());
			item.setNmOfFnsItem(itemInDb.getNumOfFinishItem());
			if (getEquals(item) != null)
				throw new ResourceException(String.format("Mã quản lý %s đã tồn tại", item.getManageCode()));
			getCurrentSession().merge(item);
		}

	}

	@Autowired
	private QuotationDao quotationDao;

	private QuotationItem getEquals(QuotationItem item) {
		Criteria c = getCurrentSession().createCriteria(QuotationItem.class);
		c.add(Restrictions.eq("company", item.getCompany()));
		c.add(Restrictions.eq("manageCode", item.getManageCode()));
		if (!Formater.isNull(item.getId()))
			c.add(Restrictions.ne("id", item.getId()));
		List<QuotationItem> tmp = c.setMaxResults(1).list();
		if (tmp.isEmpty())
			return null;
		return tmp.get(0);
	}

	@Override
	public <V> void updatePojoList(Object proxy, String lstFieldName, Object pojo, ReconcileObj<V> reconcileObj)
			throws Exception {
		if (Formater.isNull(lstFieldName))
			return;
		Field f = ReflectionUtils.getField(pojo.getClass(), lstFieldName);
		if (!f.isAccessible())
			f.setAccessible(true);
		ParameterizedTypeImpl t1 = (ParameterizedTypeImpl) f.getGenericType();
		java.lang.reflect.Type[] fieldType = t1.getActualTypeArguments();
		Class<?> objClss = (Class<?>) fieldType[0];
		Method fGetter = ReflectionUtils.getGeter(pojo.getClass(), f);
		List<?> lstProxyObject = (List<?>) fGetter.invoke(proxy);
		List<?> lstPojoObject = (List<?>) f.get(pojo);
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
				delete((QuotationItemExe) proxyItem);
			} else {
				if (reconcileObj != null)
					reconcileObj.reconcile((V) proxyItem, (V) pojoItem, null);
			}
		}
		// Xoa phan con lai
		List<Object> lstDels = new ArrayList<Object>();
		for (Object proxyItem : lstProxyObject) {
			Object pojoItem = find(proxyItem, lstPojoObject);
			if (pojoItem == null) {
				lstDels.add(proxyItem);
			}

		}
		for (Object proxyItem : lstDels) {
			lstProxyObject.remove(proxyItem);
			delete((QuotationItemExe) proxyItem);
		}

	}

	@Autowired
	private WorkOrderDao workOrderDao;

	private void delete(QuotationItemExe qie) throws Exception {
		for (WorkOrder wo : qie.getWorkOrders())
			workOrderDao.del(wo);

	}

	@Autowired
	private QuotationItemDao quotationItemDao;

	public Boolean exists(String code) {
		List<Quotation> lstTemp = getCurrentSession().createCriteria(QuotationItem.class)
				.add(Restrictions.like("code", code, MatchMode.ANYWHERE).ignoreCase()).setMaxResults(1).list();
		return lstTemp.size() > 0;
	}

	public List<QuotationItem> getQuotationItems(String quotationId) {
		Criteria criteria = getCurrentSession().createCriteria(QuotationItem.class);
		criteria.add(Restrictions.eq("quotationId.id", quotationId));
		return criteria.list();
	}

	public Boolean existsOther(String code, String quotationId) {
		Criteria criteria = getCurrentSession().createCriteria(QuotationItem.class)
				.add(Restrictions.eq("code", code).ignoreCase());
		if (!Formater.isNull(quotationId))
			criteria.add(Restrictions.ne("quotationId.id", quotationId));
		List<Quotation> lstTemp = criteria.setMaxResults(1).list();
		return lstTemp.size() > 0;
	}

	public Boolean exists(String id, String code) {
		Criteria criteria = getCurrentSession().createCriteria(QuotationItem.class)
				.add(Restrictions.eq("code", code).ignoreCase());
		if (!Formater.isNull(id))
			criteria.add(Restrictions.ne("id", id));
		List<Quotation> lstTemp = criteria.setMaxResults(1).list();
		return lstTemp.size() > 0;
	}

	public List<AstMachine> getAssignedMachine(QuotationItem qi) {
		Criteria c = getCurrentSession().createCriteria(AstMachine.class);
		c.add(Restrictions.sqlRestriction(
				"exists\r\n" + " (select 1\r\n"
						+ "          from quotation_item qi, quotation_item_exe qie, work_order wo\r\n"
						+ "         where qi.id = qie.quotation_item_id\r\n"
						+ "           and qie.id = wo.quotation_item_exe_id\r\n"
						+ "           and wo.machine_id = {alias}.id and qi.id = ?)",
				new Object[] { qi.getId() }, new Type[] { new StringType() }));
		return c.list();

	}

	public List<QuotationItem> getListByThreadSession(List<String> itemFinishs) {
		List<QuotationItem> quotationItems = new ArrayList<QuotationItem>();
		Criteria criteria = getCurrentSession().createCriteria(QuotationItem.class);
		criteria.add(Restrictions.in("id", itemFinishs));
		quotationItems = criteria.list();
		return quotationItems;
	}

	public Long getNumOfDelivered(QuotationItem qi) {
		Criteria c = getCurrentSession().createCriteria(DLVPackageDetail.class);
		c.add(Restrictions.eq("quotationItem", qi));
		List<DLVPackageDetail> lstDetail = c.list();
		Long lRs = 0l;
		for (DLVPackageDetail detail : lstDetail)
			lRs += detail.getAmount();
		return lRs;
	}

	public QuotationItem getByManageCode(String manageCode, Company company) {
		Criteria criteria = getCurrentSession().createCriteria(QuotationItem.class)
				.add(Restrictions.eq("manageCode", manageCode));
		criteria.createAlias("quotationId", "q");
		criteria.add(Restrictions.eq("q.company", company));
		List<QuotationItem> lstTemp = criteria.setMaxResults(1).list();
		if (lstTemp.size() > 0)
			return lstTemp.get(0);
		else
			return null;
	}

	public BigDecimal numOfFinishItem(String id) {
		return getCurrentSession().doReturningWork(new ReturningWork<BigDecimal>() {
			public BigDecimal execute(Connection connection) throws SQLException {
				CallableStatement call = connection.prepareCall("{ ? = call MES_EXE.numOfFinishItem(?) }");
				call.registerOutParameter(1, Types.DECIMAL);
				call.setString(2, id);
				call.execute();
				return call.getBigDecimal(1);
			}
		});

	}
}
