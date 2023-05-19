package frwk.controller.sys;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import common.util.Formater;
import entity.frwk.BssParam;
import entity.frwk.CatCharge;
import entity.frwk.CatChargeDetail;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.BssParamDao;
import frwk.dao.hibernate.sys.CatChargeDao;
import frwk.form.CatChargeForm;

@Controller
@RequestMapping("/catCharge")
public class CatChargeController extends CatalogController<CatChargeForm, CatCharge> {

	static Logger lg = Logger.getLogger(CatChargeController.class);

	@Autowired
	private CatChargeDao catChargeDao;

	@Autowired
	private MessageSource messageSource;

	@Override
	public BaseDao<CatCharge> getDao() {
		return catChargeDao;
	}

	@Override
	public void pushToJa(JSONArray ja, CatCharge r, CatChargeForm modal) throws Exception {
		ja.put("<a href = '#' onclick = 'edit(\"" + r.getId() + "\")'>" + StringEscapeUtils.escapeHtml(r.getCode())
				+ "</a>");
		ja.put(StringEscapeUtils.escapeHtml(r.getName()));
		ja.put(r.getDescription());
	}

	@Override
	public String getJsp() {
		return "qtht/danh_muc_bieu_phi";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, CatChargeForm form)
			throws Exception {
		model.addAttribute("lstFreeTypes", bssParamDao.getAll());

	}

	@Autowired
	private BssParamDao bssParamDao;

	@Override
	public void createSearchDAO(HttpServletRequest request, CatChargeForm form,BaseDao<CatCharge> dao) throws Exception {
		CatChargeForm sysParamForm = (CatChargeForm) form;
		if (!Formater.isNull(sysParamForm.getSname()))
			dao.addRestriction(
					Restrictions.like("name", sysParamForm.getSname().trim(), MatchMode.ANYWHERE).ignoreCase());
		if (!Formater.isNull(sysParamForm.getScode()))
			dao.addRestriction(
					Restrictions.like("code", sysParamForm.getScode().trim(), MatchMode.ANYWHERE).ignoreCase());
		dao.addOrder(Order.desc("code"));
		dao.addOrder(Order.desc("name"));
	}

	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, CatChargeForm form)
			throws Exception {
		if (Formater.isNull(form.getCatCharge().getId()))
			form.getCatCharge().setCreatedDate(Calendar.getInstance().getTime());
		else {
			CatCharge bssParam = catChargeDao.getObject(form.getCatCharge());
			form.getCatCharge().setCreatedDate(bssParam.getCreatedDate());
		}
		for (CatChargeDetail c : form.getCatCharge().getLstCharge())
			c.setCatCharge(form.getCatCharge());

		super.save(model, rq, rs, form);
	}
}