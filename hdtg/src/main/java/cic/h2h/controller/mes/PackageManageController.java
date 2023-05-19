package cic.h2h.controller.mes;

import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ibm.icu.util.Calendar;

import cic.h2h.dao.hibernate.CustomerManageDao;
import cic.h2h.dao.hibernate.PackageManageDao;
import cic.h2h.form.PackageManageForm;
import common.util.FormatNumber;
import common.util.Formater;
import common.util.ResourceException;
import common.util.StreamUtils;
import common.util.XmlUtils;
import entity.Customer;
import entity.DLVPackage;
import entity.DLVPackages;
import entity.ParentItem;
import entity.QuotationItem;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysUsersDao;
import integration.genhub.GenHub;
import integration.genhub.IFile;
import integration.genhub.SaveFormat;

@Controller
@RequestMapping(value = "/packageMng")
public class PackageManageController extends frwk.controller.SearchController<PackageManageForm, DLVPackage> {
	@Autowired
	private PackageManageDao packageManageDao;

	@Override
	public void createSearchDAO(HttpServletRequest request, PackageManageForm form, BaseDao<DLVPackage> dao)
			throws Exception {
		if (!Formater.isNull(form.getPackageCode()))
			dao.addRestriction(
					Restrictions.like("code", form.getPackageCode().trim(), MatchMode.ANYWHERE).ignoreCase());
		if (!Formater.isNull(form.getCreator()))
			dao.addRestriction(Restrictions.eq("creator.id", form.getCreator()));
		if (!Formater.isNull(form.getCusCode()))
			dao.addRestriction(Restrictions.eq("customer.id", form.getCusCode()));
		if (!Formater.isNull(form.getFrDate()))
			dao.addRestriction(Restrictions.ge("createDate", Formater.str2date(form.getFrDate())));
		if (!Formater.isNull(form.gettDate())) {
			Calendar t = Calendar.getInstance();
			t.setTime(Formater.str2date(form.gettDate()));
			t.add(Calendar.DATE, 1);
			dao.addRestriction(Restrictions.lt("createDate", t.getTime()));
		}

		if (!Formater.isNull(form.getQuotationItemCode())) {
			dao.addRestriction(Restrictions.sqlRestriction(
					"exists(select 1 from quotation_item i, dlv_package_detail d where d.dlv_package_id = {alias}.id and d.quotation_item_id = i.id and lower(i.code) like ?)",
					"%" + form.getQuotationItemCode().toLowerCase().trim() + "%", StringType.INSTANCE));
		}
		if (!Formater.isNull(form.getQuotationItemName())) {
			dao.addRestriction(Restrictions.sqlRestriction(
					"exists(select 1 from quotation_item i, dlv_package_detail d where d.dlv_package_id = {alias}.id and d.quotation_item_id = i.id and lower(i.name) like ?)",
					"%" + form.getQuotationItemName().toLowerCase().trim() + "%", StringType.INSTANCE));
		}
		if (!Formater.isNull(form.getOrderCode())) {
			dao.addRestriction(Restrictions.sqlRestriction(
					"exists(select 1 from quotation_item i, quotation q, dlv_package_detail d where q.id = i.quotation_id and d.dlv_package_id = {alias}.id and d.quotation_item_id = i.id and lower(q.code) like ?)",
					"%" + form.getOrderCode().toLowerCase().trim() + "%", StringType.INSTANCE));
		}
		if (!Formater.isNull(form.getManagerCode())) {
			dao.addRestriction(Restrictions.sqlRestriction(
					"exists(select 1 from quotation_item i, dlv_package_detail d where d.dlv_package_id = {alias}.id and d.quotation_item_id = i.id and (lower(i.manage_code) like ? or lower(i.root_manage_code) like ?))",
					new Object[] { "%" + form.getManagerCode().toLowerCase().trim() + "%",
							"%" + form.getManagerCode().toLowerCase().trim() + "%" },
					new Type[] { new StringType(), new StringType() }));
		}

		if (form.getStatus() != null) {
			if (Byte.valueOf((byte) 1).equals(form.getStatus()))
				dao.addRestriction(Restrictions.eq("status", form.getStatus()));
			else
				dao.addRestriction(Restrictions.or(Restrictions.eq("status", Byte.valueOf((byte) 0)),
						Restrictions.isNull("status")));
		}
		// du lieu cua cong ty minh
		dao.addRestriction(Restrictions.sqlRestriction(
				"exists(select 1 from quotation_item i, dlv_package_detail d, quotation q where q.id=i.quotation_id and d.dlv_package_id = {alias}.id and d.quotation_item_id = i.id and lower(q.company) = ?)",
				getSessionUser().getCompany().getId(), StringType.INSTANCE));
		dao.addOrder(Order.desc("createDate"));
		// Cung ban ve dung gan nhau
		dao.addOrder(Order.asc("pckCode"));
		// Code -- stt
		dao.addOrder(Order.asc("code"));
	}

	@Override
	protected void pushToJa(JSONArray ja, DLVPackage e, PackageManageForm modelForm) throws Exception {
		ja.put("<a href = 'javascript:;' onclick='openDetail(\"" + e.getId() + "\")'>" + e.getCode() + "</a>");
		QuotationItem qi = e.getLstPackDetail().get(0).getQuotationItem();
		ja.put(qi.getQuotationId().getCustomer().getOrgName());
		ja.put(String.format("<font class='characterwrap' title='Tên chi tiết: %s'>%s<font>",
				new Object[] { qi.getName(), qi.getCode() }));
		if (e.getLstPackDetail().size() > 1) {
			ja.put("<font color='red' title='Gộp gói từ nhiều đơn hàng'>" + e.getDsMaQlDung() + "</font>");
		} else {
			ja.put(e.getDsMaQlDung());
		}
		ja.put(FormatNumber.num2Str(e.getQuality()));
		ja.put(e.getCreator().getName());
		ja.put(Formater.date2ddsmmsyyyspHHmmss(e.getCreateDate()));
		ja.put(Formater.date2ddsmmsyyyspHHmmss(e.getDeliverDate()));
		if (e.getStatus() == null)
			ja.put("Chưa in");
		else if (e.getStatus().byteValue() == 1)
			ja.put("Đã in");
		else
			ja.put("Chưa in");
	}

	@Override
	public BaseDao<DLVPackage> getDao() {
		return packageManageDao;
	}

	@Override
	public String getJsp() {
		return "ke_hoach_san_xuat/quan_ly_xuat_hang";
	}

	@Autowired
	private SysUsersDao sysUsersDao;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, PackageManageForm form)
			throws Exception {
		form.setStatus((byte) 0);
		// Mac dinh ngay hien tai
		form.setFrDate(Formater.date2str(Calendar.getInstance().getTime()));
		model.addAttribute("customers", customerManageDao.getByType(Customer.IS_CUSTOMER));
		form.setCreator(getSessionUser().getId());
		model.addAttribute("lstWorker", sysUsersDao.getCompanyUser(getSessionUser().getCompany()));
	}

	@Autowired
	private CustomerManageDao customerManageDao;
	@Autowired
	private GenHub genHub;

	public void exportPoQrCode(ModelMap model, HttpServletRequest rq, HttpServletResponse response,
			PackageManageForm form) throws Exception {
		if (Formater.isNull(form.getFrDate()))
			throw new ResourceException("Cần nhập Ngày thực hiện, từ!");

		Date toDate = Formater.isNull(form.gettDate()) ? Calendar.getInstance().getTime()
				: Formater.str2date(form.gettDate());
		Calendar cTodate = Calendar.getInstance();
		cTodate.setTime(toDate);
		cTodate.add(Calendar.MONTH, -1);
		cTodate.add(Calendar.DATE, -1);
		if (cTodate.getTime().after(Formater.str2date(form.getFrDate())))
			throw new ResourceException("Khoảng thời gian ngày thực hiện từ - đến phải nhỏ hơn 1 tháng");

		PackageManageDao dao = (PackageManageDao) getDao().createCriteria();
		createSearchDAO(rq, form, dao);
		List<DLVPackage> lstDLVPackage = dao.search();
		if (lstDLVPackage.isEmpty())
			throw new ResourceException("Không tồn tại dữ liệu xuất hàng!");

		Date deliverDate = Calendar.getInstance().getTime();
		DLVPackages packages = new DLVPackages();
		for (DLVPackage o : lstDLVPackage) {
			if (o.getStatus() == null || o.getStatus().byteValue() == 0) {
				o.setStatus((byte) 1);
				// Cap nhat trang thai da in
				o.setDeliverDate(deliverDate);
			}
			packages.getLstDlvPackage().add(o);
		}

		IFile file = genHub.genFile("PCKQR", XmlUtils.convertObjToXML(packages), SaveFormat.PDF, null);
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

	public void getDetail(ModelMap model, HttpServletRequest rq, HttpServletResponse response, PackageManageForm form)
			throws Exception {
		String pckId = rq.getParameter("pckId");
		DLVPackage dlvPackage = packageManageDao.get(DLVPackage.class, pckId);

//		response.setContentType("application/json;charset=utf-8");
//		response.setCharacterEncoding("utf-8");
//		response.setHeader("Cache-Control", "no-store");
//
//		OutputStream os = response.getOutputStream();
//		ParentItem.objectMapper.writeValue(os, dlvPackage);
//		os.flush();
//		os.close();
		returnJson(response, dlvPackage);
	}

	/*
	 * @Autowired private DLVPackageDetail exeStepTypeDao;
	 */
	public void delPackage(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			PackageManageForm form) throws Exception {
		String idPackage = request.getParameter("id");
		DLVPackage dLVPackage = packageManageDao.get(DLVPackage.class, idPackage);
		if (Formater.isNull(idPackage) || dLVPackage == null)
			throw new ResourceException("Không tồn tại gói!");
		form.setdLVPackage(dLVPackage);
		packageManageDao.del(form.getdLVPackage());
	}
}
