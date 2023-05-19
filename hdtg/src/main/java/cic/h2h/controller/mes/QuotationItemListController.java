package cic.h2h.controller.mes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.type.TypeReference;

import cic.h2h.dao.hibernate.CustomerManageDao;
import cic.h2h.dao.hibernate.QuotationItemDao;
import cic.h2h.form.QuotationItemForm;
import cic.utils.ExportExcel;
import common.util.FormatNumber;
import common.util.Formater;
import common.util.ResourceException;
import common.util.StreamUtils;
import common.util.XmlUtils;
import constants.Mes;
import entity.Customer;
import entity.ParentItem;
import entity.QuotationItem;
import entity.QuotationItemExe;
import entity.QuotationItemPackage;
import entity.frwk.SysDictParam;
import entity.frwk.SysUsers;
import frwk.constants.RightConstants;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.CompanyDao;
import frwk.dao.hibernate.sys.RightUtils;
import frwk.dao.hibernate.sys.SysDictParamDao;
import frwk.dao.hibernate.sys.SysUsersDao;
import integration.genhub.GenHub;
import integration.genhub.IFile;
import integration.genhub.SaveFormat;

@Controller
@RequestMapping(value = "/quanlyCT")
public class QuotationItemListController extends CatalogController<QuotationItemForm, QuotationItem> {

	private static final Logger log = Logger.getLogger(QuotationItemListController.class);

	@Override
	public void createSearchDAO(HttpServletRequest request, QuotationItemForm form, BaseDao<QuotationItem> dao)
			throws Exception {
		if (!Formater.isNull(form.getCode())) {
			Criterion cCode = null;
			String[] temp = form.getCode().split(";");
			for (String code : temp) {
				if (cCode == null)
					cCode = Restrictions.like("code", code.trim(), MatchMode.ANYWHERE).ignoreCase();
				else
					cCode = Restrictions.or(cCode,
							Restrictions.like("code", code.trim(), MatchMode.ANYWHERE).ignoreCase());
			}
			dao.addRestriction(cCode);
		}
		if (!Formater.isNull(form.getName()))
			dao.addRestriction(Restrictions.like("name", form.getName().trim(), MatchMode.ANYWHERE).ignoreCase());

		if (!Formater.isNull(form.getCompanyId()))
			dao.addRestriction(Restrictions.eq("company.id", form.getCompanyId()));
		SysUsers su = getSessionUser();
		if (Boolean.TRUE.equals(su.getIsPartner()) || !Formater.isNull(form.getOrderCode())
				|| !Formater.isNull(form.getCusCode())) {
			dao.createAlias("quotationId", "q");
			if (Boolean.TRUE.equals(su.getIsPartner()))
				dao.addRestriction(Restrictions.eq("q.customer.id", su.getPartner().getId()));
			if (!Formater.isNull(form.getOrderCode()))
				dao.addRestriction(
						Restrictions.like("q.code", form.getOrderCode().trim(), MatchMode.ANYWHERE).ignoreCase());
			if (!Formater.isNull(form.getCusCode()))
				dao.addRestriction(Restrictions.eq("q.customer.id", form.getCusCode()));
		}

		if (!Formater.isNull(form.getDeliverFromDate()))
			dao.addRestriction(Restrictions.ge("deliverDate", Formater.str2date(form.getDeliverFromDate())));
		if (!Formater.isNull(form.getDeliverToDate())) {
			Calendar t = Calendar.getInstance();
			t.setTime(Formater.str2date(form.getDeliverToDate()));
			t.add(Calendar.DATE, 1);
			dao.addRestriction(Restrictions.lt("deliverDate", t.getTime()));
		}
		if (!Formater.isNull(form.getCreateDateForm()))
			dao.addRestriction(Restrictions.ge("createDate", Formater.str2date(form.getCreateDateForm())));
		if (!Formater.isNull(form.getCreateDateTo())) {
			Calendar t = Calendar.getInstance();
			t.setTime(Formater.str2date(form.getCreateDateTo()));
			t.add(Calendar.DATE, 1);
			dao.addRestriction(Restrictions.lt("createDate", t.getTime()));
		}
		if (!Formater.isNull(form.getManageCodeSearch())) {
			Criterion mCode = null;
			String[] temp = form.getManageCodeSearch().split(";");
			for (String code : temp) {
				if (mCode == null)
					mCode = Restrictions.like("manageCode", code.trim()).ignoreCase();
				else
					mCode = Restrictions.or(mCode, Restrictions.like("manageCode", code.trim()).ignoreCase());
			}
			dao.addRestriction(mCode);
		}
		if (!Formater.isNull(form.getTechnical()))
			dao.addRestriction(Restrictions.eq("technicalId.id", form.getTechnical()));
		if (!Formater.isNull(form.getAccountManager()))
			dao.addRestriction(Restrictions.eq("acId.id", form.getAccountManager()));
		if (form.getStatus() != null) {
			// Chua thuc hien
			if (form.getStatus() == -1) {
				dao.addRestriction(Restrictions.sqlRestriction(
						"not exists (select 1 from quotation_item_exe qie, work_order wo, work_order_exe woe\r\n"
								+ "where qie.id = wo.quotation_item_exe_id and wo.id = woe.work_order_id \r\n"
								+ "and qie.quotation_item_id = {alias}.id)"));
			} else if (form.getStatus() == 0) {// Dang thuc hien
				dao.addRestriction(Restrictions.sqlRestriction(
						"exists (select 1 from quotation_item_exe qie, work_order wo, work_order_exe woe\r\n"
								+ "where qie.id = wo.quotation_item_exe_id and wo.id = woe.work_order_id \r\n"
								+ "and qie.quotation_item_id = {alias}.id)"));
				dao.addRestriction(Restrictions.neProperty("nmOfFnsItem", "quality"));
			} else if (form.getStatus() == 1) {// Da hoan thanh
				dao.addRestriction(Restrictions.eqProperty("nmOfFnsItem", "quality"));
			}
		}
		if (!Formater.isNull(form.getFromDate()))
			dao.addRestriction(Restrictions.ge("deliverDate", Formater.str2date(form.getFromDate())));
		if (!Formater.isNull(form.getToDate())) {
			Calendar t = Calendar.getInstance();
			t.setTime(Formater.str2date(form.getToDate()));
			t.add(Calendar.DATE, 1);
			dao.addRestriction(Restrictions.lt("deliverDate", t.getTime()));
		}

		if (!Formater.isNull(form.getCreator()))
			dao.addRestriction(Restrictions.eq("creator", form.getCreator()));
		if (Boolean.TRUE.equals(form.getRepaired())) {
			dao.addRestriction(Restrictions.ltProperty("repairedAmount", "qcRepaire"));
		}

		if (Boolean.TRUE.equals(form.getToPolishing())) {
			dao.addRestriction(Restrictions.gt("polishingQuatity", 0l));
		}
		if (Boolean.TRUE.equals(form.getToQc())) {
			dao.addRestriction(Restrictions.gt("qcQuatity", 0l));
		}
		dao.addOrder(Order.desc("createDate"));

	}

	@Autowired
	private QuotationItemDao quotationItemDao;

	@Override
	public BaseDao<QuotationItem> getDao() {
		return quotationItemDao;
	}

	@Override
	public String getJsp() {
		if (Formater.isNull(getModelForm().getQuotationItem().getId()))
			return "ke_hoach_san_xuat/quan_ly_chi_tiet_sx";
		else
			return "ke_hoach_san_xuat/chi_tiet";
	}

	@Autowired
	private SysUsersDao sysUsersDao;
	@Autowired
	private QuotationItemController item;
	@Autowired
	private CompanyDao companyDao;

	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QuotationItemForm form)
			throws Exception {
		item.save(model, rq, rs, form);
	}

	@Override
	public void del(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QuotationItemForm form)
			throws Exception {
		item.del(model, rq, rs, form);
	}

	@Autowired
	private CustomerManageDao customerManageDao;
	@Autowired
	private SysDictParamDao sysDictParamDao;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QuotationItemForm form)
			throws Exception {
		// Mo man hinh chi tiet
		if (!Formater.isNull(form.getQuotationItem().getId())) {
			item.initData(model, rq, rs, form);
			quotationItemDao.load(form.getQuotationItem());
			form.setQuotation(form.getQuotationItem().getQuotationId());
			for (QuotationItemExe exe : form.getQuotationItem().getQuotationItemAllExeList()) {
				if (Boolean.TRUE.equals(exe.getExeStepId().getProgram()))
					form.getQuotationItem().getQuotationItemProList().add(exe);
				else {
					form.getQuotationItem().getQuotationItemExeList().add(exe);
				}
			}
			Collections.sort(form.getQuotationItem().getQuotationItemExeList(), new QuotationItemExe.SortQie());
			Collections.sort(form.getQuotationItem().getQuotationItemProList(), new QuotationItemExe.SortQie());
			form.getQuotationItem().getStepPrice();
			List<SysDictParam> lstDrwngFile = sysDictParamDao.getByType("DOC");
			model.addAttribute("lstFile",
					StringEscapeUtils.escapeEcmaScript(ParentItem.objectMapper.writeValueAsString(lstDrwngFile)));
			List<SysDictParam> lstProFileType = sysDictParamDao.getByType("PRO");
			model.addAttribute("lstProFileType",
					StringEscapeUtils.escapeEcmaScript(ParentItem.objectMapper.writeValueAsString(lstProFileType)));
			return;
		} else {
			model.addAttribute("lstAc", sysUsersDao.getDepartmentUser(null, Mes.DEPT_CODE_AC));
			model.addAttribute("lstTech",
					sysUsersDao.getDepartmentUser(getSessionUser().getCompany(), Mes.DEPT_CODE_PRO));
			model.addAttribute("lstPlan",
					sysUsersDao.getDepartmentUser(getSessionUser().getCompany(), Mes.DEPT_CODE_PLAN));
		}
		SysUsers su = getSessionUser();
		if (Boolean.TRUE.equals(su.getIsPartner())) {
			form.setCusCode(su.getPartner().getId());
			model.addAttribute("lstCus", Arrays.asList(su.getPartner()));
			model.addAttribute("lstCompany", companyDao.getAllOrderAstName());
		} else {
			model.addAttribute("lstCus", customerManageDao.getByType(Customer.IS_CUSTOMER));
			if (rightUtils.haveRight(rq, RightConstants.VIEW_DATA_ALL)) {
				model.addAttribute("lstCompany", companyDao.getAllOrderAstName());
			} else {
				model.addAttribute("lstCompany", Arrays.asList(getSessionUser().getCompany()));
			}
			form.setCompanyId(su.getCompany().getId());
			model.addAttribute("lstSysUser", sysUsersDao.getCompanyUser(getSessionUser().getCompany()));
		}

		List<SysDictParam> lstDrwngFile = sysDictParamDao.getByType("DOC");
		model.addAttribute("lstFile",
				StringEscapeUtils.escapeEcmaScript(ParentItem.objectMapper.writeValueAsString(lstDrwngFile)));
		List<SysDictParam> lstProFileType = sysDictParamDao.getByType("PRO");
		model.addAttribute("lstProFileType",
				StringEscapeUtils.escapeEcmaScript(ParentItem.objectMapper.writeValueAsString(lstProFileType)));
	}

	@Autowired
	private RightUtils rightUtils;

	public QuotationItemDao getQuotationItemDao() {
		return quotationItemDao;
	}

	public void setQuotationItemDao(QuotationItemDao quotationItemDao) {
		this.quotationItemDao = quotationItemDao;
	}

	@Autowired
	private MessageSource messageSource;

	@Override
	protected void pushToJa(JSONArray ja, QuotationItem e, QuotationItemForm modelForm) throws Exception {
		if ("select".equals(modelForm.getTo())) {
			ja.put("<a title='Sử dụng' class='characterwrap' href='javascipt:;' onclick='window.opener.selectItem(\""
					+ e.getId() + "\");window.close();'>" + e.getCode() + "</a>");
		} else {
			if (e.getWorkSts() == ParentItem.WORK_STS_PENDDING) {
				boolean hasWorkOrder = false;
				for (QuotationItemExe qie : e.getQuotationItemAllExeList()) {
					if (Boolean.TRUE.equals(qie.getExeStepId().getProgram()))
						continue;
					if (!qie.getWorkOrders().isEmpty()) {
						hasWorkOrder = true;
					}
				}
				if (hasWorkOrder) {
					ja.put("<a title='Chi tiết lệnh sản xuất' class='characterwrap' href='javascipt:;' onclick='window.open(\"datlenhsx?hiddenMenu=true&makeWoByQi=true&quotationItem.id="
							+ e.getId() + "\",\"\",big_window_property)'>" + e.getCode() + "</a>");
				} else {
					ja.put("<a title='Chi tiết lệnh sản xuất' class='characterwrap' href='javascipt:;' onclick='window.open(\"datlenhsx?hiddenMenu=true&makeWoByQi=true&quotationItem.id="
							+ e.getId() + "\",\"\",big_window_property)'>"
							+ "<font color='red' title='Chưa tạo lệnh sản xuất'>" + e.getCode() + "<font>" + "</a>");
				}

			} else {
				ja.put("<a title='Chi tiết lệnh sản xuất' class='characterwrap' href='javascipt:;' onclick='window.open(\"datlenhsx?hiddenMenu=true&makeWoByQi=true&quotationItem.id="
						+ e.getId() + "\",\"\",big_window_property)'>" + e.getCode() + "</a>");
			}
		}

		SysUsers creator = sysUsersDao.get(e.getCreator());
		ja.put(String.format(
				"<a title='Cán bộ nhập liệu: %s. Click để xem thông tin chi tiết bản vẽ' href='javascript:;' onclick='window.open(\"quanlyCT?hiddenMenu=true&quotationItem.id=%s\",\"\",big_window_property)'>%s<a>",
				new Object[] { creator == null ? "" : creator.getName(), e.getId(), e.getName() }));
		ja.put(String.format(
				"<a class='characterwrap' title='Trạng thái lệnh sản xuất' href='javascript:;' onclick='openQi(\"%s\")'>%s<a>",
				new Object[] { e.getManageCode(), e.getManageCode() }));
		ja.put(String.format(
				"<a class='characterwrap' title='Chi tiết đơn hàng' href='javascript:;' onclick='window.open(\"item?quotationId=%s\")'>%s<a>",
				new Object[] { e.getQuotationId().getId(), e.getQuotationId().getCode() }));
		ja.put("<font title='" + e.getQuotationId().getCustomer().getOrgName() + "'>"
				+ e.getQuotationId().getCustomer().getCode() + "</font>");

		// Can bo tao, ngay tao/ngay giao hang
		ja.put(String.format("<font title='Ngày tạo: %s'>%s<font>",
				new Object[] { Formater.date2str(e.getCreateDate()), Formater.date2str(e.getDeliverDate()) }));
		ja.put(e.getQualityStr());
		ja.put(e.getBookingSetupTimeStr());
		ja.put(e.getTotalEstTimeStr());
		ja.put(Formater.date2strDateTime(e.getStartTime()));
		ja.put(Formater.date2strDateTime(e.getEndTime()));
		// Thoi gian setup
		ja.put(FormatNumber.num2Str(e.getExeSetupTime()));
		// Tong so chi tiet
		// Bo qua ng qc san xuat, vi neu chuyen qc trong gd san xuat thi chi tiet cung chua duoc tinh la hoan thanh
		BigDecimal finish = e.getNumOfFinishItem();
		Long ng = e.getNgAmount();
		Long nqQc = e.getQcNgAmount();
		if (nqQc != null) {
			finish = finish.subtract(new BigDecimal(nqQc));
			ng += nqQc;
		}
		BigDecimal total = finish.add(new BigDecimal(e.getNgAmount()));
		if (e.getBrokenAmount() != null)
			total = total.add(new BigDecimal(e.getBrokenAmount()));
		String amountDetail = FormatNumber.num2Str(total) + "/" + FormatNumber.num2Str(finish);
		if (ng != null && ng > 0)
			amountDetail += "/" + "<font color ='red'>" + FormatNumber.num2Str(ng) + "</front>";
		else
			amountDetail += "/0";
		if (e.getBrokenAmount() != null && e.getBrokenAmount() > 0)
			amountDetail += "/" + "<font color ='red'>" + FormatNumber.num2Str(e.getBrokenAmount()) + "</front>";
		else
			amountDetail += "/0";

		ja.put(String.format("<span title='%s' class='characterwrap'>%s</span>", new Object[] {
				messageSource.getMessage("total.ok.ng.broken", null, "Default", LocaleContextHolder.getLocale()),
				amountDetail }));

		ja.put(e.getEstTimeStr() + "/" + e.getExeTimeStr());
		// Cham
		BigDecimal cham = e.getExeTime().subtract(e.getEstTime());
		if (cham.compareTo(new BigDecimal(0)) > 0) {
			if (e.getEstTime() != null && e.getEstTime().compareTo(new BigDecimal(0)) != 0)
				ja.put("<font color ='red'>" + FormatNumber.num2Str(cham) + "/"
						+ FormatNumber.num2Str(
								cham.multiply(new BigDecimal(100)).divide(e.getEstTime(), 3, RoundingMode.HALF_UP))
						+ "</front>");
			else
				ja.put("<font color ='red'>" + FormatNumber.num2Str(cham) + "/0</front>");
		} else {
			if (e.getEstTime() == null || e.getEstTime().compareTo(new BigDecimal(0)) == 0)
				ja.put(FormatNumber.num2Str(cham) + "/0");
			else
				ja.put(FormatNumber.num2Str(cham) + "/" + FormatNumber
						.num2Str(cham.multiply(new BigDecimal(100)).divide(e.getEstTime(), 3, RoundingMode.HALF_UP)));
		}
		// Nguoi: So luong chuyen, so luong da thuc hien, so luong hoan thanh, so luong loi
		ja.put("<p class='characterwrap' title='Số lượng chuyển/Đã thực hiện/OK/NG/Hủy'>"
				+ FormatNumber.num2Str(e.getPolishingQuatity()) + "/" + FormatNumber.num2Str(e.getPolishingDone()) + "/"
				+ FormatNumber.num2Str(e.getPolishingOk()) + "/" + "<font color='red'>"
				+ FormatNumber.num2Str(e.getPolishingNg()) + "</font>" + "/<font color='red'>"
				+ FormatNumber.num2Str(e.getPolishingDestroyAmout()) + "</font>" + "</p>");
		// QC: So luong chuyen, so luong da qc, so luong phai sua, so luong da sua, qc huy
		ja.put("<p class='characterwrap' title='Số lượng chuyển/Đã thực hiện/Chuyển lại SX sửa/SX đã sửa/QC hủy'>"
				+ FormatNumber.num2Str(e.getQcQuatity()) + "/" + FormatNumber.num2Str(e.getQcDone()) + "/"
				+ "<font color='red'>" + FormatNumber.num2Str(e.getQcRepaire()) + "</font>" + "/"
				+ "<font color='blue'>" + FormatNumber.num2Str(e.getRepairedAmount()) + "</font>" + "/"
				+ "<font color='red'>" + FormatNumber.num2Str(e.getQcDestroyAmout()) + "</font>" + "</p>");
		// So luong con lai
		BigDecimal outStanding = new BigDecimal(e.getQuality().doubleValue());
		if (e.getQcDestroyAmout() != null)
			outStanding = outStanding.add(new BigDecimal(e.getQcDestroyAmout()));
		if (finish != null)
			outStanding = outStanding.subtract(finish);
		if (outStanding.doubleValue() == 0)
			ja.put("<font title='Đã hoàn thành' color='blue'>" + FormatNumber.num2Str(outStanding) + "</font>");
		else
			ja.put(FormatNumber.num2Str(outStanding));
		// Thoi gian con lai
		if (e.getTotalEstTime() != null) {
			BigDecimal outStandingTime = e.getTotalEstTime().subtract(e.getExeTime());
			ja.put(FormatNumber.num2Str(outStandingTime));
		} else
			ja.put("");

	}

	@Autowired
	private ExportExcel exportExcel;

	public void exportExcel(ModelMap model, HttpServletRequest rq, HttpServletResponse response, QuotationItemForm form)
			throws Exception {
		if (Formater.isNull(form.getFromDate()))
			throw new ResourceException("Cần nhập ngày giao hàng, từ");
		Date toDate = Formater.isNull(form.getToDate()) ? Calendar.getInstance().getTime()
				: Formater.str2date(form.getToDate());
		Calendar cTodate = Calendar.getInstance();
		cTodate.setTime(toDate);
		cTodate.add(Calendar.MONTH, -3);
		cTodate.add(Calendar.DATE, -1);
		if (cTodate.getTime().after(Formater.str2date(form.getFromDate())))
			throw new ResourceException("Khoảng thời gian ngày giao hàng từ - đến phải nhỏ hơn 3 tháng");

		QuotationItemDao dao = (QuotationItemDao) getDao().createCriteria();
		createSearchDAO(rq, form, dao);
		List<?> temp = (List<?>) dao.search();
		if (temp.isEmpty())
			throw new ResourceException("Không tồn tại dữ liệu xuất hàng!");
		List<QuotationItem> quotationItems = new ArrayList<QuotationItem>();
		for (Object e : temp) {
			if (e.getClass().isArray()) {
				for (Object o : (Object[]) e) {
					if (o.getClass().equals(dao.getModelClass())) {
						QuotationItem quotationItem = (QuotationItem) o;
						if (quotationItem.getExeTime().compareTo(quotationItem.getEstTime()) > 0)
							quotationItem.setTrangThaiSX("Chậm tiến độ");
						else if (quotationItem.getExeTime().compareTo(quotationItem.getEstTime()) == 0)
							quotationItem.setTrangThaiSX("Đúng tiến độ");
						else if (quotationItem.getExeTime().compareTo(quotationItem.getEstTime()) < 0)
							quotationItem.setTrangThaiSX("Vượt tiến độ");
						quotationItem.setTrangThaiBanVe(quotationItem.getStatusDescription());
						quotationItems.add(quotationItem);
					}
				}
			} else if (e.getClass().equals(dao.getModelClass())) {
				QuotationItem quotationItem = (QuotationItem) e;
				if (quotationItem.getExeTime().compareTo(quotationItem.getEstTime()) > 0)
					quotationItem.setTrangThaiSX("Chậm tiến độ");
				else if (quotationItem.getExeTime().compareTo(quotationItem.getEstTime()) == 0)
					quotationItem.setTrangThaiSX("Đúng tiến độ");
				else if (quotationItem.getExeTime().compareTo(quotationItem.getEstTime()) < 0)
					quotationItem.setTrangThaiSX("Vượt tiến độ");
				quotationItem.setTrangThaiBanVe(quotationItem.getStatusDescription());
				quotationItems.add(quotationItem);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("reports", quotationItems);
		map.put("drawCodeSearch", form.getCode());
		map.put("manageCodeSearch", form.getManageCodeSearch());
		map.put("name", form.getName());
		map.put("orderCode", form.getOrderCode());
		if (!Formater.isNull(form.getCusCode())) {
			Customer cust = customerManageDao.get(form.getCusCode());
			map.put("cusCode", cust.getCode());
			map.put("cusName", cust.getOrgName());
		}

		map.put("fromDate", form.getFromDate());
		map.put("toDate", form.getToDate());
		map.put("technical", form.getTechnical());
		map.put("accountManager", form.getAccountManager());
		if (!Formater.isNull(form.getStatus())) {
			if (form.getStatus() == -1)
				map.put("status", "Chưa thực hiện");
			else if (form.getStatus() == 0)
				map.put("status", "Đang thực hiện");
			else if (form.getStatus() == 1)
				map.put("status", "Đã hoàn thành");
		}
		exportExcel.export("ds_chi_tiet", response, map);
	}

	@Override
	public Boolean loadConcurrent() {
		return Boolean.TRUE;
	}

	@Override
	public void edit(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QuotationItemForm form)
			throws Exception {
		item.edit(model, rq, rs, form);
	}

	@Autowired
	private GenHub genHub;

	public void exportPoQrCode(ModelMap model, HttpServletRequest rq, HttpServletResponse response,
			QuotationItemForm form) throws Exception {
		if (Formater.isNull(form.getCreateDateForm()))
			throw new ResourceException("Cần nhập Ngày thực hiện, từ!");

		Date toDate = Formater.isNull(form.getCreateDateTo()) ? Calendar.getInstance().getTime()
				: Formater.str2date(form.getCreateDateTo());
		Calendar cTodate = Calendar.getInstance();
		cTodate.setTime(toDate);
		cTodate.add(Calendar.MONTH, -3);
		cTodate.add(Calendar.DATE, -1);
		if (cTodate.getTime().after(Formater.str2date(form.getCreateDateForm())))
			throw new ResourceException("Khoảng thời gian ngày thực hiện từ - đến phải nhỏ hơn 3 tháng");

		QuotationItemDao dao = (QuotationItemDao) getDao().createCriteria();
		createSearchDAO(rq, form, dao);
		List<?> temp = (List<?>) dao.search();
		if (temp.isEmpty())
			throw new ResourceException("Không tồn tại dữ liệu xuất hàng!");
		QuotationItemPackage packages = new QuotationItemPackage();
		for (Object e : temp) {
			if (e.getClass().isArray()) {
				for (Object o : (Object[]) e) {
					if (o.getClass().equals(dao.getModelClass())) {
						packages.getQuotationItems().add((QuotationItem) o);
					}
				}
			} else {
				if (e.getClass().equals(dao.getModelClass())) {
					packages.getQuotationItems().add((QuotationItem) e);
				}
			}
		}
		IFile file = genHub.genFile("REPQR", XmlUtils.convertObjToXML(packages), SaveFormat.PDF, null);
		dao = (QuotationItemDao) getDao();
		dao.clear();
		if (file.getIs() == null || file.getFileType() == null)
			throw new ResourceException("Kiểm tra service genFile");
		// return file to client
		response.setContentLength(file.getContentLength());
		if (SaveFormat.PDF == file.getFileType())
			response.setContentType("application/pdf");
		else if (SaveFormat.DOCX == file.getFileType())
			response.setContentType("application/msword");
		response.setHeader("Content-Disposition", "inline; filename*=UTF-8''" + file.getFileName());
		response.setHeader("filename", file.getFileName());
		StreamUtils.copy(file.getIs(), response.getOutputStream());

	}

}
