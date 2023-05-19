package cic.h2h.controller.bisAdmin;

import common.util.Formater;

import java.util.Formatter;

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
import cic.h2h.form.ExeStepTypeForm;
import entity.ExeStepType;
import frwk.controller.CatalogController;
import frwk.controller.ClearOnFinish;
import frwk.dao.hibernate.BaseDao;

@Controller
@RequestMapping("/stepType")
public class ExeStepTypeController extends CatalogController<ExeStepTypeForm, ExeStepType> {

	@Autowired
	private ExeStepTypeDao exeStepTypeDao;

	@Override
	public void createSearchDAO(HttpServletRequest request, ExeStepTypeForm form, BaseDao<ExeStepType> dao)
			throws Exception {
		if (!Formater.isNull(form.getCode())) {
			dao.addRestriction(Restrictions.eq("code", form.getCode()));
		}
		if (!Formater.isNull(form.getName())) {
			dao.addRestriction(Restrictions.ilike("name", form.getName(), MatchMode.ANYWHERE));
		}
	}

	@Override
	protected void pushToJa(JSONArray ja, ExeStepType e, ExeStepTypeForm modelForm) throws Exception {

		ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + e.getId() + "\")'>" + e.getCode() + "</a>");
		ja.put(e.getName());
		ja.put(e.getDescription());
	}

	@Override
	public BaseDao<ExeStepType> getDao() {
		return exeStepTypeDao;
	}

	@Override
	public String getJsp() {
		return "quan_tri_nghiep_vu/loai_cong_doan_gia_cong";
	}

	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ExeStepTypeForm form)
			throws Exception {

		if (Formater.isNull(form.getStepType().getId()))
			form.getStepType().setId(null);
		super.save(model, rq, rs, form);
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ExeStepTypeForm form)
			throws Exception {
	}

}
