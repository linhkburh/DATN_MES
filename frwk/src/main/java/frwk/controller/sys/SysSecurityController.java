package frwk.controller.sys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import common.util.Formater;
import entity.frwk.SysSecurity;
import frwk.constants.Constants;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysSecurityDao;
import frwk.form.SysSecurityForm;

@Controller
@RequestMapping(value = "/security")
public class SysSecurityController extends CatalogController<SysSecurityForm,SysSecurity> {
	private static Logger lg = Logger.getLogger(SysSecurityController.class);

	@Autowired
	private SysSecurityDao sysSecurityDao;
	
	@Autowired
	private MessageSource messageSource;


	@Override
	public String getJsp() {
		
		return "qtht/quan_ly_bao_mat";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysSecurityForm form)
			throws Exception {
		

	}

	@Override
	public void createSearchDAO(HttpServletRequest request, SysSecurityForm form, BaseDao<SysSecurity> dao) throws Exception {
		
		if (!Formater.isNull(form.getsCode())) {
			dao.addRestriction(Restrictions.like("code", form.getsCode().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
		if (!Formater.isNull(form.getsName())) {
			dao.addRestriction(Restrictions.like("name", form.getsName().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
	}
	
	@Override
	public void pushToJa(JSONArray ja, SysSecurity temp, SysSecurityForm modal) throws Exception {
		ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + temp.getId() + "\")'>"
				+ temp.getCode() + "</a>");
		ja.put(temp.getName());
		if (!Formater.isNull(temp.getValue()))
			ja.put(temp.getValue());
		else ja.put("");
		if (temp.isActive()) {
			ja.put(messageSource.getMessage(Constants.ACTIVE, null, "Default", LocaleContextHolder.getLocale()));
		} else {
			ja.put(messageSource.getMessage(Constants.INACTIVE, null, "Default", LocaleContextHolder.getLocale()));
		}
		
	}

	@Override
	public BaseDao<SysSecurity> getDao() {
		return sysSecurityDao;
	}

}
