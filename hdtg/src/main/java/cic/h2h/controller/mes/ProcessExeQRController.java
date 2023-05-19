package cic.h2h.controller.mes;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.ProcessExeDao;
import cic.h2h.dao.hibernate.QuotationItemDao;
import cic.h2h.dao.hibernate.QuotationItemProcessDao;
import cic.h2h.form.ProcessExeForm;

import common.util.Formater;
import common.util.ResourceException;

import entity.ProcessExe;
import entity.QuotationItem;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysUsersDao;

@Controller
@RequestMapping(value = "/processExeQR")
public class ProcessExeQRController extends frwk.controller.SearchController<ProcessExeForm, ProcessExe> {

	@Autowired
	private ProcessExeDao processExeDao;

	@Override
	public void createSearchDAO(HttpServletRequest request, ProcessExeForm form, BaseDao<ProcessExe> dao)
			throws Exception {

	}

	@Override
	protected void pushToJa(JSONArray ja, ProcessExe e, ProcessExeForm modelForm) throws Exception {

	}

	@Override
	public BaseDao<ProcessExe> getDao() {

		return processExeDao;
	}

	public void addProcessExe(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ProcessExeForm form)
			throws Exception {
		ProcessExe pe = form.getProcessExe();
		QuotationItem quotationItem = quotationItemDao.getByManageCode(form.getManageCode(),
				getSessionUser().getCompany());
		pe.setQuotationItem(quotationItem);
		if (pe.getStartTime().after(Calendar.getInstance().getTime()))
			throw new ResourceException("Thời gian bắt đầu kg được sau thời điểm hiện tại!");
		if (pe.getNgAmount() != null && pe.getNgAmount().compareTo(new BigDecimal(0)) < 0)
			throw new ResourceException("Số lượng lỗi (NG) %s phải >= 0!", pe.getNgAmountStr());
		if (pe.getBrokenAmount() != null && pe.getBrokenAmount() < 0)
			throw new ResourceException("Số lượng hủy %s phải >= 0!", pe.getBrokenAmountStr());
		// Truong hop sua, revert so luong con lai
		pe.setExeTime(new BigDecimal((pe.getEndTime().getTime() - pe.getStartTime().getTime()) / 1000 / 60));
		if (pe.getExeTime().compareTo(new BigDecimal(0)) <= 0)
			throw new ResourceException("Thời gian bắt đầu phải trước thời gian kết thúc!");
		pe.setUpdator(getSessionUser());
		pe.setCreateDate(Calendar.getInstance().getTime());
		processExeDao.save(pe);
	}

	@Override
	public String getJsp() {
		return "ke_hoach_san_xuat/thuc_hien_nguoi_qc";
	}

	@Autowired
	private SysUsersDao sysUsersDao;

	@Autowired
	private QuotationItemProcessDao quotationItemProcessDao;

	@Autowired
	private QuotationItemDao quotationItemDao;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ProcessExeForm form)
			throws Exception {
		if (Formater.isNull(form.getManageCode()))
			throw new ResourceException("Nhập mã quản lý");
		QuotationItem quotationItem = quotationItemDao.getByManageCode(form.getManageCode(),
				getSessionUser().getCompany());
		if (quotationItem == null)
			throw new ResourceException("Không tồn tại mã quản lý");
		form.getProcessExe().setQuotationItem(quotationItem);
		form.getProcessExe().setType(form.getType());
		form.getProcessExe().sumary();
		Long remain = 0l;
		if ("QC".equals(form.getType())) {
			remain = quotationItem.getQcQuatity() - quotationItem.getQcDone();
		} else {
			remain = quotationItem.getPolishingQuatity() - quotationItem.getPolishingDone();
		}
		if (remain <= 0)
			throw new ResourceException("Mã quản lý không tồn tại chi tiết cần thực hiện !");
		model.addAttribute("lstSysUser", sysUsersDao.getCompanyUser(getSessionUser().getCompany()));
	}

}
