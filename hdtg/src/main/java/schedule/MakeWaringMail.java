package schedule;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.spring.VelocityEngineUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;

import cic.h2h.form.BookingStatusForm.BookingStatus;
import common.sql.DataSourceConfiguration;
import common.util.Formater;
import entity.frwk.SysMail;
import entity.frwk.SysMailAtt;
import entity.frwk.SysUsers;
import frwk.controller.jdbc.v2.PrintController;
import frwk.dao.hibernate.sys.SysMailDao;
import frwk.schedule.ClusterTimerTask;
import net.sf.jxls.transformer.XLSTransformer;
import oracle.jdbc.OracleTypes;

@Component
@Scope("prototype")
public class MakeWaringMail extends ClusterTimerTask {
	private static final Logger logger = Logger.getLogger(PrintController.class);
	@Autowired
	private VelocityEngine velocityEngine;
	@Autowired
	private SysMailDao sysMailDao;

	@SuppressWarnings("deprecation")
	@Override
	protected void execute() throws Exception {
		// Danh sach nhan email canh bao
		String receiptWrngMail = sysMailDao.getReceiptWrngMail();
		if (receiptWrngMail == null)
			return;
		SysMail sysMail = new SysMail();
		Map<String, Object> map = new HashMap<String, Object>();
		sysMail.setReceipt(receiptWrngMail);
		sysMail.setSubject("Cảnh báo giao hàng");
		String body = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "/templates/Email/demotempalte.vm",
				map);
		sysMail.setBody(body);
		sysMail.setHtml(Boolean.TRUE);
		SysMailAtt att1 = makeMailAtt(sysMail, 1);
		SysMailAtt att2 = makeMailAtt(sysMail, 0);
		if (att1 == null && att2 == null)
			return;
		if (att1 != null)
			sysMail.getLstAtt().add(att1);
		if (att2 != null)
			sysMail.getLstAtt().add(att2);
		sysMailDao.getCurrentSession().save(sysMail);
	}

	@Autowired
	private DataSource dataSource;

	private SysMailAtt makeMailAtt(SysMail sysMail, int iReportType) throws Exception {
		Map<String, Object> beans = new HashMap<String, Object>();
		Connection myConnect = null;
		CallableStatement cStmt = null;
		ResultSet rs1 = null;
		try {
			myConnect = DataSourceUtils.getConnection(dataSource);
			// Goi thu tuc
			cStmt = myConnect.prepareCall("{CALL " + "hdtg_report.booking_sts4(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)" + "}");
			// Thiet lap tham so out, in
			// pushParam(null, cStmt, form);

			Calendar tenDayAgo = Calendar.getInstance();
			if (iReportType == 1) {
				cStmt.setInt("p_late_delivery", 1);
				cStmt.setNull("p_tobe_late", OracleTypes.INTEGER);
				beans.put("reportTitle", "Đơn hàng bị chậm");
			} else {
				cStmt.setInt("p_tobe_late", 1);
				cStmt.setNull("p_late_delivery", OracleTypes.INTEGER);
				beans.put("reportTitle", "Đơn hàng nguy cơ chậm");

			}
			beans.put("tungay", Formater.date2str(tenDayAgo.getTime()));
			cStmt.setString("p_report_type", "qi");
			cStmt.setNull("p_step_type", OracleTypes.VARCHAR);
			cStmt.setNull("p_company_id", OracleTypes.VARCHAR);
			tenDayAgo.add(Calendar.DATE, -10);
			cStmt.setDate("p_from_date", new java.sql.Date(tenDayAgo.getTime().getTime()));
			cStmt.setNull("p_to_date", OracleTypes.DATE);
			// Ma ban ve
			cStmt.setNull("p_drw_code", OracleTypes.VARCHAR);
			// Ma quan ly
			cStmt.setNull("p_manage_code", OracleTypes.VARCHAR);
			// Ma khach hang
			cStmt.setNull("p_customer_id", OracleTypes.VARCHAR);
			// Ma don hang
			cStmt.setNull("p_order_code", OracleTypes.VARCHAR);

			cStmt.setNull("i_From", OracleTypes.INTEGER);
			cStmt.setNull("i_To", OracleTypes.INTEGER);
			cStmt.registerOutParameter("o_Total", OracleTypes.INTEGER);
			cStmt.registerOutParameter("cResult", OracleTypes.CURSOR);
			// Thuc thi
			cStmt.execute();
			rs1 = (ResultSet) cStmt.getObject("cResult");
			List<BookingStatus> reportData = new ArrayList<BookingStatus>();
			beans.put("reports", reportData);
			if (rs1 != null) {
				while (rs1.next()) {
					BookingStatus bs = new BookingStatus();
					bs.setType("qi");
					bs.read(rs1);
					reportData.add(bs);
				}
			}
			if (reportData.isEmpty())
				return null;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw ex;
		} finally {
			DataSourceConfiguration.releaseSqlResources(rs1, cStmt, myConnect);
		}
		// Create file
		String pathFile = File.separator + "templates" + File.separator + "report" + File.separator
				+ "Bao_cao_san_luong.xlsx";
		InputStream tempFile = getClass().getClassLoader().getResourceAsStream(pathFile);
		Workbook book = new XLSTransformer().transformXLS(tempFile, beans);
		tempFile.close();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		book.write(os);
		os.flush();
		os.close();
		SysMailAtt att = new SysMailAtt(os.toByteArray(), sysMail);
		if (iReportType == 1) {
			att.setFileName("Danh sach cham giao hang.xlsx");
		} else {
			att.setFileName("Danh sach co nguy co.xlsx");
		}
		return att;
	}
}
