package cic.h2h.controller.mes;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ibm.icu.util.Calendar;

import cic.h2h.dao.hibernate.QuotationItemDao;
import cic.h2h.dao.hibernate.QuotationItemExeDao;
import cic.h2h.form.QuotationItemExeForm;
import common.util.FormatNumber;
import common.util.Formater;
import entity.Customer;
import entity.QuotationItemExe;
import frwk.controller.SearchController;
import frwk.dao.hibernate.BaseDao;

@Controller
@RequestMapping(value = "/lsx")
public class QuotationItemExeOpController extends SearchController<QuotationItemExeForm, QuotationItemExe> {
	@Autowired
	private QuotationItemExeDao quotationItemExeDao;

	@Override
	public void createSearchDAO(HttpServletRequest request, QuotationItemExeForm form, BaseDao<QuotationItemExe> dao)
			throws Exception {
		dao.createAlias("exeStepId", "step");
		// Khong phai cong doan lap trinh (lap trinh phan cong tai chuc nang khac)
		dao.addRestriction(
				Restrictions.or(Restrictions.eq("step.program", Boolean.FALSE), Restrictions.isNull("step.program")));
		// Luon dat alias trong bang quotation item, do order theo ngay lap ban ve
		dao.createAlias("quotationItemId", "qi");
		if (!Formater.isNull(form.getDrawingCode())) {
			dao.addRestriction(
					Restrictions.like("qi.code", form.getDrawingCode().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
		if (!Formater.isNull(form.getQiName()))
			dao.addRestriction(Restrictions.like("qi.name", form.getQiName().trim(), MatchMode.ANYWHERE).ignoreCase());
		if (!Formater.isNull(form.getManageCode()))
			dao.addRestriction(
					Restrictions.like("qi.manageCode", form.getManageCode().trim(), MatchMode.ANYWHERE).ignoreCase());
		dao.createAlias("qi.quotationId", "q");
		dao.createAlias("q.customer", "cus");
		// Search trong bang quotation
		if (!Formater.isNull(form.getOrderCode())) {
			dao.addRestriction(
					Restrictions.like("q.code", form.getOrderCode().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
		if (!Formater.isNull(form.getCusName())) {
			dao.addRestriction(
					Restrictions.like("cus.orgName", form.getCusCode().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
		if (!Formater.isNull(form.getCusCode())) {
			dao.addRestriction(
					Restrictions.like("cus.code", form.getCusCode().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
		if (form.getFromDate() != null) {
			dao.addRestriction(Restrictions.ge("q.quotationDate", form.getFromDate()));
		}
		if (form.getToDate() != null) {
			Calendar cTodatePlusOne = Calendar.getInstance();
			cTodatePlusOne.setTime(form.getToDate());
			cTodatePlusOne.add(Calendar.DATE, 1);
			dao.addRestriction(Restrictions.lt("q.quotationDate", cTodatePlusOne.getTime()));
		}

		// Trang thai
		if (form.getAssignStatus() != null) {
			// Chua tao LSX
			if (form.getAssignStatus() == -1) {
				dao.addRestriction(Restrictions.sqlRestriction(
						"not exists(select 1 from work_order wo where wo.QUOTATION_ITEM_EXE_ID = {alias}.id)"));
			} else if (form.getAssignStatus() == 1) { // Da tao LSX
				dao.addRestriction(Restrictions.sqlRestriction(
						"(select quality from QUOTATION_ITEM qi where qi.id={alias}.quotation_item_id) = (select sum(amount) from work_order wo where wo.quotation_item_exe_id = {alias}.id)"));
			} else if (form.getAssignStatus() == 0) {// Dang tao LSX
				dao.addRestriction(Restrictions.sqlRestriction(
						"exists(select 1 from work_order wo where wo.QUOTATION_ITEM_EXE_ID = {alias}.id) and ((select quality from QUOTATION_ITEM qi where qi.id={alias}.quotation_item_id) <> (select sum(amount) from work_order wo where wo.quotation_item_exe_id = {alias}.id))"));
			}
		}
		// Sap xep theo ngay tao don hang
		dao.addOrder(Order.desc("q.createDate"));
		// Ma ban ve
		dao.addOrder(Order.asc("qi.code"));
		// Ma lsx
		dao.addOrder(Order.asc("id"));

	}

	@Override
	protected void pushToJa(JSONArray ja, QuotationItemExe e, QuotationItemExeForm modelForm) throws Exception {
		ja.put(e.getQuotationItemId().getQuotationId().getCode());
		Customer khachHang = e.getQuotationItemId().getQuotationId().getCustomer();
		ja.put(khachHang.getOrgName());
		ja.put(e.getQuotationItemId().getCode());
		ja.put(e.getExeStepId().getStepType().getCode() + "-" + e.getExeStepId().getStepName());
		e.summary();
		// Tong so chi tiet
		ja.put(FormatNumber.num2Str(e.getQuotationItemId().getQuality()));
		// So chi tiet da phan cong
		ja.put(FormatNumber.num2Str(e.getAssignAmount()));
		// So chi tiet con lai
		ja.put(FormatNumber.num2Str(e.getQuotationItemId().getQuality().subtract(e.getAssignAmount())));
		String color = null, statusText = null;
		Long remain = e.getRemain().longValue();
		if (remain == null || remain == 0) {
			color = "blue";
			statusText = "Đã tạo lệnh sản xuất";
		}

		else if (remain == e.getQuotationItemId().getQuality().longValue()) {
			color = "red";
			statusText = "Tạo lệnh sản xuất";

		} else {
			color = "violet";
			statusText = "Đang tạo lệnh sản xuất";
		}
		String href = String.format(
				"<a style='color:%s;' href='#' onclick='window.open(\"datlenhsx?hiddenMenu=true&quotationItemExe.id=%s\",\"\",%s)'>%s</a>",
				new Object[] { color, e.getId(), "big_window_property", statusText });
		ja.put(href);

	}

	@Override
	public BaseDao<QuotationItemExe> getDao() {
		return quotationItemExeDao;
	}

	@Override
	public String getJsp() {

		return "quan_tri_nghiep_vu/danh_muc_lsx";
	}

	@Autowired
	private QuotationItemDao quotationItemDao;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QuotationItemExeForm form)
			throws Exception {
		// Duoc goi tu form khac (kg phai tu menu). Truyen gia tri tren request
		if (!Formater.isNull(form.getQuotationItem().getId())) {
			quotationItemDao.load(form.getQuotationItem());
			form.setOrderCode(form.getQuotationItem().getQuotationId().getCode());
			form.setDrawingCode(form.getQuotationItem().getCode());
			form.setManageCode(form.getQuotationItem().getManageCode());
			form.setQiName(form.getQuotationItem().getName());
		}

	}

	public QuotationItemExeDao getQuotationItemExeDao() {
		return quotationItemExeDao;
	}

	public void setQuotationItemExeDao(QuotationItemExeDao quotationItemExeDao) {
		this.quotationItemExeDao = quotationItemExeDao;
	}

}
