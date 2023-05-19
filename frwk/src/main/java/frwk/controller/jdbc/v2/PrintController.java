package frwk.controller.jdbc.v2;

import java.io.File;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.ui.ModelMap;

import common.sql.DataSourceConfiguration;
import common.util.Formater;
import frwk.form.SearchForm;
import net.sf.jxls.transformer.XLSTransformer;
import oracle.jdbc.OracleTypes;

public abstract class PrintController<F extends SearchForm<T>, T> extends SearchController<F, T> {
	private static final Logger logger = Logger.getLogger(PrintController.class);

	protected abstract String getReportTitle();

	@Autowired
	private DataSource dataSource;

	public void print(ModelMap model, HttpServletRequest request, HttpServletResponse response, F form)
			throws Exception {
		validateInput(form);
		Map<String, Object> beans = new HashMap<String, Object>();
		beans.put("reportTitle", getReportTitle());
		pushReportParam(beans, form);
		Connection myConnect = null;
		CallableStatement cStmt = null;
		ResultSet rs1 = null;
		Date startTime = Calendar.getInstance().getTime();
		try {
			myConnect = DataSourceUtils.getConnection(dataSource);
			// Goi thu tuc
			cStmt = myConnect.prepareCall("{CALL " + getProcedure() + "}");
			// Thiet lap tham so out, in
			pushParam(null, cStmt, form);
			cStmt.setNull("i_From", OracleTypes.INTEGER);
			cStmt.setNull("i_To", OracleTypes.INTEGER);
			cStmt.registerOutParameter("o_Total", OracleTypes.INTEGER);
			cStmt.registerOutParameter(getResulSetName(), OracleTypes.CURSOR);
			// Thuc thi
			cStmt.execute();
			rs1 = (ResultSet) cStmt.getObject(getResulSetName());
			List<T> reportData = new ArrayList<T>();
			beans.put("reports", reportData);
			if (rs1 != null) {
				while (rs1.next())
					reportData.add(makeModelObj(form, rs1));
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw ex;
		} finally {
			DataSourceConfiguration.releaseSqlResources(rs1, cStmt, myConnect);
		}
		// Create file
		String pathFile = File.separator + "templates" + File.separator + "report" + File.separator
				+ getTemplateFileName();
		InputStream tempFile = getClass().getClassLoader().getResourceAsStream(pathFile);
		Workbook book = new XLSTransformer().transformXLS(tempFile, beans);
		tempFile.close();
		// return file
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition",
				"attachment;filename=" + (getExportFileName() != null ? getExportFileName() : getTemplateFileName()));
		ServletOutputStream out = response.getOutputStream();
		book.write(out);
		out.flush();
		out.close();
		Date endTime = Calendar.getInstance().getTime();
		logger.info(String.format("Export from %s to %s, total %s",
				new Object[] { Formater.date2ddsmmsyyyspHHmmss(startTime), Formater.date2ddsmmsyyyspHHmmss(endTime),
						(endTime.getTime() - startTime.getTime()) / 1000 }));
	}

	public abstract void validateInput(F form) throws Exception;

	/**
	 * Ten file template
	 * 
	 * @return
	 */
	protected abstract String getExportFileName();

	protected abstract void pushReportParam(Map<String, Object> beans, F form) throws Exception;

	/**
	 * Ten file ket xuat excel
	 *
	 * @return
	 */
	protected abstract String getTemplateFileName();
}
