package frwk.controller;

import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import common.util.Formater;
import common.util.ResourceException;
import frwk.dao.hibernate.sys.RightUtils;
import frwk.form.SearchForm;

public abstract class CatalogController<F extends SearchForm<T>, T> extends SearchController<F, T> {
	@Autowired
	private RightUtils rightUtils;
	private static final Logger logger = Logger.getLogger(CatalogController.class);

	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, F form) throws Exception {
		Serializable id = getDao().getId(form.getModel());

		boolean insert = false;
		if (id == null)
			insert = true;
		else {
			if (id.getClass().equals(String.class))
				insert = Formater.isNull((String) id);
			else if (id.getClass().equals(Long.class))
				insert = ((Long) id).intValue() == 0;

		}

		if (insert) {
			if (!rightUtils.haveAction(rq, "method=save&saveType=createNew"))
				throw new ResourceException("B&#7841;n kh&#244;ng c&#243; quy&#7873;n th&#234;m m&#7899;i!");

		} else {
			if (!rightUtils.haveAction(rq, "method=save&saveType=update"))
				throw new ResourceException("B&#7841;n kh&#244;ng c&#243; quy&#7873;n s&#7917;a!");
		}

		getDao().save(form.getModel());
	}

	public void del(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, F form) throws Exception {
		if (!rightUtils.haveAction(rq, "method=del"))
			throw new ResourceException("B&#7841;n kh&#244;ng c&#243; quy&#7873;n x&#243;a!");
		if(!getDao().getCurrentSession().contains(form.getModel()))
			getDao().load(form.getModel());
		getDao().del(form.getModel());

	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	@Override
	public String execute(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			@ModelAttribute("formDataModelAttr") F form) throws Exception {
		String method = rq.getParameter("method");
		if (Formater.isNull(method)) {
			if (rightUtils.haveAction(rq, "method=save&saveType=update")
					|| rightUtils.haveAction(rq, "method=save&saveType=createNew")) {
				model.addAttribute("save", true);
				if (rightUtils.haveAction(rq, "method=save&saveType=createNew"))
					model.addAttribute("add", true);
				else
					model.addAttribute("add", false);
			}
			if (rightUtils.haveAction(rq, "method=del"))
				model.addAttribute("del", true);

		} else {
			try {
				if ("datatable".equals(method)) {
					datatable(model, rq, rs, form);
					return null;
				}
				if ("del".equals(method)) {
					del(model, rq, rs, form);
					return null;
				}
				if ("edit".equals(method)) {
					edit(model, rq, rs, form);
					return null;
				}
				if ("save".equals(method)) {
					save(model, rq, rs, form);
					return null;
				}
				if ("copy".equals(method)) {
					copy(model, rq, rs, form);
					return null;
				}
			} catch (Exception ex) {
				logger.error("Loi", ex);
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
					if (!Formater.isNull(rse.getMessage())) {
						rs.setContentType("text/plan;charset=utf-8");
						PrintWriter pw = rs.getWriter();
						if (!Formater.isNull(rse.getParam()))
							pw.print(String.format(getText(rse.getMessage()), rse.getParam()));
						else if (!Formater.isNull(rse.getParams()))
							pw.print(String.format(getText(rse.getMessage()), rse.getParams()));
						else
							pw.print(getText(rse.getMessage()));
						pw.flush();
						pw.close();
						throw rse;
					}
				}
				throw ex;
			}

		}
		return super.execute(model, rq, rs, form);
	}

	private Class<T> modalClss;

	@SuppressWarnings("unchecked")
	@PostConstruct
	private void init() {
		Class<?> modalFormClss;
		try {
			modalFormClss = (Class<?>) ((ParameterizedType) this.getClass().getGenericSuperclass())
					.getActualTypeArguments()[0];
		} catch (Exception e) {
			modalFormClss = (Class<?>) ((ParameterizedType) this.getClass().getSuperclass().getGenericSuperclass())
					.getActualTypeArguments()[0];
		}
		modalClss = (Class<T>) ((ParameterizedType) modalFormClss.getGenericSuperclass()).getActualTypeArguments()[0];
	}

	@ClearOnFinish
	@SuppressWarnings("unchecked")
	public void edit(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, F form) throws Exception {
		String id = rq.getParameter("id");
		getDao().getCurrentSession().load(form.getModel(), id);
		returnJson(rs, form.getModel());
		((SearchController<SearchForm<T>, T>) this).getDao().clear();
	}

	@SuppressWarnings("unchecked")
	public void copy(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, F form) throws Exception {
		T o = (T) getDao().getObject(modalClss, getDao().getId(form.getModel()));
		try {
			Method copy = form.getModel().getClass().getDeclaredMethod("copy");
			o = (T) copy.invoke(o);
			getDao().save(o);
		} catch (NoSuchMethodException ex) {
			throw new ResourceException(
					"Class " + form.getModel().getClass().toString() + " chua co phuong thuc copy khong doi");
		}

	}
}
