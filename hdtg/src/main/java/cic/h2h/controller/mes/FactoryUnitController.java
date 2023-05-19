package cic.h2h.controller.mes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.BssFactoryUnitDao;
import cic.h2h.form.BssFactoryUnitForm;
import common.util.Formater;
import entity.BssFactoryUnit;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.CompanyDao;

@Controller
@RequestMapping(value = "/factoryUnit")
public class FactoryUnitController extends CatalogController<BssFactoryUnitForm, BssFactoryUnit> {
	@Autowired
	private BssFactoryUnitDao bssFactoryUnitDao;

	@Override
	public void createSearchDAO(HttpServletRequest request, BssFactoryUnitForm form, BaseDao<BssFactoryUnit> dao)
			throws Exception {
		dao.createAlias("company", "company");
		if (!Formater.isNull(form.getKeyWord())) {
			dao.addRestriction(Restrictions.or(
					Restrictions.like("code", form.getKeyWord().trim(), MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.or(
							Restrictions.like("name", form.getKeyWord().trim(), MatchMode.ANYWHERE).ignoreCase(),
							Restrictions.or(
									Restrictions.like("company.code", form.getKeyWord().trim(), MatchMode.ANYWHERE)
											.ignoreCase(),
									Restrictions.like("company.name", form.getKeyWord().trim(), MatchMode.ANYWHERE)
											.ignoreCase()))));
		}

	}

	@Override
	protected void pushToJa(JSONArray ja, BssFactoryUnit e, BssFactoryUnitForm modelForm) throws Exception {
		if (e.getId() != null || !Formater.isNull(e.getCode()))
			ja.put("<a href = '#' onclick = 'edit(\"" + e.getId() + "\")'>" + e.getCode() + "</a>");
		else
			ja.put("");
		if (!Formater.isNull(e.getName()))
			ja.put(e.getName());
		else
			ja.put("");
		if (!Formater.isNull(e.getCompany().getName()))
			ja.put(e.getCompany().getName());
		else
			ja.put("");
	}

	@Override
	public BaseDao<BssFactoryUnit> getDao() {
		return bssFactoryUnitDao;
	}

	@Override
	public String getJsp() {
		return "quan_tri_nghiep_vu/phan_xuong";
	}

	@Autowired
	private CompanyDao companyDao;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, BssFactoryUnitForm form)
			throws Exception {
		model.addAttribute("companies", companyDao.getAllOrderAstName());
	}

}
