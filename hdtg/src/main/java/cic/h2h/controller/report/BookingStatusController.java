package cic.h2h.controller.report;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.CustomerManageDao;
import cic.h2h.dao.hibernate.ExeStepTypeDao;
import cic.h2h.dao.hibernate.QuotationItemDao;
import cic.h2h.form.BookingStatusForm;
import cic.h2h.form.BookingStatusForm.BookingStatus;
import common.util.FormatNumber;
import common.util.Formater;
import common.util.ResourceException;
import entity.Company;
import entity.Customer;
import entity.ExeStepType;
import frwk.constants.RightConstants;
import frwk.controller.jdbc.v2.PrintController;
import frwk.dao.hibernate.sys.CompanyDao;
import frwk.dao.hibernate.sys.RightUtils;
import oracle.jdbc.OracleTypes;

@Controller
@RequestMapping(value = "/bookingSts")
public class BookingStatusController extends PrintController<BookingStatusForm, BookingStatus> {

	@Override
	protected String getReportTitle() {
		if ("drw".equals(getModelForm().getType()))
			return "Tình trạng sản xuất theo bản vẽ";
		else if ("cus".equals(getModelForm().getType()))
			return "Tình trạng sản xuất theo khách hàng";
		else if ("q".equals(getModelForm().getType()))
			return "Tình trạng sản xuất theo đơn hàng";
		return "Tình trạng sản xuất";
	}

	@Override
	public void validateInput(BookingStatusForm form) throws Exception {
		if (Formater.isNull(form.getFrDate()))
			throw new ResourceException("Phải nhập từ ngày");
		Date toDate = Formater.isNull(form.getToDate()) ? Calendar.getInstance().getTime()
				: Formater.str2date(form.getToDate());
		Calendar cTodate = Calendar.getInstance();
		cTodate.setTime(toDate);
		cTodate.add(Calendar.MONTH, -6);
		cTodate.add(Calendar.DATE, -1);
		if (cTodate.getTime().after(Formater.str2date(form.getFrDate())))
			throw new ResourceException("Khoảng thời gian ngày tạo, từ ngày - đến ngày phải nhỏ hơn 6 tháng");
	}

	@Override
	protected String getExportFileName() {
		return "Tinh_trang_san_xuat.xlsx";
	}

	@Autowired
	private CompanyDao companyDao;

	@Override
	protected void pushReportParam(Map<String, Object> beans, BookingStatusForm form) throws Exception {
		if (!Formater.isNull(form.getModel().getType())) {
			beans.put("loaibaocao", getReportTitle());
		}
		if (!Formater.isNull(form.getCompany())) {
			Company com = companyDao.get(form.getCompany());
			beans.put("congty", com.getName());
		}
		if (!Formater.isNull(form.getFrDate()))
			beans.put("tungay", form.getFrDate());
		if (!Formater.isNull(form.getToDate()))
			beans.put("denngay", form.getToDate());
		if (!Formater.isNull(form.getCode()))
			beans.put("mabanve", form.getCode());
		if (!Formater.isNull(form.getManageCodeSearch()))
			beans.put("maquanly", form.getManageCodeSearch());
		if (!Formater.isNull(form.getCustomerCode()))
			beans.put("makhachhang", form.getCustomerCode());
	}

	@Override
	protected String getTemplateFileName() {
		BookingStatusForm form = getModelForm();
		if ("drw".equals(form.getType()))
			return "Bao_cao_san_luong_theo_ban_ve.xlsx";
		else if ("cus".equals(form.getType()))
			return "Bao_cao_san_luong_theo_khach_hang.xlsx";
		else if ("q".equals(form.getType()))
			return "Bao_cao_san_luong_theo_don_hang.xlsx";
		return "Bao_cao_san_luong.xlsx";
	}

	@Autowired
	private CustomerManageDao customerManageDao;
	@Autowired
	private QuotationItemDao quotationItemDao;

	@Override
	protected void customizeModelObj(BookingStatus objModel, ResultSet rs) throws Exception {
		objModel.setType(getModelForm().getType());
		objModel.read(rs);

	}

	@Override
	public String getProcedure() {
		return "hdtg_report.booking_sts4(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	}

	@Autowired
	private ExeStepTypeDao exeStepTypeDao;

	@Override
	public void pushParam(ModelMap model, CallableStatement cStmt, BookingStatusForm form) throws Exception {
		cStmt.setNull("p_late_delivery", OracleTypes.INTEGER);
		cStmt.setNull("p_tobe_late", OracleTypes.INTEGER);
		cStmt.setString("p_report_type", form.getType());
		if (Formater.isNull(form.getSource()))
			cStmt.setNull("p_step_type", OracleTypes.VARCHAR);
		else {
			cStmt.setString("p_step_type", form.getSource());
		}
		if (Formater.isNull(form.getCompany()))
			cStmt.setNull("p_company_id", OracleTypes.VARCHAR);
		else
			cStmt.setString("p_company_id", form.getCompany());
		if (Formater.isNull(form.getFrDate()))
			cStmt.setNull("p_from_date", OracleTypes.DATE);
		else
			cStmt.setDate("p_from_date", new java.sql.Date(Formater.str2date(form.getFrDate()).getTime()));
		if (Formater.isNull(form.getToDate()))
			cStmt.setNull("p_to_date", OracleTypes.DATE);
		else
			cStmt.setDate("p_to_date", new java.sql.Date(Formater.str2date(form.getToDate()).getTime()));
		// Ma ban ve
		if (Formater.isNull(form.getCode()))
			cStmt.setNull("p_drw_code", OracleTypes.VARCHAR);
		else {
			String p_drw_code = null;
			String temp[] = form.getCode().split(";");
			for (String code : temp) {
				if (p_drw_code == null)
					p_drw_code = code.trim();
				else
					p_drw_code += "," + code.trim();
			}
			cStmt.setString("p_drw_code", p_drw_code);
		}
		// Ma quan ly
		if (Formater.isNull(form.getManageCodeSearch()))
			cStmt.setNull("p_manage_code", OracleTypes.VARCHAR);
		else {
			String p_manage_code = null;
			String temp[] = form.getManageCodeSearch().split(";");
			for (String code : temp) {
				if (p_manage_code == null)
					p_manage_code = code.trim();
				else
					p_manage_code += "," + code.trim();
			}
			cStmt.setString("p_manage_code", p_manage_code);
		}
		// Ma khach hang
		if (Formater.isNull(form.getCustomerCode()))
			cStmt.setNull("p_customer_id", OracleTypes.VARCHAR);
		else {
			cStmt.setString("p_customer_id", form.getCustomerCode());
		}
		// Ma don hang
		if (Formater.isNull(form.getOrderCode()))
			cStmt.setNull("p_order_code", OracleTypes.VARCHAR);
		else {
			cStmt.setString("p_order_code", form.getOrderCode());
		}

	}

	@Override
	public void pushParamExcel(ModelMap model, CallableStatement cStmt, HttpServletRequest request,
			BookingStatusForm form) throws Exception {

	}

	@Override
	public void pushToExcel(HttpServletRequest request, HttpServletResponse rs, ResultSet rs1) throws Exception {

	}

	@Override
	public String getResulSetName() {
		return "cResult";
	}

	@Override
	public String getJsp() {
		return "bao_cao/thong_ke_don_hang";
	}
	@Autowired
	private RightUtils rightUtils;
	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, BookingStatusForm form)
			throws Exception {
		if (rightUtils.haveRight(rq, RightConstants.VIEW_DATA_ALL)) {
			model.addAttribute("companies", companyDao.getAllOrderAstName());
		} else {
			model.addAttribute("companies", getSessionUser().getCompany());
		}
		model.addAttribute("customers", customerManageDao.getByType(Customer.IS_CUSTOMER));
		// Mac dinh khach hang
		if (Formater.isNull(form.getType()))
			form.setType("qi");
		// Mac dinh 10 ngay
		if (!form.isFistTime()) {
			Calendar lastMonth = Calendar.getInstance();
			lastMonth.add(Calendar.DATE, -10);
			form.setFrDate(Formater.date2str(lastMonth.getTime()));
			form.setFistTime(true);
			form.setCompany(getSessionUser().getCompany().getId());
		}
		List<ExeStepType> lstSource = new ArrayList<ExeStepType>(Arrays.asList(new ExeStepType("", "Tất cả"),
				new ExeStepType("1", "CNC"), exeStepTypeDao.getByCode("EN")));
		model.addAttribute("lstSource", lstSource);

	}

	@Override
	public void pushToJa(JSONArray ja, BookingStatus t) throws Exception {
		ja.put(String.format("<span title='Tên khách hàng: %s'>%s</span>",
				new Object[] { t.getCustomer().getOrgName(), t.getCustomer().getCode() }));
		// Tong hop theo khach hang, bo qua ma ban ve
		if (!"cus".equals(getModelForm().getType())) {
			if ("q".equals(getModelForm().getType()))
				ja.put(String.format("<span class='characterwrap'>%s</span>", t.getQi().getQcode()));
			else
				ja.put(String.format("<span class='characterwrap'>%s</span>", t.getQi().getCode()));
		}
		// San luong can ma quan ly
		if ("qi".equals(getModelForm().getType()))
			ja.put(String.format("<span class='characterwrap'>%s</span>", t.getQi().getManageCode()));
		// bao gia
		BookingStatus.Work booking = t.getBooking();
		// Truong hop QI (san luong) gia tri nay luon =1 (so luong ban ve)
		if (!"qi".equals(getModelForm().getType()))
			ja.put(FormatNumber.num2Str(booking.getItems()));
		// Bao gia
		ja.put(FormatNumber.num2Str(booking.getAmount()));
		ja.put(FormatNumber.num2Str(booking.getSetupTime()));
		ja.put(FormatNumber.num2Str(booking.getTarget().getExeTime()));
		ja.put(FormatNumber.num2Str(booking.getPlan().getExeTime()));
		// da thuc hien
		BookingStatus.Work exe = t.getExe();
		// So luong chi tiet
		ja.put(exe.getAmountDes());

		ja.put(FormatNumber.num2Str(exe.getExe().getProTime()));
		if (t.getSetupLate() != null && t.getSetupLate().compareTo(new BigDecimal(0)) > 0)
			ja.put(String.format("<font color='red'>%s</font>", FormatNumber.num2Str(exe.getExe().getSetupTime())));
		else
			ja.put(FormatNumber.num2Str(exe.getExe().getSetupTime()));
		// Muc tieu
		BookingStatus.Work target = t.getExe().getTarget();
		ja.put(FormatNumber.num2Str(target.getExeTime()));
		// Ke hoach
		ja.put(FormatNumber.num2Str(exe.getPlan().getExeTime()));
		// Thuc te
		if (t.getExeLate() != null && t.getExeLate().compareTo(new BigDecimal(0)) > 0)
			ja.put(String.format("<font color='red'>%s</font>", FormatNumber.num2Str(exe.getExe().getExeTime())));
		else
			ja.put(FormatNumber.num2Str(exe.getExe().getExeTime()));
		//
		// t.getOs().push(ja);
		t.getPo().push(ja);
		t.getQc().push(ja);
		t.getRp().push(ja);
		// Con lai
		ja.put(Formater.num2str(t.getOutStandingAmount()));
		ja.put(Formater.num2str(t.getPlanOutStandingTime()));
		ja.put(Formater.num2str(t.getOutStandingTime()));
	}

}
