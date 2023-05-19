package cic.utils;

import java.io.InputStream;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

@Service
public class ExportExcel {

	private static final Logger logger = Logger.getLogger(ExportExcel.class);

	public void export(String templateName, HttpServletResponse response, Map<String, Object> beans) throws Exception {
		logger.info("BEGIN export " + templateName);
		// Tao file
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream tempFile = classLoader.getResourceAsStream("/templates/report/" + templateName + ".xlsx");
		Workbook book = new XLSTransformer().transformXLS(tempFile, beans);
		tempFile.close();
		// Download file
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment;filename=" + templateName + ".xlsx");
		ServletOutputStream out = response.getOutputStream();
		book.write(out);
		out.close();
		logger.info("END export " + templateName);
	}

}
