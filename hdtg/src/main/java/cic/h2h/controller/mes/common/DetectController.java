package cic.h2h.controller.mes.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import frwk.controller.CommonController;
@Controller
@RequestMapping(value = "/detect")
public class DetectController extends CommonController<DetectForm, String>{

		@Override
		public String getJsp() {
			return "DetecOnWeb/detect_cascade_online/index";
		}

		@Override
		public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, DetectForm form) throws Exception {
			
		}
}
