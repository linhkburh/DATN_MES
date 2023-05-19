package frwk.controller.sys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import common.util.Formater;
import entity.frwk.SysObjects;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysObjectDao;
import frwk.form.SysObjectForm;

@Controller
@RequestMapping("/right")
public class SysObjectController extends CatalogController<SysObjectForm, SysObjects> {
	static Logger lg = Logger.getLogger(SysObjectController.class);
	@Autowired
	private SysObjectDao sysObjectDao;

	@Override
	public BaseDao<SysObjects> getDao() {
		return sysObjectDao;
	}
	@Override
	public void createSearchDAO(HttpServletRequest request, SysObjectForm form,BaseDao<SysObjects> dao) throws Exception {
		if (!Formater.isNull(form.getKeyWord()))
			dao.addRestriction(Restrictions.or(
					Restrictions.or(Restrictions.like("objectId", form.getKeyWord(), MatchMode.ANYWHERE).ignoreCase(),
							Restrictions.like("name", form.getKeyWord(), MatchMode.ANYWHERE).ignoreCase()),
					Restrictions.like("action", form.getKeyWord(), MatchMode.ANYWHERE).ignoreCase()));
		dao.addOrder(Order.asc("objectId"));
	}

	@Override
	public void pushToJa(JSONArray ja, SysObjects o1, SysObjectForm modal) throws Exception {
		ja.put("<a href = '#' onclick = 'edit(\"" + o1.getId() + "\")'>" + o1.getObjectId() + "</a>");
		ja.put(o1.getName());
		ja.put(o1.getAction());
		if(o1.getSysObjects()!=null)
			ja.put(o1.getSysObjects().getName());
		else ja.put("");
		

	}
	
	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysObjectForm form)
			throws Exception {
		SysObjectForm sysObjectForm = (SysObjectForm) form;
		// Neu client submit len gia tri cha rong
		if(Formater.isNull(sysObjectForm.getSysObj().getSysObjects().getId())) 
			sysObjectForm.getSysObj().setSysObjects(null);
		sysObjectDao.save(sysObjectForm.getSysObj());
	}
	
	@Override
	public void del(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysObjectForm form) throws Exception {
		SysObjectForm sysObjectForm = (SysObjectForm) form;
		//check quyen dang duoc su dung la quyen cha
		sysObjectDao.checkUse(sysObjectForm.getSysObj().getId());
		
		//ckeck quyen dang duoc tich chon o tree role
		sysObjectDao.checkUseTreeRole(sysObjectForm.getSysObj().getId());
		super.del(model, rq, rs, form);
	}
	
	@Override
	public String getJsp() {
		return "qtht/danh_muc_quyen";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysObjectForm form)
			throws Exception {
		model.addAttribute("danhSachQuyen", sysObjectDao.getTreeRight());

	}
}