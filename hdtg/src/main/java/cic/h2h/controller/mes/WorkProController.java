package cic.h2h.controller.mes;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.ExeStepTypeDao;
import cic.h2h.dao.hibernate.QuotationItemExeDao;
import cic.h2h.dao.hibernate.WorkProDao;
import cic.h2h.form.WorkProForm;
import common.util.FormatNumber;
import common.util.Formater;
import common.util.ResourceException;
import entity.ExeStepType;
import entity.QuotationItemExe;
import entity.WorkPro;
import entity.frwk.SysUsers;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysUsersDao;

@Controller
@RequestMapping(value = "/lenhsx")
public class WorkProController extends CatalogController<WorkProForm, WorkPro> {

	@Autowired
	private WorkProDao workProDao;

	@Override
	public void createSearchDAO(HttpServletRequest request, WorkProForm form, BaseDao<WorkPro> dao) throws Exception {
		dao.createAlias("qi.quotationId", "q");
		dao.createAlias("q.customer", "cus");
		dao.createAlias("quotationItemExe", "qie");
		dao.createAlias("qie.quotationItemId", "qi");
		dao.createAlias("qie.exeStepId", "exeStepId");
		dao.createAlias("exeStepId.stepType", "stepType");
		if (!Formater.isNull(form.getOrderCode()))
			dao.addRestriction(
					Restrictions.like("q.code", form.getOrderCode().trim(), MatchMode.ANYWHERE).ignoreCase());
		if (!Formater.isNull(form.getCusName()))
			dao.addRestriction(
					Restrictions.like("cus.orgName", form.getCusName().trim(), MatchMode.ANYWHERE).ignoreCase());
		if (!Formater.isNull(form.getProgramer()))
			dao.addRestriction(Restrictions.eq("programer.id", form.getProgramer()));
		if (!Formater.isNull(form.getFrDate()))
			dao.addRestriction(Restrictions.ge("startTime", Formater.str2date(form.getFrDate())));
		if (!Formater.isNull(form.getToDate())) {
			Calendar t = Calendar.getInstance();
			t.setTime(Formater.str2date(form.getToDate()));
			t.add(Calendar.DATE, 1);
			dao.addRestriction(Restrictions.lt("startTime", t.getTime()));
		}

		if (!Formater.isNull(form.getDrCode()))
			dao.addRestriction(Restrictions.like("code", form.getDrCode().trim(), MatchMode.ANYWHERE).ignoreCase());

		if (!Formater.isNull(form.getStepType())) {
			dao.addRestriction(Restrictions.eq("stepType.id", form.getStepType()));
		}
	}

	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, WorkProForm form) throws Exception {
		WorkPro wp = form.getModel();
		if (wp.getEndTime() != null && wp.getStartTime() != null) {
			wp.setDuration(new BigDecimal((wp.getEndTime().getTime() - wp.getStartTime().getTime()) / 1000 / 60));
			if (wp.getDuration().compareTo(new BigDecimal(0)) <= 0)
				throw new ResourceException("Thời hạn bắt đầu phải trước thời hạn kết thúc!");
		}
		super.save(model, rq, rs, form);
	}

	@Override
	protected void pushToJa(JSONArray ja, WorkPro e, WorkProForm modelForm) throws Exception {
		ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + e.getId() + "\")'>" + e.getCode() + "</a>");
		ja.put("<span class='characterwrap'>" + e.getQuotationItemExe().getQuotationItemId().getQuotationId().getCode() + "</span>");
		ja.put(e.getQuotationItemExe().getQuotationItemId().getCode());
		if (e.getQuotationItemExe().getQuotationItemId().getQuotationId().getCustomer() != null)
			ja.put(e.getQuotationItemExe().getQuotationItemId().getQuotationId().getCustomer().getOrgName());
		else
			ja.put("");
		ja.put(e.getQuotationItemExe().getExeStepId().getStepType().getName());
		ja.put(e.getQuotationItemExe().getExeTimeStr());
		ja.put(Formater.dateTime2str(e.getStartTime()));
		ja.put(Formater.dateTime2str(e.getEndTime()));
		ja.put(FormatNumber.num2Str(e.getDuration()));
		ja.put(e.getProgramer().getName());

	}

	@Override
	public BaseDao<WorkPro> getDao() {

		return workProDao;
	}

	@Override
	public String getJsp() {

		return "ke_hoach_san_xuat/quan_ly_lap_trinh";
	}

	@Autowired
	private SysUsersDao sysUsersDao;

	@Autowired
	private QuotationItemExeDao quotationItemExeDao;
	@Autowired
	private ExeStepTypeDao exeStepTypeDao;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, WorkProForm form)
			throws Exception {
		List<SysUsers> lstSysUsers = sysUsersDao.getCompanyUser(getSessionUser().getCompany());
		model.addAttribute("lstSysUsers", lstSysUsers);
		model.addAttribute("lstStepType", exeStepTypeDao.getAll());
		String id = rq.getParameter("id");
		if (!Formater.isNull(id)) {
			WorkPro workPro = workProDao.getWorkProByQuotationItemExeId(id);
			if (workPro != null) {
				workPro.setEndTimeStr(Formater.dateTime2str(workPro.getEndTime()));
				workPro.setStartTimeStr(Formater.dateTime2str(workPro.getStartTime()));
				form.setWorkPro(workPro);
			} else {
				QuotationItemExe itemExe = quotationItemExeDao.get(QuotationItemExe.class, id);
				if (itemExe != null) {
					form.getWorkPro().setQuotationItemExe(itemExe);
					String code = "";
					if (itemExe.getQuotationItemId() != null && itemExe.getQuotationItemId().getQuotationId() != null)
						code = itemExe.getQuotationItemId().getQuotationId().getCustomer().getCode() + "-"
								+ itemExe.getQuotationItemId().getCode() + "-"
								+ itemExe.getExeStepId().getStepType().getCode() + "-"
								+ itemExe.getExeStepId().getStepCode();
					form.getWorkPro().setCode(code);
				} else
					throw new ResourceException("Sai thong tin lap tring");
			}
		}
	}
}
