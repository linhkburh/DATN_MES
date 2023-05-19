package frwk.controller.sys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import entity.frwk.SysUsers;
import frwk.controller.CommonController;
import frwk.form.LoginForm;
import frwk.form.ModelForm;

@Controller
@RequestMapping("/logout")
public class LogoutController extends CommonController<LoginForm, SysUsers> {


	@Override
	public String getJsp() {
		return "qtht/login";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, LoginForm form)
			throws Exception {rq.getSession().invalidate();
	}

}