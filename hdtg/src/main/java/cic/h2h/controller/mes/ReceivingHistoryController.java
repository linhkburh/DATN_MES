package cic.h2h.controller.mes;

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

import cic.h2h.dao.hibernate.CustomerManageDao;
import cic.h2h.dao.hibernate.QcInDao;
import cic.h2h.form.QcInForm;
import common.util.Formater;
import common.util.ResourceException;
import entity.Customer;
import entity.QcIn;
import entity.QuotationItem;
import entity.frwk.SysUsers;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysUsersDao;

@Controller
@RequestMapping(value = "receivingHis")
public class ReceivingHistoryController extends CatalogController<QcInForm, QcIn> {
	@Autowired
	private QcInDao qcInDao;

	@Override
	public void createSearchDAO(HttpServletRequest request, QcInForm form, BaseDao<QcIn> dao) throws Exception {
		dao.createAlias("quotationItem", "qi");
		if (!Formater.isNull(form.getDrawingCode())) {
			dao.addRestriction(
					Restrictions.like("qi.code", form.getDrawingCode().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
		if (!Formater.isNull(form.getManageCodeSearch())) {
			dao.addRestriction(Restrictions.like("qi.manageCode", form.getManageCodeSearch().trim(), MatchMode.ANYWHERE)
					.ignoreCase());
		}
		if (!Formater.isNull(form.getFrDate()))
			dao.addRestriction(Restrictions.ge("receiptDate", Formater.str2date(form.getFrDate())));
		if (!Formater.isNull(form.gettDate())) {
			Calendar t = Calendar.getInstance();
			t.setTime(Formater.str2date(form.gettDate()));
			t.add(Calendar.DATE, 1);
			dao.addRestriction(Restrictions.lt("receiptDate", t.getTime()));
		}
		if (!Formater.isNull(form.getCusId()))
			dao.addRestriction(Restrictions.eq("customers", new Customer(form.getCusId())));
		if (!Formater.isNull(form.getWorkerSearch()))
			dao.addRestriction(Restrictions.eq("sysUsers", new SysUsers(form.getWorkerSearch())));
		dao.addOrder(Order.desc("createDate"));
	}

	@Override
	protected void pushToJa(JSONArray ja, QcIn e, QcInForm modelForm) throws Exception {
		// Thanh pham
		if (e.getWorkOrder() == null) {
			ja.put(String.format("<font class='characterwrap'>%s</font>", e.getQuotationItem().getCode()));
			ja.put(String.format("<font class='characterwrap'>%s</font>", e.getQcOs().getDsmaql()));
			ja.put("Thành phẩm");
//			ja.put(Formater.num2str(e.getQuotationItem().getToPartnerAmount()));
		} else {
			QuotationItem qiInDb = e.getWorkOrder().getQuotationItemExe().getQuotationItemId();
			ja.put(String.format("<font class='characterwrap'>%s</font>", qiInDb.getCode()));
			ja.put(String.format("<font class='characterwrap'>%s</font>", qiInDb.getManageCode()));
			ja.put("Bán thành phẩm");
//			ja.put(Formater.num2str(e.getWorkOrder().getToPartnerAmount()));
		}
		ja.put(Formater.num2str(e.getQcOs().getAmount()));
		ja.put(Formater.num2str(e.getTotalAmount()));
		ja.put(Formater.num2str(e.getAmount()));
		ja.put(Formater.num2str(e.getNgAmount()));
		ja.put(Formater.num2str(e.getBrokenAmount()));
		if (e.getCustomers() != null)
			ja.put(e.getCustomers().getOrgName());
		else
			ja.put("");
		ja.put(Formater.dateTime2str(e.getReceiptDate()));
		ja.put(Formater.dateTime2str(e.getReceiptDateTo()));
		if (!Formater.isNull(e.getSysUsers().getName()))
			ja.put(e.getSysUsers().getName());
		else
			ja.put("");
		// truyen tham so de an hien truong
//		ja.put("<a style='color:blue;' href='javascipt:;' onclick = 'edit(\"" + e.getId() + "\",\""
//				+ (e.getWorkOrder() == null ? "qi" : "wo") + "\")'>Cập nhật</a>");
		ja.put("<a style='color:blue;' title='' href='javascipt:;' onclick = 'changeQcIn(\"" + e.getQcOs().getId()
				+ "\",\"" + e.getId() + "\")'>Cập nhật</a>");
	}

	@Override
	public BaseDao<QcIn> getDao() {
		return qcInDao;
	}

	@Override
	public String getJsp() {
		return "ke_hoach_san_xuat/lich_su_nhan_hang";
	}

	@Autowired
	private CustomerManageDao customerManageDao;
	@Autowired
	private SysUsersDao sysUsersDao;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QcInForm form)
			throws Exception {
		model.addAttribute("lstSysUser", sysUsersDao.getCompanyUser(getSessionUser().getCompany()));
		model.addAttribute("lstPartner", customerManageDao.getByType(Customer.IS_PARTNER));
	}

	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QcInForm form) throws Exception {
		QcIn qcIn = form.getModel();
		if (qcIn.getReceiptDateTo().getTime() - qcIn.getReceiptDate().getTime() < 0)
			throw new ResourceException("Thời điểm kết thúc phải sau thời điểm bắt đầu");
		QcIn qcIn2db = qcInDao.get(form.getQcIn().getId());
		if (qcIn2db == null)
			throw new ResourceException("Không tồn tại bản ghi!");
		form.getQcIn().setQcOs(qcIn2db.getQcOs());
		long toPartnerAmount = 0l;
		long partnerDone = 0l;
		if (qcIn2db.getWorkOrder() == null) {
			toPartnerAmount = qcIn2db.getQuotationItem().getToPartnerAmount();
			partnerDone = qcIn2db.getQuotationItem().getPartnerDone();
		} else {
			toPartnerAmount = qcIn2db.getWorkOrder().getToPartnerAmount();
			partnerDone = qcIn2db.getWorkOrder().getPartnerDone();
		}
		if (form.getQcIn().getTotalAmount() > ((toPartnerAmount - partnerDone) + qcIn2db.getTotalAmount()))
			throw new ResourceException("Số lượng nhận vượt quá số lượng cho phép!");
		if (form.getQcIn().getTotalAmount() < form.getQcIn().getAmount())
			throw new ResourceException("Số lượng hoàn thành (OK) phải <= số lượng nhận !");
		if (form.getQcIn().getBrokenAmount() < 0)
			throw new ResourceException("Số lượng chi tiết hủy phải >= 0(kiểm tra lại số lượng nhận,OK,NG)!");
		form.getQcIn().setCreateDate(Calendar.getInstance().getTime());
		form.getQcIn().setSysUsers(getSessionUser());
		super.save(model, rq, rs, form);
	}

	public void delReceivingHis(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QcInForm form)
			throws Exception {
		String qcInId = rq.getParameter("qcInId");
		QcIn qcIn = qcInDao.get(QcIn.class, qcInId);
		if (Formater.isNull(qcInId) || qcIn == null)
			throw new ResourceException("Không tồn tại bản ghi!");
		qcInDao.del(qcIn);
		
	}

}
