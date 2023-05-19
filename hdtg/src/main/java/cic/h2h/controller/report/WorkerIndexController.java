package cic.h2h.controller.report;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.form.WorkerForm;
import entity.WorkOrder;
import frwk.controller.CommonController;

@Controller
@RequestMapping("/workerIndex")
public class WorkerIndexController extends CommonController<WorkerForm, WorkOrder>{

	@Override
	public String getJsp() {
		return "base/worker_index";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, WorkerForm form)
			throws Exception {
	}
	

}
