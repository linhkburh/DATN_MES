package frwk.controller.jdbc;

import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import common.sql.DataSourceConfiguration;
import common.util.Formater;
import common.util.ResourceException;
import frwk.controller.CommonController;
import frwk.controller.SearchParam;
import frwk.form.SearchForm;
import oracle.jdbc.OracleTypes;
@Deprecated
public abstract class SearchController<F extends SearchForm<T>, T> extends CommonController<F, T> {

	private static final Logger lg = Logger.getLogger(SearchController.class);
	private String keyword;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	private String keyWord;
	private String tokenId, tokenIdKey;

	public void datatable(ModelMap model, HttpServletRequest request, HttpServletResponse response, F form)
			throws Exception {
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

		JSONObject result = getData(model, request, start, amount, form);
		returnJson(response, result);
	}

	public void exportExcel(ModelMap model, HttpServletRequest request, HttpServletResponse rs, F form)
			throws Exception {
		pushDataExport(model, request, rs, form);
	}

	protected abstract void customizeModelObj(T objModel, ResultSet rs) throws Exception;
	@SuppressWarnings("unchecked")
	public T makeModelObj(F form, ResultSet rs1) throws Exception {
		T t = (T) form.getModel().getClass().newInstance();
		customizeModelObj(t, rs1);
		return t;
	}
	public JSONObject getData(ModelMap model, HttpServletRequest request, int start, int amout, F form)
			throws Exception {
		JSONObject result = new JSONObject();
		pushData(model, result, new SearchParam(start, amout), form);
		return result;
	}

	public void pushDataExport(ModelMap model, HttpServletRequest request, HttpServletResponse rs, F form)
			throws Exception {
		Connection connection = DataSourceUtils.getConnection(dataSource);
		CallableStatement cStmt = null;
		ResultSet rs1 = null;
		try {
			// Goi thu tuc
			cStmt = connection.prepareCall("{CALL " + getProcedure() + "}");
			// Thiet lap tham so out, in
			cStmt.setInt("iFrom", 0);
			cStmt.setInt("iTo", 0);
			pushParamExcel(model, cStmt, request, form);
			cStmt.registerOutParameter("oRowcount", Types.INTEGER);
			cStmt.registerOutParameter(getResulSetName(), OracleTypes.CURSOR);
			// Thu thi
			cStmt.execute();
			// Tong so ket qua
			// rs1 = cStmt.getResultSet();
			rs1 = (ResultSet) cStmt.getObject(getResulSetName());
			pushToExcel(request, rs, rs1);
		} catch (Exception ex) {
			lg.error(ex.getMessage(), ex);
			throw ex;
		} finally {
			DataSourceConfiguration.releaseSqlResources(rs1, cStmt, connection);
		}
	}

	@Autowired
	private DataSource dataSource;

	public void pushData(ModelMap model, JSONObject result, SearchParam param, F form) throws Exception {
		Connection connection = DataSourceUtils.getConnection(dataSource);
		CallableStatement cStmt = null;
		ResultSet rs1 = null;
		JSONArray array = new JSONArray();
		try {
			cStmt = connection.prepareCall("{CALL " + getProcedure() + "}");
			pushParam(model, cStmt, form);
			// Thiet lap tham so out, in
			int iIndex = param.getBeginIndex();
			cStmt.setInt("i_From", iIndex);
			cStmt.setInt("i_To", iIndex + param.getPageSize());

			cStmt.registerOutParameter("o_Total", Types.INTEGER);
			cStmt.registerOutParameter(getResulSetName(), OracleTypes.CURSOR);

			// Thu thi
			cStmt.execute();
			// Tong so ket qua
			int iToTal = cStmt.getInt("o_Total");
			result.put("iTotalRecords", iToTal);
			result.put("iTotalDisplayRecords", iToTal);
			// Hien thi ket qua
			rs1 = (ResultSet) cStmt.getObject(getResulSetName());
			if (rs1 != null) {
				while (rs1.next()) {
					JSONArray ja = new JSONArray();
					ja.put(++iIndex);
					try {
						pushToJa(ja, rs1);
					} catch (Exception e) {
						lg.error(e.getMessage(),e);
						throw e;
					}
					array.put(ja);
				}
			}
		} catch (Exception e) {
			lg.error(e.getMessage(), e);
			throw e;
		} finally {
			DataSourceConfiguration.releaseSqlResources(rs1, cStmt, connection);
		}
		result.put("aaData", array);
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	public String getKeyWord() {
		return keyWord;
	}

	/**
	 * Lay ten thu tuc search
	 * 
	 * @return
	 */
	public abstract String getProcedure(); // sp_emps_in_dept(?)

	/**
	 * Truyen dieu kien tim kiem vao thu tuc
	 * 
	 * @param cStmt
	 * @throws SQLException
	 */
	public abstract void pushParam(ModelMap model, CallableStatement cStmt, F form) throws Exception; // cStmt.setInt(1,
																										// deptId);

	public abstract void pushParamExcel(ModelMap model, CallableStatement cStmt, HttpServletRequest request, F form)
			throws Exception;

	/**
	 * Dien dong tra ve vao object json
	 * 
	 * @param ja  Json object
	 * @param rs1 Dong ket qua truy van
	 * @throws SQLException
	 */
	public abstract void pushToJa(JSONArray ja, ResultSet rs1) throws Exception;

	public abstract void pushToExcel(HttpServletRequest request, HttpServletResponse rs, ResultSet rs1)
			throws Exception;

	public abstract String getResulSetName();

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getTokenId() {
		return StringEscapeUtils.escapeHtml(tokenId);
	}

	public void setTokenIdKey(String tokenIdKey) {
		this.tokenIdKey = tokenIdKey;
	}

	public String getTokenIdKey() {
		return StringEscapeUtils.escapeHtml(tokenIdKey);
	}

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	public String execute(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			@ModelAttribute("formDataModelAttr") F form) throws Exception {
		String method = rq.getParameter("method");
		if ("datatable".equals(method)) {
			try {
				datatable(model, rq, rs, form);
				return null;
			} catch (Exception ex) {
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
				lg.error("Loi", ex);
				throw ex;
			}
		} else if ("exportExcel".equals(method)) {
			try {
				exportExcel(model, rq, rs, form);
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

}
