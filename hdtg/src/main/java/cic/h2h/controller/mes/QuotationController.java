package cic.h2h.controller.mes;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cic.h2h.dao.hibernate.BssFactoryUnitDao;
import cic.h2h.dao.hibernate.CustomerManageDao;
import cic.h2h.dao.hibernate.ExeStepDao;
import cic.h2h.dao.hibernate.MaterialDao;
import cic.h2h.dao.hibernate.QuotationDao;
import cic.h2h.dao.hibernate.QuotationItemDao;
import cic.h2h.dao.hibernate.ReduceByAmountDao;
import cic.h2h.form.QuotationForm;
import cic.utils.ExportExcel;
import common.util.FormatNumber;
import common.util.Formater;
import common.util.ResourceException;
import constants.Constants;
import entity.BssFactoryUnit;
import entity.Company;
import entity.Customer;
import entity.Department;
import entity.ExeStep;
import entity.Material;
import entity.Quotation;
import entity.QuotationItem;
import entity.QuotationItemExe;
import entity.QuotationItemMaterial;
import entity.ReduceByAmount;
import entity.frwk.BssParam;
import entity.frwk.SysDictParam;
import entity.frwk.SysParam;
import entity.frwk.SysUsers;
import frwk.constants.RightConstants;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.BssParamDao;
import frwk.dao.hibernate.sys.CompanyDao;
import frwk.dao.hibernate.sys.DepartmentDao;
import frwk.dao.hibernate.sys.RightUtils;
import frwk.dao.hibernate.sys.SysDictParamDao;
import frwk.dao.hibernate.sys.SysParamDao;
import frwk.dao.hibernate.sys.SysUsersDao;
import integration.genhub.GenHub;

@Controller
@RequestMapping("/quotation")
public class QuotationController extends CatalogController<QuotationForm, Quotation> {
	private static final Logger log = Logger.getLogger(QuotationController.class);
	@Autowired
	private QuotationDao quotationDao;

	@Override
	public void createSearchDAO(HttpServletRequest request, QuotationForm form, BaseDao<Quotation> dao)
			throws Exception {
		if (!Formater.isNull(form.getCompanyId())) {
			dao.addRestriction(Restrictions.eq("company", new Company(form.getCompanyId())));
		} else {
			if (!rightUtils.haveRight(request, RightConstants.VIEW_DATA_ALL)) {
				dao.addRestriction(Restrictions.eq("company", getSessionUser().getCompany()));
			}
		}
		if (!Formater.isNull(form.getCusCode()))
			dao.addRestriction(Restrictions.eq("customer.id", form.getCusCode()));

		if (!Formater.isNull(form.getCode()))
			dao.addRestriction(Restrictions.like("code", form.getCode().trim(), MatchMode.ANYWHERE).ignoreCase());

		if (!Formater.isNull(form.getFromDate())) {
			dao.addRestriction(Restrictions.ge("createDate", Formater.str2date(form.getFromDate())));
		}
		if (!Formater.isNull(form.getToDate())) {
			Calendar now = Calendar.getInstance();
			now.setTime(Formater.str2date(form.getToDate()));
			now.add(Calendar.DATE, 1);
			dao.addRestriction(Restrictions.lt("createDate", now.getTime()));
		}
		if (form.getStatus() != null) {
			// Chua thuc hien
			if (form.getStatus() == -1) {
				dao.addRestriction(Restrictions.sqlRestriction(
						"not exists (select 1 from quotation_item qi, quotation_item_exe qie, work_order wo, work_order_exe woe\r\n"
								+ "where qi.id = qie.quotation_item_id and qie.id = wo.quotation_item_exe_id and wo.id = woe.work_order_id \r\n"
								+ "and qi.quotation_id = {alias}.id)"));
			} else if (form.getStatus() == 0) {// Dang thuc hien
				dao.addRestriction(Restrictions.gt("numOfChildren", new BigDecimal("0")));
				dao.addRestriction(Restrictions.sqlRestriction(
						"exists (select 1 from quotation_item qi, quotation_item_exe qie, work_order wo, work_order_exe woe\r\n"
								+ "where qi.id = qie.quotation_item_id and qie.id = wo.quotation_item_exe_id and wo.id = woe.work_order_id \r\n"
								+ "and qi.quotation_id = {alias}.id)"));
				dao.addRestriction(Restrictions.neProperty("numOfFinishChildren", "numOfChildren"));
			} else if (form.getStatus() == 1) {// Da hoan thanh
				dao.addRestriction(Restrictions.gt("numOfChildren", new BigDecimal("0")));
				dao.addRestriction(Restrictions.eqProperty("numOfFinishChildren", "numOfChildren"));
			}

		}
		// Tam thoi bo qua 2 tieu chi nay

		// if (!Formater.isNull(form.getCompanyId())) {
		// dao.addRestriction(Restrictions.eq("company", new
		// Company(form.getCompanyId())));
		// }
		// if (!Formater.isNull(form.getDepartmentId())) {
		// dao.addRestriction(Restrictions.eq("department", new
		// Department(form.getDepartmentId())));
		// }
		if (form.getDone() != null && form.getDone().equals("1"))
			dao.addRestriction(Restrictions.eq("done", Short.valueOf("1")));

		if (!Formater.isNull(form.getDrawingCode())) {
			dao.addRestriction(Restrictions.sqlRestriction(
					"exists(select 1 from quotation_item qi where qi.quotation_id = {alias}.id and lower(qi.code) like ?)",
					"%" + form.getDrawingCode().trim().toLowerCase() + "%", StringType.INSTANCE));
		}
		if (!Formater.isNull(form.getManagerCode())) {
			dao.addRestriction(Restrictions.sqlRestriction(
					"exists(select 1 from quotation_item qi where qi.quotation_id = {alias}.id and lower(qi.MANAGE_CODE) like ?)",
					new Object[] { "%" + form.getManagerCode().trim().toLowerCase() + "%" },
					new Type[] { StringType.INSTANCE }));
		}

		SysUsers su = getSessionUser();
		if (Boolean.TRUE.equals(su.getIsPartner()))
			dao.addRestriction(Restrictions.eq("customer", su.getPartner()));

		dao.addOrder(Order.desc("createDate"));
	}

	@Autowired
	private ExportExcel exportExcel;

	public void exportExcel(ModelMap model, HttpServletRequest rq, HttpServletResponse response, QuotationForm form)
			throws Exception {
		if (Formater.isNull(form.getFromDate()) || Formater.isNull(form.getToDate()))
			throw new ResourceException("Phải nhập từ ngày và đến ngày");
		QuotationDao dao = (QuotationDao) getDao().createCriteria();
		createSearchDAO(rq, form, dao);
		List<?> temp = (List<?>) dao.search();
		if (temp.isEmpty())
			throw new ResourceException("Không tồn tại dữ liệu xuất hàng!");
		List<Quotation> quotations = new ArrayList<Quotation>();
		for (Object e : temp) {
			if (e.getClass().isArray()) {
				for (Object o : (Object[]) e) {
					if (o.getClass().equals(dao.getModelClass())) {
						Quotation quotation = (Quotation) o;
						if (!Short.valueOf((short) 1).equals(quotation.getDone())) {
							// Trang thai san xuat
							String status = "";
							if (quotation.getStatus() == null || quotation.getStatus() == 0)
								status = "T&#7841;o m&#7899;i";
							else if (quotation.getStatus() == 1)
								status = "Ch&#7901; duy&#7879;t";
							else if (quotation.getStatus() == 2)
								status = "&#272;&#227; duy&#7879;t";
							else if (quotation.getStatus() == 3)
								status = "Kh&#244;ng duy&#7879;t";
							else if (quotation.getStatus() == 4)
								status = "&#272;ang s&#7843;n xu&#7845;t";
							else
								status = "K&#7871;t th&#250;c";
							quotation.setTrangThaiSX(status);
						} else {
							// Trang thai san xuat
							if (quotation.getExeTime().compareTo(quotation.getEstTime()) > 0)
								quotation.setTrangThaiDonHang("Chậm tiến độ");
							else if (quotation.getExeTime().compareTo(quotation.getEstTime()) == 0)
								quotation.setTrangThaiDonHang("Đúng tiến độ");
							else if (quotation.getExeTime().compareTo(quotation.getEstTime()) < 0)
								quotation.setTrangThaiDonHang("Vượt tiến độ");
							// TODO: Nang cap sau, tam thoi hien thi dong nhat
							quotation.setTrangThaiSX(quotation.getStatusDescription());
						}
						quotations.add(quotation);
					}
				}
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("reports", quotations);
		if (!Formater.isNull(form.getCompanyId()))
			map.put("companyName", companyDao.get(form.getCompanyId()).getName());
		if (!Formater.isNull(form.getDepartmentId()))
			map.put("departmentName", departmentDao.get(form.getDepartmentId()).getName());
		map.put("code", form.getCode());
		map.put("cusCode", form.getCusCode());
		map.put("cusName", form.getCusName());
		map.put("fromDate", form.getFromDate());
		map.put("toDate", form.getToDate());
		map.put("drawingCode", form.getDrawingCode());
		map.put("managerCode", form.getManagerCode());
		if (form.getStatus() != null) {
			if (form.getStatus() == -1)
				map.put("status", "Chưa thực hiện");
			else if (form.getStatus() == 0)
				map.put("status", "Đang thực hiện");
			else if (form.getStatus() == 1)
				map.put("status", "Đã hoàn thành");
		}
		exportExcel.export("don_hang", response, map);
	}

	@Override
	protected void pushToJa(JSONArray ja, Quotation e, QuotationForm modelForm) throws Exception {
		quotationDao.sts(e);
		String detail = "<a class='characterwrap' href = '#' onclick = 'window.location=\"item?quotationId=" + e.getId()
				+ "\"'>" + (Formater.isNull(e.getCode()) ? "Chi ti&#7871;t" : e.getCode()) + "</a>";
		ja.put(detail);
		ja.put(e.getCustomer().getCode());
		ja.put(e.getCustomer().getOrgName());

		// So luong chi tiet
		ja.put(FormatNumber.num2Str(e.getNumOfFinishChildren()) + "/" + FormatNumber.num2Str(e.getNumOfChildren()));
		BigDecimal totalEstimateTime = e.getTotalEstTime();
		if (totalEstimateTime == null)
			totalEstimateTime = e.getSetupTime();
		else {
			if (e.getSetupTime() != null)
				totalEstimateTime = totalEstimateTime.add(e.getSetupTime());
		}
		ja.put(FormatNumber.num2Str(totalEstimateTime));

		ja.put(e.getPriceStr());

		ja.put(Formater.date2str(e.getQuotationDate()));
		ja.put(e.getQuotationEndDateStr());
		ja.put(Formater.date2strDateTime(e.getStartTime()));
		ja.put(Formater.date2strDateTime(e.getEndTime()));
		if (e.getExeTime() != null)
			ja.put(FormatNumber.num2Str(e.getEstTime()) + "/" + FormatNumber.num2Str(e.getExeTime()));
		else
			ja.put("");
		if (!Short.valueOf((short) 1).equals(e.getDone())) {
			// Trang thai san xuat
			ja.put("");
			String status = "";
			if (e.getStatus() == null || e.getStatus() == 0)
				status = "T&#7841;o m&#7899;i";
			else if (e.getStatus() == 1)
				status = "Ch&#7901; duy&#7879;t";
			else if (e.getStatus() == 2)
				status = "&#272;&#227; duy&#7879;t";
			else if (e.getStatus() == 3)
				status = "Kh&#244;ng duy&#7879;t";
			else if (e.getStatus() == 4)
				status = "&#272;ang s&#7843;n xu&#7845;t";
			else
				status = "K&#7871;t th&#250;c";
			ja.put(status);
		} else {
			// Trang thai san xuat
			if (e.getExeTime() == null)
				ja.put("");
			else if (e.getExeTime().compareTo(e.getEstTime()) > 0)
				ja.put("<div style='background:red;'>" + "Chậm tiến độ" + "</div>");
			else if (e.getExeTime().compareTo(e.getEstTime()) == 0)
				ja.put("Đúng tiến độ");
			else if (e.getExeTime().compareTo(e.getEstTime()) < 0)
				ja.put("Vượt tiến độ");
			ja.put(e.getStatusDescription());
		}

	}

	@Override
	public BaseDao<Quotation> getDao() {
		return quotationDao;
	}

	@Override
	public String getJsp() {
		return "bao_gia/quan_ly_bao_gia";
	}

	@Autowired
	private RightUtils rightUtils;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QuotationForm form)
			throws Exception {
		if (Boolean.TRUE.equals(getSessionUser().getIsPartner())) {
			model.addAttribute("customers", Arrays.asList(getSessionUser().getPartner()));
		} else {
			model.addAttribute("customers", customerManageDao.getByType(Customer.IS_CUSTOMER));
			form.setCompanyId(getSessionUser().getCompany().getId());
		}

		if (getSessionUser().getDepartment() != null)
			form.setDepartmentId(getSessionUser().getDepartment().getId());

		List<Company> lstCompany = null;
		List<Department> lstDepartment = null;
		// Phan quyen du lieu
		// Xem du lieu toan he thong
		if (rightUtils.haveRight(rq, RightConstants.VIEW_DATA_ALL)) {
			lstCompany = companyDao.getAllOrderAstName();
		} else {
			lstCompany = Arrays.asList(getSessionUser().getCompany());
		}

		if (rightUtils.haveRight(rq, RightConstants.VIEW_DATA_COMPANY)
				|| rightUtils.haveRight(rq, RightConstants.VIEW_DATA_ALL)) {
			lstDepartment = departmentDao.getAllByCompanyOrderAscName(form.getCompanyId());
		} else {
			lstDepartment = Arrays.asList(getSessionUser().getDepartment());
		}
		model.addAttribute("lstCompany", lstCompany);
		model.addAttribute("lstDepartment", lstDepartment);
		if ("createNew".equals(rq.getParameter("to"))) {
			List<SysDictParam> listCurrency = sysDictParamDao.getByType(Constants.CAT_TYPE_CURRENCY);
			model.addAttribute("listCurrency", listCurrency);
			// Mac dinh cac gia tri
			Quotation q = form.getQuotation();
			// Phan tram rate gio may mac dinh
			BssParam defaultRatMachine = bssParamDao.getSysParamByCode("DFLT_RT_MCH");
			BigDecimal ratMachine = null;
			try {
				ratMachine = new BigDecimal(defaultRatMachine.getValue());
			} catch (Exception ex) {
				log.error(ex);
			}
			q.setRatMachine(ratMachine);
			q.setQuotationDate(Calendar.getInstance().getTime());
		}

	}

	@Autowired
	private BssParamDao bssParamDao;
	@Autowired
	private SysDictParamDao sysDictParamDao;
	@Autowired
	private CompanyDao companyDao;
	@Autowired
	private DepartmentDao departmentDao;

	public void reloadDepartment(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QuotationForm form)
			throws Exception {
		String conpanyId = rq.getParameter("conpanyId");
		List<Department> lstDepartment = departmentDao.getAllByCompanyOrderAscName(conpanyId);
		returnJsonArray(rs, lstDepartment);
	}

	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QuotationForm form)
			throws Exception {
		if (Formater.isNull(form.getQuotation().getCustomer().getId()))
			throw new ResourceException("Bạn chưa chọn khách hàng");
		Quotation q = quotationDao.getQuotationByCode(form.getQuotation().getCode(), getSessionUser().getCompany());
		if (q != null)
			throw new ResourceException(String.format("Đã tồn tại đơn hàng có mã %s", form.getQuotation().getCode()));
		form.getQuotation().setCreateDate(Calendar.getInstance().getTime());
		form.getQuotation().setCreator(getSessionUser().getUsername());
		form.getQuotation().setCompany(getSessionUser().getCompany());
		form.getQuotation().setDepartment(getSessionUser().getDepartment());
		form.getQuotation().setDone((short) 1);
		super.save(model, rq, rs, form);
		returnJson(rs, form.getQuotation());
	}

	@Autowired
	private CustomerManageDao customerManageDao;

	public void getCustomerById(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QuotationForm form)
			throws Exception {
		String customerId = rq.getParameter("customerId");
		Customer c = (Customer) customerManageDao.getObject(Customer.class, customerId);
		customerManageDao.makeOrderCode(c);
		returnJson(rs, c);
	}

	public void getCustomerIfOnly(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QuotationForm form)
			throws Exception {
		String cusCode = rq.getParameter("cusCode");
		String cusName = rq.getParameter("cusName");
		Customer c = (Customer) customerManageDao.getOnly(cusCode, cusName);
		customerManageDao.makeOrderCode(c);
		returnJson(rs, c);
	}

	public void download(ModelMap model, HttpServletRequest request, HttpServletResponse response, QuotationForm form)
			throws Exception {
		InputStream fileInputStream = getClass().getClassLoader()
				.getResourceAsStream("/templates/report/template_don_hang.xlsx");
		response.setContentType("application/vnd.ms-excel");
		response.addHeader("Content-Disposition", "attachment; filename=template_don_hang.xlsx");
		OutputStream responseOutputStream = response.getOutputStream();
		int bytes;
		while ((bytes = fileInputStream.read()) != -1)
			responseOutputStream.write(bytes);
		fileInputStream.close();
		responseOutputStream.close();
	}

	@Autowired
	private ExeStepDao exeStepDao;
	@Autowired
	private MaterialDao materialDao;

	@Autowired
	private SysParamDao sysParamDao;

	private static final DataFormatter dataFormatter = new DataFormatter();

	@RequestMapping(value = "upload", method = RequestMethod.POST)
	public void upload(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			@RequestParam("inputFile") MultipartFile inputFile, @RequestParam("fileName") String fileName)
			throws Exception {
		SysUsers su = getSessionUser();
		if (Boolean.TRUE.equals(su.getIsPartner())) {
			returnTxtHtml(rs, "Đối tác không có quyền thực hiện chức năng!");
			return;
		}
		Workbook wb = fileName.endsWith("xlsx") ? new XSSFWorkbook(inputFile.getInputStream())
				: new HSSFWorkbook(inputFile.getInputStream());
		Sheet sheet = wb.getSheetAt(0);
		// Du lieu bat dau tu dong 4
		int iRowIdex = 4;
		// Phan tram hao ton nhien lieu mac dinh
		SysParam defaultWaste = sysParamDao.getSysParamByCode("WASTE");
		BigDecimal bWaste = null;
		try {
			bWaste = new BigDecimal(defaultWaste.getValue());
		} catch (Exception ex) {
			log.error(ex);
		}
		// Phan tram rate gio may mac dinh
		BssParam defaultRatMachine = bssParamDao.getSysParamByCode("DFLT_RT_MCH");
		BigDecimal ratMachine = null;
		try {
			ratMachine = new BigDecimal(defaultRatMachine.getValue());
		} catch (Exception ex) {
			log.error(ex);
		}
		while (true) {
			Row row = sheet.getRow(iRowIdex);
			if (row == null)
				break;
			String cellOvalue = dataFormatter.formatCellValue(row.getCell(0));
			// Kiem tra cot 1 rong thi stop
			if ("eof".equals(cellOvalue) || Formater.isNull(cellOvalue))
				break;
			try {
				String poCode = dataFormatter.formatCellValue(row.getCell(4));
				if (Formater.isNull(poCode))
					throw new ResourceException("Chưa nhập thông tin đơn hàng");
				if (checkWhileSpace(poCode))
					throw new ResourceException("Không được nhập ký tự trắng vào mã đơn hàng : " + poCode);
				Quotation quotation = quotationDao.getQuotationByCode(poCode, getSessionUser().getCompany());
				if (quotation == null) {
					quotation = makeQuotation(row);
					quotation.setRatMachine(ratMachine);
					quotation.setCode(poCode);
					quotationDao.save(quotation);
				}
				QuotationItem item = makeQi(row, bWaste, quotation);
				quotationItemDao.save(item);
				// Flush de co the validate cac ban ghi trong file upload voi nhau
				quotationItemDao.getCurrentSession().flush();
			} catch (ResourceException ex) {
				log.error(ex.getMessage(), ex);
				String error = String.format("Lỗi đọc dữ liệu dòng %s, chi tiết lỗi: %s",
						new Object[] { iRowIdex + 1, ex.getMessage() });
				returnTxtHtml(rs, error);
				throw ex;
			}
			iRowIdex++;
		}
	}

	private Quotation makeQuotation(Row row) throws ResourceException {
		Quotation quotation = new Quotation(getSessionUser());
		quotation.setDone((short) 1);
		String cusCode = dataFormatter.formatCellValue(row.getCell(3));
		if (Formater.isNull(cusCode))
			throw new ResourceException("Chưa nhập thông tin khách hàng");
		if (checkWhileSpace(cusCode))
			throw new ResourceException("Không được nhập ký tự trắng vào mã khách hàng : " + cusCode);
		Customer customer = customerManageDao.getCusbyCode(cusCode);
		if (customer == null)
			throw new ResourceException("không tồn tại khách hàng với mã : " + cusCode);
		quotation.setCustomer(customer);
		if (Formater.isNull(dataFormatter.formatCellValue(row.getCell(12)))) {
			throw new ResourceException("Chưa nhập thông tin ngày nhận PO ");
		}
		quotation.setQuotationDate(getDateFormExcel(row.getCell(12)));

		return quotation;
	}

	private Date getDateFormExcel(Cell cell) throws ResourceException {
		Date date = null;
		try {
			date = cell.getDateCellValue();
		} catch (Exception e) {
			String dateStr = dataFormatter.formatCellValue(cell);
			try {
				date = Formater.str2date(dateStr);
			} catch (Exception e2) {
				throw new ResourceException("Sai định dạng dữ liệu : " + dateStr);
			}
		}
		return date;
	}

	@Autowired
	private SysUsersDao sysUsersDao;

	@Autowired
	private ReduceByAmountDao reduceByAmountDao;

	@Autowired
	private QuotationItemDao quotationItemDao;

	@Autowired
	private BssFactoryUnitDao bssFactoryUnitDao;

	private QuotationItem makeQi(Row row, BigDecimal bWaste, Quotation quotation) throws Exception {
		QuotationItem item = new QuotationItem(quotation);
		item.setCreator(getSessionUser().getId());
		String tecthCode = dataFormatter.formatCellValue(row.getCell(1));
		if (Formater.isNull(tecthCode))
			throw new ResourceException("Chưa nhập thông tin người phụ trách kỹ thuật");
		SysUsers usersTecth = sysUsersDao.getSysUserByUserName(tecthCode);
		if (usersTecth == null)
			throw new ResourceException("Thông tin người phụ trách kỹ thuật chưa đúng : " + tecthCode);

		item.setTechnicalId(usersTecth);
		String acCode = dataFormatter.formatCellValue(row.getCell(2));
		if (Formater.isNull(acCode))
			throw new ResourceException("Chưa nhập thông tin người phụ trách AC");
		SysUsers userAc = sysUsersDao.getSysUserByUserName(acCode);
		if (userAc == null)
			throw new ResourceException("Thông tin người phụ trách AC chưa đúng : " + acCode);
		item.setAcId(userAc);
		String workOrderNum = dataFormatter.formatCellValue(row.getCell(5));
		if (Formater.isNull(workOrderNum))
			throw new ResourceException("Chưa nhập thông tin số lệnh sản xuất");
		if (checkWhileSpace(workOrderNum))
			throw new ResourceException("Không được nhập ký tự trắng vào số lệnh sản xuất : " + workOrderNum);
		item.setWorkOderNumber(workOrderNum);
		String managerCode = dataFormatter.formatCellValue(row.getCell(6));
		if (Formater.isNull(managerCode))
			throw new ResourceException("Chưa nhập thông tin mã quản lý");
		if (checkWhileSpace(managerCode))
			throw new ResourceException("Không được nhập ký tự trắng vào mã quản lý : " + managerCode);
		// mã bản vẻ
		if (Formater.isNull(dataFormatter.formatCellValue(row.getCell(7))))
			throw new ResourceException("Chưa nhập thông tin tên bản vẻ");
		if (checkWhileSpace(dataFormatter.formatCellValue(row.getCell(7))))
			throw new ResourceException(
					"Không được nhập ký tự trắng vào tên bản vẻ : " + dataFormatter.formatCellValue(row.getCell(7)));
		item.setName(dataFormatter.formatCellValue(row.getCell(7)));
		if (Formater.isNull(dataFormatter.formatCellValue(row.getCell(8))))
			throw new ResourceException("Chưa nhập thông tin mã bản vẻ");
		if (checkWhileSpace(dataFormatter.formatCellValue(row.getCell(8))))
			throw new ResourceException(
					"Không được nhập ký tự trắng vào mã bản vẻ : " + dataFormatter.formatCellValue(row.getCell(8)));
		item.setCode(dataFormatter.formatCellValue(row.getCell(8)));
		if (item.getCode().length() > 40)
			throw new ResourceException(String.format("Mã chi tiết, %s độ dài %s >40 ký tự!",
					new Object[] { item.getCode(), item.getCode().length() }));
		String levelItem = dataFormatter.formatCellValue(row.getCell(9));
		if (Formater.isNull(levelItem))
			throw new ResourceException("Chưa nhập thông tin mức độ");
		if ("T".equalsIgnoreCase(levelItem))
			item.setDrawingType(sysDictParamDao.getByTypeAndCode("DRWT", "MED"));
		else if ("D".equalsIgnoreCase(levelItem))
			item.setDrawingType(sysDictParamDao.getByTypeAndCode("DRWT", "SFT"));
		else if ("K".equalsIgnoreCase(levelItem))
			item.setDrawingType(sysDictParamDao.getByTypeAndCode("DRWT", "HRD"));
		else
			throw new ResourceException(
					"Không tồn tại chủng loại bản vẽ " + levelItem + ", các giá trị có thể nhận T, D, K");
		// Vat lieu xem lại
		String materialCode = dataFormatter.formatCellValue(row.getCell(10));
		if (Formater.isNull(materialCode))
			throw new ResourceException("Chưa nhập thông tin vật liệu");
		if (checkWhileSpace(materialCode))
			throw new ResourceException("Không được nhập ký tự trắng vào mã vật liệu : " + materialCode);
		Material material = materialDao.getByCode(materialCode);
		if (material == null)
			throw new ResourceException("Thông tin vật liệu không chính xác : " + materialCode);
		QuotationItemMaterial quotationItemMaterial = new QuotationItemMaterial(item, material);
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(40)))) {
			try {
				quotationItemMaterial.setSizeLong(new BigDecimal(dataFormatter.formatCellValue(row.getCell(40))));
			} catch (Exception e) {

				throw new ResourceException(
						"Dữ liệu phải là định dạng số : " + dataFormatter.formatCellValue(row.getCell(40)));
			}

		}
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(41)))) {
			try {
				quotationItemMaterial.setSizeWidth(new BigDecimal(dataFormatter.formatCellValue(row.getCell(41))));
			} catch (Exception e) {

				throw new ResourceException(
						"Dữ liệu phải là định dạng số : " + dataFormatter.formatCellValue(row.getCell(41)));
			}
		}
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(42)))) {

			try {
				quotationItemMaterial.setSizeHeight(new BigDecimal(dataFormatter.formatCellValue(row.getCell(42))));
			} catch (Exception e) {

				throw new ResourceException(
						"Dữ liệu phải là định dạng số : " + dataFormatter.formatCellValue(row.getCell(42)));
			}
		}
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(43)))) {
			try {
				quotationItemMaterial.setAngleWorkpiece(new BigDecimal(dataFormatter.formatCellValue(row.getCell(43))));
			} catch (Exception e) {

				throw new ResourceException(
						"Dữ liệu phải là định dạng số : " + dataFormatter.formatCellValue(row.getCell(43)));
			}
		}
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(44)))) {
			quotationItemMaterial.setColdRolled(dataFormatter.formatCellValue(row.getCell(44)));
		}
		if (Formater.isNull(dataFormatter.formatCellValue(row.getCell(45)))) {
			throw new ResourceException("Thông tin tên kỹ thuật không được để trống");
		}
		quotationItemMaterial.setTechnicalName(dataFormatter.formatCellValue(row.getCell(45)));
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(46)))) {
			try {
				quotationItemMaterial.setPrice(new BigDecimal(dataFormatter.formatCellValue(row.getCell(46))));
			} catch (Exception e) {

				throw new ResourceException(
						"Dữ liệu phải là định dạng số : " + dataFormatter.formatCellValue(row.getCell(46)));
			}
		}
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(47)))) {
			quotationItemMaterial.setEmbryoNote(dataFormatter.formatCellValue(row.getCell(47)));
		}

		quotationItemMaterial.setWaste(bWaste);
		item.getQuotationItemMaterialList().add(quotationItemMaterial);
		// end vat lieu
		String quality = dataFormatter.formatCellValue(row.getCell(11));
		if (Formater.isNull(quality))
			throw new ResourceException("Chưa nhập thông tin số lượng chi tiết");
		item.setQualityStr(quality);
		if (item.getQuality() != null && item.getQuality().doubleValue() > 0) {
			ReduceByAmount reduceByAmount = reduceByAmountDao.getByQuality(item.getQuality().longValue());
			if (reduceByAmount != null)
				item.setReduce(reduceByAmount.getAmountReduce());
		}

		String quoItemDate = dataFormatter.formatCellValue(row.getCell(13));
		if (Formater.isNull(quoItemDate))
			throw new ResourceException("Chưa nhập thông tin ngày giao hàng");
		try {
			item.setDeliverDate(getDateFormExcel(row.getCell(13)));
		} catch (Exception e) {
			throw new ResourceException("Sai định dạng dữ liệu : " + quoItemDate);
		}
		// hang le, hang loat
		String sigle = dataFormatter.formatCellValue(row.getCell(49));
		if (Formater.isNull(sigle))
			throw new ResourceException("Chưa nhập loại hàng (hàng lẻ/hàng loạt)!");
		SysDictParam param = sysDictParamDao.getByTypeAndCode("SIGGLE", sigle.trim());
		if (param == null)
			throw new ResourceException(
					"Mã hàng lẻ/hàng loạt không đúng, nhập mã tương ứng hàng lẻ (LCL)/hàng loạt (SERIES)");
		item.setSiggle(param);

		// can bo ke hoach
		String planUserCode = dataFormatter.formatCellValue(row.getCell(50));
		if (!Formater.isNull(planUserCode)) {
			SysUsers planUser = sysUsersDao.getSysUserByUserName(planUserCode);
			if (planUser == null)
				throw new ResourceException("Thông tin người phụ trách kế hoạch chưa đúng : " + planUserCode);

			item.setPlanner(planUser);
		}

		// phan xuong san xuat
		String factoryCode = dataFormatter.formatCellValue(row.getCell(51));
		if (!Formater.isNull(factoryCode)) {
			BssFactoryUnit unit = bssFactoryUnitDao.getBssFactoryUnitByCode(getSessionUser().getCompany().getId(),
					factoryCode);
			if (unit == null)
				throw new ResourceException(
						"Không tồn tại phân xưởng : " + dataFormatter.formatCellValue(row.getCell(51)));
			item.setFactoryUnit(unit);
		} else {
			throw new ResourceException("Nơi sản xuất không được để trống !");
		}
		// begin tien
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(14)))) {
//			exeStep = exeStepDao.getByTypeAndCode("SETUP", "ST", Boolean.FALSE);
//			createQuotationItemExeFormRow(row, exeStep, 14, item);
			try {
				item.setBookingSetupTime(new BigDecimal(dataFormatter.formatCellValue(row.getCell(14))));
			} catch (Exception e) {
				// TODO: handle exception
				throw new ResourceException(
						"Sai định dạng dữ liệu : " + dataFormatter.formatCellValue(row.getCell(14)));
			}
		}

		ExeStep exeStep = null;
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(15)))) {
			exeStep = exeStepDao.getByTypeAndCode("T", "OP1", Boolean.FALSE);
			makeQie(row, exeStep, 15, item);
		}
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(16)))) {
			exeStep = exeStepDao.getByTypeAndCode("T", "OP2", Boolean.FALSE);
			makeQie(row, exeStep, 16, item);
		}
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(17)))) {
			exeStep = exeStepDao.getByTypeAndCode("T", "OP3", Boolean.FALSE);
			makeQie(row, exeStep, 17, item);
		}
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(18)))) {
			exeStep = exeStepDao.getByTypeAndCode("T", "OP4", Boolean.FALSE);
			makeQie(row, exeStep, 18, item);
		}
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(19)))) {
			exeStep = exeStepDao.getByTypeAndCode("T", "OP5", Boolean.FALSE);
			makeQie(row, exeStep, 19, item);
		}
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(20)))) {
			exeStep = exeStepDao.getByTypeAndCode("T", "OP6", Boolean.FALSE);
			makeQie(row, exeStep, 20, item);
		}
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(21)))) {
			exeStep = exeStepDao.getByTypeAndCode("T", "OP7", Boolean.FALSE);
			makeQie(row, exeStep, 21, item);
		}
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(22)))) {
			exeStep = exeStepDao.getByTypeAndCode("T", "OP8", Boolean.FALSE);
			makeQie(row, exeStep, 22, item);
		}
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(23)))) {
			exeStep = exeStepDao.getByTypeAndCode("T", "OP9", Boolean.FALSE);
			makeQie(row, exeStep, 23, item);
		}
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(24)))) {
			exeStep = exeStepDao.getByTypeAndCode("T", "OP10", Boolean.FALSE);
			makeQie(row, exeStep, 24, item);
		}
		// end
		// begin phay
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(25)))) {
			exeStep = exeStepDao.getByTypeAndCode("P", "OP1", Boolean.FALSE);
			makeQie(row, exeStep, 25, item);
		}
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(26)))) {
			exeStep = exeStepDao.getByTypeAndCode("P", "OP2", Boolean.FALSE);
			makeQie(row, exeStep, 26, item);
		}
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(27)))) {
			exeStep = exeStepDao.getByTypeAndCode("P", "OP3", Boolean.FALSE);
			makeQie(row, exeStep, 27, item);
		}
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(28)))) {
			exeStep = exeStepDao.getByTypeAndCode("P", "OP4", Boolean.FALSE);
			makeQie(row, exeStep, 28, item);
		}
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(29)))) {
			exeStep = exeStepDao.getByTypeAndCode("P", "OP5", Boolean.FALSE);
			makeQie(row, exeStep, 29, item);
		}
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(30)))) {
			exeStep = exeStepDao.getByTypeAndCode("P", "OP6", Boolean.FALSE);
			makeQie(row, exeStep, 30, item);
		}
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(31)))) {
			exeStep = exeStepDao.getByTypeAndCode("P", "OP7", Boolean.FALSE);
			makeQie(row, exeStep, 31, item);
		}
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(32)))) {
			exeStep = exeStepDao.getByTypeAndCode("P", "OP8", Boolean.FALSE);
			makeQie(row, exeStep, 32, item);
		}
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(33)))) {
			exeStep = exeStepDao.getByTypeAndCode("P", "OP9", Boolean.FALSE);
			makeQie(row, exeStep, 33, item);
		}
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(34)))) {
			exeStep = exeStepDao.getByTypeAndCode("P", "OP10", Boolean.FALSE);
			makeQie(row, exeStep, 34, item);
		}
		// end
		// begin other exestep
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(35)))) {
			exeStep = exeStepDao.getByTypeAndCode("M_C_DB", "MAI_CO_DB", Boolean.FALSE);
			makeQie(row, exeStep, 35, item);
		}
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(36)))) {
			exeStep = exeStepDao.getByTypeAndCode("WC", "WC", Boolean.FALSE);
			makeQie(row, exeStep, 36, item);
		}
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(37)))) {
			exeStep = exeStepDao.getByTypeAndCode("EDM", "EDM", Boolean.FALSE);
			makeQie(row, exeStep, 37, item);
		}
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(38)))) {
			exeStep = exeStepDao.getByTypeAndCode("EDM", "CD", Boolean.FALSE);
			makeQie(row, exeStep, 38, item);
		}
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(39)))) {
			String exeStepCode = dataFormatter.formatCellValue(row.getCell(39));
			exeStep = exeStepDao.getExeStepByCodeAndType(exeStepCode, "EDM");
			if (exeStep == null)
				throw new ResourceException("Không tồn tại hình thức gia công có mã là : " + exeStepCode);
			makeQie(row, exeStep, 0, item);
		}
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(48)))) {
			exeStep = exeStepDao.getByTypeAndCode("EN", "EN", Boolean.FALSE);
			makeQie(row, exeStep, 48, item);
		}
		String rootManagerCode = dataFormatter.formatCellValue(row.getCell(52));
		if (!Formater.isNull(rootManagerCode)) {
			if (checkWhileSpace(rootManagerCode))
				throw new ResourceException("Không được nhập ký tự trắng vào mã quản lý gốc: " + rootManagerCode);
			if (rootManagerCode.equals(managerCode))
				throw new ResourceException("Mã quản lý gốc không được trùng mã quản lý: " + rootManagerCode);
			if (quotationItemDao.getByManageCode(rootManagerCode, getSessionUser().getCompany()) == null)
				throw new ResourceException("Mã quản lý gốc: %s không tồn tại", new Object[] { rootManagerCode });
			item.setRootManageCode(rootManagerCode);
		}
		if (quotationItemDao.getByManageCode(managerCode, getSessionUser().getCompany()) != null)
			throw new ResourceException(String.format("Mã quản lý %s đã tồn tại ", managerCode));
		item.setManageCode(managerCode);
		return item;

	}

	private Pattern pattern;
	private Matcher matcher;
	private static final String REG_SPACE = "\\s";

	private boolean checkWhileSpace(String str) {
		if (Formater.isNull(str))
			return true;
		pattern = Pattern.compile(REG_SPACE);
		matcher = pattern.matcher(str);
		return matcher.find();
	}

	private QuotationItemExe makeQie(Row row, ExeStep exeStep, int numberValue, QuotationItem quotationItem)
			throws Exception {
		QuotationItemExe itemExe = new QuotationItemExe();
		itemExe.setExeStepId(exeStep);
		if (numberValue != 0) {
			try {
				itemExe.setQuotationExeTime(new BigDecimal(dataFormatter.formatCellValue(row.getCell(numberValue))));
			} catch (Exception e) {

				throw new ResourceException(
						"Sai định dạng dữ liệu : " + dataFormatter.formatCellValue(row.getCell(numberValue)));
			}
		} else {
			itemExe.setQuotationExeTime(new BigDecimal("0"));
		}
		if (exeStep.getInitPrice() != null)
			itemExe.setInitPrice(exeStep.getInitPrice());
		else {
			itemExe.setInitPrice(quotationItem.getFinalOpUnitPrice());
		}
		itemExe.setQuotationItemId(quotationItem);
		itemExe.setDisOrder((short) quotationItem.getQuotationItemAllExeList().size());
		quotationItem.getQuotationItemAllExeList().add(itemExe);
		return itemExe;
	}

	@Override
	public Boolean loadConcurrent() {
		return Boolean.TRUE;
	}
}
