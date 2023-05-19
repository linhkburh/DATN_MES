package frwk.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/error")
public class ErrorController {
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	public String execute(ModelMap model, HttpServletRequest rq, HttpServletResponse rs) throws Exception {
		return "base/defined_error";

	}
}
