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
import cic.h2h.form.WorkerStatusForm;
import common.util.Formater;
import common.util.ResourceException;
import entity.WorkerStatus;
import frwk.controller.jdbc.PrintController;
import frwk.controller.jdbc.SearchController;
import oracle.jdbc.OracleTypes;

@Controller
@RequestMapping(value = "/workerSts")
public class WorkerStatusController extends PrintController<WorkerStatusForm, WorkerStatus>{

	@Override
	public String getProcedure() {
		return "MES_EXE.workerSts(?,?,?,?,?,?,?,?,?,?,?,?)";
	}

	@Override
	public void pushParam(ModelMap model, CallableStatement cStmt, WorkerStatusForm form) throws Exception {
		if (Formater.isNull(form.getWoCode()))
			cStmt.setNull("p_userAd", OracleTypes.VARCHAR);
		else
			cStmt.setString("p_userAd", form.getWoCode());
		if (Formater.isNull(form.getNaWorker()))
			cStmt.setNull("p_Worker_name", OracleTypes.VARCHAR);
		else
			cStmt.setString("p_Worker_name", form.getNaWorker());
		if (Formater.isNull(form.getFrDate()))
			cStmt.setNull("p_from_date", OracleTypes.DATE);
		else
			cStmt.setDate("p_from_date", new java.sql.Date(Formater.str2date(form.getFrDate()).getTime()));
		if (Formater.isNull(form.getToDate()))
			cStmt.setNull("p_to_date", OracleTypes.DATE);
		else
			cStmt.setDate("p_to_date", new java.sql.Date(Formater.str2date(form.getToDate()).getTime()));
		if (Formater.isNull(form.getDrCode()))
			cStmt.setNull("p_drw_code", OracleTypes.DATE);
		else
			cStmt.setString("p_drw_code", form.getDrCode());
		if (Formater.isNull(form.getOpCode()))
			cStmt.setNull("p_op_code", OracleTypes.VARCHAR);
		else
			cStmt.setString("p_op_code", form.getOpCode());
		if (Formater.isNull(form.getMaCode()))
			cStmt.setNull("p_machine_code", OracleTypes.VARCHAR);
		else
			cStmt.setString("p_machine_code", form.getMaCode());
		if (Formater.isNull(form.getMachiningSts()))
			cStmt.setNull("p_exe_status", OracleTypes.FLOAT);
		else
			cStmt.setFloat("p_exe_status", Float.valueOf(form.getMachiningSts()));
		
	}

	@Override
	public void pushParamExcel(ModelMap model, CallableStatement cStmt, HttpServletRequest request,
			WorkerStatusForm form) throws Exception {
		
		
	}

	@Override
	public void pushToJa(JSONArray ja, ResultSet rs1) throws Exception {
		ja.put(rs1.getString("USERNAME"));
		ja.put("<span class='characterwrap'>" + rs1.getString("CODE") + "</span>");
		ja.put(Formater.num2str(rs1.getLong("SO_LUONG_CHI_TIET")));
		ja.put(Formater.num2str(rs1.getLong("TG_TRUNG_BINH")));
		ja.put(Formater.num2str(rs1.getLong("TONG_THOI_GIAN")));
		ja.put(Formater.num2str(rs1.getLong("TG_THUC_TE")));
		ja.put(Formater.num2str(rs1.getLong("TG_CHAM")));
		ja.put(Formater.num2str(rs1.getLong("PHAN_TRAM_CHAM")));
		String iTinhTrangGiaCong = rs1.getString("TRANG_THAI_GIA_CONG");
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
		return "bao_cao/tinh_trang_cong_nhan";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, WorkerStatusForm form)
			throws Exception {
		
	}

	@Override
	protected String getReportTitle() {
		return "BÁO CÁO TÌNH TRẠNG CÔNG NHÂN";
	}

	@Override
	protected void customizeModelObj(WorkerStatus objModel, ResultSet rs) throws Exception {
		objModel.setWokerName(rs.getString("USERNAME"));
		objModel.setWorkCode(rs.getString("CODE"));
		objModel.setItemAmount(rs.getLong("SO_LUONG_CHI_TIET"));
		objModel.setItemAverageTime(rs.getLong("TG_TRUNG_BINH"));
		objModel.setTimeTotal(rs.getLong("TONG_THOI_GIAN"));
		objModel.setRealTime(rs.getLong("TG_THUC_TE"));
		objModel.setSlowMins(rs.getLong("TG_CHAM"));
		objModel.setSlowPercent(rs.getLong("PHAN_TRAM_CHAM"));
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
	public void validateInput(WorkerStatusForm form) throws Exception {
		if(Formater.isNull(form.getFrDate()))
			throw new ResourceException("Phải nhập từ ngày");
		if(Formater.isNull(form.getToDate()))
			throw new ResourceException("Phải nhập đến ngày");
	}

	@Override
	protected String getExportFileName() {
		return "Bao_cao_tinh_trang_cong_nhan.xlsx";
	}

	@Override
	protected void pushReportParam(Map<String, Object> beans, WorkerStatusForm form) throws Exception {
		if(!Formater.isNull(form.getWoCode()))
			beans.put("woCode", form.getWoCode());
		if(!Formater.isNull(form.getNaWorker()))
			beans.put("woName",form.getNaWorker());
		if(!Formater.isNull(form.getFrDate()))
			beans.put("frDate",form.getFrDate());
		if(!Formater.isNull(form.getToDate()))
			beans.put("toDate",form.getToDate());
		if(!Formater.isNull(form.getDrCode()))
			beans.put("drCode",form.getDrCode());
		if(!Formater.isNull(form.getOpCode()))
			beans.put("opCode",form.getOpCode());
		if(!Formater.isNull(form.getMaCode()))
			beans.put("maCode",form.getMaCode());
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
		return "Bao_cao_tinh_trang_cong_nhan.xlsx";
	}

}
