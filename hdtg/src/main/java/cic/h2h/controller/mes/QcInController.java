package cic.h2h.controller.mes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.hibernate.proxy.HibernateProxy;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ibm.icu.util.Calendar;

import cic.h2h.dao.hibernate.CustomerManageDao;
import cic.h2h.dao.hibernate.QcInDao;
import cic.h2h.dao.hibernate.QcInDetailDao;
import cic.h2h.dao.hibernate.QcOsDao;
import cic.h2h.dao.hibernate.QcOsDetailDao;
import cic.h2h.form.QcInForm;
import common.util.Formater;
import common.util.JsonUtils;
import common.util.ResourceException;
import entity.Customer;
import entity.QcChkOutSrcDetail;
import entity.QcIn;
import entity.QcInDetail;
import entity.QcOs;
import entity.QcOsDetail;
import entity.QuotationItem;
import entity.QuotationItemExe;
import entity.WorkOrder;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysUsersDao;

@Controller
@RequestMapping(value = "/qcIn")
public class QcInController extends CatalogController<QcInForm, QcIn> {
	private static final Logger logger = Logger.getLogger(QcInController.class);
	@Autowired
	private QcInDao qcInDao;

	@Override
	public void createSearchDAO(HttpServletRequest request, QcInForm form, BaseDao<QcIn> dao) throws Exception {

	}

	@Override
	protected void pushToJa(JSONArray ja, QcIn e, QcInForm modelForm) throws Exception {

	}

	@Override
	public BaseDao<QcIn> getDao() {
		return qcInDao;
	}

	@Override
	public String getJsp() {
		return "ke_hoach_san_xuat/nhan_hang_gcn";
	}

	@Autowired
	private SysUsersDao sysUsersDao;
	@Autowired
	private QcOsDao qcOsDao;
	@Autowired
	private CustomerManageDao customerManageDao;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QcInForm form)
			throws Exception {
		if (Formater.isNull(form.getIdQcOs()))
			throw new ResourceException("Không tồn tại hàng nhận!");
		if (!Formater.isNull(form.getIdQcIn())) {
			QcIn qcIn = qcInDao.get(form.getIdQcIn());
			form.setQcIn(qcIn);
		}
		QcOs qcOs = qcOsDao.get(form.getIdQcOs());
		if (qcOs == null)
			throw new ResourceException("Không tồn tại hàng chuyển!");
		form.getModel().setQcOs(qcOs);
		form.getModel().setQuotationItem(qcOs.getQuotationItem());
		if (qcOs.getWorkOrder() != null)
			form.getModel().setWorkOrder(qcOs.getWorkOrder());
		form.getModel().setCustomers(qcOs.getCustomers());
		logger.info(JsonUtils.writeToString(qcOs.getCustomers()));
		model.addAttribute("lstQcOsDetail", qcOs.getQcOsDetails());
		// Performace
		List<QcOsDetail> osDetail = new ArrayList<QcOsDetail>();
		for (QcOsDetail d : qcOs.getQcOsDetails()) {
			QcOsDetail d1 = new QcOsDetail();
			d1.setAmount(d.getAmount());
			d1.setId(d.getId());
			osDetail.add(d1);
		}

		model.addAttribute("lstQcOsDetailStr", StringEscapeUtils.escapeEcmaScript(JsonUtils.writeToString(osDetail)));

		model.addAttribute("lstSysUser", sysUsersDao.getCompanyUser(getSessionUser().getCompany()));
		model.addAttribute("lstPartner", customerManageDao.getByType(Customer.IS_PARTNER));
	}

	@Autowired
	private QcInDetailDao qcInDetailDao;

	public void addQcIn(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QcInForm form) throws Exception {
		if (form.getQcIn().getTotalAmount() <= 0)
			throw new ResourceException("Số lượng nhận phải lớn hơn 0!");
		QcIn qcIn = form.getModel();

		// Truong hop sua
		if (!Formater.isNull(qcIn.getId())) {
			QcIn in1 = new QcIn();
			in1.setId(qcIn.getId());
			qcInDao.load(in1);
			qcInDao.getCurrentSession().delete(in1);
			qcInDao.flush();
			qcIn.setId(null);
		}

		qcOsDao.load(qcIn.getQcOs());
		// validate
		if (qcIn.getTotalAmount() + qcIn.getQcOs().getPartnerDone() > qcIn.getQcOs().getAmount()) {
			throw new ResourceException("Số lượng chi tiết nhận %s vượt quá số lượng còn lại %s !", new Object[] {
					qcIn.getTotalAmount(), qcIn.getQcOs().getAmount() - qcIn.getQcOs().getPartnerDone() });
		}

		// Chuan hoa thong tin: Set cac truong an total (truong hop co grid), sinh dong detail mac dinh (th khong grid)
		// => day du thong tin
		// Ban thanh pham hoac khong gom
		if (qcIn.getQcOs().getQcOsDetails().size() == 1
				|| qcIn.getQcOs().getQcOsDetails().get(0).getWorkOrder() != null) {
			qcIn.getQcInDetails().add(new QcInDetail(qcIn));
		} else {
			if (qcIn.getQcInDetails().size() == 0)
				throw new ResourceException("Chưa nhập danh sách nhận hàng!");
			for (QcInDetail d : qcIn.getQcInDetails()) {
				qcIn.setAmount(qcIn.getAmount() + (d.getAmount() == null ? 0l : d.getAmount()));
				qcIn.setBrokenAmount(qcIn.getBrokenAmount() + (d.getBrokenAmount() == null ? 0l : d.getBrokenAmount()));
				qcIn.setNgAmount(qcIn.getNgAmount() + (d.getNgAmount() == null ? 0l : d.getNgAmount()));
			}
		}
		// Thuc hien tren du lieu da chuan hoa
		long totalGridAmount = 0l;
		for (int i = 0; i < qcIn.getQcInDetails().size(); i++) {
			QcInDetail itemI = qcIn.getQcInDetails().get(i);
			Long totalIAmount = itemI.getAmount() == null ? 0l : itemI.getAmount();
			totalIAmount += itemI.getNgAmount() == null ? 0l : itemI.getNgAmount();
			totalIAmount += itemI.getBrokenAmount() == null ? 0l : itemI.getBrokenAmount();
			if (totalIAmount == 0)
				throw new ResourceException("Chưa nhập dữ liệu nhận hàng cho dòng " + (i + 1));
			if (itemI.getQcOsDetail().getAmount() - itemI.getQcOsDetail().getPartnerDoneAmount() < totalIAmount) {
				throw new ResourceException("Dòng " + (i + 1) + " tổng số Ok, NG, Hủy = " + totalIAmount
						+ " phải, <= số lượng được phép nhận "
						+ (itemI.getQcOsDetail().getAmount() - itemI.getQcOsDetail().getPartnerDoneAmount()));
			}
			if (qcOsDetailDao.getCurrentSession().contains(itemI.getQcOsDetail())) {
				qcOsDetailDao.load(itemI.getQcOsDetail());
				if (itemI.getQcOsDetail().getAmount() - itemI.getQcOsDetail().getPartnerDoneAmount() == 0)
					throw new ResourceException("Dòng %s, mã quản lý %s, đã nhập đủ số lượng!",
							new Object[] { i + 1, itemI.getQcOsDetail().getQuotationItem().getManageCode() });
				if (totalIAmount + itemI.getQcOsDetail().getPartnerDoneAmount() > itemI.getQcOsDetail().getAmount())
					throw new ResourceException(
							"Mã quản lý %s tổng số lượng đã nhận %s lớn hơn số lượng chuyển gia công ngoài %s!",
							new Object[] { itemI.getQcOsDetail().getQuotationItem().getManageCode(),
									totalIAmount + itemI.getQcOsDetail().getPartnerDoneAmount(),
									itemI.getQcOsDetail().getAmount() });
			}
			totalGridAmount += totalIAmount;
			// Thanh pham va co nhieu hon 1 ma quan ly
			if (itemI.getQcOsDetail().getWorkOrder() == null && qcIn.getQcOs().getQcOsDetails().size() > 1) {
				for (int j = i + 1; j < qcIn.getQcInDetails().size(); j++) {
					QcInDetail itemJ = qcIn.getQcInDetails().get(j);
					if (itemI.getQcOsDetail().getId().equals(itemJ.getQcOsDetail().getId())) {
						throw new ResourceException("Mã quản lý dòng %s, trùng với dòng %s!",
								new Object[] { j + 1, i + 1 });
					}
				}

			}
		}
		if (totalGridAmount != qcIn.getTotalAmount())
			throw new ResourceException("Tổng số Ok, NG, Hủy = %s phải bằng số lượng nhận %s!",
					new Object[] { totalGridAmount, qcIn.getTotalAmount() });

		form.getQcIn().setCreateDate(Calendar.getInstance().getTime());
		form.getQcIn().setSysUsers(getSessionUser());
		for (QcInDetail d : form.getQcIn().getQcInDetails())
			d.setQcIn(form.getQcIn());
		qcInDao.save(form.getQcIn());
	}

	@Autowired
	private QcOsDetailDao qcOsDetailDao;

}
