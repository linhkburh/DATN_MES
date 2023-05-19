package cic.h2h.controller.mes;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
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
import cic.h2h.dao.hibernate.QcOsDao;
import cic.h2h.form.OutSourcedHistoryForm;
import common.util.Formater;
import common.util.JsonUtils;
import common.util.ResourceException;
import entity.Customer;
import entity.QcOs;
import entity.QcOsDetail;
import entity.frwk.SysUsers;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysUsersDao;

@Controller
@RequestMapping(value = "outSourced")
public class OutSourcedHistoryController extends CatalogController<OutSourcedHistoryForm, QcOs> {
	@Autowired
	private QcOsDao qcOsDao;

	@Override
	public void createSearchDAO(HttpServletRequest request, OutSourcedHistoryForm form, BaseDao<QcOs> dao)
			throws Exception {
		if (!Formater.isNull(form.getWorkCode())) {
			dao.createAlias("workOrder", "wo");
			dao.addRestriction(
					Restrictions.like("wo.code", form.getWorkCode().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
		if (!Formater.isNull(form.getManagerCode()) || !Formater.isNull(form.getDrwingCode())) {
			dao.createAlias("quotationItem", "qi");
			if (!Formater.isNull(form.getManagerCode())) {
				Criterion mCode = null;
				String[] temp = form.getManagerCode().split(";");
				for (String code : temp) {
					if (mCode == null)
						mCode = Restrictions.like("qi.manageCode", code.trim(), MatchMode.ANYWHERE).ignoreCase();
					else
						mCode = Restrictions.or(mCode, Restrictions.like("qi.manageCode", code.trim()).ignoreCase());
				}
				dao.addRestriction(Restrictions.or(mCode,
						Restrictions.like("dsmaql", form.getManagerCode().trim(), MatchMode.ANYWHERE).ignoreCase()));
			}

			if (!Formater.isNull(form.getDrwingCode())) {
				Criterion cCode = null;
				String[] temp = form.getDrwingCode().split(";");
				for (String code : temp) {
					if (cCode == null)
						cCode = Restrictions.like("qi.code", code.trim(), MatchMode.ANYWHERE).ignoreCase();
					else
						cCode = Restrictions.or(cCode,
								Restrictions.like("qi.code", code.trim(), MatchMode.ANYWHERE).ignoreCase());
				}
				dao.addRestriction(cCode);
			}
		}

		if (!Formater.isNull(form.getCus())) {
			dao.addRestriction(Restrictions.eq("customers", new Customer(form.getCus())));
		}
		if (!Formater.isNull(form.getUser())) {
			dao.addRestriction(Restrictions.eq("sysUsers", new SysUsers(form.getUser())));
		}
		if (!Formater.isNull(form.getFrDate()))
			dao.addRestriction(Restrictions.ge("createDate", Formater.str2date(form.getFrDate())));
		if (!Formater.isNull(form.gettDate())) {
			Calendar t = Calendar.getInstance();
			t.setTime(Formater.str2date(form.gettDate()));
			t.add(Calendar.DATE, 1);
			dao.addRestriction(Restrictions.lt("createDate", t.getTime()));
		}
		// dao.addRestriction(Restrictions.gtProperty("qi.totalToOs", "qi.partnerDone"));
		dao.addOrder(Order.desc("createDate"));
	}

	@Override
	protected void pushToJa(JSONArray ja, QcOs e, OutSourcedHistoryForm modelForm) throws Exception {
		ja.put(String.format("<font class='characterwrap'>%s</font>", e.getQi().getCode()));
		if (e.getAmount() > e.getPartnerDone()) {
			ja.put("<a class='characterwrap' title='Chi tiết nội dung chuyển GCN' style='color:blue;' href='javascipt:;' onclick = 'edit(\""
					+ e.getId() + "\",\"" + (e.getQcOsDetails().get(0).getWorkOrder() == null ? "qi" : "wo") + "\")'>"
					+ (Formater.isNull(e.getDsmaql()) ? e.getQuotationItem().getManageCode() : e.getDsmaql()) + "</a>");
		} else {
			ja.put(Formater.isNull(e.getDsmaql()) ? e.getQuotationItem().getManageCode() : e.getDsmaql());
		}
		// Ban thanh pham
		boolean semifinished = e.getQcOsDetails().get(0).getWorkOrder() != null;
		ja.put(semifinished ? "Bán thành phẩm" : "Thành phẩm");
		// So luong tai QC
		// Ban thanh pham
		if (semifinished) {
			ja.put(Formater.num2str(e.getWorkOrder().getToOsedAmount()));
		} else {
			// Thanh pham
			// So luong chuyen sang QC - so luong qc tra lai (khi qc hoac khi nhan hang gcn)
			long amout = 0l;
			for (QcOsDetail d : e.getQcOsDetails()) {
				amout += d.getQuotationItem().getQcQuatity();
				if (d.getQuotationItem().getQcNgAmount() != null)
					amout -= e.getQi().getQcNgAmount();
				if (d.getQuotationItem().getQcDestroyAmout() != null)
					amout -= e.getQi().getQcDestroyAmout();
			}
			ja.put(Formater.num2str(amout));

		}
		ja.put(Formater.num2str(e.getAmount()));
		ja.put(Formater.num2str(e.getPartnerDone()));
		if (e.getCustomers() != null)
			ja.put(e.getCustomers().getOrgName());
		else
			ja.put("");
		if (e.getCreateDate() != null)
			ja.put(Formater.date2strDateTime(e.getCreateDate()));
		else
			ja.put("");
		if (!Formater.isNull(e.getSysUsers().getName()))
			ja.put(e.getSysUsers().getName());
		else
			ja.put("");
		if (e.getAmount() > e.getPartnerDone())
			ja.put("<a style='color:blue;' href='javascipt:;' onclick = 'getItems(\"" + e.getId()
					+ "\")'>Nhận hàng</a>");
		else
			ja.put("Đã nhận đủ hàng");
	}

	@Override
	public BaseDao<QcOs> getDao() {
		return qcOsDao;
	}

	@Override
	public String getJsp() {
		return "ke_hoach_san_xuat/lich_su_chuyen_gia_cong_ngoai";
	}

	@Autowired
	private SysUsersDao sysUsersDao;
	@Autowired
	private CustomerManageDao customerManageDao;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, OutSourcedHistoryForm form)
			throws Exception {
		model.addAttribute("lstSysUser", sysUsersDao.getCompanyUser(getSessionUser().getCompany()));
		model.addAttribute("lstPartner", customerManageDao.getByType(Customer.IS_PARTNER));
	}

	public void delOutSourcedHistory(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			OutSourcedHistoryForm form) throws Exception {
		String idOS = rq.getParameter("idOS");
		QcOs qcOS = qcOsDao.get(QcOs.class, idOS);
		if (Formater.isNull(idOS) || qcOS == null)
			throw new ResourceException("Không tồn tại bản ghi!");
		if (qcOS.getPartnerDone() > 0)
			throw new ResourceException("Đã tồn tại dữ liệu nhận hàng, không thể xóa!");
		qcOsDao.del(qcOS);
	}

	@Override
	public void edit(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, OutSourcedHistoryForm form)
			throws Exception {
		String id = rq.getParameter("id");
		QcOs os = form.getModel();
		getDao().getCurrentSession().load(os, id);
		// Tuning performace
		// Ban thanh pham
		if (os.getQcOsDetails().get(0).getWorkOrder() != null) {
			ObjectMapper om = JsonUtils.objectMapper2();
			// Bo qua qcOsDetails (tunning performace)
			om.setAnnotationIntrospector(new CustomIntrospector());
			returnJson(rs, form.getModel(), om);
		} else {
			returnJson(rs, form.getModel());
		}

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
}
