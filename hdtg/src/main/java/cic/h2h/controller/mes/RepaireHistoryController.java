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

import cic.h2h.dao.hibernate.AstMachineDao;
import cic.h2h.dao.hibernate.QuotationRepaireDao;
import cic.h2h.form.QuotationRepaireForm;
import common.util.FormatNumber;
import common.util.Formater;
import common.util.ResourceException;
import entity.AstMachine;
import entity.QuotationRepaire;
import entity.frwk.SysUsers;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysUsersDao;

@Controller
@RequestMapping(value = "/repaireHistory")
public class RepaireHistoryController
		extends frwk.controller.CatalogController<QuotationRepaireForm, QuotationRepaire> {
	@Autowired
	private QuotationRepaireDao quotationRepaireDao;

	@Override
	public void createSearchDAO(HttpServletRequest request, QuotationRepaireForm form, BaseDao<QuotationRepaire> dao)
			throws Exception {
		dao.createAlias("sysUser", "su");
		dao.createAlias("quotationItem", "qi");
		dao.createAlias("qi.quotationId", "q");
		dao.addRestriction(Restrictions.eq("q.company", getSessionUser().getCompany()));
		// Tim kiem dung, ignore case
		if (!Formater.isNull(form.getDrawingCode()))
			dao.addRestriction(Restrictions.eq("qi.code", form.getDrawingCode()).ignoreCase());
		// Tim kiem dung, ignore case
		if (!Formater.isNull(form.getManageCodeSearch()))
			dao.addRestriction(Restrictions.eq("qi.manageCode", form.getManageCodeSearch().trim()).ignoreCase());
		if (!Formater.isNull(form.getOrderCode()))
			dao.addRestriction(
					Restrictions.like("q.code", form.getOrderCode().trim(), MatchMode.ANYWHERE).ignoreCase());

		if (!Formater.isNull(form.getFromDate())) {
			dao.addRestriction(Restrictions.ge("q.quotationDate", Formater.str2date(form.getFromDate())));
		}
		if (!Formater.isNull(form.getToDate())) {
			Calendar cTodatePlusOne = Calendar.getInstance();
			cTodatePlusOne.setTime(Formater.str2date(form.getToDate()));
			cTodatePlusOne.add(Calendar.DATE, 1);
			dao.addRestriction(Restrictions.lt("q.quotationDate", cTodatePlusOne.getTime()));
		}
		if (!Formater.isNull(form.getWorkerSearch()))
			dao.addRestriction(Restrictions.eq("sysUser.id", form.getWorkerSearch()));

		if (!Formater.isNull(form.getFrDate()))
			dao.addRestriction(Restrictions.ge("startTime", Formater.str2date(form.getFrDate())));
		if (!Formater.isNull(form.gettDate())) {
			Calendar t = Calendar.getInstance();
			t.setTime(Formater.str2date(form.gettDate()));
			t.add(Calendar.DATE, 1);
			dao.addRestriction(Restrictions.lt("startTime", t.getTime()));
		}
		if (!Formater.isNull(form.getUpdatorSearch())) {
			dao.addRestriction(Restrictions.eq("sysUser.id", form.getUpdatorSearch()));
		}
		dao.addOrder(Order.desc("createDate"));
	}

	@Override
	protected void pushToJa(JSONArray ja, QuotationRepaire e, QuotationRepaireForm modelForm) throws Exception {
		ja.put(e.getQuotationItem().getCode());
		ja.put(e.getQuotationItem().getManageCode());
		Long repaireAmount = e.getQuotationItem().getQcRepaire();
		Long repairedAmount = e.getQuotationItem().getRepairedAmount();
		Long outStandingAmount = repaireAmount - repairedAmount;
		ja.put(FormatNumber.num2Str(repaireAmount));
		ja.put(FormatNumber.num2Str(repairedAmount));
		ja.put(FormatNumber.num2Str(outStandingAmount));
		if (e.getSysUser() != null)
			ja.put(e.getSysUser().getName());
		else
			ja.put("");
		if (e.getStartTime() != null)
			ja.put(Formater.date2ddsmmsyyyspHHmmss(e.getStartTime()));
		else
			ja.put("");
		if (e.getEndTime() != null)
			ja.put(Formater.date2ddsmmsyyyspHHmmss(e.getEndTime()));
		else
			ja.put("");
		if (e.getExeTime() != null)
			ja.put(Formater.num2str(e.getExeTime()));
		else
			ja.put("");

		ja.put(Formater.num2str(e.getTotalAmount()));
		ja.put(Formater.num2str(e.getAmount()));
		ja.put(Formater.num2str(e.getNgAmount()));
		if (e.getUpdator() != null)
			ja.put(e.getUpdator().getName());
		else
			ja.put("");
		ja.put("<a style='color:blue;' href='javascipt:;' onclick = 'editProductionHistory(\"" + e.getId() + "\",\""
				+ Formater.date2ddsmmsyyyspHHmmss(e.getStartTime()) + "\",\""
				+ Formater.date2ddsmmsyyyspHHmmss(e.getEndTime()) + "\",\"" + FormatNumber.num2Str(e.getAmount())
				+ "\",\"" + e.getSysUser().getId() + "\",\"" + FormatNumber.num2Str(e.getTotalAmount()) + "\",\""
				+ FormatNumber.num2Str(e.getNgAmount()) + "\",\""
				+ (Formater.isNull(e.getNgDescription()) ? "" : e.getNgDescription()) + "\",\""
				+ (e.getAstMachine() == null ? "" : e.getAstMachine().getId()) + "\")'>edit</a>");
	}

	@Override
	public BaseDao<QuotationRepaire> getDao() {
		return quotationRepaireDao;
	}

	@Override
	public String getJsp() {
		return "ke_hoach_san_xuat/lich_su_sua_hang";
	}

	@Autowired
	private SysUsersDao sysUsersDao;
	@Autowired
	private AstMachineDao astMachineDao;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QuotationRepaireForm form)
			throws Exception {
		model.addAttribute("lstSysUser", sysUsersDao.getCompanyUser(getSessionUser().getCompany()));
		model.addAttribute("lstAstMachine", astMachineDao.getAll(getSessionUser().getCompany()));
	}

	public void addRepaire(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QuotationRepaireForm form)
			throws Exception {
		String repaireId = rq.getParameter("repaireId");
		QuotationRepaire quotationRepaire = quotationRepaireDao.get(repaireId);
		if (quotationRepaire == null)
			throw new ResourceException("Không tồn tại chi tiết sửa");
		String startTime = rq.getParameter("startTime");
		quotationRepaire.setStartTimeStr(startTime);
		String endTime = rq.getParameter("endTime");
		quotationRepaire.setEndTimeStr(endTime);
		String ngAmount = rq.getParameter("ngAmount");
		quotationRepaire.setNgAmountStr(ngAmount);
		String ngDes = rq.getParameter("ngDes");
		quotationRepaire.setNgDescription(ngDes);
		quotationRepaire.setExeTime(
				(quotationRepaire.getEndTime().getTime() - quotationRepaire.getStartTime().getTime()) / 1000 / 60);
		if (quotationRepaire.getExeTime() < 0)
			throw new ResourceException("Thời điểm kết thúc phải sau thời điểm bắt đầu!");
		String amount = rq.getParameter("amount");
		quotationRepaire.setAmountStr(amount);
		String totalAmount = rq.getParameter("totalAmount");
		quotationRepaire.setTotalAmountStr(totalAmount);
		String userid = rq.getParameter("userid");
		SysUsers worker = sysUsersDao.getUser(userid);
		quotationRepaire.setSysUser(worker);
		quotationRepaire.setUpdator(getSessionUser());
		String machineID = rq.getParameter("machineID");
		AstMachine machine = astMachineDao.get(machineID);
		quotationRepaire.setAstMachine(machine);
		quotationRepaire.setCreateDate(Calendar.getInstance().getTime());
		quotationRepaireDao.save(quotationRepaire);
	}

	public void delRepaireHistory(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			QuotationRepaireForm form) throws Exception {
		String repaireId = rq.getParameter("repaireID");
		QuotationRepaire quotationRepaire = quotationRepaireDao.get(QuotationRepaire.class, repaireId);
		if (Formater.isNull(repaireId) || quotationRepaire == null)
			throw new ResourceException("Không tồn tại hàng sửa!");
		quotationRepaireDao.del(quotationRepaire);
	}
}
