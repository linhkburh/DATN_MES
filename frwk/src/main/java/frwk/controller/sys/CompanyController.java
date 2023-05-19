package frwk.controller.sys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import common.util.FormatNumber;
import entity.AstMachine;
import entity.Company;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.CompanyDao;
import frwk.dao.hibernate.sys.SysUsersDao;
import frwk.form.CompanyForm;

@Controller
@RequestMapping(value = "/company")
public class CompanyController extends CatalogController<CompanyForm, Company> {

	private static Logger log = Logger.getLogger(CompanyController.class);

	@Autowired
	private CompanyDao companyDao;

	@Override
	public void createSearchDAO(HttpServletRequest request, CompanyForm form, BaseDao<Company> dao) throws Exception {
	}

	@Override
	protected void pushToJa(JSONArray ja, Company e, CompanyForm modelForm) throws Exception {
		
		ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + e.getId() + "\")'>" + e.getCode()
				+ "</a>");
		ja.put(e.getName());
		ja.put(e.getSortName());
		ja.put(e.getAddress());
		ja.put(e.getPhoneNumber());
	}

	@Override
	public BaseDao<Company> getDao() {
		
		return companyDao;
	}

	@Override
	public String getJsp() {
		
		return "qtht/company";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, CompanyForm form)
			throws Exception {
	}
	

}
