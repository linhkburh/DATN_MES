package frwk.controller.sys;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import common.util.Formater;
import entity.Company;
import entity.Department;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.CompanyDao;
import frwk.dao.hibernate.sys.DepartmentDao;
import frwk.form.DepartmentForm;

@Controller
@RequestMapping(value = "/department")
public class DepartmentController extends CatalogController<DepartmentForm, Department> {

	private static Logger log = Logger.getLogger(DepartmentController.class);

	@Autowired
	private DepartmentDao departmentDao;

	@Autowired
	private CompanyDao companyDao;

	@Override
	public void createSearchDAO(HttpServletRequest request, DepartmentForm form,BaseDao<Department> dao) throws Exception {
		dao.createAlias("company", "c");
		if(!Formater.isNull(form.getNameSearch()))
			dao.addRestriction(
					Restrictions.like("name", form.getNameSearch().trim(), MatchMode.ANYWHERE).ignoreCase());
		if(!Formater.isNull(form.getCodeSearch()))
			dao.addRestriction(
					Restrictions.like("code", form.getCodeSearch().trim(), MatchMode.ANYWHERE).ignoreCase());
		if(!Formater.isNull(form.getCompanyId()))
			dao.addRestriction(
					Restrictions.like("c.id", form.getCompanyId().trim(), MatchMode.ANYWHERE).ignoreCase());
	}

	@Override
	protected void pushToJa(JSONArray ja, Department e, DepartmentForm modelForm) throws Exception {
		ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + e.getId() + "\")'>" + e.getCode() + "</a>");
		ja.put(e.getName());
		ja.put(e.getSortName());
		if (e.getCompany() != null) {
			ja.put(e.getCompany().getAddress());
		} else {
			ja.put("");
		}
		ja.put(e.getPhoneNumber());
		if (e.getCompany() != null) {
			ja.put(e.getCompany().getName());
		} else {
			ja.put("");
		}
	}

	@Override
	public BaseDao<Department> getDao() {
		
		return departmentDao;
	}

	@Override
	public String getJsp() {
		
		return "qtht/department";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, DepartmentForm form)
			throws Exception {
		model.addAttribute("companies", companyDao.getAll());
	}

}
