package cic.h2h.controller.bisAdmin;

import java.util.Date;

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

import com.ibm.icu.util.Calendar;

import cic.h2h.dao.hibernate.MaterialTypeDao;
import cic.h2h.form.MaterialForm;
import cic.h2h.form.MaterialTypeForm;
import common.util.FormatNumber;
import common.util.Formater;
import entity.MaterialType;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;

@Controller
@RequestMapping(value = "/materialType")
public class MaterialTypeController extends CatalogController<MaterialTypeForm, MaterialType> {

	private static Logger log = Logger.getLogger(MaterialTypeController.class);

	@Autowired
	private MaterialTypeDao materialTypeDao;

	@Override
	public void createSearchDAO(HttpServletRequest request, MaterialTypeForm form, BaseDao<MaterialType> dao) throws Exception {
		if (!Formater.isNull(form.getCodeSearch())) {
			dao.addRestriction(Restrictions.like("code", form.getCodeSearch(), MatchMode.ANYWHERE).ignoreCase());
		}
		if (!Formater.isNull(form.getNameSearch())) {
			dao.addRestriction(Restrictions.like("name", form.getNameSearch(), MatchMode.ANYWHERE).ignoreCase());
		}
	}

	@Override
	protected void pushToJa(JSONArray ja, MaterialType e, MaterialTypeForm modelForm) throws Exception {
		ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + e.getId() + "\")'>" + e.getCode() + "</a>");
		ja.put(e.getName());
		ja.put(e.getInitPriceKStr());
		ja.put(e.getInitPriceTBStr());
		ja.put(e.getInitPriceDStr());
		ja.put(e.getDescription());
	}

	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, MaterialTypeForm form)
			throws Exception {
		form.getMaterialType().setCreateDate(Calendar.getInstance().getTime());
		super.save(model, rq, rs, form);
	}

	@Override
	public BaseDao<MaterialType> getDao() {
		
		return materialTypeDao;
	}

	@Override
	public String getJsp() {
		
		return "quan_tri_nghiep_vu/loai_vat_lieu";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, MaterialTypeForm form)
			throws Exception {
		

	}

}
