package frwk.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

import common.util.AESUtils;
import common.util.Formater;
import common.util.JsonUtils;
import common.util.LocaleUtils;
import common.util.Messages;
import common.util.ResourceException;
import entity.DLVPackage;
import entity.frwk.SysUsers;
import frwk.editors.spring.BigDecimalEditor;
import frwk.editors.spring.DateEditor;
import frwk.editors.spring.LongEditor;
import frwk.editors.spring.TimestampEditor;
import frwk.form.ModelForm;
import frwk.form.SearchForm;
import frwk.utils.ApplicationContext;
import net.sf.ehcache.CacheManager;

public abstract class CommonController<F extends ModelForm<T>, T> {
	private static Logger lg = Logger.getLogger(CommonController.class);

	public abstract String getJsp();

	private F modelForm;

	public F getModelForm() {
		return modelForm;
	}

	@Autowired
	private HttpSession session;

	public SysUsers getSessionUser() {
		ApplicationContext appContext = (ApplicationContext) session
				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
		return (SysUsers) appContext.getAttribute(ApplicationContext.USER);
	}

	private Boolean isAjaxRq(HttpServletRequest rq) {
		return "XMLHttpRequest".equals(rq.getHeader("X-Requested-With"));
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	public String execute(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			@ModelAttribute("formDataModelAttr") F form) throws Exception {
		try {
			this.modelForm = form;
			String method = rq.getParameter("method");
			if (!Formater.isNull(method)) {
				redirectMethod(model, rq, rs, form, method);
				if (isAjaxRq(rq))
					return null;
				else {
					if (rs.isCommitted()) {
						lg.info("response is commit");
						return null;
					}
					return getJsp();
				}
			}
			initData(model, rq, rs, form);
			String tokenId = UUID.randomUUID().toString();
			form.setTokenId(tokenId);
			String tokenIdKey = UUID.randomUUID().toString();
			form.setTokenIdKey(tokenIdKey);
			rq.getSession().setAttribute(tokenIdKey, tokenId);
			model.addAttribute("formDataModelAttr", form);
			return getJsp();
		} catch (Exception ex) {
			lg.error("Loi", ex);
			ResourceException rse = null;
			if (ex instanceof ResourceException)
				rse = (ResourceException) ex;
			else {
				Throwable cause = ex.getCause();
				while (cause != null) {
					if (cause instanceof ResourceException) {
						rse = (ResourceException) cause;
						break;
					}
					cause = cause.getCause();
				}
			}
			if (rse != null) {
				String message = null;
				if (!Formater.isNull(rse.getMessage())) {
					if (!Formater.isNull(rse.getParam()))
						message = String.format(getText(rse.getMessage()), rse.getParam());
					else if (!Formater.isNull(rse.getParams()))
						message = String.format(getText(rse.getMessage()), rse.getParams());
					else
						message = getText(rse.getMessage());
				}
				if (isAjaxRq(rq)) {
					returnTxtHtml(rs, message);
				} else {
					model.addAttribute("errorMsg", message);
					return "base/defined_error";
				}
				// Throw to rollback transaction
				throw rse;
			}
			throw ex;
		}

	}

	public abstract void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, F form)
			throws Exception;

	@SuppressWarnings("unchecked")
	private void redirectMethod(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, F form, String method)
			throws Exception {
		Method mt = getMethod(method,
				new Class[] { ModelMap.class, HttpServletRequest.class, HttpServletResponse.class, form.getClass() });
		if (mt == null)
			throw new ResourceException("Khong ton tai phuong thuc xu ly");

		if (mt.isAnnotationPresent(HttpMethod.class)) {
			Annotation anotation = mt.getAnnotation(HttpMethod.class);
			HttpMethod httpMethod = (HttpMethod) anotation;
			String smtmethod = httpMethod.method() == RequestMethod.GET ? "GET" : "POST";
			if (!smtmethod.equals(rq.getMethod()))
				throw new ResourceException("action only support " + smtmethod);
		}
		mt.invoke(this, model, rq, rs, form);
		if (mt.isAnnotationPresent(ClearOnFinish.class))
			((SearchController<SearchForm<T>, T>) this).getDao().clear();

	}

	private Method getMethod(String methodName, Class<?>[] parameterTypes) {
		for (Method method : this.getClass().getMethods()) {
			if (!method.getName().equals(methodName))
				continue;
			Class<?>[] clsParameterTypes = method.getParameterTypes();
			if (clsParameterTypes.length != parameterTypes.length)
				continue;
			for (int i = 0; i < clsParameterTypes.length; i++) {
				boolean iOk = true;
				if (!clsParameterTypes[i].equals(parameterTypes[i])) {
					iOk = false;
					break;
				}
				if (!iOk)
					continue;
			}
			return method;
		}
		return null;

	}

	private static final String MESSAGES_BUNDLE = Messages.BUNDLE_MESSAGE_NAME;

	protected String getText(String key, final Object... args) {
		// return getCurrentMessages(MESSAGES_BUNDLE).get(key, args);
		return key;
	}

	private Messages getCurrentMessages(String baseName) {
		return Messages.buildMessages(baseName, getCurrentLocale());
	}

	protected Locale getCurrentLocale() {
		Locale locale = LocaleContextHolder.getLocale();
		return LocaleUtils.fullLocale(locale);
	}

	public void returnJson(HttpServletResponse rs, Object o) throws IOException {
		if (o == null)
			return;
		JsonUtils.writeToResponse(o, rs);
	}
	public void returnJson(HttpServletResponse rs, Object o, ObjectMapper om) throws IOException {
		rs.setContentType("application/json;charset=utf-8");
		rs.setCharacterEncoding("utf-8");
		rs.setHeader("Cache-Control", "no-store");
		OutputStream os = rs.getOutputStream();
		om.writeValue(os, o);
		os.flush();
		os.close();
		
	}
	@SuppressWarnings("rawtypes")
	public void returnJsonArray(HttpServletResponse rs, Collection lst) throws IOException {
		String lstRmAsJson = new ObjectMapper().writeValueAsString(lst);
		rs.setContentType("application/json;charset=utf-8");
		rs.setHeader("Cache-Control", "no-store");
		PrintWriter out = rs.getWriter();
		JSONArray jsonArray = new JSONArray(lstRmAsJson);
		out.print(jsonArray);
		out.flush();
		out.close();
	}

	public void returnTxtHtml(HttpServletResponse rs, String foo) throws IOException {
		rs.setContentType("text/plan;charset=utf-8");
		PrintWriter pw = rs.getWriter();
		pw.print(foo);
		pw.flush();
		pw.close();
	}

	@Autowired
	private TimestampEditor timestampEditor;
	@Autowired
	private DateEditor dateEditor;
	@Autowired
	private BigDecimalEditor bigDecimalEditor;
	@Autowired
	private LongEditor longEditor;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(TemporalType.class, timestampEditor);
		binder.registerCustomEditor(Date.class, dateEditor);
		binder.registerCustomEditor(BigDecimal.class, bigDecimalEditor);
		binder.registerCustomEditor(Long.class, longEditor);
		binder.registerCustomEditor(Integer.class, longEditor);

	}

	@Autowired
	private CacheManager cacheManager;

	public void clearCache(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, F form) {
		lg.info("Begin cacheManager.clearAll()");
		cacheManager.clearAll();
		lg.info("End cacheManager.clearAll()");
	}

	public void encrypt(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, F form)
			throws IOException, Exception {
		returnTxtHtml(rs, AESUtils.encrypt(rq.getParameter("input")));
	}

	public void decrypt(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, F form) throws IOException, Exception {
		returnTxtHtml(rs, AESUtils.decrypt(rq.getParameter("input")));
	}
}
