package common.util;



import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.sql.Connection;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRReport;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

import org.apache.log4j.Logger;


public class JasperUtils {
    public static String EXCEL = "excel";
    public static String HTML = "html";
    public static String PDF = "pdf";
    static Logger lg = Logger.getLogger(JasperUtils.class);

    public static InputStream createReport(Connection con, String fileTemplate, Map<String, Object> parameters, String fileType,
                                           String fileName) {
        lg.info("BEGIN createReport");
        InputStream result = null;
        try {
        	
            result = new FileInputStream(fileTemplate);
            JasperReport jasperReport = (JasperReport)JRLoader.loadObject(result);
            jasperReport.setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, con);
            String sql = jasperReport.getMainDataset().getQuery().getText();
            for (String key : parameters.keySet()) {

                try {
                    String param = "$P!{" + key + "}";
                    String value = String.valueOf(parameters.get(key));
                    if (sql.contains(param))
                        sql = sql.replace(param, value);
                    param = "$P{" + key + "}";
                    if (sql.contains(param))
                        sql = sql.replace(param, value);


                } catch (Exception ex) {
                	lg.error("Loi", ex);
                }
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            File fileOut = null;
            if (fileType.equals(EXCEL)) {
                fileOut = File.createTempFile(fileName, "xls");
                result = exportXls(jasperPrint, fileOut, baos);
            } else if (fileType.equals(PDF)) {
                fileOut = File.createTempFile(fileName, "pdf");
                result = exportPdf(jasperPrint, fileOut);
            } else {
                fileOut = File.createTempFile(fileName, "pdf");
            }
        } catch (Exception ex) {
            lg.error(ex);
            ex.printStackTrace();
        }
        lg.info("END createReport");
        return result;
    }

    private static InputStream exportPdf(JasperPrint jasperPrint, File pdf) throws FileNotFoundException, JRException {
        InputStream result = null;
        try{
            JasperExportManager.exportReportToPdfStream(jasperPrint, new FileOutputStream(pdf));
            result = new FileInputStream(pdf);
        }catch(Exception ex){
            lg.error(ex);
            ex.printStackTrace();
        }
        return result;
    }

    private static InputStream exportXls(JasperPrint jasperPrint, File xls, ByteArrayOutputStream baos) throws JRException,
                                                                                                               IOException {
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
