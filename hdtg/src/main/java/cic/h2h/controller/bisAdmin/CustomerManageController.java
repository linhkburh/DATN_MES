package cic.h2h.controller.bisAdmin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.CustomerManageDao;
import cic.h2h.form.CustomerManageForm;
import common.util.Formater;
import entity.Customer;
import entity.frwk.SysDictParam;
import frwk.constants.RightConstants;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysDictParamDao;

@Controller
@RequestMapping("/customerMng")
public class CustomerManageController extends CatalogController<CustomerManageForm, Customer>{
	@Autowired
	private CustomerManageDao customerManageDao;
	@Override
	public void createSearchDAO(HttpServletRequest request, CustomerManageForm form, BaseDao<Customer> dao)
			throws Exception {
		if (!Formater.isNull(form.getCuCode()))
			dao.addRestriction(
					Restrictions.like("code", form.getCuCode().trim(), MatchMode.ANYWHERE).ignoreCase());
		if (!Formater.isNull(form.getCuName()))
			dao.addRestriction(
					Restrictions.like("representerName", form.getCuName().trim(), MatchMode.ANYWHERE).ignoreCase());
		if (!Formater.isNull(form.getOrgName()))
			dao.addRestriction(
					Restrictions.like("orgName", form.getOrgName().trim(), MatchMode.ANYWHERE).ignoreCase());
		if (!Formater.isNull(form.getEmail()))
			dao.addRestriction(
					Restrictions.like("email", form.getEmail().trim(), MatchMode.ANYWHERE).ignoreCase());
		if (Boolean.TRUE.equals(form.getPartner())) {
			dao.addRestriction(Restrictions.eq("isPartner", Boolean.TRUE));
		}
		if (Boolean.TRUE.equals(form.getCus())) {
			dao.addRestriction(Restrictions.eq("isCustomer", Boolean.TRUE));
		}
	}
	
	@Override
	protected void pushToJa(JSONArray ja, Customer e, CustomerManageForm modelForm) throws Exception {
		if ("select".equals(modelForm.getTo())) {
			ja.put("<a href = '#' onclick = 'select(\"" + e.getId() + "\")'>Chọn</a>");
		}
		if(!Formater.isNull(e.getCode()))
			ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\""+e.getId()+"\")'>"+e.getCode()+"</a>");
		else
			ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\""+e.getId()+"\")'>"+"Cập nhật"+"</a>");
		if(!Formater.isNull(e.getOrgName()))
			ja.put(e.getOrgName());
		else
			ja.put("");
		if(e.getNationality()!=null)
			ja.put(e.getNationality().getValue());
		else
			ja.put("");
		if(!Formater.isNull(e.getRepresenterName()))
			ja.put(e.getRepresenterName());
		else
			ja.put("");
		if(!Formater.isNull(Formater.date2str(e.getDateOfEstalishment())))
			ja.put(Formater.date2str(e.getDateOfEstalishment()));
		else
			ja.put("");
		if(!Formater.isNull(e.getEmail()))
			ja.put(e.getEmail());
		else
			ja.put("");
	}

	@Override
	public BaseDao<Customer> getDao() {
		return customerManageDao;
	}

	@Override
	public String getJsp() {
		return "qtht/quan_ly_khach_hang";
	}
	@Autowired
	private SysDictParamDao sysDictParamDao;
	@SuppressWarnings("unused")
	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, CustomerManageForm form)
			throws Exception {
		List<SysDictParam> dsNationality = sysDictParamDao.getByType(RightConstants.CAT_TYPE_NATIONALITY);
		model.addAttribute("dsNationality", dsNationality);
	}
}
