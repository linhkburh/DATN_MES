package cic.h2h.controller.mes.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import frwk.controller.CommonController;

@Controller
@RequestMapping(value = "/qr")
public class QRController extends CommonController<QRForm, String> {

	@Override
	public String getJsp() {
		return "dung_chung/quet_qr_code";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QRForm form) throws Exception {
		if ("CL".equals(form.getTo())) {
			form.setTitle("Quét mã quản lý - Thực hiện nguội");
		} else if ("QC".equals(form.getTo())) {
			form.setTitle("Quét mã quản lý - Thực hiện QC");
		}else if ("RP".equals(form.getTo())) {
			form.setTitle("Quét mã quản lý - sửa hàng");
		}
	}

}
