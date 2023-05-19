package cic.h2h.controller.report;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.form.OrderStatusForm;
import cic.h2h.form.ProStatusForm;
import common.util.Formater;
import common.util.ResourceException;
import entity.ProStatus;
import frwk.controller.jdbc.PrintController;
import frwk.controller.jdbc.SearchController;
import oracle.jdbc.OracleTypes;

@Controller
@RequestMapping(value = "/proSts")
public class ProStatusController extends PrintController<ProStatusForm, ProStatus>{

	@Override
	public String getProcedure() {
		return "MES_EXE.proSts(?,?,?,?,?,?,?,?,?,?,?)";
	}

	@Override
	public void pushParam(ModelMap model, CallableStatement cStmt, ProStatusForm form) throws Exception {
		if (Formater.isNull(form.getOrCode()))
			cStmt.setNull("p_or_code", OracleTypes.VARCHAR);
		else
			cStmt.setString("p_or_code", form.getOrCode());
		if (Formater.isNull(form.getWoCode()))
			cStmt.setNull("p_worker_code", OracleTypes.VARCHAR);
		else
			cStmt.setString("p_worker_code", form.getWoCode());
		if (Formater.isNull(form.getNaWorker()))
			cStmt.setNull("p_worker_name", OracleTypes.VARCHAR);
		else
			cStmt.setString("p_worker_name", form.getNaWorker());
		if (Formater.isNull(form.getDrCode()))
			cStmt.setNull("p_drw_code", OracleTypes.VARCHAR);
		else
			cStmt.setString("p_drw_code", form.getDrCode());
		if (Formater.isNull(form.getFrDate()))
			cStmt.setNull("p_from_date", OracleTypes.DATE);
		else
			cStmt.setDate("p_from_date", new java.sql.Date(Formater.str2date(form.getFrDate()).getTime()));
		if (Formater.isNull(form.getToDate()))
			cStmt.setNull("p_to_date", OracleTypes.DATE);
		else
			cStmt.setDate("p_to_date", new java.sql.Date(Formater.str2date(form.getToDate()).getTime()));
		if (Formater.isNull(form.getMachiningSts()))
			cStmt.setNull("p_exe_status", OracleTypes.FLOAT);
		else
			cStmt.setFloat("p_exe_status", Float.valueOf(form.getMachiningSts()));
		
	}

	@Override
	public void pushParamExcel(ModelMap model, CallableStatement cStmt, HttpServletRequest request, ProStatusForm form)
			throws Exception {
		
	}

	@Override
	public void pushToJa(JSONArray ja, ResultSet rs1) throws Exception {
		ja.put(rs1.getString("NAME"));
		ja.put(Formater.num2str(rs1.getBigDecimal("SO_BAN_VE")));
		ja.put(Formater.num2str(rs1.getLong("TONG_THOI_GIAN")));
		ja.put(Formater.num2str(rs1.getBigDecimal("SO_BAN_VE_DA_HOAN_THANH")));
		ja.put(Formater.num2str(rs1.getLong("TG_DU_KIEN")));
		ja.put(Formater.num2str(rs1.getLong("THOI_GIAN_THUC_TE")));
		ja.put(Formater.num2str(rs1.getLong("CHAM")));
		ja.put(Formater.num2str(rs1.getBigDecimal("SO_BAN_VE_CON_LAI")));
		ja.put(Formater.num2str(rs1.getLong("THOI_GIAN_CON_LAI")));
		ja.put(Formater.num2str(rs1.getLong("TG_CON_LAI_THUC_TE")));
		String iTinhTrangGiaCong = rs1.getString("trang_thai_gia_cong");
		if ("-1".equals(iTinhTrangGiaCong))
			ja.put("<span style='color:red;'>Chậm tiến độ</span>");
		else if ("0".equals(iTinhTrangGiaCong))
			ja.put("<span>Đúng tiến độ</span>");
		else if ("1".equals(iTinhTrangGiaCong))
			ja.put("<span>Vượt tiến độ</span>");
		else if ("-3".equals(iTinhTrangGiaCong))
			ja.put("<span style='color:red;'>Chưa tạo LSX</span>");
		else if ("-2".equals(iTinhTrangGiaCong))
			ja.put("<span style='color:red;'>Chưa thực hiện sản xuất</span>");
		else
			ja.put("");
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
		return "bao_cao/bao_cao_lap_trinh";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ProStatusForm form)
			throws Exception {
		
	}

	@Override
	protected String getReportTitle() {
		return "BÁO CÁO LẬP TRÌNH";
	}

	@Override
	protected void customizeModelObj(ProStatus objModel, ResultSet rs) throws Exception {
		objModel.setWorkerName(rs.getString("NAME"));
		objModel.setDrawTotal(rs.getLong("SO_BAN_VE"));
		objModel.setTimeTotal(rs.getLong("TONG_THOI_GIAN"));
		objModel.setDrawTotalDone(rs.getLong("SO_BAN_VE_DA_HOAN_THANH"));
		objModel.setPlanTime(rs.getLong("TG_DU_KIEN"));
		objModel.setRealTime(rs.getLong("THOI_GIAN_THUC_TE"));
		objModel.setSlowPlan(rs.getLong("CHAM"));
		objModel.setRemainingDraw(rs.getLong("SO_BAN_VE_CON_LAI"));
		objModel.setRemainingTimePlan(rs.getLong("THOI_GIAN_CON_LAI"));
		objModel.setRemainingTimeReality(rs.getLong("TG_CON_LAI_THUC_TE"));
		String iTinhTrangGiaCong = rs.getString("trang_thai_gia_cong");
		if ("-1".equals(iTinhTrangGiaCong))
			objModel.setStatus("Chậm tiến độ");
		else if ("0".equals(iTinhTrangGiaCong))
			objModel.setStatus("Đúng tiến độ");
		else if ("1".equals(iTinhTrangGiaCong))
			objModel.setStatus("Vượt tiến độ");
		else if ("-3".equals(iTinhTrangGiaCong))
			objModel.setStatus("Chưa tạo LSX");
		else if ("-2".equals(iTinhTrangGiaCong))
			objModel.setStatus("Chưa thực hiện sản xuất");
		else
			objModel.setStatus("");
	}

	@Override
	public void validateInput(ProStatusForm form) throws Exception {
		if(Formater.isNull(form.getFrDate()))
			throw new ResourceException("Phải nhập từ ngày");
		if(Formater.isNull(form.getToDate()))
			throw new ResourceException("Phải nhập đến ngày");
	}

	@Override
	protected String getExportFileName() {
		return "Bao_cao_lap_trinh.xlsx";
	}

	@Override
	protected void pushReportParam(Map<String, Object> beans, ProStatusForm form) throws Exception {
		if(!Formater.isNull(form.getWoCode()))
			beans.put("woCode", form.getWoCode());
		if(!Formater.isNull(form.getNaWorker()))
			beans.put("woName", form.getNaWorker());
		if(!Formater.isNull(form.getFrDate()))
			beans.put("frDate", form.getFrDate());
		if(!Formater.isNull(form.getToDate()))
			beans.put("toDate", form.getToDate());
		if(!Formater.isNull(form.getDrCode()))
			beans.put("drCode", form.getDrCode());
		if(!Formater.isNull(form.getOrCode()))
			beans.put("orCode", form.getOrCode());
		if(form.getMachiningSts() == null)
			beans.put("machiningSts","Tất cả");
		else if(form.getMachiningSts() == "-1")
			beans.put("machiningSts","Chậm tiến độ");
		else if(form.getMachiningSts() == "0")
			beans.put("machiningSts","Đúng tiến độ");
		else if(form.getMachiningSts() == "1")
			beans.put("machiningSts","Vượt tiến độ");
		else if(form.getMachiningSts() == "-3")
			beans.put("machiningSts","Chưa tạo LSX");
		else if(form.getMachiningSts() == "-2")
			beans.put("machiningSts","Chưa thực hiện sản xuất");
	}

	@Override
	protected String getTemplateFileName() {
		return "Bao_cao_lap_trinh.xlsx";
	}

}
