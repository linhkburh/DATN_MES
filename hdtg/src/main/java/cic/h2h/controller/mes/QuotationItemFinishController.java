package cic.h2h.controller.mes;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

import cic.h2h.dao.hibernate.CustomerManageDao;
import cic.h2h.dao.hibernate.PackageManageDao;
import cic.h2h.dao.hibernate.QcOsDao;
import cic.h2h.dao.hibernate.QuotationItemDao;
import cic.h2h.dao.hibernate.QuotationItemProcessDao;
import cic.h2h.form.QuotationItemFinishForm;
import common.constants.Constants;
import common.util.FormatNumber;
import common.util.Formater;
import common.util.JsonUtils;
import common.util.ResourceException;
import entity.Customer;
import entity.DLVPackage;
import entity.DLVPackageDetail;
import entity.QcOs;
import entity.QcOsDetail;
import entity.QuotationItem;
import entity.QuotationItemProcess;
import frwk.controller.SearchController;
import frwk.dao.hibernate.BaseDao;

@Controller
@RequestMapping(value = "/proExport")
public class QuotationItemFinishController extends SearchController<QuotationItemFinishForm, QuotationItem> {

	@Override
	public void createSearchDAO(HttpServletRequest request, QuotationItemFinishForm form, BaseDao<QuotationItem> dao)
			throws Exception {
		// Tim du lieu cua cong ty
		dao.addRestriction(Restrictions.eq("company", getSessionUser().getCompany()));
		// Ma ban ve
		if (!Formater.isNull(form.getDrawingCode())) {
			Criterion cCode = null;
			String[] temp = form.getDrawingCode().split(";");
			for (String code : temp) {
				if (cCode == null)
					cCode = Restrictions.eq("code", code.trim());
				else
					cCode = Restrictions.or(cCode, Restrictions.eq("code", code.trim()));
			}
			dao.addRestriction(cCode);
		}
		// Ma quan ly
		if (!Formater.isNull(form.getManageCode())) {
			Criterion cCode = null;
			String[] temp = form.getManageCode().split(";");
			for (String code : temp) {
				if (cCode == null)
					cCode = Restrictions.or(Restrictions.eq("manageCode", code.trim()),
							Restrictions.eq("rootManageCode", code.trim()));
				else
					cCode = Restrictions.or(cCode, Restrictions.or(Restrictions.eq("manageCode", code.trim()),
							Restrictions.eq("rootManageCode", code.trim())));
			}
			dao.addRestriction(cCode);
		}
		if (!Formater.isNull(form.getDrawingName()))
			dao.addRestriction(
					Restrictions.like("name", form.getDrawingName().trim(), MatchMode.ANYWHERE).ignoreCase());
		// Ngay giao
		if (!Formater.isNull(form.getFromDate()))
			dao.addRestriction(Restrictions.ge("deliverDate", Formater.str2date(form.getFromDate())));
		if (!Formater.isNull(form.getToDate())) {
			Calendar t = Calendar.getInstance();
			t.setTime(Formater.str2date(form.getToDate()));
			t.add(Calendar.DATE, 1);
			dao.addRestriction(Restrictions.lt("deliverDate", t.getTime()));
		}
		// Xuat hang, so luong da xuat
		if (Formater.isNull(form.getType())) {
			dao.addRestriction(Restrictions.sqlRestriction(
					"((select nvl(sum(d.amount),0) from DLV_PACKAGE_detail d where d.quotation_item_id={alias}.id) < {alias}.quality)"));
		} else {
			// Da co chi tiet ket thuc
			dao.addRestriction(Restrictions.isNotNull("nmOfFnsItem"));
			// So luong da chuyen nho hon so luong hoan thanh
			if ("QC".equals(form.getType()))
				dao.addRestriction(Restrictions.ltProperty("qcQuatity", "nmOfFnsItem"));
			else if ("CL".equals(form.getType()))
				dao.addRestriction(Restrictions.ltProperty("polishingQuatity", "nmOfFnsItem"));
			else if ("OS".equals(form.getType())) {
				// Chuyen gia cong ngoai thanh pham
				dao.addRestriction(Restrictions.gt("qcQuatity", 0l));
				dao.addRestriction(Restrictions.gtProperty("qcQuatity", "totalToOs"));
			}

		}

		// Quotation
		if (!Formater.isNull(form.getOrderCode()) || !Formater.isNull(form.getCusCode())) {
			dao.createAlias("quotationId", "q");
			if (!Formater.isNull(form.getOrderCode()))
				dao.addRestriction(
						Restrictions.like("q.code", form.getOrderCode().trim(), MatchMode.ANYWHERE).ignoreCase());
			if (!Formater.isNull(form.getCusCode()))
				dao.addRestriction(Restrictions.eq("q.customer.id", form.getCusCode()));

		}
		if (!Formater.isNull(form.getFrDate()))
			dao.addRestriction(Restrictions.ge("createDate", Formater.str2date(form.getFrDate())));
		if (!Formater.isNull(form.gettDate())) {
			Calendar t = Calendar.getInstance();
			t.setTime(Formater.str2date(form.gettDate()));
			t.add(Calendar.DATE, 1);
			dao.addRestriction(Restrictions.lt("createDate", t.getTime()));
		}
		dao.addOrder(Order.desc("createDate"));
	}

	@Override
	protected void pushToJa(JSONArray ja, QuotationItem e, QuotationItemFinishForm modelForm) throws Exception {
		ja.put("<span class='characterwrap'>" + e.getQuotationId().getCode() + "</span>");
		ja.put(String.format(Constants.WRAP_CHAR_SPAN_HTML, e.getQuotationId().getCustomer().getCode()));
		ja.put(e.getQuotationId().getCustomer().getOrgName());
		ja.put(String.format("<font class='characterwrap' title='Tên chi tiết: %s'>%s<font>",
				new Object[] { e.getName(), e.getCode() }));
		ja.put(e.getDeliverDateStr());
		ja.put(String.format(Constants.WRAP_CHAR_SPAN_HTML, e.getManageCode()));
		Double numOfFinish = e.getNumOfFinishItem().doubleValue();
		// so luong
		if ("OS".equals(modelForm.getType())) {
			if (numOfFinish == null || numOfFinish < e.getQuality().doubleValue())
				ja.put(String.format("<font color='red'>%s</font>",
						e.getNumOfFinishItemStr() + "/" + Formater.num2str(e.getQuality())));
			else
				ja.put(e.getNumOfFinishItemStr() + "/" + Formater.num2str(e.getQuality()));
		} else {
			if (numOfFinish == null || numOfFinish < e.getQuality().doubleValue())
				ja.put(String.format("<font color='red'>%s</font>",
						e.getNumOfFinishItemStr() + "/" + Formater.num2str(e.getQuality())));
			else
				ja.put(e.getNumOfFinishItemStr() + "/" + Formater.num2str(e.getQuality()));
		}
		String outStandingAmount;
		if ("OS".equals(modelForm.getType())) {
			// Chuyen nguoi/QC can chuyen theo so luong hoan thanh
			Long numOfDelivered = e.getTotalToOs();
			ja.put(FormatNumber.num2Str(numOfDelivered) + "/" + Formater.num2str(e.getQcQuatity()));
			// outStandingAmount =
			// Formater.num2str(e.getQuality().subtract(numOfDelivered));
			outStandingAmount = Formater.num2str(e.getQcQuatity() - numOfDelivered);
		} else {
			if (Formater.isNull(modelForm.getType())) {
				Long numOfDelivered = quotationItemDao.getNumOfDelivered(e);
				ja.put(Formater.num2str(numOfDelivered));
				outStandingAmount = Formater.num2str(e.getQuality().subtract(new BigDecimal(numOfDelivered)));
			} else {
				// Chuyen nguoi/QC can chuyen theo so luong hoan thanh
				Long numOfDelivered = "QC".equals(modelForm.getType()) ? e.getQcQuatity()
						: "OS".equals(modelForm.getType()) ? e.getTotalToOs() : e.getPolishingQuatity();
				ja.put(Formater.num2str(numOfDelivered));
				// outStandingAmount =
				// Formater.num2str(e.getQuality().subtract(numOfDelivered));
				outStandingAmount = Formater.num2str(numOfFinish - numOfDelivered);
			}
		}

		ja.put("<input disabled='true' type='text' style='text-align: right;' class='textint' name='" + e.getId()
				+ "'/>");
		ja.put("<input type='checkbox' name='chk_" + e.getId()
				+ "' onclick='changeNumberFinish(this);' outStandingAmount='" + outStandingAmount + "' value='"
				+ e.getId() + "' class='selectRow'/>");
	}

	@Override
	public BaseDao<QuotationItem> getDao() {
		return quotationItemDao;
	}

	@Override
	public String getJsp() {
		return "ke_hoach_san_xuat/thuc_hien_xuat_hang";
	}

	@Autowired
	private CustomerManageDao customerManageDao;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QuotationItemFinishForm form)
			throws Exception {
		model.addAttribute("lstCus", customerManageDao.getByType(Customer.IS_PARTNER));
		model.addAttribute("customers", customerManageDao.getByType(Customer.IS_CUSTOMER));
	}

	@Autowired
	private PackageManageDao packageManageDao;

	/**
	 * Uu tien dong goi so luong lon truoc
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @param form
	 * @throws Exception
	 */
	public void save(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			QuotationItemFinishForm form) throws Exception {
		if ("OS".equals(form.getType()))
			saveQcOs(form);
		else
			savePackage(form);
	}

	@Autowired
	private QcOsDao qcOsDao;

	private void saveQcOs(QuotationItemFinishForm form) throws Exception {
		for (QcOs os : form.getLstQcOs()) {
			String[] arrId = os.getIds().split(",");
			String[] amounts = os.getAmounts().split(",");
			for (int i = 0; i < arrId.length; i++) {
				QcOsDetail detail = new QcOsDetail(quotationItemDao.get(arrId[i]), os, Long.valueOf(amounts[i]));
				if (os.getQuotationItem() == null)
					os.setQuotationItem(detail.getQuotationItem());
			}
			os.setCreateDate(Calendar.getInstance().getTime());
			os.setSysUsers(getSessionUser());
			qcOsDao.save(os);
		}
	}

	private void savePackage(QuotationItemFinishForm form) throws Exception {
		// Thoi diem tao cho tat ca cac goi dong cung nhau, can giong nhau
		Date createDate = Calendar.getInstance().getTime();
		for (DLVPackage pck : form.getLstPackage()) {
			String[] arrId = pck.getIds().split(",");
			String[] amounts = pck.getAmounts().split(",");
			Long[] arrAmount = new Long[amounts.length];
			for (int i = 0; i < amounts.length; i++) {
				long qiAmout = FormatNumber.str2Long(amounts[i]);
				arrAmount[i] = qiAmout;
			}
			if (arrAmount.length == 0)
				return;
			// So luong bang so luong tren 1 goi
			if (pck.getNumPerPackage() == pck.getQuality()) {
				for (int i = 0; i < arrId.length; i++) {
					DLVPackageDetail detail = new DLVPackageDetail(quotationItemDao.get(arrId[i]), arrAmount[i]);
					if (i == 0) {
						pck = new DLVPackage(getSessionUser(), detail, createDate, 1);
					} else {
						pck.add(detail);
					}
				}
				packageManageDao.save(pck);
				continue;
			}
			int iPckSeq = 0;
			List<QuotationItem> lstPojoQi = new ArrayList<QuotationItem>();
			// Cac ma quan ly tu chia, du lai phan le chia sau
			for (int i = 0; i < arrId.length; i++) {
				while (arrAmount[i] >= pck.getNumPerPackage()) {
					DLVPackageDetail detail = new DLVPackageDetail(quotationItemDao.get(arrId[i]),
							pck.getNumPerPackage());
					packageManageDao.save(new DLVPackage(getSessionUser(), detail, createDate, ++iPckSeq));
					arrAmount[i] -= pck.getNumPerPackage();
				}
				if (arrAmount[i] > 0) {
					QuotationItem qi = quotationItemDao.getPoJo(arrId[i]);
					lstPojoQi.add(qi);
					qi.setQuality(new BigDecimal(arrAmount[i]));
				}
			}
			if (lstPojoQi.size() == 0)
				return;
			while (true) {
				// Xac dinh so luong qi can gom, so luong con du cua qi cuoi cung
				long totalQiAmount = 0;
				int iNumOfQi = 0;
				long lastQiOutStanding = 0;
				long lastQiAmount = 0;
				for (int i = 0; i < lstPojoQi.size(); i++) {
					totalQiAmount += lstPojoQi.get(i).getQuality().longValue();
					iNumOfQi++;
					if (totalQiAmount >= pck.getNumPerPackage()) {
						lastQiOutStanding = totalQiAmount - pck.getNumPerPackage();
						// So chi tiet duoc lay ma quan ly cuoi
						lastQiAmount = lstPojoQi.get(i).getQuality().longValue() - lastQiOutStanding;
						// Cap nhat so luong con lai cua ma quan ly cuoi
						lstPojoQi.get(i).setQuality(new BigDecimal(lastQiOutStanding));
						break;
					}
				}
				// Tao package tren qi0
				QuotationItem qi0InDB = quotationItemDao.get(lstPojoQi.get(0).getId());
				DLVPackageDetail detail0 = new DLVPackageDetail(qi0InDB, lstPojoQi.get(0).getQuality().longValue());
				DLVPackage ms = new DLVPackage(getSessionUser(), detail0, createDate, ++iPckSeq);
				// Add vao master cac qi tu 1
				for (int i = 1; i < iNumOfQi; i++) {
					QuotationItem qiInDb = quotationItemDao.get(lstPojoQi.get(i).getId());
					long qiAmount = i < iNumOfQi - 1 ? lstPojoQi.get(i).getQuality().longValue()
							: lastQiAmount > 0 ? lastQiAmount : lstPojoQi.get(i).getQuality().longValue();
					DLVPackageDetail detail = new DLVPackageDetail(qiInDb, qiAmount);
					ms.add(detail);

				}
				packageManageDao.save(ms);
				if (totalQiAmount < pck.getNumPerPackage())
					return;
				// Remove or update
				for (int i = 0; i < iNumOfQi - 1; i++)
					lstPojoQi.remove(0);
				if (lastQiOutStanding == 0)
					lstPojoQi.remove(0);
				if (lstPojoQi.size() == 0)
					return;
			}
		}

	}

	public void packageByBin(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			QuotationItemFinishForm form) throws Exception {
		for (DLVPackage pck : form.getLstPackage()) {
			String[] arrId = pck.getIds().split(",");
			String[] amounts = pck.getAmounts().split(",");
			Long[] arrAmount = new Long[amounts.length];
			long totalAmount = 0;
			for (int i = 0; i < amounts.length; i++) {
				long qiAmout = FormatNumber.str2Long(amounts[i]);
				arrAmount[i] = qiAmout;
				totalAmount += qiAmout;
			}

			// So luong goi
			if (totalAmount < pck.getNumOfPackage()) {
				QuotationItem qi0 = quotationItemDao.get(arrId[0]);
				throw new ResourceException("Mã chi tiết: %s, số lượng túi: %s lớn hơn số lượng chi tiết: %s !",
						new Object[] { qi0.getCode(), pck.getNumOfPackage(), totalAmount });
			}

			if (arrAmount.length == 0)
				return;
			// Chon 1 goi
			if (pck.getNumOfPackage().equals("1")) {
				for (int i = 0; i < arrId.length; i++) {
					DLVPackageDetail detail = new DLVPackageDetail(quotationItemDao.get(arrId[i]), arrAmount[i]);
					if (i == 0) {
						pck = new DLVPackage(getSessionUser(), detail);
					} else {
						pck.add(detail);
					}
				}
				packageManageDao.save(pck);
				continue;
			}
			// So chi tiet toi thieu trong 1 goi
			long iMinPckAmout = (pck.getQuality() / pck.getNumOfPackage());
			long iNumOfMaxAmout = (pck.getQuality() % pck.getNumOfPackage());
			List<QuotationItem> lstQi = new ArrayList<QuotationItem>();
			// Cac ma quan ly tu chia truoc, bot lai so du, so qi co the giam bot neu chia
			// het
			for (int i = 0; i < arrId.length; i++) {
				while (arrAmount[i] >= iMinPckAmout) {
					DLVPackageDetail detail = new DLVPackageDetail(quotationItemDao.get(arrId[i]));
					if (arrAmount[i] > iMinPckAmout) {
						if (iNumOfMaxAmout > 0) {
							iNumOfMaxAmout--;
							detail.setAmount(iMinPckAmout + 1);
						} else {
							detail.setAmount(iMinPckAmout);
						}
					} else {
						detail.setAmount(iMinPckAmout);
					}
					packageManageDao.save(new DLVPackage(getSessionUser(), detail));
					arrAmount[i] -= detail.getAmount();
				}
				if (arrAmount[i] > 0) {
					QuotationItem qi = quotationItemDao.getPoJo(arrId[i]);
					lstQi.add(qi);
					qi.setQuality(new BigDecimal(arrAmount[i]));
				}
			}
			if (lstQi.size() == 0)
				return;
			long lastQiOutStanding = -1l;
			while (true) {
				// Sap xep lai khi so ma quan ly gom bi le
				if (lastQiOutStanding != 0) {
					Collections.sort(lstQi, new Comparator<QuotationItem>() {
						@Override
						public int compare(QuotationItem before, QuotationItem after) {
							return (-1) * before.getQuality().compareTo(after.getQuality());
						}
					});
				}
				// Xac dinh so luong qi can gom, so item trong oi
				long totalQiAmount = 0;
				int iNumOfQi = 0;
				long pckAmount = 0;
				for (int i = 0; i < lstQi.size(); i++) {
					totalQiAmount += lstQi.get(i).getQuality().longValue();
					iNumOfQi++;
					if (iNumOfMaxAmout > 0) {
						if (totalQiAmount > iMinPckAmout) {
							pckAmount = iMinPckAmout + 1;
							lastQiOutStanding = totalQiAmount - pckAmount;
							break;
						}
					} else if (totalQiAmount >= iMinPckAmout) {
						pckAmount = iMinPckAmout;
						lastQiOutStanding = totalQiAmount - pckAmount;
						break;
					}
				}

				DLVPackage ms = null;
				for (int i = 0; i < iNumOfQi; i++) {
					Long qiAmout = null;
					if (i == iNumOfQi - 1) {
						qiAmout = lstQi.get(i).getQuality().longValue() - lastQiOutStanding;
						lstQi.get(i).setQuality(BigDecimal.valueOf(lastQiOutStanding));
					} else {
						qiAmout = lstQi.get(i).getQuality().longValue();
					}
					DLVPackageDetail detail = new DLVPackageDetail(quotationItemDao.get(lstQi.get(i).getId()), qiAmout);
					if (i == 0)
						ms = new DLVPackage(getSessionUser(), detail);
					else
						ms.add(detail);

				}
				ms.setQuality(pckAmount);
				packageManageDao.save(ms);
				// Remove or update amout
				for (int i = 0; i < iNumOfQi - 1; i++)
					lstQi.remove(0);
				if (lastQiOutStanding <= 0)
					lstQi.remove(0);
				if (lstQi.size() == 0)
					break;
			}
		}

	}

	public void preview(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			QuotationItemFinishForm form) throws ResourceException, ParseException, IOException {
		String tmp = request.getParameter("lstId");
		String lstData = request.getParameter("lstData");
		if (Formater.isNull(tmp) || Formater.isNull(lstData))
			throw new ResourceException("Chưa chọn chi tiết");
		String ids[] = tmp.split("[_]");
		String datas[] = lstData.split("[_]");
		List<DLVPackage> lstPackage = makeLstPackage(ids, datas);
		ObjectMapper om = JsonUtils.objectMapper2();
		om.setAnnotationIntrospector(new CustomIntrospector());
		returnJson(response, lstPackage, om);
	}

	private static Logger lg = Logger.getLogger(QuotationItemFileController.class);

	private static class CustomIntrospector extends JacksonAnnotationIntrospector {
		@Override
		public boolean hasIgnoreMarker(final AnnotatedMember m) {
			if (super.hasIgnoreMarker(m))
				return true;
			try {
				if (DLVPackage.class.getDeclaredField("lstPackDetail").equals(m.getAnnotated()))
					return true;
			} catch (Exception e) {
				lg.error(e.getMessage(), e);
			}
			return false;
		}
	}

	@Autowired
	private QuotationItemProcessDao quotationItemProcessDao;

	/**
	 * Chuyen nguoi/Qc
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @param form
	 * @throws Exception
	 */
	public void next(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			QuotationItemFinishForm form) throws Exception {
		String tmp = request.getParameter("lstId");
		String lstData = request.getParameter("lstData");
		if (Formater.isNull(tmp) || Formater.isNull(lstData))
			throw new ResourceException("Chưa chọn chi tiết");
		String ids[] = tmp.split("[_]");
		String datas[] = lstData.split("[_]");
		for (int i = 0; i < ids.length; i++) {
			QuotationItemProcess qip = new QuotationItemProcess(quotationItemDao.get(ids[i]),
					FormatNumber.str2num(datas[i]), form.getType(), getSessionUser());
			BigDecimal numOfDelivered = quotationItemProcessDao.getNextAmount(qip.getQuotationItem(), qip.getType());
			BigDecimal numOfFinish = qip.getQuotationItem().getNumOfFinishItem();
			Double outOutStandingAmount = numOfFinish.doubleValue() - numOfDelivered.doubleValue();
			if (outOutStandingAmount < qip.getQuality().doubleValue())
				throw new ResourceException("Mã quản lý: %s, số lượng chuyển: %s, lớn hơn số lượng còn lại: %s!",
						new String[] { qip.getQuotationItem().getManageCode(), qip.getQualityStr(),
								Formater.num2str(outOutStandingAmount) });
			quotationItemProcessDao.save(qip);
		}
	}

	private List<DLVPackage> makeLstPackage(String ids[], String datas[]) throws ParseException, ResourceException {
		List<DLVPackage> lstPackage = new ArrayList<DLVPackage>();
		for (int i = 0; i < ids.length; i++) {
			DLVPackageDetail detail = new DLVPackageDetail(quotationItemDao.get(ids[i]),
					FormatNumber.str2Long(datas[i]));
			if (detail.getAmount() <= 0)
				throw new ResourceException("Mã quản lý %s, số lượng <=0", detail.getQuotationItem().getManageCode());
			Long numOfDelivered;
			// Dong goi
			if (Formater.isNull(getModelForm().getType())) {
				numOfDelivered = quotationItemDao.getNumOfDelivered(detail.getQuotationItem());
				BigDecimal outStandingAmount = detail.getQuotationItem().getQuality()
						.subtract(new BigDecimal(numOfDelivered));
				if (outStandingAmount.compareTo(new BigDecimal(detail.getAmount())) < 0)
					throw new ResourceException("Mã quản lý %s, số lượng đóng gói %s lớn số lượng còn lại %s",
							new Object[] { detail.getQuotationItem().getManageCode(),
									Formater.num2str(detail.getAmount()), FormatNumber.num2Str(outStandingAmount) });
			} else if ("OS".equals(getModelForm().getType())) {
				// Chuyen gia cong ngoai (so luong tai QC >= So luong chuyen + so luong da chuyen
				Long osOutStanding = detail.getQuotationItem().getQcQuatity()
						- detail.getQuotationItem().getTotalToOs();
				if (osOutStanding < detail.getAmount()) {
					throw new ResourceException(
							"Mã quản lý %s, số lượng chưa chuyển gia công ngoài %s, nhỏ hơn số lượng chuyển %s",
							new Object[] { detail.getQuotationItem().getManageCode(),
									FormatNumber.num2Str(osOutStanding), FormatNumber.num2Str(detail.getAmount()) });
				}

			} else {
				// Chuyen gia cong ngoai
				numOfDelivered = "QC".equals(getModelForm().getType()) ? detail.getQuotationItem().getQcQuatity()
						: "OS".equals(getModelForm().getType()) ? detail.getQuotationItem().getTotalToOs()
								: detail.getQuotationItem().getPolishingQuatity();
				long numOfFinish = detail.getQuotationItem().getNumOfFinishItem().longValue();
				long outStandingAmount = numOfFinish - numOfDelivered;
				if (outStandingAmount <= 0)
					throw new ResourceException("Mã quản lý %s, số lượng đóng gói %s lớn số lượng còn lại %s",
							new Object[] { detail.getQuotationItem().getManageCode(),
									Formater.num2str(detail.getAmount()), FormatNumber.num2Str(outStandingAmount) });
			}
			DLVPackage addPackage = null;
			// Tim kiem goi cu de bo sung
			for (DLVPackage pck : lstPackage) {
				// Cung ma ban ve
				if (!pck.getQuotationItemCode().equals(detail.getQuotationItem().getCode()))
					continue;
				// Cung khach hang
				if (!pck.getCustomer().getId().equals(detail.getQuotationItem().getQuotationId().getCustomer().getId()))
					continue;
				pck.add(detail);
				addPackage = pck;
				break;
			}
			// Tao goi moi
			if (addPackage == null)
				lstPackage.add(new DLVPackage(getSessionUser(), detail));
		}
		return lstPackage;
	}

	public void getAllQuotationItemFinish(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			QuotationItemFinishForm form) throws Exception {
		String tmp = request.getParameter("lstId");
		String lstData = request.getParameter("lstData");
		if (Formater.isNull(tmp) || Formater.isNull(lstData))
			throw new ResourceException("Chưa chọn chi tiết");
		String ids[] = tmp.split("[_]");
		String datas[] = lstData.split("[_]");
		DLVPackage pck = new DLVPackage(getSessionUser());
		Long totalQuality = 0L;
		for (int i = 0; i < ids.length; i++) {
			DLVPackageDetail detail = new DLVPackageDetail();
			detail.setDlvPackage(pck);
			detail.setQuotationItem(quotationItemDao.get(ids[i]));
			if (pck.getCustomer() == null) {
				pck.setCustomer(detail.getQuotationItem().getQuotationId().getCustomer());
				pck.setCode(detail.getQuotationItem().getCode() + "_" + sdf.format(new Date()));
			} else {
				if (!pck.getCustomer().getId().equals(detail.getQuotationItem().getQuotationId().getCustomer().getId()))
					throw new ResourceException("Mã quản lý %s không cùng khách hàng với mã quản lý trước đó: %s",
							new Object[] { detail.getQuotationItem().getManageCode(), pck.getCustomer().getCode() });
				if (!detail.getQuotationItem().getCode().equals(pck.getQuotationItemCode()))
					throw new ResourceException("Tồn tại 2 mã bản vẽ trong gói %s và %s",
							new Object[] { pck.getQuotationItemCode(), detail.getQuotationItem().getCode() });
			}
			// So luong
			detail.setAmount(Long.valueOf(datas[i]));
			pck.setQuality(pck.getQuality().longValue() + detail.getAmount().longValue());
			// So luong da xuat
			Long numOfDelivered = quotationItemDao.getNumOfDelivered(detail.getQuotationItem());
			if (detail.getAmount().longValue() > detail.getQuotationItem().getQuality().longValue() - numOfDelivered)
				throw new ResourceException("Số lượng xuất của mã quản lý %s là %s, phải nhỏ hơn số lượng sẵn sàng %s",
						new Object[] { detail.getQuotationItem().getManageCode(), detail.getAmount(),
								detail.getQuotationItem().getQuality().longValue() - numOfDelivered });
			totalQuality = totalQuality + Long.valueOf(datas[i]);
			pck.getLstPackDetail().add(detail);
		}
		packageManageDao.save(pck);
	}

//	private void validateQuotationItemFinish(List<QuotationItem> finishs, Map<String, String> mapValue)
//			throws Exception {
//		// check cus
//		Map<String, Long> mapCus = new HashMap<String, Long>();
//		Map<String, Long> mapCode = new HashMap<String, Long>();
//		for (QuotationItem item : finishs) {
//			if (mapCus.containsKey(item.getQuotationId().getCustomer().getId())) {
//				mapCus.put(item.getQuotationId().getCustomer().getId(),
//						mapCus.get(item.getQuotationId().getCustomer().getId()) + 1L);
//			} else {
//				mapCus.put(item.getQuotationId().getCustomer().getId(), 1L);
//			}
//			if (mapCode.containsKey(item.getCode())) {
//				mapCode.put(item.getCode(), mapCus.get(item.getCode()) + 1L);
//			} else {
//				mapCode.put(item.getCode(), 1L);
//			}
//			Long numberRemaining = item.getQuality() - item.getNumOfDelivered();
//			Long numberDelivery = Long.valueOf(mapValue.get(item.getId()));
//			if (numberRemaining < numberDelivery) {
//				throw new ResourceException(
//						"Số lượng chi tiết đòng gói vượt quá số lượng chi tiết còn lại với mã quản lý : "
//								+ item.getManageCode());
//			}
//
//		}
//		if (mapCus.size() > 1)
//			throw new ResourceException("Không thể tồn tại 2 khách hàng trong cùng 1 gói");
//		if (mapCode.size() > 1)
//			throw new ResourceException("Không thể tồn tại 2 mã bản vẻ trong cùng 1 gói");
//	}

	@Autowired
	private QuotationItemDao quotationItemDao;

	/*
	 * @Autowired private PackageManageForm formPackage;
	 */

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmm");

	public DLVPackage setPackage(List<QuotationItem> finishs, Map<String, String> mapValue) throws Exception {
		DLVPackage dlvPackage = new DLVPackage();
		dlvPackage.setCreateDate(Calendar.getInstance().getTime());
		dlvPackage.setCreator(getSessionUser());
		dlvPackage.setCustomer(finishs.get(0).getQuotationId().getCustomer());
		dlvPackage.setCode(finishs.get(0).getCode() + "_" + sdf.format(new Date()));
		List<DLVPackageDetail> details = new ArrayList<DLVPackageDetail>();
		Long totalQuality = 0L;

		for (QuotationItem item : finishs) {
			DLVPackageDetail detail = new DLVPackageDetail();
			detail.setDlvPackage(dlvPackage);
			detail.setAmount(Long.valueOf(mapValue.get(item.getId())));
			detail.setQuotationItem(
					quotationItemDao.getByManageCode(item.getManageCode(), getSessionUser().getCompany()));
			totalQuality += Long.valueOf(mapValue.get(item.getId()));
			details.add(detail);
		}
		dlvPackage.setLstPackDetail(details);
		dlvPackage.setQuality(totalQuality);
		return dlvPackage;
	}
}
