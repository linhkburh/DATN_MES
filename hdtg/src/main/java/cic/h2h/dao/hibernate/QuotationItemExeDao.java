package cic.h2h.dao.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.ibm.icu.util.Calendar;

import common.util.FormatNumber;
import common.util.Formater;
import common.util.ReflectionUtils;
import common.util.ResourceException;
import entity.QuotationItem;
import entity.QuotationItemExe;
import entity.WorkOrder;

@Repository(value = "quotationItemExeDao")
public class QuotationItemExeDao extends H2HBaseDao<QuotationItemExe> {
	public void makeWorkOrder(QuotationItemExe qie) throws Exception {
		QuotationItemExe qieInDb = get(QuotationItemExe.class, qie.getId());
		// Validate
		if (!qie.getWorkOrders().isEmpty()) {
			Boolean isFinish = true;
			BigDecimal bTotal = new BigDecimal("0");
			for (WorkOrder wo : qie.getWorkOrders()) {
				if (!Formater.isNull(wo.getId())) {
					WorkOrder woIndb = getCurrentSession().get(WorkOrder.class, wo.getId());
					if (woIndb.getNumOfFinishChildren() != null) {
						if (woIndb.getNumOfFinishChildren().compareTo(wo.getAmount()) > 0)
							throw new ResourceException(String.format(
									"Số lượng chi tiết của lệnh sản xuất %s: %s, nhỏ hơn tổng số chi tiết đã sản xuất: %s",
									new Object[] { wo.getCode(), FormatNumber.num2Str(wo.getAmount()),
											FormatNumber.num2Str(woIndb.getNumOfFinishChildren()) }));
						else if (woIndb.getNumOfFinishChildren().compareTo(wo.getAmount()) < 0)
							isFinish = false;
					} else
						isFinish = false;
				} else {
					wo.setCreateDate(Calendar.getInstance().getTime());
					isFinish = false;
				}
				bTotal = bTotal.add(wo.getAmount());
			}
			if (bTotal.doubleValue() > qieInDb.getQuotationItemId().getQuality().doubleValue()) {
				throw new ResourceException(
						String.format("Tổng số chi tiết của các lệnh sản xuất là %s vượt quá số chi tiết của bản vẽ %s",
								new Object[] { Formater.num2str(bTotal),
										Formater.num2str(qieInDb.getQuotationItemId().getQuality()) }));
			}
			if (isFinish)
				throw new ResourceException(
						"Tất cả các lệnh sản xuất đã hoàn thành, không thể chỉnh sửa lệnh sản xuất!");
		}
		// Update
		updateWorkOrder(qieInDb, qie);
	}

	private void updateWorkOrder(QuotationItemExe qieInDb, QuotationItemExe qie) throws Exception {
		List<WorkOrder> lstProxyObject = qieInDb.getWorkOrders();
		List<WorkOrder> lstPojoObject = qie.getWorkOrders();
		// Them, sua
		for (WorkOrder pojoItem : lstPojoObject) {
			pojoItem.setQuotationItemExe(qieInDb);
			boolean add = pojoItem.getId() == null;
			if (add) {
				lstProxyObject.add(pojoItem);

			} else {
				WorkOrder proxyItem = (WorkOrder) find(pojoItem, lstProxyObject);
				new ReflectionUtils.ReconcileObj<WorkOrder>() {
					@Override
					public void reconcile(WorkOrder desObj, WorkOrder sourceObj, List<String> lstIgnoreFields)
							throws Exception {
						ReflectionUtils.ReconcileObj.super.reconcile(desObj, sourceObj, lstIgnoreFields);
						desObj.setAstMachine(sourceObj.getAstMachine());
					}
				}.reconcile(proxyItem, pojoItem, new ArrayList<String>(Arrays.asList("createDate")));
			}
		}

		// Xoa
		List<WorkOrder> lstDels = new ArrayList<WorkOrder>();
		// lstProxyObject hien tai da bao gom cac object duoc them
		for (WorkOrder proxyItem : lstProxyObject) {
			if (!getCurrentSession().contains(proxyItem)) {
				getCurrentSession().save(proxyItem);
				continue;
			}
			WorkOrder pojoItem = (WorkOrder) find(proxyItem, lstPojoObject);
			if (pojoItem == null)
				lstDels.add(proxyItem);
		}
		lstProxyObject.removeAll(lstDels);
		for (WorkOrder del : lstDels) {
			// Kiem tra lenh san xuat da duoc thuc hien
			if (!del.getWorkOrderExes().isEmpty())
				throw new ResourceException("Không được loại bỏ lệnh sản xuất %s đã được thực hiện!", del.getCode());
			getCurrentSession().delete(del);
		}

	}

	public void deleteWorkOrder(QuotationItemExe qie) throws ResourceException {
		for (WorkOrder wo : qie.getWorkOrders()) {
			if (!wo.getWorkOrderExes().isEmpty())
				throw new ResourceException("Lệnh sản xuất %s đã được thực hiện, không thể xóa", wo.getCode());
			getCurrentSession().delete(wo);
		}
	}
	
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<String> getItemExeIds(List<String> quotationItemIds, String exeStepId) {
		Criteria criteria = getCurrentSession().createCriteria(QuotationItemExe.class);
		criteria.add(Restrictions.in("quotationItemId.id", quotationItemIds));
		criteria.add(Restrictions.eq("exeStepId.id", exeStepId));
		criteria.setProjection(Projections.projectionList()
				.add(Projections.property("id"), "id")).setResultTransformer(Transformers.aliasToBean(QuotationItemExe.class));
		List<String> strings = new ArrayList<String>();
		if (!Formater.isNull(criteria.list())) {
		List<QuotationItemExe>  items = criteria.list();
		for (QuotationItemExe item :  items) {
			strings.add(item.getId());
			}
		}
		return strings;
	}
}
