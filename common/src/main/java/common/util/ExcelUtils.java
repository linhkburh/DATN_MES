package common.util;

import java.math.BigDecimal;

import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.jxls.common.CellData.CellType;

public class ExcelUtils {
	public static void validateWb(HSSFWorkbook wb) throws ResourceException {
		for (int i = 0; i < wb.getNumberOfSheets(); i++) {
			HSSFSheet sheet = wb.getSheetAt(i);
			Iterator itorRow = sheet.rowIterator();
			while (itorRow.hasNext()) {
				HSSFRow row = (HSSFRow) itorRow.next();
				Iterator itorCell = row.cellIterator();
				while (itorCell.hasNext()) {
					HSSFCell cell = (HSSFCell) itorCell.next();
					if (Cell.CELL_TYPE_FORMULA == cell.getCellType())
						throw new ResourceException("khong_ho_cho_file_exel_co_cong_thuc");
					// if (cell.CELL_TYPE_ERROR == cell.getCellType())
					// throw new ResourceException("khong_ho_cho_file_exel_co_cong_thuc");
				}
			}
		}
	}

	/**
	 *
	 * @param cell
	 * @return
	 * @throws ResourceException Format cell la kieu so hoac khong lay duoc gia tri
	 *                           <br>
	 *                           ly do: <br>
	 *                           user 1, dinh dang excel: 1.234.567,12 <br>
	 *                           user 2, dinh dang excel: 1,234,567.12 <br>
	 *                           Do dinh dang so nen ung dung se doc dung (gia tri
	 *                           double 1234567.12), khi do ung dung khong biet la
	 *                           nsd la user 1 hay user 2 de convert ra string tuong
	 *                           ung
	 */
	public static String getString(HSSFCell cell) throws ResourceException {
		if (cell == null)
			return null;
		if (Cell.CELL_TYPE_BLANK == cell.getCellType())
			return null;
		if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
			if (cell.getRichStringCellValue() == null)
				return "";
			return cell.getRichStringCellValue().getString().trim();
		}
		if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
			return FormatNumber.num2Str(BigDecimal.valueOf(Double.valueOf(cell.getNumericCellValue())));
		}

		if (Cell.CELL_TYPE_BOOLEAN == cell.getCellType()) {
			return Boolean.TRUE.equals(cell.getBooleanCellValue()) ? String.valueOf(Boolean.TRUE.booleanValue())
					: String.valueOf(Boolean.FALSE.booleanValue());
		}

		throw new ResourceException("khong_lay_duoc_du_lieu_cua_cell");
	}

	private static final Logger logger = Logger.getLogger(ExcelUtils.class);

	public static Long getLong(Cell cell) throws ResourceException {
		if (Cell.CELL_TYPE_BLANK == cell.getCellType())
			return null;
		if (Cell.CELL_TYPE_STRING == cell.getCellType())
			return Long.valueOf(cell.getRichStringCellValue().getString());
		if (Cell.CELL_TYPE_NUMERIC == cell.getCellType())
			return Long.valueOf(Double.valueOf(cell.getNumericCellValue()).longValue());
		throw new ResourceException("khong_lay_duoc_du_lieu_integer_cua_cell");
	}

	public static Double getDouble(HSSFCell cell) throws ResourceException {
		if (Cell.CELL_TYPE_BLANK == cell.getCellType())
			return null;
		if (Cell.CELL_TYPE_STRING == cell.getCellType())
			return Double.valueOf(cell.getRichStringCellValue().getString());
		if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
			return Double.valueOf(cell.getNumericCellValue());

		}
		if (Cell.CELL_TYPE_BOOLEAN == cell.getCellType()) {
			return Boolean.TRUE.equals(cell.getBooleanCellValue()) ? Double.valueOf(1) : Double.valueOf(0);
		}
		throw new ResourceException("khong_lay_duoc_du_lieu_double_cua_cell");
	}

	public static BigDecimal getBigDecimal(Cell cell) throws ResourceException {
		if (Cell.CELL_TYPE_BLANK == cell.getCellType())
			return null;
		if (Cell.CELL_TYPE_STRING == cell.getCellType())
			return BigDecimal.valueOf(Double.parseDouble(cell.getRichStringCellValue().getString()));
		if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
			return BigDecimal.valueOf(cell.getNumericCellValue());

		}
		if (Cell.CELL_TYPE_BOOLEAN == cell.getCellType()) {
			return Boolean.TRUE.equals(cell.getBooleanCellValue()) ? BigDecimal.valueOf(1) : BigDecimal.valueOf(0);
		}
		throw new ResourceException("khong_lay_duoc_du_lieu_double_cua_cell");
	}

	public static Boolean getBoolean(HSSFCell cell) throws ResourceException {
		if (Cell.CELL_TYPE_BLANK == cell.getCellType())
			return null;
		if (Cell.CELL_TYPE_STRING == cell.getCellType()) {
			if (Boolean.TRUE.toString().equals(cell.getRichStringCellValue().getString())
					|| Boolean.FALSE.toString().equals(cell.getRichStringCellValue().getString()))
				return Boolean.valueOf(cell.getRichStringCellValue().getString());
		}
		if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
			if (Double.valueOf(cell.getNumericCellValue()).longValue() == 1)
				return Boolean.TRUE;
			if (Double.valueOf(cell.getNumericCellValue()).longValue() == 0)
				return Boolean.FALSE;
			throw new ResourceException("khong_lay_duoc_du_lieu_kieu_Boolean");
		}
		if (Cell.CELL_TYPE_BOOLEAN == cell.getCellType()) {
			return cell.getBooleanCellValue();
		}
		throw new ResourceException("khong_lay_duoc_du_lieu_Boolean_cua_cell");
	}

	public static Date getDate(HSSFCell cell) throws Exception {
		try {
			if (Cell.CELL_TYPE_BLANK == cell.getCellType())
				return null;
			if (Cell.CELL_TYPE_STRING == cell.getCellType()) {

				return Formater.str2date(cell.getRichStringCellValue().getString());
			}
			if (Cell.CELL_TYPE_NUMERIC == cell.getCellType()) {
				return cell.getDateCellValue();
			}
			throw new ResourceException("du_lieu_khong_dung_dinh_dang");
		} catch (Exception ex) {
			throw new ResourceException("du_lieu_khong_dung_dinh_dang");
		}

	}
}
