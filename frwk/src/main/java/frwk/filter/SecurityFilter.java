package frwk.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.filter.DelegatingFilterProxy;

import common.util.Formater;
import entity.frwk.SysObjects;
import entity.frwk.SysUsers;
import frwk.utils.ApplicationContext;

public class SecurityFilter extends DelegatingFilterProxy {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse rs = (HttpServletResponse) response;
		String uri = req.getRequestURI();
		String contextPath = req.getContextPath(); // /web
		String URI = uri.substring(uri.indexOf(contextPath) + contextPath.length() + 1);
		if (Formater.isNull(URI))
			rs.sendRedirect("login");

		HttpSession session = req.getSession();

		// Ajax session timeout
		if ("XMLHttpRequest".equals(req.getHeader("X-Requested-With"))) {
			if (!byPassSession(URI)) {
				if (session.isNew() || session.getAttribute(ApplicationContext.APPLICATIONCONTEXT) == null) {
					rs.setContentType("text/html; charset=UTF8");
					PrintWriter pw = rs.getWriter();
					pw.print("SessionTimeout");
					pw.flush();
					pw.close();
					return;
				}
			}
		}

		// Cac tai nguyen khong phai action struts hay jsp file
		if (!URI.endsWith(".jsp") && URI.contains(".")) {
			filterChain.doFilter(request, response);
			return;
		}

		ApplicationContext ac = session.getAttribute(ApplicationContext.APPLICATIONCONTEXT) == null ? null
				: (ApplicationContext) session.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
		String contextRoot = uri.split("/")[1];

		if (byPassSession(URI)) {
			if (ac == null && !URI.equals("login") && !URI.equals("common") && !URI.endsWith("beta/index.jsp"))
				rs.sendRedirect("login");
			filterChain.doFilter(request, response);
			return;
		}

		if (ac == null) {
			rs.sendRedirect("login");
		} else {
			SysUsers user = (SysUsers) ac.getAttribute(ApplicationContext.USER);
			if (!haveRight(URI, contextPath, req, user)) {
				if (!"XMLHttpRequest".equals(req.getHeader("X-Requested-With"))) {
					rs.sendRedirect("/" + contextRoot + "/checkright");
					return;
				} else {
					rs.setContentType("text/html; charset=UTF8");
					PrintWriter pw = rs.getWriter();
					pw.print("Người dùng không có quyền thực hiện chức năng");
					pw.flush();
					pw.close();
					return;
				}

			}
			if (!URI.equals("index")) {

				if (!user.getPssWordValidTime()) {
					rs.sendRedirect("/" + contextRoot + "/index?to=changePassWord");
				}
			}
			filterChain.doFilter(request, response);
			return;
		}
		filterChain.doFilter(request, response);
	}

	@SuppressWarnings("unchecked")
	private boolean haveRight(String URI, String contextPath, HttpServletRequest req, SysUsers user) {
		// Admin luon vao duoc 2 chuc nang quan ly nguoi dung, quan ly nhom quyen
		if ("admin".equals(user.getUsername())) {
			if (URI.endsWith("role") || URI.endsWith("manageUser"))
				return true;
		}

		if (byPassRight(URI))
			return true;

		ApplicationContext context = (ApplicationContext) req.getSession()
				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);

		String requestUrl = req.getRequestURL().toString();
		String fullContextPath = requestUrl.substring(0, requestUrl.indexOf(contextPath) + contextPath.length() + 1); // http://localhost:7101/web/

		// Kiem tra trong quyen nsd
		ArrayList<SysObjects> userRights = (ArrayList<SysObjects>) context.getAttribute(ApplicationContext.USER_RIGHT);
		// if (Formater.isNull(userRights))
		// return false;
		for (SysObjects obj : userRights) {
			if (yesItis(obj, URI, contextPath, req, fullContextPath))
				return true;
		}

		// Kiem tra quyen co duoc dinh nghia trong SysRights khong
		ArrayList<SysObjects> sysRights = (ArrayList<SysObjects>) context.getAttribute(ApplicationContext.SYS_RIGHT);
		if (Formater.isNull(sysRights))
			return true;
		for (SysObjects obj : sysRights) {
			if (yesItis(obj, URI, contextPath, req, fullContextPath))
				return false;
		}
		return true;
	}

	private boolean byPassRight(String URI) {
		return false;
	}

	private boolean yesItis(SysObjects obj, String URI, String contextPath, HttpServletRequest req,
			String fullContextPath) {
		URL objUrl;
		try {
			objUrl = new URL(fullContextPath + obj.getAction());
		} catch (MalformedURLException e) {
			return false;
		}
		String objUri = null;
		try {
			objUri = objUrl.toURI().getPath();
			objUri = objUri.substring(objUri.indexOf(contextPath) + contextPath.length() + 1);
			if (!objUri.endsWith(""))
				objUri += "";
		} catch (Exception e) {
			return false;
		}
		String requestURI = URI.endsWith("") ? URI : URI + "";
		if (!objUri.equalsIgnoreCase(requestURI))
			return false;

		// kiem tra tham so tinh (tham so dong bo qua ex: id=124)
		String sActionParam = obj.getAction().split("\\?").length > 1 ? obj.getAction().split("\\?")[1] : "";
		String[] arrActionParam = Formater.isNull(sActionParam) ? new String[] {} : sActionParam.split("&");

		// True neu moi tham so trong cau hinh deu ton tai trong request
		if (arrActionParam.length > 0) {
			for (String param : arrActionParam) {
				String[] paramWithValue = param.split("=");
				String paramKey = paramWithValue[0];
				String paramValue = paramWithValue[1];
				if (!Formater.isNull(paramValue))
					paramValue = paramValue.trim().toLowerCase();
				String requestValue = req.getParameter(paramKey);
				if (!Formater.isNull(requestValue))
					requestValue = requestValue.trim().toLowerCase();
				if (Formater.isNull(paramValue) && Formater.isNull(requestValue))
					continue;
				if (Formater.isNull(paramValue) && !Formater.isNull(requestValue))
					return false;
				if (Formater.isNull(requestValue) && !Formater.isNull(paramValue))
					return false;
				if (!paramValue.equals(requestValue))
					return false;
			}
		}
		// submit den method nhung action khai bao khong co tham so method
		else if (req.getParameter("method") != null)
			return false;
		return true;
	}

	public boolean byPassSession(String URI) {
		if (URI.equals("login"))
			return true;
		if (URI.equals("RenewToken") || URI.equals("RenewToken"))
			return true;
		if (URI.equals("logout"))
			return true;

		if (URI.equals("help"))
			return true;

		if (URI.equals("checkright"))
			return true;
		if (URI.equals("index"))
			return true;
		if (URI.equals("locale"))
			return true;
		if (URI.equals("common"))
			return true;

		return false;
	}

}
