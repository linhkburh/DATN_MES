package cic.h2h.controller.mes.common;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.QuotationItemDao;
import cic.h2h.dao.hibernate.QuotationItemProcessDao;
import common.util.ResourceException;
import entity.QuotationItem;
import frwk.controller.CommonController;

@Controller
@RequestMapping(value = "/next")
public class NextController extends CommonController<NextForm, QuotationItem> {

	@Override
	public String getJsp() {
		return "dung_chung/chuyen_tiep";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, NextForm form)
			throws Exception {
		if ("CL".equals(form.getQuotationItemProcess().getType())) {
			form.setTitle("Chuyển nguội");
			form.setQrTitle("Scan mã quản lý");
		}

		else if ("QC".equals(form.getQuotationItemProcess().getType())) {
			form.setTitle("Chuyển QC");

			form.setQrTitle("Scan mã quản lý");
		}
	}

	@Autowired
	private QuotationItemProcessDao quotationItemProcessDao;
	@Autowired
	private QuotationItemDao quotationItemDao;

	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, NextForm form) throws Exception {
		QuotationItem qi = quotationItemDao.getByManageCode(form.getQuotationItem().getManageCode(), getSessionUser().getCompany());
		if (qi == null)
			throw new ResourceException("Không tồn tại mã quản lý " + form.getQuotationItem().getManageCode());
		form.setQuotationItem(qi);
		form.getQuotationItemProcess().setQuotationItem(qi);
		form.getQuotationItemProcess().setCreator(getSessionUser());
		form.getQuotationItemProcess().setCreateDate(Calendar.getInstance().getTime());
		quotationItemProcessDao.save(form.getQuotationItemProcess());
	}

}
