package cic.h2h.controller.report;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.form.OrderStatusForm;
import common.util.Formater;
import common.util.ResourceException;
import entity.OrderStatus;
import frwk.controller.jdbc.PrintController;
import oracle.jdbc.OracleTypes;

@Controller
@RequestMapping(value = "/orderSts")
public class OrderStatusController extends PrintController<OrderStatusForm, OrderStatus> {

	@Override
	public String getProcedure() {

		return "MES_EXE.orderSts(?,?,?,?,?,?,?,?,?,?,?)";
	}

	@Override
	public void pushToJa(JSONArray ja, ResultSet rs1) throws Exception {
		ja.put(rs1.getString("CUSTOMER_CODE"));
		ja.put(rs1.getString("CUSTOMER_NAME"));
		ja.put(Formater.num2str(rs1.getLong("SO_BAN_VE")));
		ja.put(Formater.num2str(rs1.getBigDecimal("TG_DON_HANG")));
		ja.put(Formater.num2str(rs1.getLong("TG_DA_THUC_HIEN")));
		ja.put(Formater.num2str(rs1.getLong("KHOI_LUONG")));
		ja.put(Formater.num2str(rs1.getLong("TG_THUC_TE")));
		ja.put(Formater.num2str(rs1.getLong("TG_CHAM")));
		ja.put(Formater.num2str(rs1.getLong("TG_CHAM_PERCENT")));
		ja.put(Formater.num2str(rs1.getLong("TG_CON_LAI_THUC_TE")));
		// Tinh trang gia cong
		String iTinhTrangGiaCong = rs1.getString("TINH_TRANG_GIA_CONG");
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
		// Tinh trang don hang
		String iTinhTrangDonHang = rs1.getString("TINH_TRANG_DON_HANG");
		if ("-1".equals(iTinhTrangDonHang))
			ja.put("<span>Còn thời hạn</span>");
		else if ("0".equals(iTinhTrangDonHang))
			ja.put("<span>Đã hoàn thành</span>");
		else if ("1".equals(iTinhTrangDonHang))
			ja.put("<span style='color:red;'>Hết thời hạn</span>");
		else
			ja.put("Chưa thực hiện");
		String orderId = rs1.getString("ID_DON_HANG");
		ja.put("<a class='characterwrap' href = '#' onclick = 'detailLSX(\"" + orderId + "\")'>Chi tiết LSX</a>");
	}

	@Override
	public void pushToExcel(HttpServletRequest request, HttpServletResponse rs, ResultSet rs1) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<OrderStatus> orderStatusList = new ArrayList<OrderStatus>();
		while (rs1.next()) {
			OrderStatus orderStatus = new OrderStatus();
			orderStatus.setCustometCode(rs1.getString("CUSTOMER_CODE"));
			orderStatusList.add(orderStatus);
		}
		map.put("reports", orderStatusList);
	}

	@Override
	public String getResulSetName() {

		return "cResult";
	}

	@Override
	public String getJsp() {
		return "bao_cao/trang_thai_don_hang";
	}

	@Override
	protected String getReportTitle() {
		return "Báo cáo tình trạng đơn hàng";
	}

	@Override
	protected void customizeModelObj(OrderStatus objModel, ResultSet rs) throws Exception {
		objModel.setCustometCode(rs.getString("CUSTOMER_CODE"));
		objModel.setCustometName(rs.getString("CUSTOMER_NAME"));
		objModel.setNumberOfDraw(rs.getLong("SO_BAN_VE"));
		objModel.setTotalOder(rs.getLong("TG_DON_HANG"));
		objModel.setTotalOderMade(rs.getLong("TG_DA_THUC_HIEN"));
		objModel.setTotalOderReality(rs.getLong("KHOI_LUONG"));
		objModel.setTotalOderlow(rs.getLong("TG_THUC_TE"));
		objModel.setTotalOderlowPercent(rs.getLong("TG_CHAM"));
		objModel.setTotalOderRemaining(rs.getLong("TG_CHAM_PERCENT"));
		objModel.setTotalOderRemainingReality(rs.getLong("TG_CON_LAI_THUC_TE"));
		String iTinhTrangGiaCong = rs.getString("TINH_TRANG_GIA_CONG");
		if ("-1".equals(iTinhTrangGiaCong))
			objModel.setExeStepSts("Chậm tiến độ");
		else if ("0".equals(iTinhTrangGiaCong))
			objModel.setExeStepSts("Đúng tiến độ");
		else if ("1".equals(iTinhTrangGiaCong))
			objModel.setExeStepSts("Vượt tiến độ");
		else if ("-3".equals(iTinhTrangGiaCong))
			objModel.setExeStepSts("Chưa tạo lệnh sản xuất");
		else if ("-2".equals(iTinhTrangGiaCong))
			objModel.setExeStepSts("Chưa thực hiện sản xuất");
		else
			objModel.setExeStepSts("");
		String iTinhTrangDonHang = rs.getString("TINH_TRANG_DON_HANG");
		if ("-1".equals(iTinhTrangDonHang))
			objModel.setOrderSts("Còn thời hạn");
		else if ("0".equals(iTinhTrangDonHang))
			objModel.setOrderSts("Đã hoàn thành");
		else if ("1".equals(iTinhTrangDonHang))
			objModel.setOrderSts("Hết thời hạn");
		else
			objModel.setOrderSts("Chưa thực hiện");

	}

	@Override
	public void validateInput(OrderStatusForm form) throws Exception {
		if (Formater.isNull(form.getOrderFrom()))
			throw new ResourceException("Phải nhập từ ngày");
		if (Formater.isNull(form.getOrderTo()))
			throw new ResourceException("Phải nhập đến ngày");
	}

	@Override
	protected String getExportFileName() {
		return "Bao_cao_tinh_trang_don_hang.xlsx";
	}

	@Override
	protected String getTemplateFileName() {
		return "Bao_cao_tinh_trang_don_hang.xlsx";
	}

	@Override
	public void pushParam(ModelMap model, CallableStatement cStmt, OrderStatusForm form) throws Exception {
		cStmt.setNull("p_or_id", OracleTypes.VARCHAR);
		if (Formater.isNull(((OrderStatusForm) form).getCusCode()))
			cStmt.setNull("p_customer_code", OracleTypes.VARCHAR);
		else
			cStmt.setString("p_customer_code", ((OrderStatusForm) form).getCusCode());
		if (Formater.isNull(((OrderStatusForm) form).getCusName()))
			cStmt.setNull("p_customer_name", OracleTypes.VARCHAR);
		else
			cStmt.setString("p_customer_name", ((OrderStatusForm) form).getCusName());
		if (Formater.isNull(((OrderStatusForm) form).getOrderFrom()))
			cStmt.setNull("p_order_from", OracleTypes.DATE);
		else
			cStmt.setDate("p_order_from",
					new java.sql.Date(Formater.str2date(((OrderStatusForm) form).getOrderFrom()).getTime()));
		if (Formater.isNull(((OrderStatusForm) form).getOrderTo()))
			cStmt.setNull("p_order_to", OracleTypes.DATE);
		else
			cStmt.setDate("p_order_to",
					new java.sql.Date(Formater.str2date(((OrderStatusForm) form).getOrderTo()).getTime()));

		if (Formater.isNull(((OrderStatusForm) form).getMachiningSts()))
			cStmt.setNull("p_exe_status", OracleTypes.FLOAT);
		else
			cStmt.setFloat("p_exe_status", Float.valueOf(((OrderStatusForm) form).getMachiningSts()));

		if (Formater.isNull(((OrderStatusForm) form).getOrderSts()))
			cStmt.setNull("p_status", OracleTypes.FLOAT);
		else
			cStmt.setFloat("p_status", Float.valueOf(((OrderStatusForm) form).getOrderSts()));

	}

	@Override
	public void pushParamExcel(ModelMap model, CallableStatement cStmt, HttpServletRequest request,
			OrderStatusForm form) throws Exception {

	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, OrderStatusForm form)
			throws Exception {

	}

	@Override
	protected void pushReportParam(Map<String, Object> beans, OrderStatusForm form) throws Exception {
		if (!Formater.isNull(form.getCusCode()))
			beans.put("makhachhang", form.getCusCode());
		if (!Formater.isNull(form.getCusName()))
			beans.put("tenkhachhang", form.getCusName());
		if (!Formater.isNull(form.getOrderFrom()))
			beans.put("tungay", form.getOrderFrom());
		if (!Formater.isNull(form.getOrderTo()))
			beans.put("denngay", form.getOrderTo());
		if (Formater.isNull(form.getMachiningSts()))
			beans.put("ttgiacong", "Tất cả");
		else if ("-1".equals(form.getMachiningSts()))
			beans.put("ttgiacong", "Chậm tiến độ");
		else if ("0".equals(form.getMachiningSts()))
			beans.put("ttgiacong", "Đúng tiến độ");
		else if ("1".equals(form.getMachiningSts()))
			beans.put("ttgiacong", "Vượt tiến độ");
		else if ("-3".equals(form.getMachiningSts()))
			beans.put("ttgiacong", "Chưa tạo LSX");
		else
			beans.put("ttgiacong", "Chưa thực hiện sản xuất");
		if (Formater.isNull(form.getOrderSts()))
			beans.put("ttdonhang", "Tất cả");
		else if ("-1".equals(form.getOrderSts()))
			beans.put("ttdonhang", "Còn thời hạn");
		else if ("0".equals(form.getOrderSts()))
			beans.put("ttdonhang", "Đã hoàn thành");
		else
			beans.put("ttdonhang", "Hết thời hạn");
	}
}
