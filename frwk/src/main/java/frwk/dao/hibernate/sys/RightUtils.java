package frwk.dao.hibernate.sys;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import common.util.ResourceException;
import entity.frwk.SysObjects;
import entity.frwk.SysUsers;
import frwk.utils.ApplicationContext;

@Repository(value = "rightUtils")
public class RightUtils {
	private static final Logger logger = Logger.getLogger(RightUtils.class);
	@Autowired
	private UserDao userDao;


	@SuppressWarnings("unchecked")
	public boolean haveRight(String rightCode, ApplicationContext context) {
		ArrayList<SysObjects> rights = (ArrayList<SysObjects>) context.getAttribute(ApplicationContext.USER_RIGHT);
		for (SysObjects o : rights)
			if (rightCode.equals(o.getObjectId()))
				return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	public boolean haveAnyRight(String rightCode, ApplicationContext context) {
		ArrayList<SysObjects> rights = (ArrayList<SysObjects>) context.getAttribute(ApplicationContext.USER_RIGHT);
		return userDao.haveAnyRight(rightCode, rights);
	}

	@SuppressWarnings("unchecked")
	public boolean haveRight(HttpServletRequest rq, String rightCode) {
		ApplicationContext appContext = (ApplicationContext) rq.getSession()
				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
		ArrayList<SysObjects> rights = (ArrayList<SysObjects>) appContext.getAttribute(ApplicationContext.USER_RIGHT);
		for (SysObjects o : rights)
			if (rightCode.equals(o.getObjectId()))
				return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	public boolean haveMenuGroup(String mnGroupCode, ApplicationContext context) {
		ArrayList<SysObjects> rights = (ArrayList<SysObjects>) context.getAttribute(ApplicationContext.USER_RIGHT);
		for (SysObjects o : rights) {
			if (o.getSysObjects().getObjectId().equals(mnGroupCode))
				return true;
		}

		return true;
	}

	public void chkRight(HttpServletRequest rq, String rightNeedChk) throws ResourceException {
		ApplicationContext appContext = (ApplicationContext) rq.getSession()
				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
		if (!haveRight(rightNeedChk, appContext))
			throw new ResourceException("not_access");
	}

	@Autowired
	private HttpSession session;

	public SysUsers getSessionUser() {
		ApplicationContext appContext = (ApplicationContext) session
				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
		return (SysUsers) appContext.getAttribute(ApplicationContext.USER);
	}

	@SuppressWarnings("unchecked")
	@Cacheable(value = "rightUtils.haveAction", unless = "#result == null")
	public boolean haveAction(HttpServletRequest rq, String action) throws ResourceException {
		String uri = rq.getRequestURI();
		// /hdtg
		String contextPath = rq.getContextPath();
		String URI = uri.substring(uri.indexOf(contextPath) + contextPath.length() + 1);
		// Admin luon vao duoc 2 chuc nang quan ly nguoi dung, quan ly nhom quyen
		ApplicationContext ac = (ApplicationContext) rq.getSession()
				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
		SysUsers user = (SysUsers) ac.getAttribute(ApplicationContext.USER);
		if ("admin".equals(user.getUsername())) {
			if (URI.endsWith("role") || URI.endsWith("manageUser"))
				return true;
		}

		if (byPassRight(URI))
			return true;

		ApplicationContext context = (ApplicationContext) rq.getSession()
				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);

		String requestUrl = rq.getRequestURL().toString();
		String fullContextPath = requestUrl.substring(0, requestUrl.indexOf(contextPath) + contextPath.length() + 1); // http://localhost:7101/web/

		// Kiem tra trong quyen nsd
		ArrayList<SysObjects> userRights = (ArrayList<SysObjects>) context.getAttribute(ApplicationContext.USER_RIGHT);
		// if (Formater.isNull(userRights))
		// return false;
		for (SysObjects obj : userRights) {
			if (yesItis(obj, URI, contextPath, rq, fullContextPath, action))
				return true;
		}

		// Kiem tra quyen co duoc dinh nghia trong SysRights khong
		ArrayList<SysObjects> sysRights = (ArrayList<SysObjects>) context.getAttribute(ApplicationContext.SYS_RIGHT);
		if (Formater.isNull(sysRights))
			return true;
		for (SysObjects obj : sysRights) {
			if (yesItis(obj, URI, contextPath, rq, fullContextPath, action))
				return false;
		}
		return true;

	}

	private boolean yesItis(SysObjects obj, String URI, String contextPath, HttpServletRequest req,
			String fullContextPath, String action) {
		URL objUrl;
		try {
			objUrl = new URL(fullContextPath + obj.getAction());
		} catch (MalformedURLException e) {
			return false;
		}
		String objUri = null;
		try {
			objUri = objUrl.toURI().getPath();
			objUri = objUri.substring(objUri.indexOf(contextPath) + contextPath.length() + 1);
			if (!objUri.endsWith(""))
				objUri += "";
		} catch (Exception e) {
			return false;
		}

		if (!objUri.equalsIgnoreCase(URI))
			return false;

		// kiem tra tham so tinh (tham so dong bo qua ex: id=124)
		String sActionParam = obj.getAction().split("\\?").length > 1 ? obj.getAction().split("\\?")[1] : "";
		String[] arrActionParam = Formater.isNull(sActionParam) ? new String[] {} : sActionParam.split("&");
		String[] actionParamPair = action.split("&");
		Map<String, String> mActionParam = new HashMap<String, String>();
		for (int i = 0; i < actionParamPair.length; i++) {
			String[] actionParamInf = actionParamPair[i].split("=");
			if (actionParamInf.length != 2)
				continue;
			mActionParam.put(actionParamInf[0], actionParamInf[1]);
		}

		// True neu moi tham so trong cau hinh deu ton tai trong request
		if (arrActionParam.length > 0) {
			for (String param : arrActionParam) {
				String[] paramWithValue = param.split("=");
				String paramKey = paramWithValue[0];
				String paramValue = paramWithValue[1];
				if (!Formater.isNull(paramValue))
					paramValue = paramValue.trim().toLowerCase();
				String requestValue = mActionParam.get(paramKey);
				if (!Formater.isNull(requestValue))
					requestValue = requestValue.trim().toLowerCase();
				if (Formater.isNull(paramValue) && Formater.isNull(requestValue))
					continue;
				if (Formater.isNull(paramValue) && !Formater.isNull(requestValue))
					return false;
				if (Formater.isNull(requestValue) && !Formater.isNull(paramValue))
					return false;
				if (!paramValue.equals(requestValue))
					return false;
			}
		}
// submit den method nhung action khai bao khong co tham so method
		// else if (req.getParameter("method") != null)
		else if (action.indexOf("method") >= 0)
			return false;
		return true;
	}

	private boolean byPassRight(String URI) {
		return false;
	}

	@Autowired
	private SysObjectDao sysObjectDao;

	/**
	 * Check quyen thuc hien theo ma quyen
	 * 
	 * @param su
	 * @param rightCode Ma quyen
	 * @throws ResourceException Nguoi dung khong co quyen thuc hien
	 */
	@SuppressWarnings("unchecked")
	public void checkRight(SysUsers su, String rightCode) throws ResourceException {
		Criteria search = sysObjectDao.getCurrentSession().createCriteria(SysObjects.class);
		search.add(Restrictions.eq("objectId", rightCode));
		search.add(Restrictions.sqlRestriction(
				"exists (select 1\r\n" + "  from sys_roles rl, sys_rights rg, sys_users u, sys_roles_users ru\r\n"
						+ " where {alias}.id = rg.object_id\r\n" + "   and rg.role_id = rl.id\r\n"
						+ "   and rl.id = ru.role_id\r\n" + "   and ru.user_id = u.id\r\n" + "   and u.username = ?)",
				new Object[] { su.getUsername() }, new Type[] { new StringType() }));
		List<SysObjects> lstObjs = search.setMaxResults(1).list();
		if (lstObjs.isEmpty())
			throw new ResourceException("Người dùng không có quyền thực hiện chức năng");
	}

}
