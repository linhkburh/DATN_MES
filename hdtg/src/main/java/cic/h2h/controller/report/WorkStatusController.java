package cic.h2h.controller.report;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.jdbc.OracleTypes;

import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import common.util.FormatNumber;
import common.util.Formater;
import cic.h2h.form.WorkStatusForm;
import entity.WorkStatus;
import frwk.controller.jdbc.PrintController;
import frwk.controller.jdbc.SearchController;

@Controller
@RequestMapping(value = "/workSts")
public class WorkStatusController extends PrintController<WorkStatusForm, WorkStatus> {

	@Override
	public String getProcedure() {

		return "MES_EXE.woSts(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	}

	@Override
	public void pushParam(ModelMap model, CallableStatement cStmt, WorkStatusForm form) throws Exception {
		cStmt.setNull("p_wo_id", OracleTypes.VARCHAR);
		if (Formater.isNull(form.getQuotationId()))
			cStmt.setNull("p_quotation_id", OracleTypes.VARCHAR);
		else
			cStmt.setString("p_quotation_id", form.getQuotationId());
		if (Formater.isNull(form.getWorkCode()))
			cStmt.setNull("p_or_code", OracleTypes.VARCHAR);
		else
			cStmt.setString("p_or_code", form.getWorkCode());
		if (Formater.isNull(form.getCusName()))
			cStmt.setNull("p_cus_name", OracleTypes.VARCHAR);
		else
			cStmt.setString("p_cus_name", form.getCusName());

		if (Formater.isNull(form.getWorkCode()))
			cStmt.setNull("p_wo_code", OracleTypes.VARCHAR);
		else
			cStmt.setString("p_wo_code", form.getWorkCode());

		if (Formater.isNull(form.getDrawingCode()))
			cStmt.setNull("p_drw_code", OracleTypes.VARCHAR);
		else
			cStmt.setString("p_drw_code", form.getDrawingCode());

		if (Formater.isNull(form.getStageCode()))
			cStmt.setNull("p_op_code", OracleTypes.VARCHAR);
		else
			cStmt.setString("p_op_code", form.getStageCode());

		if (Formater.isNull(form.getMachineCode()))
			cStmt.setNull("p_machine_code", OracleTypes.VARCHAR);
		else
			cStmt.setString("p_machine_code", form.getMachineCode());

		if (Formater.isNull(form.getMachiningSts()))
			cStmt.setNull("p_exe_status", OracleTypes.FLOAT);
		else
			cStmt.setFloat("p_exe_status", Float.valueOf(form.getMachiningSts()));

		if (Formater.isNull(form.getWorkSts()))
			cStmt.setNull("p_status", OracleTypes.FLOAT);
		else
			cStmt.setFloat("p_status", Float.valueOf(form.getWorkSts()));

	}

	@Override
	public void pushParamExcel(ModelMap model, CallableStatement cStmt, HttpServletRequest request, WorkStatusForm form)
			throws Exception {

	}

	@Override
	public void pushToJa(JSONArray ja, ResultSet rs1) throws Exception {

		ja.put("<span class='characterwrap'>" + rs1.getString("MA_LSX") + "</span>");
		ja.put(Formater.num2str(rs1.getLong("SO_LUONG_CHI_TIET")));
		ja.put(Formater.num2str(rs1.getBigDecimal("TG_TRUNG_BINH")));
		ja.put(Formater.num2str(rs1.getLong("TG_TONG")));
		ja.put(Formater.num2str(rs1.getLong("SO_LUONG_DA_HOAN_THIEN")));
		ja.put(Formater.num2str(rs1.getLong("TG_THEO_KH")));
		ja.put(Formater.num2str(rs1.getLong("TG_DA_THUC_HIEN")));
		ja.put(Formater.num2str(rs1.getLong("CHAM")));
		ja.put(Formater.num2str(rs1.getLong("PHAN_TRAM_CHAM")));
		ja.put(Formater.num2str(rs1.getLong("SO_LUONG_CON_LAI")));
		ja.put(Formater.num2str(rs1.getLong("TG_CON_THEO_KE_HOACH")));
		ja.put(Formater.num2str(rs1.getLong("TG_CON_THUC_TE")));
		String trangthaigiacong = rs1.getString("tinh_trang_gia_cong");
		if ("-1".equals(trangthaigiacong))
			ja.put("<span style='color:red;'>Chậm tiến độ</span>");
		else if ("0".equals(trangthaigiacong))
			ja.put("<span>Đúng tiến độ</span>");
		else if ("1".equals(trangthaigiacong))
			ja.put("<span>Vượt tiến độ</span>");
		else if ("-3".equals(trangthaigiacong))
			ja.put("<span style='color:red;'>Chưa tạo LSX</span>");
		else if ("-2".equals(trangthaigiacong))
			ja.put("<span style='color:red;'>Chưa thực hiện sản xuất</span>");
		else
			ja.put("");
		String trangthailsx = rs1.getString("TRANG_THAI_LSX");
		if ("-1".equals(trangthailsx))
			ja.put("<span>Còn thời hạn</span>");
		else if ("0".equals(trangthailsx))
			ja.put("<span>Đã hoàn thành</span>");
		else
			ja.put("<span style='color:red;'>Hết thời hạn</span>");
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
		return "bao_cao/trang_thai_lenh_san_xuat";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, WorkStatusForm form)
			throws Exception {

		String orderId = rq.getParameter("orderId");
		if (!Formater.isNull(orderId))
			form.setQuotationId(orderId);
	}

	@Override
	protected String getReportTitle() {
		return "BÁO CÁO TÌNH TRẠNG LỆNH SẢN XUẤT";
	}

	@Override
	protected void customizeModelObj(WorkStatus objModel, ResultSet rs) throws Exception {
		objModel.setWorkCode(rs.getString("MA_LSX"));
		objModel.setItemAmount(rs.getLong("SO_LUONG_CHI_TIET"));
		objModel.setItemAverageTime(rs.getLong("TG_TRUNG_BINH"));
		objModel.setTotalTime(rs.getLong("TG_TONG"));
		objModel.setProcessedAmount(rs.getLong("SO_LUONG_DA_HOAN_THIEN"));
		objModel.setPlanTime(rs.getLong("TG_THEO_KH"));
		objModel.setRealTime(rs.getLong("TG_DA_THUC_HIEN"));
		objModel.setSlowPlanMin(rs.getLong("CHAM"));
		objModel.setSlowPlanPercent(rs.getLong("PHAN_TRAM_CHAM"));
		objModel.setRemainingAmount(rs.getLong("SO_LUONG_CON_LAI"));
		objModel.setRemainingTimePlan(rs.getLong("TG_CON_THEO_KE_HOACH"));
		objModel.setRemainingTimeReality(rs.getLong("TG_CON_THEO_KE_HOACH"));
		objModel.setRemainingTimePlan(rs.getLong("TG_CON_THUC_TE"));
		String trangthaigiacong = rs.getString("tinh_trang_gia_cong");
		if ("-1".equals(trangthaigiacong))
			objModel.setExeStepSts("Chậm tiến độ");
		else if ("0".equals(trangthaigiacong))
			objModel.setExeStepSts("Đúng tiến độ");
		else if ("1".equals(trangthaigiacong))
			objModel.setExeStepSts("Vượt tiến độ");
		else if ("-3".equals(trangthaigiacong))
			objModel.setExeStepSts("Chưa tạo LSX");
		else if ("-2".equals(trangthaigiacong))
			objModel.setExeStepSts("Chưa thực hiện sản xuất");
		else
			objModel.setExeStepSts("");
		String trangthailsx = rs.getString("TRANG_THAI_LSX");
		if ("-1".equals(trangthailsx))
			objModel.setWorkSts("Còn thời hạn");
		else if ("0".equals(trangthailsx))
			objModel.setWorkSts("Đã hoàn thành");
		else
			objModel.setWorkSts("Hết thời hạn");
	}

	@Override
	public void validateInput(WorkStatusForm form) throws Exception {
		
	}

	@Override
	protected String getExportFileName() {
		return "Bao_cao_tinh_trang_lsx.xlsx";
	}

	@Override
	protected void pushReportParam(Map<String, Object> beans, WorkStatusForm form) throws Exception {
		if(!Formater.isNull(form.getOrderCode()))
			beans.put("orderCode",form.getOrderCode());
		if(!Formater.isNull(form.getCusName()))
			beans.put("cusName",form.getCusName());
		if(!Formater.isNull(form.getWorkCode()))
			beans.put("workCode",form.getWorkCode());
		if(!Formater.isNull(form.getDrawingCode()))
			beans.put("drawingCode",form.getDrawingCode());
		if(!Formater.isNull(form.getStageCode()))
			beans.put("stageCode",form.getStageCode());
		if(!Formater.isNull(form.getMachineCode()))
			beans.put("machineCode",form.getMachineCode());
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
		if(form.getWorkSts() == null)
			beans.put("workSts", "Tất cả");
		else if(form.getWorkSts() == "-1")
			beans.put("workSts", "Còn thời hạn");
		else if(form.getWorkSts() == "0")
			beans.put("workSts", "Đã hoàn thành");
		else if(form.getWorkSts() == "1")
			beans.put("workSts", "Hết thời hạn");	
	}

	@Override
	protected String getTemplateFileName() {
		return "Bao_cao_tinh_trang_lsx.xlsx";
	}

}
