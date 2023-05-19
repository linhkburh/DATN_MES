package cic.h2h.controller.bisAdmin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.ExchRateDao;
import cic.h2h.form.ExchRateForm;
import cic.h2h.form.ExeStepTypeForm;
import common.util.FormatNumber;
import common.util.Formater;
import entity.ExchRate;
import entity.ExeStepType;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysDictParamDao;

@Controller
@RequestMapping("/exchRate")
public class ExchRateConntroller extends CatalogController<ExchRateForm, ExchRate> {

	@Override
	public void createSearchDAO(HttpServletRequest request, ExchRateForm form, BaseDao<ExchRate> dao) throws Exception {
		if (!Formater.isNull(form.getCurrency()))
			dao.addRestriction(Restrictions.eq("currency.id", form.getCurrency()));
		if (!Formater.isNull(form.getExchDate()))
			dao.addRestriction(Restrictions.eq("exchDate", Formater.str2date(form.getExchDate())));
		dao.addOrder(Order.desc("exchDate"));
		dao.addOrder(Order.asc("currency.id"));
	}

	@Override
	protected void pushToJa(JSONArray ja, ExchRate e, ExchRateForm modelForm) throws Exception {
		ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + e.getId() + "\")'>"
				+ e.getCurrency().getCode() + "</a>");
		ja.put(Formater.date2str(e.getExchDate()));
		ja.put(FormatNumber.num2Str(e.getExchRate()));
		ja.put(e.getExchSource());

	}

	@Autowired
	private ExchRateDao exchRateDao;

	@Override
	public BaseDao<ExchRate> getDao() {
		return exchRateDao;
	}

	@Override
	public String getJsp() {
		return "qtht/ti_gia";
	}

	@Autowired
	private SysDictParamDao sysDictParamDao;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ExchRateForm form)
			throws Exception {
		rq.setAttribute("lstCurrency", sysDictParamDao.getByType("06"));

	}

}
