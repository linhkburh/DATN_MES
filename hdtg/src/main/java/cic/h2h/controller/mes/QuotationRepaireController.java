package cic.h2h.controller.mes;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.AstMachineDao;
import cic.h2h.dao.hibernate.QuotationItemDao;
import cic.h2h.dao.hibernate.QuotationRepaireDao;
import cic.h2h.form.QuotationRepaireForm;
import common.util.FormatNumber;
import common.util.Formater;
import common.util.ResourceException;
import entity.AstMachine;
import entity.QuotationItem;
import entity.QuotationRepaire;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysUsersDao;

@Controller
@RequestMapping(value = "/quotationRepaireExe")
public class QuotationRepaireController
		extends frwk.controller.SearchController<QuotationRepaireForm, QuotationRepaire> {

	@Autowired
	private QuotationRepaireDao quotationRepaireDao;

	@Override
	public void createSearchDAO(HttpServletRequest request, QuotationRepaireForm form, BaseDao<QuotationRepaire> dao)
			throws Exception {

	}

	@Override
	protected void pushToJa(JSONArray ja, QuotationRepaire e, QuotationRepaireForm modelForm) throws Exception {

	}

	@Override
	public BaseDao<QuotationRepaire> getDao() {
		return quotationRepaireDao;
	}

	@Override
	public String getJsp() {
		return "ke_hoach_san_xuat/thuc_hien_sua_hang";
	}

	@Autowired
	private SysUsersDao sysUsersDao;
	@Autowired
	private AstMachineDao astMachineDao;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QuotationRepaireForm form)
			throws Exception {
		if (Formater.isNull(form.getManageCode()))
			throw new ResourceException("Hãy nhập thông tin mã quản lý!");
		QuotationItem quotationItem = quotationItemDao.getByManageCode(form.getManageCode(),
				getSessionUser().getCompany());
		if (quotationItem == null)
			throw new ResourceException("Không tồn tại mã quản lý!");
		else {
			if (quotationItem.getQcRepaire() <= quotationItem.getRepairedAmount())
				throw new ResourceException("Không tồn tại chi tiết cần sửa!");
		}
		form.getModel().setQuotationItem(quotationItem);

		model.addAttribute("lstSysUser", sysUsersDao.getCompanyUser(getSessionUser().getCompany()));
		model.addAttribute("lstAstMachine", astMachineDao.getAll(getSessionUser().getCompany()));
	}

	@Autowired
	private QuotationItemDao quotationItemDao;

	public void addQuotationRepaire(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			QuotationRepaireForm form) throws Exception {
		if (form.getQuotationRepaire().getTotalAmount().compareTo(new BigDecimal(0)) <= 0)
			throw new ResourceException("Số lượng phải lớn hơn 0!");
		QuotationItem quotationItem = quotationItemDao.getByManageCode(form.getManageCode(),
				getSessionUser().getCompany());
		if (quotationItem == null)
			throw new ResourceException("Không tồn tại mã quản lý!");
		long outStandingAmount = quotationItem.getQcRepaire();
		if (quotationItem.getRepairingAmount() != null)
			outStandingAmount -= quotationItem.getRepairingAmount();
		if (!Formater.isNull(form.getQuotationRepaire().getId())) {
			// Truong hop sua thong tin, revert so luong da sa
			QuotationRepaire oldInDb = quotationRepaireDao.getPoJo(form.getQuotationRepaire().getId());
			outStandingAmount += oldInDb.getTotalAmount().longValue();
		}
		if (outStandingAmount <= 0)
			throw new ResourceException("Không tồn tại chi tiết cần sửa!");
		if (outStandingAmount < form.getQuotationRepaire().getTotalAmount().longValue())
			throw new ResourceException("Số lượng chi tiết còn lại %s nhỏ hơn số lượng chi tiết sửa %s!",
					new Object[] { FormatNumber.num2Str(outStandingAmount),
							FormatNumber.num2Str(form.getQuotationRepaire().getTotalAmount()) });
		if (form.getQuotationRepaire().getNgAmount() < 0)
			throw new ResourceException("Số lượng chi tiết hoàn thành %s lớn hơn tổng số lượng chi tiết %s!",
					new Object[] { FormatNumber.num2Str(form.getQuotationRepaire().getAmount()),
							FormatNumber.num2Str(form.getQuotationRepaire().getTotalAmount()) });

		form.getQuotationRepaire().setQuotationItem(quotationItem);
		form.getQuotationRepaire().setExeTime((form.getQuotationRepaire().getEndTime().getTime()
				- form.getQuotationRepaire().getStartTime().getTime()) / 1000 / 60);
		if(form.getQuotationRepaire().getExeTime()<=0)
			throw new ResourceException("Thời điểm kết thúc phải sau thời điểm bắt đầu!");
		form.getQuotationRepaire().setUpdator(getSessionUser());
		quotationRepaireDao.save(form.getQuotationRepaire());
	}

}
