package cic.h2h.controller.report;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ibm.icu.util.Calendar;

import cic.h2h.dao.hibernate.BssFactoryUnitDao;
import cic.h2h.form.WorkerSumaryForm;
import cic.h2h.form.WorkerSumaryForm.WorkerSumary;
import common.util.FormatNumber;
import common.util.Formater;
import common.util.ResourceException;
import frwk.controller.jdbc.v2.PrintController;
import frwk.dao.hibernate.sys.CompanyDao;
import frwk.dao.hibernate.sys.SysDictParamDao;
import frwk.dao.hibernate.sys.SysUsersDao;
import oracle.jdbc.OracleTypes;

@Controller
@RequestMapping(value = "/workerStsSumary")
public class WorkerSumaryStatusController extends PrintController<WorkerSumaryForm, WorkerSumary> {

	@Override
	public String getProcedure() {
		return "hdtg_report.workerSts(?,?,?,?,?,?,?,?,?,?)";
	}

	@Override
	public void pushParam(ModelMap model, CallableStatement cStmt, WorkerSumaryForm form) throws Exception {
		if (Formater.isNull(form.getWoCode()))
			cStmt.setNull("p_userId", OracleTypes.VARCHAR);
		else
			cStmt.setString("p_userId", form.getWoCode());

		if (Formater.isNull(form.getFrDate()))
			cStmt.setNull("p_from_date", OracleTypes.DATE);
		else
			cStmt.setDate("p_from_date", new java.sql.Date(Formater.str2date(form.getFrDate()).getTime()));

		if (Formater.isNull(form.getToDate()))
			cStmt.setNull("p_to_date", OracleTypes.DATE);
		else
			cStmt.setDate("p_to_date", new java.sql.Date(Formater.str2date(form.getToDate()).getTime()));

		if (Formater.isNull(form.getCompany()))
			cStmt.setNull("p_company", OracleTypes.VARCHAR);
		else
			cStmt.setString("p_company", form.getCompany());

		if (Formater.isNull(form.getShift()))
			cStmt.setNull("p_shift", OracleTypes.VARCHAR);
		else
			cStmt.setString("p_shift", form.getShift());

		if (Formater.isNull(form.getFactoryUnit()))
			cStmt.setNull("p_factory_unit", OracleTypes.VARCHAR);
		else
			cStmt.setString("p_factory_unit", form.getFactoryUnit());
	}

	@Override
	public void pushParamExcel(ModelMap model, CallableStatement cStmt, HttpServletRequest request,
			WorkerSumaryForm form) throws Exception {

	}

	@Override
	public void pushToJa(JSONArray ja, WorkerSumary objModel) throws Exception {
		ja.put(objModel.getWorker().getUsername() + "-" + objModel.getWorker().getName());
		// Gia cong
		ja.put(FormatNumber.num2Str(objModel.getMachiningTotal()));
		ja.put(FormatNumber.num2Str(objModel.getMachiningOk()));
		ja.put(FormatNumber.num2Str(objModel.getMachiningNg()));
		ja.put(FormatNumber.num2Str(objModel.getMachiningEst()));
		ja.put(FormatNumber.num2Str(objModel.getMachiningSetupTime()));
		ja.put(FormatNumber.num2Str(objModel.getMachiningTime()));
		if (objModel.getMachiningSlow() != null && objModel.getMachiningSlow().compareTo(new BigDecimal(0)) > 0) {
			ja.put("<font color='red'>" + FormatNumber.num2Str(objModel.getMachiningSlow()) + "</font>");
			ja.put("<font color='red'>" + FormatNumber.num2Str(objModel.getMachiningSlowPercent()) + "</font>");
		} else {
			ja.put(FormatNumber.num2Str(objModel.getMachiningSlow()));
			ja.put(FormatNumber.num2Str(objModel.getMachiningSlowPercent()));
		}
		ja.put(FormatNumber.num2Str(objModel.getMachiningRpAmount()));
		ja.put(FormatNumber.num2Str(objModel.getMachiningRpTime()));
		// Sua hang
		ja.put(FormatNumber.num2Str(objModel.getRpAmount()));
		ja.put(FormatNumber.num2Str(objModel.getRpTime()));
		// Nguoi
		ja.put(FormatNumber.num2Str(objModel.getPolishingTotal()));
		ja.put(FormatNumber.num2Str(objModel.getPolishingOk()));
		ja.put(FormatNumber.num2Str(objModel.getPolishingNg()));
		ja.put(FormatNumber.num2Str(objModel.getPolishingTime()));
		ja.put(FormatNumber.num2Str(objModel.getPolishingRp()));
		ja.put(FormatNumber.num2Str(objModel.getPolishingRpTime()));
		// Qc
		ja.put(FormatNumber.num2Str(objModel.getQcAmount()));
		ja.put(FormatNumber.num2Str(objModel.getQcTime()));
		// Tong
		ja.put(FormatNumber.num2Str(objModel.getTotalTime()));

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
		return "bao_cao/tong_hop_cong_nhan";
	}

	@Autowired
	private BssFactoryUnitDao bssFactoryUnitDao;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, WorkerSumaryForm form)
			throws Exception {
		form.setCompany(getSessionUser().getCompany().getId());
		// Mac dinh tu ngay thang truoc
		Calendar lastMonth = Calendar.getInstance();
		lastMonth.add(Calendar.MONTH, -1);
		form.setFrDate(Formater.date2str(lastMonth.getTime()));
		model.addAttribute("companies", companyDao.getAllOrderAstName());
		model.addAttribute("lstShift", sysDictParamDao.getByType("SHIFTS"));
		model.addAttribute("lstFactoryUnit", bssFactoryUnitDao.getFactoryByCompany(getSessionUser().getCompany()));
		model.addAttribute("lstWorker", sysUsersDao.getCompanyUser(getSessionUser().getCompany()));
	}

	@Override
	protected String getReportTitle() {
		return "BÁO CÁO TỔNG HỢP KẾT QUẢ SẢN XUẤT";
	}

	@Autowired
	private SysUsersDao sysUsersDao;

	@Override
	protected void customizeModelObj(WorkerSumary objModel, ResultSet rs) throws Exception {
		String userId = rs.getString("user_id");

		objModel.setWorker(sysUsersDao.get(userId));
		// Gia cong
		objModel.setMachiningTotal(rs.getBigDecimal("tong_m"));
		objModel.setMachiningOk(rs.getBigDecimal("ok_m"));
		objModel.setMachiningNg(rs.getBigDecimal("ng_m"));
		objModel.setMachiningEst(rs.getBigDecimal("tg_giao_m"));
		objModel.setMachiningSetupTime(rs.getBigDecimal("setup_time_m"));
		objModel.setMachiningTime(rs.getBigDecimal("tg_thuc_hien_m"));
		objModel.setMachiningSlow(rs.getBigDecimal("cham_m"));
		objModel.setMachiningSlowPercent(rs.getBigDecimal("cham_phan_tram_m"));
		objModel.setMachiningRpAmount(rs.getBigDecimal("so_luong_sua_m"));
		objModel.setMachiningRpTime(rs.getBigDecimal("tg_thuc_hien_sua_m"));
		// Sua hang
		objModel.setRpAmount(rs.getBigDecimal("so_luong_r"));
		objModel.setRpTime(rs.getBigDecimal("tg_thuc_hien_r"));
		// Nguoi
		objModel.setPolishingTotal(rs.getBigDecimal("so_luong_p"));
		objModel.setPolishingTime(rs.getBigDecimal("tg_thuc_hien_p"));
		objModel.setPolishingOk(rs.getBigDecimal("so_luong_p_ok"));
		objModel.setPolishingNg(rs.getBigDecimal("so_luong_p_ng"));
		objModel.setPolishingRp(rs.getBigDecimal("so_luong_sua_p"));
		objModel.setPolishingRpTime(rs.getBigDecimal("tg_thuc_hien_sua_p"));
		// QC
		objModel.setQcAmount(rs.getBigDecimal("so_luong_q"));
		objModel.setQcTime(rs.getBigDecimal("tg_thuc_hien_q"));
		// Tong
		objModel.setTotalTime(rs.getBigDecimal("tong_tg_thuc_hien"));
	}

	@Override
	public void validateInput(WorkerSumaryForm form) throws Exception {
		if (Formater.isNull(form.getFrDate()))
			throw new ResourceException("Phải nhập từ ngày");
		Calendar fromDate = Calendar.getInstance();
		fromDate.setTime(Formater.str2date(form.getFrDate()));
		fromDate.add(Calendar.MONTH, 3);
		Calendar toDate = Calendar.getInstance();
		if (!Formater.isNull(form.getToDate()))
			toDate.setTime(Formater.str2date(form.getToDate()));
		if (fromDate.before(toDate))
			throw new ResourceException("Dữ liệu báo cáo trong vòng 3 tháng, kiểm tra lại từ ngày, đến ngày");
	}

	@Override
	protected String getExportFileName() {
		return "tong_hop_cong_nhan.xlsx";
	}

	@Autowired
	private CompanyDao companyDao;
	@Autowired
	private SysDictParamDao sysDictParamDao;

	@Override
	protected void pushReportParam(Map<String, Object> beans, WorkerSumaryForm form) throws Exception {
		if (!Formater.isNull(form.getFrDate()))
			beans.put("frDate", form.getFrDate());
		if (!Formater.isNull(form.getToDate()))
			beans.put("toDate", form.getToDate());
		if (!Formater.isNull(form.getCompany()))
			beans.put("company", companyDao.get(form.getCompany()).getName());
		if (!Formater.isNull(form.getShift()))
			beans.put("shift", sysDictParamDao.get(form.getShift()).getValue());
		if (!Formater.isNull(form.getFactoryUnit()))
			beans.put("factoryUnit", sysDictParamDao.get(form.getFactoryUnit()).getValue());
	}

	@Override
	protected String getTemplateFileName() {
		return "tong_hop_cong_nhan.xlsx";
	}

}
