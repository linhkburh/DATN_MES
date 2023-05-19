package frwk.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.context.internal.ThreadLocalSessionContext;
import org.hibernate.criterion.Projections;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import common.util.Formater;
import common.util.ResourceException;
import concurrent.ConcurrentProcess;
import concurrent.HibernateSessionConcurrentItemProcessor;
import frwk.dao.hibernate.BaseDao;
import frwk.form.SearchForm;

public abstract class SearchController<F extends SearchForm<T>, T> extends CommonController<F, T> {

	private static Logger lg = Logger.getLogger(SearchController.class);

	public void datatable(ModelMap model, HttpServletRequest request, HttpServletResponse response, F form)
			throws Exception {
		String keyWord = request.getParameter("sSearch");
		if (!Formater.isNull(keyWord))
			form.setKeyWord(keyWord.trim());

		// Dieu kien tim kiem
		BaseDao<T> dao = getDao().createCriteria();
		createSearchDAO(request, form, dao);
		JSONObject result = new JSONObject();

		// Tong so ban ghi
		dao.getCriteria().setProjection(Projections.rowCount());
		lg.info("Start fetch amount, " + this.getClass().getCanonicalName());
		int total = ((Long) dao.getCriteria().uniqueResult()).intValue();
		lg.info("End fetch amount, " + this.getClass().getCanonicalName());
		result.put("iTotalRecords", total);
		result.put("iTotalDisplayRecords", total);
		if (total <= 0) {
			result.put("aaData", new JSONArray());
			returnJson(response, result);
			return;
		}
		preSearch(model, request, response, form);
		// Lay du lieu
		int start = 0;
		String sStart = request.getParameter("iDisplayStart");
		if (sStart != null) {
			start = Integer.parseInt(sStart);
			if (start < 0) {
				start = 0;
			}
		}

		int amount = 10;
		String sAmount = request.getParameter("iDisplayLength");
		if (sAmount != null) {
			amount = Integer.parseInt(sAmount);
			if (amount < 0) {
				amount = 10;
			}
		}

		dao.getCriteria().setProjection(null);
		dao.setSearchParam(new SearchParam(start, amount));
		lg.info("Begin fetch data, " + this.getClass().getCanonicalName());
		pushData(result, dao, form);
		lg.info("End fetch data, " + this.getClass().getCanonicalName());

		returnJson(response, result);

	}

	public void preSearch(ModelMap model, HttpServletRequest request, HttpServletResponse response, F form) {

	}

	public abstract void createSearchDAO(HttpServletRequest request, F form, BaseDao<T> dao) throws Exception;

	@SuppressWarnings("unchecked")
	private void pushData(JSONObject result, BaseDao<T> dao, F form) throws Exception {
		JSONArray array = new JSONArray();
		long iIndex = dao.getSearchParam().getBeginIndex();
		lg.info("start search");
		List<T> temp = (List<T>) dao.search();
		lg.info("end search");
		if (loadConcurrent()) {
			LocaleContextHolder.setLocale(LocaleContextHolder.getLocale(), Boolean.TRUE);
			Map<T, JSONArray> map = new HashMap<T, JSONArray>();
			for (T e : temp) {
				JSONArray ja = new JSONArray();
				if (e.getClass().isArray()) {
					for (Object o : (Object[]) e) {
						if (o.getClass().equals(dao.getModelClass())) {
							map.put((T) o, ja);
							break;
						}
					}
				} else {
					map.put(e, ja);
				}
				ja.put(++iIndex);
				array.put(ja);
			}
			ConcurrentProcess.execute(temp, new HibernateSessionConcurrentItemProcessor<T>() {
				@Override
				public void process(T e) throws Exception {
					Session ss = getDao().getCurrentSession();
					if (e.getClass().isArray()) {
						for (Object o : (Object[]) e) {
							if (o.getClass().equals(dao.getModelClass())) {
								T o1 = (T) ss.get(o.getClass(), getDao().getId(o));
								pushToJa(map.get(o), o1, form);
								break;
							}
						}
					} else {
						T o1 = (T) ss.get(e.getClass(), getDao().getId(e));
						pushToJa(map.get(e), o1, form);
					}
				}

				@Override
				public void releaseResource(Exception ex) {
					Session sess = ThreadLocalSessionContext.unbind(getDao().getSessionFactory());
					if (sess == null || sess.getTransaction().getStatus() != TransactionStatus.ACTIVE)
						return;
					if (ex == null)
						sess.getTransaction().commit();
					else
						sess.getTransaction().rollback();
				}

			}, 2, new Integer(15), Boolean.TRUE);
		} else {
			for (T e : temp) {
				JSONArray ja = new JSONArray();
				ja.put(++iIndex);
				if (e.getClass().isArray()) {
					for (Object o : (Object[]) e) {
						if (o.getClass().equals(dao.getModelClass())) {
							this.pushToJa(ja, (T) o, form);
							break;
						}
					}
				} else {
					this.pushToJa(ja, e, form);
				}
				array.put(ja);
			}
		}
		result.put("aaData", array);
	}

	public Boolean loadConcurrent() {
		return Boolean.FALSE;
	}

	protected abstract void pushToJa(JSONArray ja, T e, F modelForm) throws Exception;

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	@Override
	public String execute(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			@ModelAttribute("formDataModelAttr") F form) throws Exception {
		String method = rq.getParameter("method");
		if ("datatable".equals(method)) {
			try {
				datatable(model, rq, rs, form);
				return null;
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
					if (!Formater.isNull(rse.getMessage())) {
						if (!Formater.isNull(rse.getParam()))
							returnTxtHtml(rs, String.format(getText(rse.getMessage()), rse.getParam()));
						else if (!Formater.isNull(rse.getParams()))
							returnJson(rs, String.format(getText(rse.getMessage()), rse.getParams()));
						else
							returnTxtHtml(rs, getText(rse.getMessage()));
						throw rse;
					}
				}
				throw ex;
			}
		}
		return super.execute(model, rq, rs, form);
	}

	public abstract BaseDao<T> getDao();
}