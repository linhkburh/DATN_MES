package cic.h2h.controller.bisAdmin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.ReduceByAmountDao;
import cic.h2h.form.ReduceByAmountForm;
import common.util.FormatNumber;
import common.util.Formater;
import entity.ReduceByAmount;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;

@Controller
@RequestMapping(value = "/reduceByAmount")
public class ReduceByAmountController extends CatalogController<ReduceByAmountForm, ReduceByAmount> {

	@Autowired
	private ReduceByAmountDao reduceByAmountDao;

	@Override
	public void createSearchDAO(HttpServletRequest request, ReduceByAmountForm form, BaseDao<ReduceByAmount> dao)
			throws Exception {
		if (!Formater.isNull(form.getKeyWord())) {
			dao.addRestriction(Restrictions.or(
					Restrictions.like("description", form.getKeyWord().trim(), MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("code", form.getKeyWord().trim(), MatchMode.ANYWHERE).ignoreCase()));
		}
	}

	@Override
	protected void pushToJa(JSONArray ja, ReduceByAmount e, ReduceByAmountForm modelForm) throws Exception {
		ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + e.getId() + "\")'>" + e.getCode() + "</a>");
		if (e.getAmountFrom() != null)
			ja.put(Formater.num2str(e.getAmountFrom()));
		else
			ja.put("");

		if (e.getAmountTo() != null)
			ja.put(Formater.num2str(e.getAmountTo()));
		else
			ja.put("");

		if (e.getAmountReduce() != null)
			ja.put(FormatNumber.num2Str(e.getAmountReduce()));
		else
			ja.put("");
		ja.put(e.getDescription());
	}

	@Override
	public BaseDao<ReduceByAmount> getDao() {
		
		return reduceByAmountDao;
	}

	@Override
	public String getJsp() {
		
		return "quan_tri_nghiep_vu/giam_gia_don_hang_theo_so_luong_chi_tiet";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ReduceByAmountForm form)
			throws Exception {
		

	}

}
