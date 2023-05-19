package cic.h2h.controller.mes;

import java.math.BigDecimal;

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

import com.ibm.icu.util.Calendar;

import cic.h2h.dao.hibernate.CustomerManageDao;
import cic.h2h.dao.hibernate.ProcessExeDao;
import cic.h2h.dao.hibernate.QuotationItemProcessDao;
import cic.h2h.form.QuotationItemProcessForm;
import common.util.FormatNumber;
import common.util.Formater;
import common.util.ResourceException;
import entity.Customer;
import entity.ProcessExe;
import entity.QuotationItemProcess;
import entity.frwk.SysUsers;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysUsersDao;

@Controller
@RequestMapping(value = "/itemProcess")
public class QuotationItemProcessController extends CatalogController<QuotationItemProcessForm, QuotationItemProcess> {

	private static Logger log = Logger.getLogger(QuotationItemProcessController.class);

	@Autowired
	private QuotationItemProcessDao quotationItemProcessDao;

	@Override
	public void createSearchDAO(HttpServletRequest request, QuotationItemProcessForm form,
			BaseDao<QuotationItemProcess> dao) throws Exception {
		dao.addRestriction(Restrictions.eq("company", getSessionUser().getCompany()));
		if (!Formater.isNull(form.getFrDate()))
			dao.addRestriction(Restrictions.ge("createDate", Formater.str2date(form.getFrDate())));
		if (!Formater.isNull(form.gettDate())) {
			Calendar t = Calendar.getInstance();
			t.setTime(Formater.str2date(form.gettDate()));
			t.add(Calendar.DATE, 1);
			dao.addRestriction(Restrictions.lt("createDate", t.getTime()));
		}
		if (!Formater.isNull(form.getUser()))
			dao.addRestriction(Restrictions.eq("creator.id", form.getUser()));
		if (!Formater.isNull(form.getType()))
			dao.addRestriction(Restrictions.eq("type", form.getType()));

		// Qi
		boolean qiAlias = false;
		if (!Formater.isNull(form.getQuotationItemCode()) || !Formater.isNull(form.getQuotationItemName())
				|| !Formater.isNull(form.getManagerCode())) {
			dao.createAlias("quotationItem", "qi");
			qiAlias = true;
			if (!Formater.isNull(form.getQuotationItemCode()))
				dao.addRestriction(Restrictions.eq("qi.code", form.getQuotationItemCode().trim()).ignoreCase());
			if (!Formater.isNull(form.getQuotationItemName()))
				dao.addRestriction(Restrictions.eq("qi.name", form.getQuotationItemName().trim()).ignoreCase());
			if (!Formater.isNull(form.getManagerCode()))
				dao.addRestriction(Restrictions.eq("qi.manageCode", form.getManagerCode().trim()).ignoreCase());
		}

		if ("RP".equals(form.getTo())) {
			if (!qiAlias) {
				dao.createAlias("quotationItem", "qi");
				qiAlias = true;
			}
			// So luong qc chuyen ve lon hon so luong sx da sua
			dao.addRestriction(Restrictions.and(Restrictions.eq("type", "QC"),Restrictions.gtProperty("qi.qcRepaire", "qi.repairingAmount")));
		} else {
			if (form.getStatus() != null) {
				String quatityProp = "CL".equals(form.getType()) ? "qi.polishingQuatity" : "qi.qcQuatity";
				String doneProp = "CL".equals(form.getType()) ? "qi.polishingDone" : "qi.qcDone";
				// Da hoan thanh
				if (form.getStatus() == 1)
					dao.addRestriction(Restrictions.eqProperty(doneProp, quatityProp));
				else
					dao.addRestriction(Restrictions.neProperty(doneProp, quatityProp));
			}
		}

		// q
		if (!Formater.isNull(form.getOrderCode()) || !Formater.isNull(form.getCusCode())) {
			if (!qiAlias)
				dao.createAlias("quotationItem", "qi");
			qiAlias = true;
			dao.createAlias("qi.quotationId", "q");
			if (!Formater.isNull(form.getOrderCode())) {
				dao.addRestriction(
						Restrictions.like("q.code", form.getOrderCode().trim(), MatchMode.ANYWHERE).ignoreCase());
			}
			if (!Formater.isNull(form.getCusCode())) {
				if (!Formater.isNull(form.getCusCode()))
					dao.addRestriction(Restrictions.eq("q.customer.id", form.getCusCode().trim()));
			}
		}
		dao.addOrder(Order.desc("createDate"));
	}

	@Override
	protected void pushToJa(JSONArray ja, QuotationItemProcess e, QuotationItemProcessForm modelForm) throws Exception {
		ja.put(e.getQuotationItem().getQuotationId().getCode());
		ja.put("<font title='" + e.getQuotationItem().getQuotationId().getCustomer().getOrgName() + "'>"
				+ e.getQuotationItem().getQuotationId().getCustomer().getCode() + "</font>");
		ja.put(e.getQuotationItem().getCode());
		if (!Formater.isNull(e.getQuotationItem().getManageCode())) {
			if ("RP".equals(modelForm.getTo())) {
				ja.put("<a style='color:blue;' title='Sửa hàng' href='javascipt:;' onclick = 'itemProcess(\""
						+ modelForm.getTo() + "\",\"" + e.getQuotationItem().getManageCode() + "\")'>"
						+ e.getQuotationItem().getManageCode() + "</a>");
			} else {
				if ("CL".equals(e.getType())) {
					if (e.getQuotationItem().getPolishingQuatity() == e.getQuotationItem().getPolishingDone()) {
						ja.put(e.getQuotationItem().getManageCode());
					} else {
						ja.put("<a style='color:blue;' title='Thực hiện nguội' href='javascipt:;' onclick = 'itemProcess(\""
								+ e.getType() + "\",\"" + e.getQuotationItem().getManageCode() + "\")'>"
								+ e.getQuotationItem().getManageCode() + "</a>");
					}
				} else {
					if (e.getQuotationItem().getQcQuatity() == e.getQuotationItem().getQcDone()) {
						ja.put(e.getQuotationItem().getManageCode());
					} else {
						ja.put("<a style='color:blue;' title='Thực hiện QC' href='javascipt:;' onclick = 'itemProcess(\""
								+ e.getType() + "\",\"" + e.getQuotationItem().getManageCode() + "\")'>"
								+ e.getQuotationItem().getManageCode() + "</a>");
					}
				}
			}
		} else {
			ja.put("");
		}
		if ("RP".equals(modelForm.getTo())) {
			if (e.getQuotationItem().getQcRepaire() != null) {
				ja.put(Formater.num2str(e.getQuotationItem().getQcRepaire()));
			} else
				ja.put("");
			if (e.getQuotationItem().getRepairedAmount() != null) {
				ja.put(Formater.num2str(e.getQuotationItem().getRepairedAmount()));
			} else
				ja.put("");
		} else {
			// So luong
			if (e.getQuality() != null) {
				ja.put(Formater.num2str(e.getQuality()));
			} else
				ja.put("");
			// Tong
			Long quatity = "CL".equals(e.getType()) ? e.getQuotationItem().getPolishingQuatity()
					: e.getQuotationItem().getQcQuatity();
			ja.put(Formater.num2str(quatity));
			// Tong done
			Long done = "CL".equals(e.getType()) ? e.getQuotationItem().getPolishingDone()
					: e.getQuotationItem().getQcDone();
			ja.put(Formater.num2str(done));
			// Con lai
			Long remain = quatity - done;
			ja.put(Formater.num2str(remain));
		}
		ja.put(e.getCreator().getName());
		ja.put(Formater.date2ddsmmsyyyspHHmmss(e.getCreateDate()));
		if (!"RP".equals(modelForm.getTo()))
			ja.put("<a style='color:blue;' href='javascipt:;' onclick = 'deleteQIP(\"" + e.getId() + "\",\""
					+ FormatNumber.num2Str(e.getQuality()) + "\")'>Xóa</a>");
	}

	@Autowired
	private ProcessExeDao processExeDao;

	public void addProcessExe(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			QuotationItemProcessForm form) throws Exception {
		String processItemId = rq.getParameter("processItemId");
		QuotationItemProcess itemProcess = quotationItemProcessDao.get(processItemId);
		if (itemProcess == null)
			throw new ResourceException("Không tồn tại chi tiết chuyển");
		Long totalRemain = 0l;
		Long totalDone = 0l;
		ProcessExe processExe = new ProcessExe();
		if ("QC".equalsIgnoreCase(rq.getParameter("type"))) {
			String rpAmount = rq.getParameter("rpAmount");
			processExe.setNgAmount(new BigDecimal(rpAmount));
			totalRemain = itemProcess.getQuotationItem().getQcQuatity();
			totalDone = itemProcess.getQuotationItem().getQcDone();
		} else {
			processExe.setNgAmount(new BigDecimal("0"));
			totalRemain = itemProcess.getQuotationItem().getPolishingQuatity();
			totalDone = itemProcess.getQuotationItem().getPolishingDone();
		}

		String startTime = rq.getParameter("startTime");
		processExe.setStartTimeStr(startTime);
		String endTime = rq.getParameter("endTime");
		processExe.setEndTimeStr(endTime);
		String totalAmount = rq.getParameter("totalAmount");
		processExe.setTotalAmountStr(totalAmount);
		String ngAmount = rq.getParameter("ngAmount");
		processExe.setNgAmountStr(ngAmount);
		String ngDes = rq.getParameter("ngDes");
		processExe.setNgDescription(ngDes);
		if (processExe.getStartTime().after(processExe.getEndTime()))
			throw new ResourceException("Thời điểm kết thúc phải sau thời điểm bắt đầu!");
		String amount = rq.getParameter("amount");
		processExe.setAmountStr(amount);
		processExe.setType(rq.getParameter("type"));
		if ((processExe.getTotalAmount().doubleValue() - processExe.getAmount().doubleValue()
				- processExe.getNgAmount().doubleValue()) < 0)
			throw new ResourceException("Số lượng hoàn thành phải nhỏ hơn hoặc bằng tổng số!");
		String userid = rq.getParameter("userid");
		SysUsers worker = sysUsersDao.getUser(userid);
		processExe.setSysUser(worker);
		processExe.setUpdator(getSessionUser());
		processExe.setExeTime(
				new BigDecimal((processExe.getEndTime().getTime() - processExe.getStartTime().getTime()) / 1000 / 60));
		processExe.setQuotationItem(itemProcess.getQuotationItem());
		processExe.setCreateDate(Calendar.getInstance().getTime());
		totalRemain = totalRemain - totalDone;
		if (totalRemain < processExe.getTotalAmount().longValue())
			throw new ResourceException("Mã quản lý %s, số lượng chuyển %s, vượt quá số lượng còn lại %s",
					new Object[] { itemProcess.getQuotationItem().getManageCode(),
							FormatNumber.num2Str(processExe.getTotalAmount()), FormatNumber.num2Str(totalRemain) });
		// save process exe
		processExeDao.save(processExe);
	}

	@Override
	public BaseDao<QuotationItemProcess> getDao() {

		return quotationItemProcessDao;
	}

	@Override
	public String getJsp() {

		return "ke_hoach_san_xuat/lich_su_chuyen_nguoi_qc";
	}

	@Autowired
	private SysUsersDao sysUsersDao;
	@Autowired
	private CustomerManageDao customerManageDao;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QuotationItemProcessForm form)
			throws Exception {
		model.addAttribute("lstSysUser", sysUsersDao.getCompanyUser(getSessionUser().getCompany()));
		model.addAttribute("customers", customerManageDao.getByType(Customer.IS_CUSTOMER));

	}

	@Override
	public void del(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QuotationItemProcessForm form)
			throws Exception {
		// Load thong tin trong db ra de thuc hien xoa (do client submit moi id nen kg du thong tin)
		quotationItemProcessDao.load(form.getQuotationItemProcess());
		super.del(model, rq, rs, form);
	}
}
