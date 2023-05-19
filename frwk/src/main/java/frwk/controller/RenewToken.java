package frwk.controller;

import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
@Controller
@RequestMapping("/RenewToken")
public class RenewToken {

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	public String execute(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("text/html; charset=UTF8");
        PrintWriter pw = response.getWriter();
        String newTokenId = UUID.randomUUID().toString();
        pw.print("newTokenId:" + newTokenId);
        String tokenIdKey = request.getParameter("tokenIdKey");
        request.getSession().setAttribute(tokenIdKey, newTokenId);
        pw.flush();
        pw.close(); 
		return null;

	}

}
