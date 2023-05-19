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

import cic.h2h.form.MachineStatusForm;
import common.util.Formater;
import entity.MachineStatus;
import frwk.controller.jdbc.PrintController;
import oracle.jdbc.OracleTypes;


@Controller
@RequestMapping(value = "/machineSts")
public class MachineStatusController extends PrintController<MachineStatusForm, MachineStatus>{

	@Override
	public String getProcedure() {
		return "MES_EXE.machineSts(?,?,?,?,?,?,?,?)";
	}

	@Override
	public void pushParam(ModelMap model, CallableStatement cStmt, MachineStatusForm form) throws Exception {
		cStmt.setNull("p_machine_id", OracleTypes.VARCHAR);
		if (Formater.isNull(form.getMaCode()))
			cStmt.setNull("p_machine_code", OracleTypes.VARCHAR);
		else
			cStmt.setString("p_machine_code", form.getMaCode());
		if (Formater.isNull(form.getMaName()))
			cStmt.setNull("p_machine_name", OracleTypes.VARCHAR);
		else
			cStmt.setString("p_machine_name", form.getMaName());
		if (Formater.isNull(form.getMachiningSts()))
			cStmt.setNull("p_exe_status", OracleTypes.FLOAT);
		else
			cStmt.setFloat("p_exe_status", Float.valueOf(form.getMachiningSts()));
		
	}

	@Override
	public void pushParamExcel(ModelMap model, CallableStatement cStmt, HttpServletRequest request,
			MachineStatusForm form) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pushToJa(JSONArray ja, ResultSet rs1) throws Exception {
		ja.put(rs1.getString("TEN_MAY"));
		ja.put(Formater.num2str(rs1.getLong("SO_LUONG_LSX")));
		ja.put(Formater.num2str(rs1.getBigDecimal("TONG_THOI_THEO_KE_HOACH")));
		ja.put(Formater.num2str(rs1.getLong("TG_DA_THUC_HIEN_THEO_KH")));
		ja.put(Formater.num2str(rs1.getLong("TG_THUC_TE")));
		ja.put(Formater.num2str(rs1.getLong("TG_CHAM")));
		ja.put(Formater.num2str(rs1.getLong("TG_CON_LAI")));
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getResulSetName() {
		return "cResult";
	}

	@Override
	public String getJsp() {
		return "bao_cao/tinh_trang_may";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, MachineStatusForm form)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String getReportTitle() {
		return "BÁO CÁO TÌNH TRẠNG MÁY";
	}

	@Override
	protected void customizeModelObj(MachineStatus objModel, ResultSet rs) throws Exception {
		objModel.setMachineCode(rs.getString("TEN_MAY"));
		objModel.setWorkAmount(rs.getLong("SO_LUONG_LSX"));
		objModel.setTimeTotal(rs.getLong("TONG_THOI_THEO_KE_HOACH"));
		objModel.setPlanTime(rs.getLong("TG_DA_THUC_HIEN_THEO_KH"));
		objModel.setRealTime(rs.getLong("TG_THUC_TE"));
		objModel.setSlowPlan(rs.getLong("TG_CHAM"));
		objModel.setRemainingTimePlan(rs.getLong("TG_CON_LAI"));
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
	public void validateInput(MachineStatusForm form) throws Exception {
		
	}

	@Override
	protected String getExportFileName() {
		return "Bao_cao_tinh_trang_may.xlsx";
	}

	@Override
	protected void pushReportParam(Map<String, Object> beans, MachineStatusForm form) throws Exception {
		if(!Formater.isNull(form.getMaCode()))
			beans.put("machineCode",form.getMaCode());
		if(!Formater.isNull(form.getMaName()))
			beans.put("machineName",form.getMaName());
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
		return "Bao_cao_tinh_trang_may.xlsx";
	}

}
