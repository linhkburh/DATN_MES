package frwk.controller;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.Calendar;

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
import frwk.constants.ApproveConstants;
import frwk.dao.hibernate.sys.RightUtils;
import frwk.form.SearchForm;

public abstract class ApproveCatalogController<F extends SearchForm<T>, T> extends CatalogController<F, T> {
	private static final Logger logger = Logger.getLogger(ApproveCatalogController.class);
	@Autowired
	private RightUtils rightUtils;

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	public String execute(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			@ModelAttribute("formDataModelAttr") F form) throws Exception {
		String method = rq.getParameter("method");
		if (Formater.isNull(method)) {
			if (rightUtils.haveAction(rq, "method=toApprove"))
				model.addAttribute("toApprove", true);
			if (rightUtils.haveAction(rq, "method=approve"))
				model.addAttribute("approve", true);
			// Approve form
			model.addAttribute("approveForm", true);
		} else {
			try {
				if ("toApprove".equals(method)) {
					if (!rightUtils.haveAction(rq, "method=save&saveType=createNew")
							&& !rightUtils.haveAction(rq, "method=save&saveType=update"))
						throw new ResourceException(
								"B&#7841;n kh&#244;ng c&#243; quy&#7873;n th&#7921;c hi&#7879;n ch&#7913;c n&#259;ng!");
					getDao().getObject(form.getModel());
					Short iCurrentStatus = getStatus(form.getModel());
					if (iCurrentStatus != null && !ApproveConstants.APP_STS_NEW.equals(iCurrentStatus))
						throw new ResourceException("Tr&#7841;ng th&#225;i kh&#244;ng h&#7907;p l&#7879;!");
					next(model, rq, rs, form);
					return null;
				}
				if ("cancelToApprove".equals(method)) {
					if (!rightUtils.haveAction(rq, "method=save&saveType=createNew")
							&& !rightUtils.haveAction(rq, "method=save&saveType=update"))
						throw new ResourceException(
								"B&#7841;n kh&#244;ng c&#243; quy&#7873;n th&#7921;c hi&#7879;n ch&#7913;c n&#259;ng!");
					getDao().getObject(form.getModel());
					Short iCurrentStatus = getStatus(form.getModel());
					if (!ApproveConstants.APP_STS_TO_APP.equals(iCurrentStatus))
						throw new ResourceException("Tr&#7841;ng th&#225;i kh&#244;ng h&#7907;p l&#7879;!");
					previous(model, rq, rs, form);
					return null;
				}
				if ("approve".equals(method)) {
					if (!rightUtils.haveAction(rq, "method=approve"))
						throw new ResourceException(
								"B&#7841;n kh&#244;ng c&#243; quy&#7873;n th&#7921;c hi&#7879;n ch&#7913;c n&#259;ng!");
					getDao().getObject(form.getModel());
					// Check trang thai hop le
					Short iCurrentStatus = getStatus(form.getModel());
					if (!ApproveConstants.APP_STS_TO_APP.equals(iCurrentStatus))
						throw new ResourceException("Tr&#7841;ng th&#225;i kh&#244;ng h&#7907;p l&#7879;!");
					// Thong tin nguoi phe duyet
					updateApproveInf(form);
					next(model, rq, rs, form);
					return null;
				}

				if ("unApprove".equals(method)) {
					if (!rightUtils.haveAction(rq, "method=approve"))
						throw new ResourceException(
								"B&#7841;n kh&#244;ng c&#243; quy&#7873;n th&#7921;c hi&#7879;n ch&#7913;c n&#259;ng!");
					getDao().getObject(form.getModel());
					Short iCurrentStatus = getStatus(form.getModel());
					// Check trang thai hop le
					if (!ApproveConstants.APP_STS_TO_APP.equals(iCurrentStatus))
						throw new ResourceException("Tr&#7841;ng th&#225;i kh&#244;ng h&#7907;p l&#7879;!");
					// Thong tin nguoi phe duyet
					updateApproveInf(form);
					updateSts(model, rq, rs, form, ApproveConstants.APP_STS_UN_APP);
					return null;
				}
				if ("cancelApprove".equals(method)) {
					// Check trang thai hop le
					Short iCurrentStatus = getStatus(form.getModel());
					if (!ApproveConstants.APP_STS_APP.equals(iCurrentStatus))
						throw new ResourceException("Tr&#7841;ng th&#225;i kh&#244;ng h&#7907;p l&#7879;!");
					getDao().getObject(form.getModel());
					updateSts(model, rq, rs, form, ApproveConstants.APP_STS_NEW);
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

	private Short getStatus(T model) throws Exception {
		Field f = model.getClass().getDeclaredField("status");
		if (f == null)
			throw new ResourceException("Class % skh&#244;ng t&#7891;n t&#7841;i: private static Short status",
					model.getClass().getName());
		return (Short) f.get(model);
	}

	protected void updateApproveInf(F form) throws Exception {
		Field fApprover = form.getModelClass().getDeclaredField("approver");
		if (fApprover == null)
			throw new ResourceException("Class % skh&#244;ng t&#7891;n t&#7841;i: private static String approver",
					form.getModel().getClass().getName());
		fApprover.set(form.getModel(), getSessionUser().getUsername());
		Field fApproveDate = form.getModelClass().getDeclaredField("approveDate");
		if (fApproveDate == null)
			throw new ResourceException("Class % skh&#244;ng t&#7891;n t&#7841;i: private static Date approveDate",
					form.getModel().getClass().getName());
		fApproveDate.set(form.getModel(), Calendar.getInstance().getTime());
	}

	private void previous(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, F form) throws Exception {
		Field f = form.getModel().getClass().getDeclaredField("status");
		if (f == null)
			throw new ResourceException("Class % skh&#244;ng t&#7891;n t&#7841;i: private static Short status",
					form.getModel().getClass().getName());
		short currentSts = (short) f.get(form.getModel());
		updateSts(model, rq, rs, form, currentSts--);

	}

	private void next(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, F form) throws Exception {
		updateSts(model, rq, rs, form, (short) -1);
	}

	private void updateSts(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, F form, Short newSts)
			throws Exception {
		Field f = form.getModel().getClass().getDeclaredField("status");
		if (f == null)
			throw new ResourceException("Class % skh&#244;ng t&#7891;n t&#7841;i: private static Short status",
					form.getModel().getClass().getName());
		if (newSts < ApproveConstants.APP_STS_NEW || newSts > ApproveConstants.APP_STS_NEW)
			throw new ResourceException("Tr&#7841;ng th&#225;i kh&#244;ng h&#7907;p l&#7879");
		f.set(form.getModel(), newSts);
		getDao().save(form.getModel());
	}

}
