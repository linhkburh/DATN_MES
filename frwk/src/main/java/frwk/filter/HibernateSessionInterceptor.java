package frwk.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLIntegrityConstraintViolationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.context.internal.ThreadLocalSessionContext;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import common.util.ResourceException;

public class HibernateSessionInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = Logger.getLogger(HandlerInterceptorAdapter.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		Session sess = ThreadLocalSessionContext.unbind(sessionFactory);
		if (sess == null || sess.getTransaction().getStatus() != TransactionStatus.ACTIVE)
			return;
		if (ex == null) {
			try {
				sess.getTransaction().commit();
			} catch (Exception e1) {
				ConstraintViolationException violationExcpt = null;
				if (e1 instanceof ConstraintViolationException)
					violationExcpt = (ConstraintViolationException) e1;
				else {
					Throwable cause = e1.getCause();
					while (cause != null) {
						if (cause instanceof ConstraintViolationException) {
							violationExcpt = (ConstraintViolationException) cause;
							break;
						}
						cause = cause.getCause();
					}
				}
				if (violationExcpt != null) {
					logger.error(violationExcpt.getMessage(), violationExcpt);
					if (violationExcpt.getErrorCode() == 1)
						returnTxtHtml(response,
								messageSource.getMessage("ORA_0001", null, "Default", LocaleContextHolder.getLocale()));
					else if (violationExcpt.getErrorCode() == 1400)
						returnTxtHtml(response,
								messageSource.getMessage("ORA_1400", null, "Default", LocaleContextHolder.getLocale()));
					else if (violationExcpt.getErrorCode() == 2292)
						returnTxtHtml(response,
								messageSource.getMessage("ORA_2292", null, "Default", LocaleContextHolder.getLocale()));
					else {
						returnTxtHtml(response,
								messageSource.getMessage("CMM_000", null, "Default", LocaleContextHolder.getLocale()));
					}
				} else {
					if (handler instanceof HandlerMethod) {
						HandlerMethod hm = (HandlerMethod) handler;
						logger.error(String.format("Message: %s, bean: %s, method: %s",
								new Object[] { e1.getMessage(), hm.getBeanType(), hm.getMethod() }), e1);
					} else
						logger.error(e1.getMessage(), e1);
					returnTxtHtml(response,
							messageSource.getMessage("CMM_000", null, "Default", LocaleContextHolder.getLocale()));
				}
				throw e1;
			}
		} else {
			sess.getTransaction().rollback();
			if (!(ex instanceof ResourceException)) {
				if (handler instanceof HandlerMethod) {
					HandlerMethod hm = (HandlerMethod) handler;
					logger.error(String.format("Message: %s, bean: %s, method: %s",
							new Object[] { ex.getMessage(), hm.getBeanType(), hm.getMethod() }), ex);
				} else
					logger.error(ex.getMessage(), ex);
				returnTxtHtml(response,
						messageSource.getMessage("CMM_000", null, "Default", LocaleContextHolder.getLocale()));
			}
		}

		super.afterCompletion(request, response, handler, ex);
	}

	private MessageSource messageSource;

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void returnTxtHtml(HttpServletResponse rs, String foo) throws IOException {
		rs.setContentType("text/plan;charset=utf-8");
		PrintWriter pw = rs.getWriter();
		pw.print(foo);
		pw.flush();
		pw.close();
	}
}
