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
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.BssParamDao;
import frwk.form.BssParamForm;

@Controller
@RequestMapping("/bssParam")
public class BssParamController extends CatalogController<BssParamForm, BssParam> {

	static Logger lg = Logger.getLogger(BssParamController.class);

	@Autowired
	private BssParamDao bssParamDao;

	@Autowired
	private MessageSource messageSource;

	@Override
	public BaseDao<BssParam> getDao() {
		return bssParamDao;
	}

	@Override
	public void pushToJa(JSONArray ja, BssParam r, BssParamForm modal) throws Exception {
		ja.put("<a href = '#' onclick = 'edit(\"" + r.getId() + "\")'>" + StringEscapeUtils.escapeHtml(r.getCode())
				+ "</a>");
		ja.put(StringEscapeUtils.escapeHtml(r.getName()));
		ja.put(r.getDescription());
		ja.put(StringEscapeUtils.escapeHtml(r.getValue()));
	}

	@Override
	public String getJsp() {
		return "qtht/danh_muc_tham_so_nghiep_vu";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, BssParamForm form)
			throws Exception {
	}

	@Override
	public void createSearchDAO(HttpServletRequest request, BssParamForm form, BaseDao<BssParam> dao) throws Exception {
		BssParamForm sysParamForm = (BssParamForm) form;
		if (!Formater.isNull(sysParamForm.getSname()))
			dao.addRestriction(
					Restrictions.like("name", sysParamForm.getSname().trim(), MatchMode.ANYWHERE).ignoreCase());
		if (!Formater.isNull(sysParamForm.getScode()))
			dao.addRestriction(
					Restrictions.like("code", sysParamForm.getScode().trim(), MatchMode.ANYWHERE).ignoreCase());
		dao.addOrder(Order.desc("code"));
		dao.addOrder(Order.desc("name"));
	}

	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, BssParamForm form)
			throws Exception {
		if (Formater.isNull(form.getBssParam().getId()))
			form.getBssParam().setCreatedDate(Calendar.getInstance().getTime());
		else {
			BssParam bssParam = bssParamDao.getObject(form.getBssParam());
			form.getBssParam().setCreatedDate(bssParam.getCreatedDate());
		}

		super.save(model, rq, rs, form);
	}
}