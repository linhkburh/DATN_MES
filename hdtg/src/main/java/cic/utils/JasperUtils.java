package cic.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import common.util.Formater;
import constants.Constants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

@Service
public class JasperUtils {
	static Logger lg = Logger.getLogger(JasperUtils.class);

	public InputStream createReport(String fileTemplate, Map<String, Object> parameters, String fileType,
			String fileName, Connection connection, List<String> subReports) throws Exception {
		lg.info("BEGIN createReport fileTemplate " + fileTemplate);
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream result = loader.getResourceAsStream("templates/report/" + fileTemplate + ".jrxml");
		JasperReport compiledReport = JasperCompileManager.compileReport(result);
		if (!Formater.isNull(subReports)) {
			URI urlSub = null;
			for (String sub : subReports) {
				urlSub = getClass().getClassLoader().getResource("templates/report/" + sub + ".jasper").toURI();
				parameters.put(sub, urlSub.toString());
			}
		}
		JasperPrint jasperPrint = JasperFillManager.fillReport(compiledReport, parameters, connection);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		File fileOut = null;
		if (fileType.equals(Constants.Excel)) {
			fileOut = File.createTempFile(fileName, "xls");
			result = exportXls(jasperPrint, fileOut, baos);
		} else if (fileType.equals(Constants.PDF)) {
			fileOut = File.createTempFile(fileName, "pdf");
			result = exportPdf(jasperPrint, fileOut);
		}
		lg.info("END createReport fileTemplate " + fileTemplate);
		return result;

	}

	private static InputStream exportPdf(JasperPrint jasperPrint, File pdf) throws FileNotFoundException, JRException {
		InputStream result = null;
		try {
			JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(pdf));
			result = new FileInputStream(pdf);
		} catch (Exception ex) {
			lg.error(ex);
			ex.printStackTrace();
		}
		return result;
	}

	private static InputStream exportXls(JasperPrint jasperPrint, File xls, ByteArrayOutputStream baos)
			throws JRException, IOException {
		InputStream result = new FileInputStream(xls);
		try {
			OutputStream out = new FileOutputStream(xls);
			JRXlsExporter excelExporter = new JRXlsExporter();
			SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
			configuration.setOnePagePerSheet(true);
			configuration.setDetectCellType(true);
			configuration.setAutoFitPageHeight(true);
			configuration.setCollapseRowSpan(false);
			configuration.setRemoveEmptySpaceBetweenColumns(true);
			configuration.setRemoveEmptySpaceBetweenRows(true);
			configuration.setWrapText(true);
			configuration.setWhitePageBackground(false);
			excelExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
			excelExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
			excelExporter.setConfiguration(configuration);
			excelExporter.exportReport();
			result = new FileInputStream(xls);
		} catch (Exception ex) {
			lg.error(ex);
			ex.printStackTrace();
		}
		return result;
	}
}
