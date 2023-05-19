package cic.h2h.controller.bisAdmin;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.AstMachineDao;
import cic.h2h.dao.hibernate.ExeStepTypeDao;
import cic.h2h.form.SysAstMachineForm;
import common.util.Formater;
import common.util.ResourceException;
import entity.AstMachine;
import entity.Company;
import entity.ExeStepType;
import entity.MachineExeStepType;
import entity.frwk.SysUsers;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.CompanyDao;
import frwk.dao.hibernate.sys.SysDictParamDao;
import frwk.utils.ApplicationContext;

@Controller
@RequestMapping("/sysAstMachine")
public class AstMachineController extends CatalogController<SysAstMachineForm, AstMachine> {

	private static final Logger log = Logger.getLogger(AstMachineController.class);

	@Autowired
	private AstMachineDao astMachineDao;

	@Override
	public void createSearchDAO(HttpServletRequest request, SysAstMachineForm form, BaseDao<AstMachine> dao)
			throws Exception {
		if (!Formater.isNull(form.getUserAd()))
			dao.addRestriction(Restrictions
					.sqlRestriction("exists (select 1 from sys_users su where upper(su.username) like upper('%"
							+ StringEscapeUtils.escapeSql(form.getUserAd()) + "%') and su.id={alias}.create_id)"));
		if (form.getIsUsed())
			dao.addRestriction(Restrictions.isNotNull("assetContr"));
		if (!Formater.isNull(form.getFromDate())) {
			dao.addRestriction(Restrictions.ge("createDt", Formater.str2date(form.getFromDate())));
		}
		if (!Formater.isNull(form.getToDate())) {
			Calendar c = Calendar.getInstance();
			c.setTime(Formater.str2date(form.getToDate()));
			c.add(Calendar.DATE, 1);
			dao.addRestriction(Restrictions.lt("createDt", c.getTime()));
		}

		if (form.getAstCertiNo() != null && !form.getAstCertiNo().equals("")) {
			dao.addRestriction(
					Restrictions.like("certiNo", form.getAstCertiNo().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
		if (!Formater.isNull(form.getCompanyId()))
			dao.addRestriction(Restrictions.eq("company.id", form.getCompanyId()));
		if (!Formater.isNull(form.getAstName()))
			dao.addRestriction(Restrictions.like("astName", form.getAstName().trim(), MatchMode.ANYWHERE).ignoreCase());

	}

	@Override
	protected void pushToJa(JSONArray ja, AstMachine temp, SysAstMachineForm modelForm) throws Exception {
		ja.put("<a href = '#' onclick = 'edit(\"" + temp.getId() + "\")'>"
				+ StringEscapeUtils.escapeHtml(temp.getAstName()) + "</a>");
		ja.put(temp.getCompany().getName());
		ja.put(StringEscapeUtils.escapeHtml(temp.getCertiNo()));
		ja.put("<span class='characterwrap'>" + (Formater.isNull(temp.getFrameNo()) ? "" : temp.getFrameNo())
				+ "</span>");
		ja.put("<span class='characterwrap'>" + (Formater.isNull(temp.getMachineNo()) ? "" : temp.getMachineNo())
				+ "</span>");

		ja.put("");
		ja.put(temp.getCreateDtStr());
	}

	@Override
	public BaseDao<AstMachine> getDao() {
		return astMachineDao;
	}

	@Override
	public String getJsp() {
		return "quan_tri_nghiep_vu/may_gia_cong";
	}

	@Autowired
	private SysDictParamDao sysDictParamDao;

	@Autowired
	private ExeStepTypeDao exeStepTypeDao;
	@Autowired
	private CompanyDao companyDao;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysAstMachineForm form)
			throws Exception {
		model.addAttribute("companies", companyDao.getAll());
		model.addAttribute("statusMachines", sysDictParamDao.getByType("AST_MACHINE_STATUS"));
		model.addAttribute("lstExeStepTypes", exeStepTypeDao.getAll());
		form.setCompanyId(getSessionUser().getCompany().getId());
	}

	@Autowired
	private HttpSession session;

	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysAstMachineForm form)
			throws Exception {

		ApplicationContext appContext = (ApplicationContext) session
				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
		SysUsers su = (SysUsers) appContext.getAttribute(ApplicationContext.USER);
		if (Formater.isNull(form.getAstMachine().getId())) {
			form.getAstMachine().setCreateDt(new Date());
			form.getAstMachine().setCreateId(su.getId());
		} else {
			form.getAstMachine().setMaintainId(su.getId());
		}
		int checkDuplicate = checkDulicateExeStepType(form.getAstMachine().getExeStepTypes());
		if (checkDuplicate > 0) {
			ExeStepType exeStep = exeStepTypeDao.getObject(ExeStepType.class,
					form.getAstMachine().getExeStepTypes().get(checkDuplicate - 1).getExeStepType().getId());
			throw new ResourceException("M\u00E3 ti\u1EBFn tr\u00ECnh :" + exeStep.getCode()
					+ " b\u1ECB tr\u00F9ng t\u1EA1i d\u00F2ng : " + checkDuplicate);
		}

		astMachineDao.save(form.getAstMachine());
	}

	private int checkDulicateExeStepType(List<MachineExeStepType> list) {
		if (Formater.isNull(list))
			return 0;
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) == null)
				continue;
			if (list.get(i).getExeStepType() == null)
				continue;
			if (Formater.isNull(list.get(i).getExeStepType().getId()))
				continue;

			if (map.get(list.get(i).getExeStepType().getId()) != null) {
				// map.put(list.get(i).getExeStepType().getId(), map.get(list.get(i).getExeStepType().getId()) + 1);
				return i + 1;
			} else {
				map.put(list.get(i).getExeStepType().getId(), i);
			}
		}

		return 0;
	}

}
