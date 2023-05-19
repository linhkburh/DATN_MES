package cic.h2h.controller.mes;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.ibm.icu.util.Calendar;
import cic.h2h.dao.hibernate.CustomerManageDao;
import cic.h2h.dao.hibernate.QcChkOutSrcDao;
import cic.h2h.dao.hibernate.QcOsDao;
import cic.h2h.dao.hibernate.QuotationItemDao;
import cic.h2h.dao.hibernate.WorkOrderDao;
import cic.h2h.form.QcChkOutSrcForm;
import common.util.FormatNumber;
import common.util.Formater;
import common.util.JsonUtils;
import common.util.ResourceException;
import entity.Customer;
import entity.DLVPackage;
import entity.QcChkOutSrc;
import entity.QcChkOutSrcDetail;
import entity.QcOs;
import entity.QcOsDetail;
import entity.QuotationItem;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysUsersDao;

@Controller
@RequestMapping(value = "osHistory")
public class HistoryOSController extends CatalogController<QcChkOutSrcForm, QcChkOutSrc> {

	@Autowired
	private QcChkOutSrcDao qcChkOutSrcDao;

	@Override
	public void createSearchDAO(HttpServletRequest request, QcChkOutSrcForm form, BaseDao<QcChkOutSrc> dao)
			throws Exception {

		if (!Formater.isNull(form.getWorker())) {
			dao.addRestriction(Restrictions.eq("worker.id", form.getWorker()));
		}
		if (!Formater.isNull(form.getFrDate()))
			dao.addRestriction(Restrictions.ge("createDate", Formater.str2date(form.getFrDate())));
		if (!Formater.isNull(form.gettDate())) {
			Calendar t = Calendar.getInstance();
			t.setTime(Formater.str2date(form.gettDate()));
			t.add(Calendar.DATE, 1);
			dao.addRestriction(Restrictions.lt("createDate", t.getTime()));
		}

		if (!Formater.isNull(form.getUpdatorSearch())) {
			dao.addRestriction(Restrictions.eq("creator.id", form.getUpdatorSearch()));
		}
		boolean checkWC = false;
		if (!Formater.isNull(form.getWorkCode())) {
			checkWC = true;
			dao.createAlias("workOrder", "w");
			dao.addRestriction(Restrictions.like("w.code", form.getWorkCode().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
		boolean chkQieQi = false;
		if (!Formater.isNull(form.getManageCode()) || !Formater.isNull(form.getDrawingCode())) {
			if (!checkWC) {
				checkWC = true;
				dao.createAlias("workOrder", "w");
			}
			chkQieQi = true;
			dao.createAlias("w.quotationItemExe", "qie");
			dao.createAlias("qie.quotationItemId", "qi");
			// Ma quan ly
			if (!Formater.isNull(form.getManageCode())) {
				Criterion cCode = null;
				String[] temp = form.getManageCode().split(";");
				for (String code : temp) {
					if (cCode == null)
						cCode = Restrictions.eq("qi.manageCode", code.trim());
					else
						cCode = Restrictions.or(cCode, Restrictions.eq("qi.manageCode", code.trim()));
				}
				dao.addRestriction(cCode);
			}
			// Ma ban ve
			if (!Formater.isNull(form.getDrawingCode())) {
				Criterion cCode = null;
				String[] temp = form.getDrawingCode().split(";");
				for (String code : temp) {
					if (cCode == null)
						cCode = Restrictions.eq("qi.code", code.trim());
					else
						cCode = Restrictions.or(cCode, Restrictions.eq("qi.code", code.trim()));
				}
				dao.addRestriction(cCode);
			}
		}
		if ("OS".equals(form.getType())) {
			if (!checkWC) {
				dao.createAlias("workOrder", "w");
				checkWC = true;
			}
			dao.addRestriction(Restrictions.gtProperty("amount", "w.totalToOs"));
		} else {
			// Lich su gia cong ngoai
			dao.addRestriction(Restrictions.isNotNull("creator"));
		}
		if (!Formater.isNull(form.getCusCode()) || !Formater.isNull(form.getOrderCode())) {
			if (!chkQieQi) {
				if (!checkWC) {
					checkWC = true;
					dao.createAlias("workOrder", "w");
				}
				chkQieQi = true;
				dao.createAlias("w.quotationItemExe", "qie");
				dao.createAlias("qie.quotationItemId", "qi");
			}

			if (!Formater.isNull(form.getOrderCode()))
				dao.addRestriction(
						Restrictions.like("qi.code", form.getOrderCode().trim(), MatchMode.ANYWHERE).ignoreCase());
			if (!Formater.isNull(form.getCusCode())) {
				dao.createAlias("qi.quotationId", "q");
				dao.addRestriction(Restrictions.eq("q.customer.id", form.getCusCode()));
			}

		}
	}

	@Override
	protected void pushToJa(JSONArray ja, QcChkOutSrc e, QcChkOutSrcForm modelForm) throws Exception {
		QuotationItem qi = e.getWorkOrder().getQuotationItemExe().getQuotationItemId();
		if ("OS".equals(modelForm.getType())) {
			// Chua thuc hien QC
			if (e.getStartTime() == null) {
				ja.put("<a href = 'javascript:;' title='Thực hiện QC' style='color:darkviolet' onclick='openDetail(\""
						+ e.getId() + "\",\"" + e.getWorkOrder().getId() + "\")'>" + e.getWorkOrder().getCode()
						+ "</a>");
			} else {
				// Da thuc hien QC
				ja.put("<a href = 'javascript:;' title='Kết quả QC' onclick='openDetail(\"" + e.getId() + "\",\""
						+ e.getWorkOrder().getId() + "\")'>" + e.getWorkOrder().getCode() + "</a>");
			}
			ja.put(qi.getQuotationId().getCode());
			ja.put(String.format("<font title='%s'>%s<font>", new Object[] {
					qi.getQuotationId().getCustomer().getOrgName(), qi.getQuotationId().getCustomer().getCode() }));
			ja.put(qi.getCode());
			ja.put(Formater.date2str(qi.getDeliverDate()));
			ja.put(qi.getManageCode());
			ja.put(Formater.date2strDateTime(e.getCreateDate()));
			ja.put(Formater.num2str(e.getAmount()));
			ja.put(Formater.num2str(e.getTotalToOs()));
			ja.put("<input disabled='true' type='text' style='text-align: right;' class='textint' name='" + e.getId()
					+ "'/>");
			ja.put("<input type='checkbox' name='chk_" + e.getId()
					+ "' onclick='changeNumberFinish(this);' outStandingAmount='"
					+ (e.getAmount() - e.getTotalToOs().longValue()) + "' value='" + e.getId()
					+ "' class='selectRow'/>");
		} else {
			ja.put(e.getWorkOrder().getCode());
			ja.put(qi.getManageCode());
			if (e.getWorker() != null)
				ja.put(e.getWorker().getName());
			else
				ja.put("");
			ja.put(Formater.date2strDateTime(e.getStartTime()));
			ja.put(Formater.date2strDateTime(e.getEndTime()));
			if (e.getEndTime() != null && e.getStartTime() != null)
				ja.put(FormatNumber.num2Str((e.getEndTime().getTime() - e.getStartTime().getTime()) / 100 / 60));
			else
				ja.put("");
			ja.put(Formater.num2str(e.getTotalAmount()));
			ja.put(Formater.num2str(e.getAmount()));
			long ngAmount = 0, brokenAmount = 0;
			for (QcChkOutSrcDetail d : e.getQcChkOutSrcDetails()) {
				if (d.getNgAmount() != null)
					ngAmount += d.getNgAmount();
				if (d.getBrokenAmount() != null)
					brokenAmount += d.getBrokenAmount();
			}
			ja.put(Formater.num2str(ngAmount));
			ja.put(Formater.num2str(brokenAmount));
			if (e.getCreator() != null)
				ja.put(e.getCreator().getName());
			else
				ja.put("");
			ja.put("<a href = 'javascript:;' title='Cập nhật kết quả QC' onclick='openDetail(\"" + e.getId() + "\",\""
					+ e.getWorkOrder().getId() + "\")'>Cập nhật</a>");
		}
	}

	@Override
	public BaseDao<QcChkOutSrc> getDao() {

		return qcChkOutSrcDao;
	}

	@Override
	public String getJsp() {

		return "ke_hoach_san_xuat/lich_su_gia_cong_ngoai";
	}

	@Autowired
	private SysUsersDao sysUsersDao;
	@Autowired
	private WorkOrderDao workOrderDao;
	@Autowired
	private CustomerManageDao customerManageDao;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QcChkOutSrcForm form)
			throws Exception {
		model.addAttribute("lstCus", customerManageDao.getByType(Customer.IS_PARTNER));
		model.addAttribute("customers", customerManageDao.getByType(Customer.IS_CUSTOMER));
		model.addAttribute("lstSysUser", sysUsersDao.getCompanyUser(getSessionUser().getCompany()));
		form.setHisType("hQcO");
		if ("OS".equals(form.getType()))
			form.setQcType("btp");
	}

	/**
	 * Tao danh sach chuyen GCN ban thanh pham
	 * 
	 * @param ids
	 * @param datas
	 * @return
	 * @throws ParseException
	 * @throws ResourceException
	 */
	private List<QcOs> makeLstPackage(String ids[], String datas[]) throws ParseException, ResourceException {
		List<QcOs> lstQcOs = new ArrayList<QcOs>();
		for (int i = 0; i < ids.length; i++) {
			QcOsDetail detail = new QcOsDetail(qcChkOutSrcDao.get(ids[i]),
					FormatNumber.str2Long(datas[i]));
//			Long outStandingAmount = qcChkOutSrcDao.get(ids[i]).getAmount()-qcChkOutSrcDao.get(ids[i]).getWorkOrder().getTotalToOs();
//			if (detail.getAmount().compareTo(new BigDecimal(outStandingAmount)) > 0) {
//				throw new ResourceException("Mã quản lý %s, số lượng chuyển %s lớn số lượng còn lại %s",
//						new Object[] { detail.getQuotationItem().getManageCode(),
//								Formater.num2str(detail.getAmount()), FormatNumber.num2Str(outStandingAmount) });
//			}
			QcOs addQcOs = null;
			for (QcOs qcOS : lstQcOs) {
				// Cung workorder
				if (!qcOS.getWorkOrder().getId().equals(detail.getWorkOrder().getId()))
					continue;
				addQcOs = qcOS;
				addQcOs.add(detail);
				break;
			}
			if (addQcOs == null) {
				addQcOs = new QcOs(getSessionUser(), detail);
				lstQcOs.add(addQcOs);
			}
		}
		return lstQcOs;
	}

	public void preview(ModelMap model, HttpServletRequest request, HttpServletResponse response, QcChkOutSrcForm form)
			throws Exception {
		String tmp = request.getParameter("lstId");
		String lstData = request.getParameter("lstData");
		if (Formater.isNull(tmp) || Formater.isNull(lstData))
			throw new ResourceException("Chưa chọn chi tiết");
		String ids[] = tmp.split("[_]");
		String datas[] = lstData.split("[_]");
		for (int i = 0; i < ids.length; i++) {
			QcChkOutSrc qcChkOutSrc = qcChkOutSrcDao.get(ids[i]);
			long amountAllowed = qcChkOutSrc.getAmount() - qcChkOutSrc.getWorkOrder().getTotalToOs();
			if (amountAllowed < Formater.str2num(datas[i]).longValue())
				throw new ResourceException(
						String.format("Mã LSX: %s, số lượng chuyển %s vượt quá số lượng được phép chuyển %s",
								new Object[] { qcChkOutSrcDao.get(ids[i]).getWorkOrder().getCode(), datas[i],
										Formater.num2str(amountAllowed) }));
		}
		List<QcOs> lstQcOs = makeLstPackage(ids, datas);
		ObjectMapper om = JsonUtils.objectMapper2();
		// Bo qua qcOsDetails (tunning performace)
		om.setAnnotationIntrospector(new CustomIntrospector());
		returnJson(response, lstQcOs, om);
	}

	private static Logger lg = Logger.getLogger(HistoryOSController.class);

	private static class CustomIntrospector extends JacksonAnnotationIntrospector {
		@Override
		public boolean hasIgnoreMarker(final AnnotatedMember m) {
			if (super.hasIgnoreMarker(m))
				return true;
			try {
				if (QcOs.class.getDeclaredField("qcOsDetails").equals(m.getAnnotated()))
					return true;
			} catch (Exception e) {
				lg.error(e.getMessage(), e);
			}
			return false;
		}
	}

	@Autowired
	private QcOsDao qcOsDao;
	@Autowired
	private QuotationItemDao quotationItemDao;

	@SuppressWarnings("unused")
	public void saveQcOs(ModelMap model, HttpServletRequest request, HttpServletResponse response, QcChkOutSrcForm form)
			throws Exception {
		for (QcOs os : form.getLstQcOs()) {
			String[] arrId = os.getIds().split(",");
			String[] amounts = os.getAmounts().split(",");
			for (int i = 0; i < arrId.length; i++) {
				QcChkOutSrc qcChkOutSrc = qcChkOutSrcDao.get(arrId[i]);
				if (Formater.isNull(os.getCustomers().getId()))
					throw new ResourceException(
							String.format("Mã bản vẽ: %s chưa chọn đơn vị gia công ngoài!", new Object[] {
									qcChkOutSrc.getWorkOrder().getQuotationItemExe().getQuotationItemId().getCode() }));
				QcOsDetail detail = new QcOsDetail(qcChkOutSrc, os, Long.valueOf(amounts[i]));
				os.getQcOsDetails().add(detail);
				if (os.getQuotationItem() == null) {
					os.setQuotationItem(detail.getQuotationItem());
					os.setWorkOrder(detail.getWorkOrder());
				}
			}
			os.setCreateDate(Calendar.getInstance().getTime());
			os.setSysUsers(getSessionUser());
			qcOsDao.save(os);
		}
	}
}
