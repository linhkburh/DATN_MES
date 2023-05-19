package frwk.controller.sys;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import frwk.utils.ApplicationContext;
@Controller
@RequestMapping("/common")
public class SysController{
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
    public void checkSession(ModelMap model, HttpServletRequest rq, HttpServletResponse rs) throws Exception {
        rs.setContentType("text/plan;charset=utf-8");
        PrintWriter pw = rs.getWriter();
        
        if (rq.getSession().getAttribute(ApplicationContext.APPLICATIONCONTEXT) == null) {
        	 pw.print("0");
        } else {
        	 pw.print("1");
        }
        pw.flush();
        pw.close();
        
    }
    
}
