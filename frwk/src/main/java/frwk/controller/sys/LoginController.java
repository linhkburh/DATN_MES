package frwk.controller.sys;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import common.util.DefinedException;
import common.util.Formater;
import common.util.RandomPassWord;
import common.util.ResourceException;
import entity.frwk.SysObjects;
import entity.frwk.SysParam;
import entity.frwk.SysRoles;
import entity.frwk.SysRolesUsers;
import entity.frwk.SysUsers;
import entity.frwk.UserLog;
import frwk.constants.Constants;
import frwk.controller.CommonController;
import frwk.dao.hibernate.sys.RightUtils;
import frwk.dao.hibernate.sys.SysObjectDao;
import frwk.dao.hibernate.sys.SysParamDao;
import frwk.dao.hibernate.sys.SysUsersDao;
import frwk.dao.hibernate.sys.UserDao;
import frwk.dao.hibernate.sys.UserLogDao;
import frwk.form.LoginForm;
import frwk.utils.ApplicationContext;

@Controller
@RequestMapping("/login")
public class LoginController extends CommonController<LoginForm, SysUsers> {
	@Override
	public String getJsp() {
		return "qtht/login";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, LoginForm form)
			throws Exception {
		Object object = rq.getSession().getAttribute("login");
		String login = object == null ? "" : object.toString();
		if (login == "false") {
			model.addAttribute("status", "false");
		} else if (login == "exprired") {
			model.addAttribute("status", "exprired");
		} else if (login == "firstLogin") {
			model.addAttribute("status", "firstLogin");
		} else {
			model.addAttribute("status", "");
		}

	}

	private static Logger lg = Logger.getLogger(LoginController.class);
	@Autowired
	private UserDao userDao;
	@Autowired
	private SysObjectDao sysObjectDao;


	@Autowired
	private MessageSource messageSource;

	public void loginProcess(ModelMap map, HttpServletRequest rq, HttpServletResponse rs, LoginForm form)
			throws Exception {

		// Authenticate
		String username = rq.getParameter("username");
		String password = rq.getParameter("password");
		SysUsers u = userDao.getActiveUserByUserName(username);
		if (u == null)
			throw new ResourceException(Constants.LOGIN_FAIL);
		// Password bi khoa
		if (Formater.isNull(u.getPasswordStatus())) {
			// Cache user infor
			ApplicationContext appContext = new ApplicationContext();
			appContext.setAttribute(ApplicationContext.USER, u);
			rq.getSession().setAttribute(ApplicationContext.APPLICATIONCONTEXT, appContext);
			// Cache thong tin NSD de xac minh NSD co phai la cong nhan khong
			List<SysObjects> lstUserObjs = sysObjectDao.getUserRight(u.getUsername());
			appContext.setAttribute(ApplicationContext.USER_RIGHT, lstUserObjs);
			List<SysObjects> lstAllObjs = sysObjectDao.getAll();
			appContext.setAttribute(ApplicationContext.SYS_RIGHT, lstAllObjs);
			throw new ResourceException(Constants.FIRSTS_LOGIN);
		}

		if (u.getBlockTime() != null) {
			Calendar temp = Calendar.getInstance();
			temp.setTime(u.getBlockTime());
			temp.add(Calendar.MINUTE, 5);
			// Chua het thoi gian cho 5 phut
			if (temp.compareTo(Calendar.getInstance()) > 0)
				throw new ResourceException(Constants.LOGIN_EXPRIRED);
		}

		// Password fail
		if (!validatePassword(u, password)) {
			long countLoginFaild = u.getLoginErrorTimes();
			if (countLoginFaild >= 5) {
				u.setBlockTime(new Date());
			} else {
				if (countLoginFaild == 0)
					countLoginFaild = 1;
				u.setLoginErrorTimes(++countLoginFaild);
			}
			// Khong throw exception, neu throw se khong commit duoc
			returnTxtHtml(rs, Constants.LOGIN_FAIL);
			return;

		} else {
			if (u.getLoginErrorTimes() > 0)
				u.setLoginErrorTimes(1);
		}

		// Ghi log dang nhap
		UserLog lg = new UserLog(u);
		lg.setAction("login");
		userLogDao.writeLoginLog(lg);

		// Cache user infor
		ApplicationContext appContext = new ApplicationContext();
		rq.getSession().setAttribute(ApplicationContext.APPLICATIONCONTEXT, appContext);
		appContext.setAttribute(ApplicationContext.USER, u);
		List<SysObjects> lstUserObjs = sysObjectDao.getUserRight(u.getUsername());
		appContext.setAttribute(ApplicationContext.USER_RIGHT, lstUserObjs);
		List<SysObjects> lstAllObjs = sysObjectDao.getAll();
		appContext.setAttribute(ApplicationContext.SYS_RIGHT, lstAllObjs);
		//String menu = menuService.make(lstUserObjs, lstAllObjs);
		String menu = menuService2.make(lstUserObjs, lstAllObjs);
		if (!Formater.isNull(menu))
			rq.getSession().setAttribute("menu", menu);

		// Mat khau het han, bat cua so thay doi mat khau
		if (passWordExpired(u.getPwdDate())) {
			returnTxtHtml(rs, Constants.EXPRIRED);
			return;
		}
		String defautPage = getDefaultPage(rq, u);
		if (!Formater.isNull(defautPage))
			returnTxtHtml(rs, defautPage);
	}

	public String getDefaultPage(HttpServletRequest rq, SysUsers su) throws ResourceException {
		SysRoles defaultRole = null;
		for (SysRolesUsers ur : su.getSysRolesUserses()) {
			SysRoles role = ur.getSysRoles();
			if (role == null || Formater.isNull(role.getDefaultPage()))
				continue;
			if (defaultRole == null) {
				defaultRole = role;
				continue;
			}
			if (role.getDefaultPagePriority() == null)
				continue;
			if (defaultRole.getDefaultPagePriority() == null) {
				defaultRole = role;
				continue;
			}
			if (!rightUtils.haveAction(rq, role.getDefaultPage()))
				continue;
			if (defaultRole.getDefaultPagePriority().intValue() > role.getDefaultPagePriority().intValue())
				defaultRole = role;
		}
		if (defaultRole != null)
			return defaultRole.getDefaultPage();
		return null;
	}

	@Autowired
	private RightUtils rightUtils;
	@Autowired
	private Menu menuService;
	@Autowired
	private Menu2 menuService2;

	private boolean passWordExpired(Date pwDate) {
		SysParam expriredDays = sysParamDao.getSysParamByCode("PW_EXPIRED_IN");
		if (expriredDays == null || Formater.isNull(expriredDays.getValue()))
			return false;
		Calendar c = Calendar.getInstance();
		c.setTime(pwDate);
		c.add(Calendar.DATE, Integer.parseInt(expriredDays.getValue()));
		if (c.after(Calendar.getInstance())) {
			return false;
		} else {
			return true;
		}
	}

	public void changepassword(ModelMap map, HttpServletRequest rq, HttpServletResponse rp, LoginForm form)
			throws Exception {
		String oldPass = rq.getParameter("oldPassWord");
		SysUsers user = getSessionUser();
		if (!validatePassword(user, oldPass)) {
			returnTxtHtml(rp, messageSource.getMessage("Login.changepw.incorrect", null, "Default",
					LocaleContextHolder.getLocale()));
			return;
		}

		String newPass = rq.getParameter("newPassWord");
		if (newPass.equals(oldPass)) {
			returnTxtHtml(rp, messageSource.getMessage("login.changepw.newpw.incorrect", null, "Default",
					LocaleContextHolder.getLocale()));
			return;
		}

		if (isWorker(rq)) {
			if (newPass.length() < 8)
				returnTxtHtml(rp, getText("Mật khẩu tối thiểu phải có 8 ký tự"));
		} else {
			if (!new RandomPassWord(8, 20).check(newPass)) {
				returnTxtHtml(rp, messageSource.getMessage("login.changepw.wrong.pw", null, "Default",
						LocaleContextHolder.getLocale()));
				return;
			}
		}

		// save encode new password
		sysUsersDao.changePassword(user, encode.encode(newPass));
		String firstLogin = rq.getParameter("firstLogin");
		if ("firstLogin".equalsIgnoreCase(firstLogin)) {
			user.setPasswordStatus("1");
			sysUsersDao.save(user);
			returnTxtHtml(rp, getText("success"));
		}

	}

	private static boolean yesItIs(String sourceAct, String desAction) {
		if (sourceAct.indexOf("?") < 0) {
			if (desAction.indexOf("?") >= 0)
				return false;
			return sourceAct.equals(desAction);
		}
		if (desAction.indexOf("?") < 0)
			return false;

		String sourceActionParam = sourceAct.split("\\?").length > 1 ? sourceAct.split("\\?")[1] : "";
		String[] arrSourceActionParam = Formater.isNull(sourceActionParam) ? new String[] {}
				: sourceActionParam.split("&");
		String desActionParam = desAction.split("\\?").length > 1 ? desAction.split("\\?")[1] : "";
		String[] arrDesActionParam = Formater.isNull(desActionParam) ? new String[] {} : desActionParam.split("&");
		// Khong cung so luong tham so
		if (arrDesActionParam.length != arrSourceActionParam.length)
			return false;

		// So sanh tham so va gia tri
		Map<String, String> mSourceParm = new HashMap<String, String>();
		for (int i = 0; i < arrSourceActionParam.length; i++) {
			String[] temp = arrSourceActionParam[i].split("=");
			mSourceParm.put(temp[0], temp[1]);
		}
		Map<String, String> mDesParm = new HashMap<String, String>();
		for (int i = 0; i < arrDesActionParam.length; i++) {
			String[] temp = arrDesActionParam[i].split("=");
			mDesParm.put(temp[0], temp[1]);
		}

		for (String sourceParam : mSourceParm.keySet()) {
			// Khong trung tham so
			if (!mDesParm.containsKey(sourceParam))
				return false;
			// Khong trung gia tri
			if (!mDesParm.get(sourceParam).equals(mSourceParm.get(sourceParam)))
				return false;
		}
		return true;

	}

	@SuppressWarnings("unchecked")
	private boolean isWorker(HttpServletRequest rq) {
		ApplicationContext appContext = (ApplicationContext) rq.getSession()
				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
		List<SysObjects> userObjects = (List<SysObjects>) appContext.getAttribute(ApplicationContext.USER_RIGHT);
		String addWoeAction = "workOderDetail?method=addWorkOrderDetail&saveType=createNew";
		// Kiem tra quyen NSD
		for (SysObjects so : userObjects) {
			if (yesItIs(addWoeAction, so.getAction()))
				return true;
		}
		// Kiem tra quyen co duoc dinh nghia trong SysRights khong
		List<SysObjects> sysRights = (List<SysObjects>) appContext.getAttribute(ApplicationContext.SYS_RIGHT);
		if (Formater.isNull(sysRights))
			return true;
		for (SysObjects so : sysRights) {
			// lg.info(String.format("menu.getAction(): %s, so.getAction(): %s", new Object[] {menu.getAction(),
			// so.getAction()}));
			if (yesItIs(addWoeAction, so.getAction()))
				return false;
		}
		return true;
	}

	private static final BCryptPasswordEncoder encode = new BCryptPasswordEncoder();

	public void logout(ModelMap map, HttpServletRequest rq, HttpServletResponse rs, LoginForm form) throws Exception {
		rq.getSession().removeAttribute(ApplicationContext.APPLICATIONCONTEXT);

	}

	@Autowired
	private UserLogDao userLogDao;
	@Autowired
	private SysUsersDao sysUsersDao;

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	public String execute(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			@ModelAttribute("loginForm") LoginForm form) throws Exception {
		return super.execute(model, rq, rs, form);

	}

	@SuppressWarnings({ "unused" })
	private boolean adAuthenticate(String user, String pass) {
		Hashtable<String, String> env = new Hashtable<String, String>(11);

		boolean b = false;

		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://ovd.vietinbank.vn:389");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, "uid=" + user + ",ou=system");
		env.put(Context.SECURITY_CREDENTIALS, pass);

		try {
			// Create initial context
			DirContext ctx = new InitialDirContext(env);

			// Close the context when we're done
			b = true;
			ctx.close();

		} catch (NamingException e) {
			b = false;
		}
		return b;
	}

	@Autowired
	private SysParamDao sysParamDao;

	private boolean validatePassword(SysUsers u, String pass) throws Exception {
		SysParam LDAP_AUTHEN = sysParamDao.getSysParamByCode("LDAP_AUTHEN");

		if (LDAP_AUTHEN == null || !"true".equalsIgnoreCase(LDAP_AUTHEN.getValue())) {
			// return u.getPassword().equals(userDao.getHash(u.getUsername(), pass));
			// new encode
			return encode.matches(pass, u.getPassword());
		}
		if (!"true".equalsIgnoreCase(LDAP_AUTHEN.getValue()))
			// return u.getPassword().equals(userDao.getHash(u.getUsername(), pass));
			return encode.matches(pass, u.getPassword());
		return ldapAuthen(u.getUsername(), pass);
	}

	private boolean ldapAuthen(String userName, String passWord) throws Exception {
		if (Formater.isNull(passWord))
			throw new DefinedException("password is null");

		try {
			Hashtable<String, String> env = new Hashtable<String, String>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.SECURITY_AUTHENTICATION, "Simple");
			env.put(Context.PROVIDER_URL, sysParamDao.getSysParamByCode("LDAPSVR").getValue());
			LdapContext ctx = new InitialLdapContext(env, null);

			// Search
			String searchFilter = "(&(objectClass=person)(sAMAccountName=" + userName + "))";

			SearchControls searchControls = new SearchControls();
			searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

			String ldapSearchBase = "dc=vietinbank,dc=com";
			NamingEnumeration<SearchResult> results = ctx.search(ldapSearchBase, searchFilter, searchControls);
			SearchResult element = results.nextElement();
			String user = element.getNameInNamespace();

			// Authen
			env.put(Context.SECURITY_PRINCIPAL, user);
			env.put(Context.SECURITY_CREDENTIALS, passWord);
			ctx = new InitialLdapContext(env, null);
			ctx.close();

			return true;
		} catch (NamingException nex) {
			System.out.println("LDAP Connection: FAILED");
			nex.printStackTrace();
			return false;
		}

	}

}
