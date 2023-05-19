package frwk.controller.sys;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import common.util.Formater;
import common.util.ResourceException;
import entity.frwk.SysRoles;
import entity.frwk.SysUsers;
import frwk.constants.RightConstants;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.RightUtils;
import frwk.dao.hibernate.sys.RoleDao;
import frwk.form.SysRolesForm;
import frwk.utils.ApplicationContext;
import frwk.utils.Utils;

@Controller
@RequestMapping("/role")
public class SysRolesController extends CatalogController<SysRolesForm, SysRoles> {

	static Logger lg = Logger.getLogger(SysRolesController.class);

	@Autowired
	private RoleDao roleDao;

	ArrayList<SysRoles> danhSachNhomQuyen;

	@Override
	public BaseDao<SysRoles> getDao() {
		return roleDao;
	}

	@Autowired
	private RightUtils rightUtils;
	@Override
	public void createSearchDAO(HttpServletRequest request, SysRolesForm form, BaseDao<SysRoles> dao) throws Exception {
		if (!Formater.isNull(form.getKeyWord())) {
			Criterion objRest = Restrictions.sqlRestriction(
					"exists (select 1\r\n" + "  from sys_objects o, sys_rights r\r\n"
							+ " where o.id = r.object_id and r.role_id = {alias}.id\r\n"
							+ "   and (lower(o.object_id) like lower(?) or lower(o.action) like lower(?) or\r\n"
							+ "       lower(o.name) like lower(?)))",
					new Object[] { form.getKeyWord(), form.getKeyWord(), form.getKeyWord() },
					new Type[] { new StringType(), new StringType(), new StringType() });
			dao.addRestriction(Restrictions.or(
					Restrictions.or(Restrictions.like("code", form.getKeyWord(), MatchMode.ANYWHERE).ignoreCase(),
							Restrictions.like("descriptionVi", form.getKeyWord(), MatchMode.ANYWHERE).ignoreCase()),
					objRest));
		}
		dao.addOrder(Order.asc("descriptionVi"));
	}

	@Override
	public void pushToJa(JSONArray ja, SysRoles r, SysRolesForm modal) throws Exception {
		ja.put("<a href = '#' onclick = 'edit(\"" + r.getId() + "\")'>" + StringEscapeUtils.escapeHtml(r.getCode())
				+ "</a>");

		ja.put(StringEscapeUtils.escapeHtml(r.getDescriptionVi()));
		// Nhom quyen cha
		ja.put(r.getSysRoles() != null ? StringEscapeUtils.escapeHtml(r.getSysRoles().getDescriptionVi()) : "");
		ja.put(r.getDefaultPage());
		ja.put(r.getDefaultPagePriority());
	}

	@Override
	public String getJsp() {
		return "qtht/danh_muc_nhom_quyen";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysRolesForm form)
			throws Exception {
		if (!Formater.isNull(rq.getParameter("id"))) {
			model.addAttribute("danhSachNhomQuyen", rq.getParameter("id"));
		} else {
			model.addAttribute("danhSachNhomQuyen", roleDao.getAllRoot());
		}

	}

	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysRolesForm form)
			throws Exception {

		ApplicationContext appContext = (ApplicationContext) rq.getSession()
				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
		SysUsers user = null;
		if (appContext != null)
			user = (SysUsers) appContext.getAttribute(ApplicationContext.USER);

		SysRolesForm sysRolesForm = (SysRolesForm) form;
//		if (!RightUtils.haveRight("ADMIN",
//				(ApplicationContext) rq.getSession().getAttribute(ApplicationContext.APPLICATIONCONTEXT))) {
//			if (Formater.isNull(role.getId())) {
//				if (!RightUtils.haveRight(RightConstants.MNROLE_Create,
//						(ApplicationContext) rq.getSession().getAttribute(ApplicationContext.APPLICATIONCONTEXT)))
//					throw new ResourceException("not_access");
//
//			} else {
//				if (!RightUtils.haveRight(RightConstants.MNROLE_Edit,
//						(ApplicationContext) rq.getSession().getAttribute(ApplicationContext.APPLICATIONCONTEXT)))
//					throw new ResourceException("not_access");
//			}
//		}

		if (Formater.isNull(sysRolesForm.getSysRoles().getId())) {
			if (roleDao.checkCode(sysRolesForm.getSysRoles().getCode())) {
				throw new ResourceException("T&#7841;o tr&#249;ng m&#227; nh&#243;m quy&#7873;n");// Táº¡o trÃ¹ng
																									// mÃ£ nhÃ³m
																									// quyá»�n
			}
		}
		SysRoles role = sysRolesForm.getSysRoles();
		if (Formater.isNull(role.getSysRoles().getId()))
			role.setSysRoles(null);
		roleDao.save(sysRolesForm.getSysRoles(), user);
	}

	@Override
	public void del(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysRolesForm form) throws Exception {
		if (!rightUtils.haveRight(RightConstants.MNROLE_Delete,
				(ApplicationContext) rq.getSession().getAttribute(ApplicationContext.APPLICATIONCONTEXT))) {
			throw new ResourceException("not_access");
		} else {
			SysRolesForm sysRolesForm = (SysRolesForm) form;
			// check co nhom quyen con
			roleDao.checkUse(sysRolesForm.getSysRoles().getId());

			// check da duoc phan quyen cho SysUsers
			roleDao.checkUseByUser(sysRolesForm.getSysRoles().getId());
			super.del(model, rq, rs, form);
		}
	}

	@Override
	public void edit(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysRolesForm form)
			throws Exception {
//		try {
//			RightUtils.chkRight(rq, "MNROLE");
//		} catch (ResourceException ex) {
//			lg.error(ex);
//		}
		SysRoles tempRole = roleDao.getWithRight(rq.getParameter("id"));
		Utils.jsonSerialize(rs, tempRole);
	}

	public void reload(ModelMap model, HttpServletRequest rq, HttpServletResponse rp, SysRolesForm form)
			throws IOException {
		rp.setContentType("text/text;charset=utf-8");
		rp.setHeader("cache-control", "no-cache");
		PrintWriter out = rp.getWriter();
		StringBuffer lstResult = new StringBuffer();
		String ttct = "";
		// if (Formater.isNull(rq.getParameter("id")))
		danhSachNhomQuyen = roleDao.getAllRoot();
		// else
		// danhSachNhomQuyen = RighImpl.getParentRighList(rq.getParameter("id"));
		String currentId = Formater.isNull(rq.getParameter("id")) ? "" : rq.getParameter("id");
		for (SysRoles pr : danhSachNhomQuyen) {
			if (!pr.getId().equals(currentId))
				ttct = ttct + "<option value='" + pr.getId() + "' >" + pr.getCode() + "</option>";
		}
		lstResult.append("<option value=''></option>" + ttct);
		out.println(lstResult.toString());
		out.flush();
		out.close();
	}

	public void treeRight(ModelMap map, HttpServletRequest rq, HttpServletResponse rp, SysRolesForm form)
			throws Exception {
		rp.setContentType("text/plain;charset=utf-8");
		PrintWriter pw = rp.getWriter();
		JSONArray array = null;
		array = roleDao.getTreeObjectsData();
		pw.print(array);
		pw.close();
	}

	public ArrayList<SysRoles> getDanhSachNhomQuyen() {
		return danhSachNhomQuyen;
	}

	public void setDanhSachNhomQuyen(ArrayList<SysRoles> danhSachNhomQuyen) {
		this.danhSachNhomQuyen = danhSachNhomQuyen;
	}

}