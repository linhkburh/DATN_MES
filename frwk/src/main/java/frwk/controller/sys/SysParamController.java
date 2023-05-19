package frwk.controller.sys;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import common.util.Formater;
import common.util.ResourceException;
import entity.frwk.SysParam;
import frwk.constants.Constants;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysParamDao;
import frwk.form.ModelForm;
import frwk.form.SysParamForm;

@Controller
@RequestMapping("/param")
public class SysParamController extends CatalogController<SysParamForm, SysParam> {

	static Logger lg = Logger.getLogger(SysParamController.class);

	@Autowired
	private SysParamDao sysParamDao;
	
	@Autowired
	private MessageSource messageSource;

	@Override
	public BaseDao<SysParam> getDao() {
		return sysParamDao;
	}

	SysParam syspa = new SysParam();


	@Override
	public void createSearchDAO(HttpServletRequest request, SysParamForm form,BaseDao<SysParam> dao) throws Exception {
		if (!Formater.isNull(form.getSname()))
			dao.addRestriction(Restrictions.like("name", form.getSname().trim(), MatchMode.ANYWHERE).ignoreCase());
		if (!Formater.isNull(form.getScode()))
			dao.addRestriction(Restrictions.like("code", form.getScode().trim(), MatchMode.ANYWHERE).ignoreCase());
		dao.addOrder(Order.desc("code"));
		dao.addOrder(Order.desc("name"));
	}

	@Override
	public void pushToJa(JSONArray ja, SysParam r, SysParamForm modal) throws Exception {
		ja.put("<a href = '#' onclick = 'edit(\"" + r.getId() + "\")'>" + StringEscapeUtils.escapeHtml(r.getCode())
				+ "</a>");
		ja.put(StringEscapeUtils.escapeHtml(r.getName()));
		ja.put(r.getDescription());
		ja.put(StringEscapeUtils.escapeHtml(r.getValue()));
	}

	@Override
	public String getJsp() {
		return "qtht/danh_muc_tham_so";
	}


	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysParamForm form)
			throws Exception {
	}
	
	
//	public void save1213(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysParamForm form)
//			throws Exception {
//		SysParamForm paramForm = (SysParamForm) form;
//		SysParam param = paramForm.getSysParam();
//		if (param != null && !Formater.isNull(param.getId())) {
//			SysParam olsParam = sysParamDao.get(SysParam.class,param.getId());
//			if (!olsParam.getCode().equals(param.getCode()) && sysParamDao.getSysParamByCode(param.getCode()) != null) {
//				throw new ResourceException(param.getCode() + messageSource.getMessage(Constants.DUPLICATE_CODE, null, "Default", null));
//			}
//		} else {
//			if (sysParamDao.getSysParamByCode(param.getCode()) != null) {
//				throw new ResourceException(param.getCode() + messageSource.getMessage(Constants.DUPLICATE_CODE, null, "Default", null));
//			}
//		}
//		
//		sysParamDao.save(param);
//	}

//	@Override
//	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysParamForm form)
//			throws Exception {
//        if (Formater.isNull(syspa.getId())) {
//            if (!RightUtils.haveRight(RightConstants.SYSPARAM_Create,
//                                      (ApplicationContext)rq.getSession().getAttribute(ApplicationContext.APPLICATIONCONTEXT))) {
//                throw new ResourceException("not_access");
//            }
//        } else {
//            if (!RightUtils.haveRight(RightConstants.SYSPARAM_Edit,
//                                      (ApplicationContext)rq.getSession().getAttribute(ApplicationContext.APPLICATIONCONTEXT))) {
//                throw new ResourceException("not_access");
//            }
//        }
//
//        super.save(model, rq, rs, form);
//	}

//	public void del(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysParamForm form) throws Exception {
//		if (!RightUtils.haveRight(RightConstants.SYSPARAM_Delete,
//				(ApplicationContext) rq.getSession().getAttribute(ApplicationContext.APPLICATIONCONTEXT))) {
//			throw new ResourceException("not_access");
//		} else {
//			super.del(model, rq, rs, form);
//		}
//	}

//	@Override
//	public void edit(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysParamForm form)
//			throws Exception {
//		try {
//			RightUtils.chkRight(rq, "SYSPARAM");
//		} catch (ResourceException ex) {
//			lg.error(ex);
//		}
//		syspa = sysParamDao.getById(rq.getParameter("id"));
//		Utils.jsonSerialize(rs, syspa);
//	}

	public SysParam getSyspa() {
		return syspa;
	}

	public void setSyspa(SysParam syspa) {
		this.syspa = syspa;
	}
}