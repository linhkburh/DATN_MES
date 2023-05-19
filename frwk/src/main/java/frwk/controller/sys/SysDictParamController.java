package frwk.controller.sys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import common.util.Formater;
import entity.frwk.SysDictParam;
import entity.frwk.SysUsers;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysDictParamDao;
import frwk.dao.hibernate.sys.SysDictTypeDao;
import frwk.form.SysDictParamForm;
import frwk.utils.ApplicationContext;

@Controller
@RequestMapping("/sysParam")
public class SysDictParamController extends CatalogController<SysDictParamForm, SysDictParam> {

	private static Logger lg = Logger.getLogger(SysDictParamController.class);

	@Autowired
	private SysDictParamDao sysDictParamDao;

	@Autowired
	private SysDictTypeDao sysDictTypeDao;

	@Override
	public void createSearchDAO(HttpServletRequest request, SysDictParamForm form, BaseDao<SysDictParam> dao) throws Exception {
		SysDictParamForm paramForm = (SysDictParamForm) form;
		if (!Formater.isNull(paramForm.getCodeSearch())) {
			dao.addRestriction(
					Restrictions.like("code", paramForm.getCodeSearch().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
		if (!Formater.isNull(paramForm.getValueSearch())) {
			dao.addRestriction(
					Restrictions.like("value", paramForm.getValueSearch().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
		if (!Formater.isNull(paramForm.getParamsType())) {
			dao.addRestriction(Restrictions.eq("sysDictType.id", paramForm.getParamsType().trim()));
		}
	}

	@Override
	public void pushToJa(JSONArray ja, SysDictParam temp, SysDictParamForm modal) throws Exception {
		ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + temp.getId() + "\")'>" + temp.getCode()
				+ "</a>");
		ja.put(temp.getValue());
		ja.put(temp.getDescription());
		if (temp.getSysDictType() != null)
			ja.put(temp.getSysDictType().getName());
		else
			ja.put("");
	}

	@Override
	public String getJsp() {
		
		return "qtht/tu_dien";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysDictParamForm form)
			throws Exception {
		
		ApplicationContext appContext = (ApplicationContext) rq.getSession()
				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
		if (appContext != null) {
			SysUsers user = (SysUsers) appContext.getAttribute(ApplicationContext.USER);
			lg.info(user);
		}
		model.addAttribute("sysTypes", sysDictTypeDao.getAll());
	}

	@Override
	public BaseDao<SysDictParam> getDao() {
		return sysDictParamDao;
	}

}
