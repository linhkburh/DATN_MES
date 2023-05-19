package cic.h2h.controller.mes;

import java.util.ArrayList;
import java.util.List;

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

import cic.h2h.dao.hibernate.ExeStepDao;
import cic.h2h.dao.hibernate.ExeStepTypeDao;
import cic.h2h.form.ExeStepForm;
import common.util.FormatNumber;
import common.util.Formater;
import entity.ExeStep;
import entity.ExeStepType;
import entity.frwk.SysDictParam;
import frwk.constants.Constants;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysDictParamDao;

@Controller
@RequestMapping("/exeStep")
public class ExeStepController extends CatalogController<ExeStepForm, ExeStep> {

	@Autowired
	ExeStepDao exeStepDao;

	@Autowired
	ExeStepTypeDao exeStepTypeDao;

	@Autowired
	SysDictParamDao sysDictParamDao;

	@Override
	public void createSearchDAO(HttpServletRequest request, ExeStepForm form, BaseDao<ExeStep> dao) throws Exception {
		if (!Formater.isNull(form.getCode()))
			dao.addRestriction(Restrictions.like("stepCode", form.getCode(), MatchMode.ANYWHERE).ignoreCase());
		if (!Formater.isNull(form.getName()))
			dao.addRestriction(Restrictions.like("stepName", form.getName(), MatchMode.ANYWHERE).ignoreCase());
		if (!Formater.isNull(form.getStepTypeId()))
			dao.addRestriction(Restrictions.eq("stepType.id", form.getStepTypeId()));
		if (form.getFixFree() != null && form.getFixFree())
			dao.addRestriction(Restrictions.isNotNull("initPrice"));
		if (Boolean.TRUE.equals(form.getIsProgram()))
			dao.addRestriction(Restrictions.eq("program", Boolean.TRUE));
		dao.addRestriction(Restrictions.isNull("previousVersion"));
		dao.addOrder(Order.asc("stepType"));
		dao.addOrder(Order.asc("stepName"));
	}

	@Override
	protected void pushToJa(JSONArray ja, ExeStep e, ExeStepForm modelForm) throws Exception {
		ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + e.getId() + "\")'>" + e.getStepCode()
				+ "</a>");
		ja.put(e.getStepName());
		if (e.getStepType() != null)
			ja.put(e.getStepType().getCode() + "-" + e.getStepType().getName());
		else
			ja.put("");
		if (e.getInitPrice() != null)
			ja.put(FormatNumber.num2Str(e.getInitPrice()) + " (" + e.getCurrency().getCode() + ")");
		else
			ja.put("");

		ja.put(e.getDescription());
		if (Boolean.TRUE.equals(e.getProgram()))
			ja.put("X");
		else
			ja.put("");
	}

	@Override
	public BaseDao<ExeStep> getDao() {
		return exeStepDao;
	}

	@Override
	public String getJsp() {
		return "quan_tri_nghiep_vu/cong_doan_gia_cong";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ExeStepForm form)
			throws Exception {
		List<ExeStepType> types = exeStepTypeDao.getAll();
		model.addAttribute("types", types);
		rq.setAttribute("stepType", types);
		List<SysDictParam> dictParams = sysDictParamDao.getByType("DIMENSION");
		model.addAttribute("dimensionMap", dictParams);
		// Loai tien vat lieu USD
		SysDictParam usd = sysDictParamDao.getByTypeAndCode(Constants.CAT_TYPE_CURRENCY, "USD");
		List<SysDictParam> lstCurrency = new ArrayList<SysDictParam>();
		lstCurrency.add(usd);
		rq.setAttribute("lstCurrency", lstCurrency);
	}

}
