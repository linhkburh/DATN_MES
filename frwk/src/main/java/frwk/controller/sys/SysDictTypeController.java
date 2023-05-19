package frwk.controller.sys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import common.util.Formater;
import entity.frwk.SysDictType;
import entity.frwk.SysUsers;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysDictTypeDao;
import frwk.form.SysDictTypeForm;
import frwk.utils.ApplicationContext;

@Controller
@RequestMapping("/sysType")
public class SysDictTypeController extends CatalogController<SysDictTypeForm,SysDictType> {

	private static Logger lg = Logger.getLogger(SysDictTypeController.class);
	
	@Autowired
	MessageSource messageSource;
	
	
	@Autowired
	private SysDictTypeDao sysDictTypeDao;
	
	
	
	@Override
	public void createSearchDAO(HttpServletRequest request, SysDictTypeForm form, BaseDao<SysDictType> dao) throws Exception {
		if (!Formater.isNull(form.getCode())) {
			dao.addRestriction(Restrictions.like("code", form.getCode().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
		if (!Formater.isNull(form.getName())) {
			dao.addRestriction(Restrictions.like("name", form.getName().trim(), MatchMode.ANYWHERE).ignoreCase());
		}	
	}

	@Override
	public void pushToJa(JSONArray ja, SysDictType temp, SysDictTypeForm modal) throws Exception {
		ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + temp.getId() + "\")'>"
				+ temp.getName() + "</a>");
		ja.put(temp.getCode());
		ja.put(temp.getDescription());
		
		
	}

	@Override
	public BaseDao<SysDictType> getDao() {
		return sysDictTypeDao;
	}

	@Override
	public String getJsp() {
		
		return "qtht/loai_tu_dien";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysDictTypeForm form)
			throws Exception {
		ApplicationContext appContext = (ApplicationContext) rq.getSession()
				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
		if (appContext != null) {
			SysUsers user = (SysUsers) appContext.getAttribute(ApplicationContext.USER);
			lg.info(user);
		}
	}

}
