package cic.h2h.controller.mes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ibm.icu.util.Calendar;

import cic.h2h.dao.hibernate.CustomerManageDao;
import cic.h2h.dao.hibernate.QcChkOutSrcDao;
import cic.h2h.dao.hibernate.WorkOrderDao;
import cic.h2h.form.QcChkOutSrcForm;
import common.util.Formater;
import common.util.ResourceException;
import entity.Customer;
import entity.QcChkOutSrc;
import entity.QcChkOutSrcDetail;
import entity.QuotationItem;
import entity.QuotationItemExe;
import entity.WorkOrder;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysDictParamDao;
import frwk.dao.hibernate.sys.SysUsersDao;

@Controller
@RequestMapping(value = "/QcChkOutSrc")
public class QcChkOutSrcController extends CatalogController<QcChkOutSrcForm, QcChkOutSrc> {

	@Autowired
	private QcChkOutSrcDao qcChkOutSrcDao;

	@Override
	public void createSearchDAO(HttpServletRequest request, QcChkOutSrcForm form, BaseDao<QcChkOutSrc> dao)
			throws Exception {

	}

	@Override
	protected void pushToJa(JSONArray ja, QcChkOutSrc e, QcChkOutSrcForm modelForm) throws Exception {

	}

	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QcChkOutSrcForm form)
			throws Exception {
		QcChkOutSrc chkOutSrc = form.getModel();
		if (chkOutSrc.getAmount() == null || chkOutSrc.getAmount() < 0)
			throw new ResourceException(
					"Tổng số NG/hủy phải nhỏ hơn số lượng nhận từ sản xuất (Số lượng OK phải >=0) !");
		chkOutSrc.setCreator(getSessionUser());
		chkOutSrc.setCreateDate(Calendar.getInstance().getTime());
		// Kiem tra du lieu trung
		if (!Formater.isNull(chkOutSrc.getQcChkOutSrcDetails())) {
			for (int i = 0; i < chkOutSrc.getQcChkOutSrcDetails().size(); i++) {
				QcChkOutSrcDetail itemI = chkOutSrc.getQcChkOutSrcDetails().get(i);
				for (int j = i + 1; j < chkOutSrc.getQcChkOutSrcDetails().size(); j++) {
					QcChkOutSrcDetail itemJ = chkOutSrc.getQcChkOutSrcDetails().get(j);
					if (itemI.getWorkOrder().getId().equals(itemJ.getWorkOrder().getId()))
						throw new ResourceException("Công đoạn dòng %s, trùng với dòng %s",
								new Object[] { j + 1, i + 1 });
				}
				if ((itemI.getBrokenAmount() == null || itemI.getBrokenAmount() == 0)
						&& (itemI.getNgAmount() == null || itemI.getNgAmount() == 0))
					throw new ResourceException("Cần phải nhập ít nhất số lượng NG hoặc số lượng hỏng cho dòng %s",
							String.valueOf(i + 1));
				itemI.setQcChkOutSrc(chkOutSrc);
			}
		}
		qcChkOutSrcDao.save(chkOutSrc);
		// Tra lai client de refresh giao dien
		returnJson(rs, chkOutSrc);
	}

	@Override

	public void del(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QcChkOutSrcForm form)
			throws Exception {
		qcChkOutSrcDao.load(form.getModel());
		qcChkOutSrcDao.del(form.getModel());

	}

	public void validate(WorkOrder wo) throws ResourceException {
		if (wo.getRepairedNgAmount() < 0)
			throw new ResourceException("Số lượng đã sửa của LSX là %s <0, đề nghị kiểm tra lại dữ liệu",
					Formater.num2str(wo.getNgAmount()));
		if (wo.getNgAmount() != null && wo.getNgAmount() < 0)
			throw new ResourceException("Số lượng NG của LSX là %s <0, đề nghị kiểm tra lại dữ liệu",
					Formater.num2str(wo.getNgAmount()));
		if (wo.getAmount() != null && wo.getAmount().compareTo(new BigDecimal(0)) < 0)
			throw new ResourceException("Số lượng hoàn thành của LSX là %s <0, đề nghị kiểm tra lại dữ liệu",
					Formater.num2str(wo.getAmount()));
		if (wo.getBrokenAmount() != null && wo.getBrokenAmount() < 0)
			throw new ResourceException("Số lượng hủy của LSX là %s <0, đề nghị kiểm tra lại dữ liệu",
					Formater.num2str(wo.getNgAmount()));

	}

	@Override
	public BaseDao<QcChkOutSrc> getDao() {

		return qcChkOutSrcDao;
	}

	@Override
	public String getJsp() {

		return "ke_hoach_san_xuat/chi_tiet_chuyen_gia_cong_ngoai";
	}

	@Autowired
	private WorkOrderDao workOrderDao;

	@Autowired
	private SysUsersDao sysUsersDao;
	@Autowired
	private SysDictParamDao sysDictParamDao;
	@Autowired
	private CustomerManageDao customerManageDao;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QcChkOutSrcForm form)
			throws Exception {
		String id = rq.getParameter("id");
		WorkOrder workOrder = null;
		if (!Formater.isNull(id)) {
			QcChkOutSrc outSrc = qcChkOutSrcDao.get(id);
			form.setQcChkOutSrc(outSrc);
			workOrder = outSrc.getWorkOrder();
		} else {
			String workOrderId = rq.getParameter("workOrderId");
			if (!Formater.isNull(workOrderId)) {
				workOrder = workOrderDao.get(workOrderId);
				form.getQcChkOutSrc().setWorkOrder(workOrder);
				form.getQcChkOutSrc()
						.setTotalAmount(workOrder.getNumOfFinishItem().longValue() - workOrder.getToOsedAmount());
				form.getQcChkOutSrc().setAmount(form.getQcChkOutSrc().getTotalAmount());
			}
		}

		// Xac dinh cac cong doan gia cong theo QuotationItem
		model.addAttribute("exeSteps", getListQie(workOrder));
		model.addAttribute("lstSysUser", sysUsersDao.getCompanyUser(getSessionUser().getCompany()));
		model.addAttribute("lstErrorCause", sysDictParamDao.getByType("ERROR_CAUSE_EXE"));
		model.addAttribute("lstCus", customerManageDao.getByType(Customer.IS_PARTNER));
	}

	/**
	 * Xac dinh cac cong doan gia cong theo QuotationItem
	 * 
	 * @param workOrder
	 */
	private List<WorkOrder> getListQie(WorkOrder workOrder) {
		// Vi tri cua cong doan gia cong ngoai gan nhat so voi cong doan hien tai
		Short idxOfPreOsStep = null;
		List<WorkOrder> exeSteps = new ArrayList<WorkOrder>();
		QuotationItem qi = workOrder.getQuotationItemExe().getQuotationItemId();
		for (QuotationItemExe qie : qi.getQuotationItemAllExeList()) {
			// Tim qie
			if (Boolean.TRUE.equals(qie.getExeStepId().getProgram()))
				continue;
			// Cung cong doan gia cong
			if (qie.getExeStepId().getId().equals(workOrder.getQuotationItemExe().getExeStepId().getId()))
				continue;
			// Xep sau cong doan OS nay
			if (qie.getDisOrder() >= workOrder.getQuotationItemExe().getDisOrder())
				continue;
			if ("OS".equals(qie.getExeStepId().getStepType().getCode())) {
				if (idxOfPreOsStep == null)
					idxOfPreOsStep = qie.getDisOrder();
				else if (qie.getDisOrder() > idxOfPreOsStep)
					idxOfPreOsStep = qie.getDisOrder();
				continue;
			}
			if (idxOfPreOsStep != null) {
				// Sep truoc cong doan gan nhat
				if (qie.getDisOrder() <= idxOfPreOsStep)
					continue;
			}
			for (WorkOrder wo : qie.getWorkOrders()) {
				// Cung day chuyen
				if (wo.getProductionLineId().equals(wo.getProductionLineId())) {
					exeSteps.add(wo);
					break;
				}
			}
		}
		// Sap xep
		Collections.sort(exeSteps, new Comparator<WorkOrder>() {
			@Override
			public int compare(WorkOrder before, WorkOrder after) {
				return before.getQuotationItemExe().getDisOrder().compareTo(after.getQuotationItemExe().getDisOrder());
			}
		});
		if (idxOfPreOsStep == null)
			idxOfPreOsStep = workOrder.getQuotationItemExe().getDisOrder();
		Iterator<WorkOrder> iter = exeSteps.iterator();
		while (iter.hasNext()) {
			WorkOrder item = iter.next();
			if (item.getQuotationItemExe().getDisOrder() >= idxOfPreOsStep)
				iter.remove();
		}
		return exeSteps;
	}
}
