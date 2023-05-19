package cic.h2h.controller.mes;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.icu.util.Calendar;

import cic.h2h.dao.hibernate.AstMachineDao;
import cic.h2h.dao.hibernate.CustomerManageDao;
import cic.h2h.dao.hibernate.ExeStepTypeDao;
import cic.h2h.dao.hibernate.QuotationItemDao;
import cic.h2h.dao.hibernate.WorkOrderDao;
import cic.h2h.dao.hibernate.WorkOrderExeDao;
import cic.h2h.form.WorkOderForm;
import cic.utils.ExportExcel;
import common.util.FormatNumber;
import common.util.Formater;
import common.util.ResourceException;
import entity.Company;
import entity.Customer;
import entity.ExeStepType;
import entity.ParentItem;
import entity.QuotationItem;
import entity.WorkOrder;
import entity.WorkOrderExe;
import entity.frwk.SysUsers;
import frwk.constants.RightConstants;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.CompanyDao;
import frwk.dao.hibernate.sys.RightUtils;
import frwk.dao.hibernate.sys.SysUsersDao;

@Controller
@RequestMapping(value = "/quanlyLSX")
public class CommandMangeController extends CatalogController<WorkOderForm, WorkOrder> {

	private static Logger log = Logger.getLogger(CommandMangeController.class);

	@Autowired
	private RightUtils rightUtils;
	@Autowired
	private WorkOrderDao workOrderDao;

	@Override
	public void createSearchDAO(HttpServletRequest request, WorkOderForm form, BaseDao<WorkOrder> dao)
			throws Exception {
		dao.createAlias("quotationItemExe", "qie");
		dao.createAlias("qie.quotationItemId", "qi");
		if (!Formater.isNull(form.getCompanyId()))
			dao.addRestriction(Restrictions.eq("qi.company.id", form.getCompanyId()));
		else {
			if (!rightUtils.haveRight(request, RightConstants.VIEW_DATA_ALL)) {
				dao.addRestriction(Restrictions.eq("qi.company.id", getSessionUser().getCompany().getId()));
			}
		}
		// Tim kiem dung ignore case
		if (!Formater.isNull(form.getDrawingCode()))
			dao.addRestriction(Restrictions.eq("qi.code", form.getDrawingCode().trim()).ignoreCase());

		// Tim kiem dung ignore case
		if (!Formater.isNull(form.getManagerCode()))
			dao.addRestriction(Restrictions.eq("qi.manageCode", form.getManagerCode().trim()).ignoreCase());

		if (!Formater.isNull(form.getItemName()))
			dao.addRestriction(
					Restrictions.like("qi.name", form.getItemName().trim(), MatchMode.ANYWHERE).ignoreCase());

		if (!Formater.isNull(form.getWorkCode()))
			dao.addRestriction(Restrictions.like("code", form.getWorkCode().trim(), MatchMode.ANYWHERE).ignoreCase());

		if (ParentItem.WORK_STS_PENDDING.equals(form.getWorkSts())) {
			dao.addRestriction(Restrictions.sqlRestriction(
					"not exists (select 1 from work_order_exe woe where woe.work_order_id = {alias}.id)"));
		} else if (ParentItem.WORK_STS_DOING.equals(form.getWorkSts())) {
			dao.addRestriction(Restrictions.and(
					Restrictions.sqlRestriction(
							"exists (select 1 from work_order_exe woe where woe.work_order_id = {alias}.id)"),
					Restrictions.neProperty("amount", "numOfFinishChildren")));

		} else if (ParentItem.WORK_STS_FINISH.equals(form.getWorkSts())) {
			dao.addRestriction(Restrictions.eqProperty("amount", "numOfFinishChildren"));
		}

		if (!Formater.isNull(form.getMachineCode()))
			dao.addRestriction(Restrictions.eq("astMachine.id", form.getMachineCode().trim()));
		if (!Formater.isNull(form.getFrDate()))
			dao.addRestriction(Restrictions.ge("createDate", Formater.str2date(form.getFrDate())));
		if (!Formater.isNull(form.getToDate())) {
			Calendar t = Calendar.getInstance();
			t.setTime(Formater.str2date(form.getToDate()));
			t.add(Calendar.DATE, 1);
			dao.addRestriction(Restrictions.lt("createDate", t.getTime()));
		}
		if (!Formater.isNull(form.getCreator()))
			dao.addRestriction(Restrictions.eq("creator.id", form.getCreator()));
		if ("QCO".equals(form.getTo())) {
			dao.createAlias("qie.exeStepId", "es");
			ExeStepType os = exeStepTypeDao.getByCode("OS");
			dao.addRestriction(Restrictions.eq("es.stepType", os));
			// So luong da hoan thanh lon hon so luong da chuyen QC
			dao.addRestriction(Restrictions.gtProperty("numOfFinishChildren", "qcChkAmount"));
		}

		if (!Formater.isNull(form.getOrderCode()) || !Formater.isNull(form.getCusName())) {
			dao.createAlias("qi.quotationId", "q");
			if (!Formater.isNull(form.getOrderCode())) {
				if (!Formater.isNull(form.getOrderCode()))
					dao.addRestriction(
							Restrictions.like("q.code", form.getOrderCode().trim(), MatchMode.ANYWHERE).ignoreCase());
				SysUsers su = getSessionUser();
				if (Boolean.TRUE.equals(su.getIsPartner()))
					dao.addRestriction(Restrictions.eq("q.customer.id", su.getPartner().getId()));
			}
			if (!Formater.isNull(form.getCusName()))
				dao.addRestriction(Restrictions.eq("q.customer.id", form.getCusName()));
		}

		dao.addOrder(Order.desc("createDate"));
		dao.addOrder(Order.asc("productionLineId"));
		dao.addOrder(Order.asc("qie.disOrder"));
	}

	@Override
	public void preSearch(ModelMap model, HttpServletRequest request, HttpServletResponse response, WorkOderForm form) {
		form.setSessionUser(getSessionUser());
	}

	@Override
	protected void pushToJa(JSONArray ja, WorkOrder e, WorkOderForm modelForm) throws Exception {
		QuotationItem qi = e.getQuotationItemExe().getQuotationItemId();
		if (Boolean.TRUE.equals(modelForm.getSessionUser().getIsPartner())) {
			ja.put(e.getCode());
			// Ma don hang
			ja.put(qi.getQuotationId().getCode());
		} else {
			if ("QCO".equals(modelForm.getTo())) {
				ja.put(String.format(
						"<a class='characterwrap' title='Thực hiện QC gia công ngoài' href='javascript:;' onclick='window.open(\"QcChkOutSrc?hiddenMenu=true&workOrderId=%s\",\"\",\"%s\")'>%s<a>",
						new Object[] { e.getId(), "width=1000, height=700, status=yes, scrollbars=yes", e.getCode() }));
				// Ma don hang
				ja.put(String.format("<span title='" + qi.getQuotationId().getCustomer().getOrgName()
						+ "' class='characterwrap' title='Chi tiết đơn hàng' href='javascript:;' onclick='window.open(\"item?quotationId=%s\")'>%s<span>",
						new Object[] { qi.getQuotationId().getId(), qi.getQuotationId().getCode() }));
			} else {
				if ("OS".equals(e.getQuotationItemExe().getExeStepId().getStepType().getCode())) {
					if (e.getReadyOsAmount() > 0 || !e.getWorkOrderExes().isEmpty())
						ja.put("<a class='characterwrap' title='Chuyển gia công ngoài' href = 'workOderDetail?workOrderId="
								+ e.getId() + "'>" + e.getCode() + "</a>");
					else
						ja.put(e.getCode());
				} else
					ja.put("<a class='characterwrap' title='Thực hiện sản xuất' href = 'workOderDetail?workOrderId="
							+ e.getId() + "'>" + e.getCode() + "</a>");// Ma don hang
				ja.put(String.format("<a title='" + qi.getQuotationId().getCustomer().getOrgName()
						+ "' class='characterwrap' title='Chi tiết đơn hàng' href='javascript:;' onclick='window.open(\"item?quotationId=%s\")'>%s<a>",
						new Object[] { qi.getQuotationId().getId(), qi.getQuotationId().getCode() }));
			}

		}

		// Ma ban ve
		ja.put(String.format("<span title='Tên chi tiết: %s'>%s</span>", new Object[] { qi.getName(), qi.getCode() }));

		if ("QCO".equals(modelForm.getTo()) || Boolean.TRUE.equals(modelForm.getSessionUser().getIsPartner())) {
			ja.put(qi.getManageCode());
		} else {
			ja.put(String.format(
					"<a class='characterwrap' title='Tình trạng bản vẽ' href='javascript:;' onclick='window.open(\"quanlyCT?manageCodeSearch=%s\")'>%s<a>",
					new Object[] { qi.getManageCode(), qi.getManageCode() }));
		}
		// Cong doan
		if (!Formater.isNull(e.getQuotationItemExe().getId())) {
			if ("QCO".equals(modelForm.getTo())) {
				ja.put(String.format("<font title='Hình thức gia công: %s'>%s</font>",
						new Object[] { e.getQuotationItemExe().getExeStepId().getStepType().getName(),
								e.getQuotationItemExe().getExeStepId().getStepName() }));
			} else {
				ja.put(String.format("<font title='Hình thức gia công: %s'>%s</font>",
						new Object[] { e.getQuotationItemExe().getExeStepId().getStepType().getName(),
								e.getQuotationItemExe().getExeStepId().getStepName() }));
			}

		} else
			ja.put("");
		// Gia cong ngoai
		if ("QCO".equals(modelForm.getTo())) {
			// so luong SX da chuyen
			ja.put(FormatNumber.num2Str(e.getNumOfFinishItem().longValue()));
			long outstanding = e.getNumOfFinishItem().longValue();
			// So luong qc da xu ly
			ja.put(FormatNumber.num2Str(e.getQcChkAmount()));
			outstanding -= e.getQcChkAmount();
			// So luong da chuyen gia cong ngoai
			ja.put(FormatNumber.num2Str(e.getTotalToOs()));
			ja.put(FormatNumber.num2Str(outstanding));
			return;
		}

		ja.put(String.format("<font title='Ngày tạo lệnh: %s'>%s</font>",
				new Object[] { Formater.date2strDateTime(e.getCreateDate()), e.getAmountStr() }));
		ja.put(Formater.num2str(e.getTotalEstTime()));
		ja.put(Formater.date2strDateTime(e.getStartTime()));
		ja.put(Formater.date2strDateTime(e.getEndTime()));
		ja.put(FormatNumber.num2Str(e.getExeSetupTime()));
		// Tong so chi tiet
		String amountDetail = "<span title='Tổng/OK/NG/Hủy'>%s</span>";
		String sumaryInf = e.getTotalExeAmountStr() + "/" + FormatNumber.num2Str(e.getNumOfFinishChildren());
		// Tong NG
		if (e.getTodoNgAmount() != null && e.getTodoNgAmount() > 0) {
			sumaryInf += "/" + "<font color ='red'>" + FormatNumber.num2Str(e.getTodoNgAmount()) + "</front>";
		} else {
			sumaryInf += "/" + FormatNumber.num2Str(e.getTodoNgAmount());
		}
		// Tong huy
		if (e.getTotalBrokenAmount().compareTo(new BigDecimal(0)) > 0) {
			sumaryInf += "/" + "<font color ='red'>" + FormatNumber.num2Str(e.getTotalBrokenAmount()) + "</front>";
		} else {
			sumaryInf += "/" + FormatNumber.num2Str(e.getTotalBrokenAmount());
		}
		ja.put(String.format(amountDetail, sumaryInf));

		// Thoi gian thuc hien
		ja.put(e.getEstTimeStr() + "/" + e.getExeTimeStr());
		// Cham
		if (e.getLateTime().compareTo(new BigDecimal(0)) > 0) {
			ja.put("<font color ='red'>" + e.getLateTimeStr() + "</front>");
			ja.put("<font color ='red'>" + e.getLatePercentStr() + "</front>");
		} else {
			ja.put(e.getLateTimeStr());
			if (e.getEstTime() == null || e.getEstTime().compareTo(new BigDecimal(0)) == 0)
				ja.put("");
			else
				ja.put(e.getLatePercentStr());
		}

		// So luong con lai
		if (e.getTodoAmount() == null || e.getTodoAmount().compareTo(new BigDecimal(0)) <= 0)
			ja.put("<font title='Đã hoàn thành' color='blue'>" + FormatNumber.num2Str(e.getTodoAmount()) + "</font>");
		else
			ja.put(FormatNumber.num2Str(e.getTodoAmount()));
		// Thoi gian con lai
		if (e.getTotalEstTime() != null)
			ja.put(FormatNumber.num2Str(e.getTotalEstTime().subtract(e.getExeTime())));
		else
			ja.put("");

	}

	@Override
	public BaseDao<WorkOrder> getDao() {

		return workOrderDao;
	}

	@Override
	public String getJsp() {
		return "ke_hoach_san_xuat/quan_ly_lenh_san_xuat";
	}

	public static final DataFormatter dataFormatter = new DataFormatter();

	@Autowired
	private WorkOrderExeDao workOrderExeDao;

	@Autowired
	private SysUsersDao sysUsersDao;

	@RequestMapping(value = "upload", method = RequestMethod.POST)
	public void upload(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			@RequestParam("inputFile") MultipartFile inputFile, @RequestParam("fileName") String fileName)
			throws Exception {
		Workbook wb = fileName.endsWith("xlsx") ? new XSSFWorkbook(inputFile.getInputStream())
				: new HSSFWorkbook(inputFile.getInputStream());
		Sheet sheet = wb.getSheetAt(0);
		// Du lieu bat dau tu dong 4
		int iRowIdex = 3;
		while (true) {
			Row row = sheet.getRow(iRowIdex);
			if (row == null)
				return;
			String data = dataFormatter.formatCellValue(row.getCell(0));
			// ExcelUtils.getString(row.getCell((short) 0)); // Kiem tra cot 1 rong thi stop
			if ("eof".equals(data) || Formater.isNull(data))
				return;
			try {
				WorkOrderExe workOrderExe = createTransFromRow(row);
				workOrderExe.setUpdator(getSessionUser());
				workOrderExeDao.save(workOrderExe);
			} catch (ResourceException ex) {
//				throw new ResourceException(String.format("Lỗi đọc dữ liệu dòng %s, chi tiết lỗi: %s",
//						new Object[] { iRowIdex, ex.getMessage() }));
				String error = String.format("Lỗi đọc dữ liệu dòng %s, chi tiết lỗi: %s",
						new Object[] { iRowIdex, ex.getMessage() });
				rs.setContentType("text/html;charset=utf-8");
				PrintWriter pw = rs.getWriter();
				pw.print(error);
				pw.flush();
				pw.close();
			}
			iRowIdex++;
		}
	}

	private WorkOrderExe createTransFromRow(Row row) throws Exception {
		String code = dataFormatter.formatCellValue(row.getCell(1));
		WorkOrder workOrder = workOrderDao.getWorkOrderByCode(code, getSessionUser().getCompany());
		if (workOrder == null)
			throw new ResourceException("Không tồn tại lệnh sản xuất");
		WorkOrderExe order = new WorkOrderExe();
		order.setWorkOrderId(workOrder);
		String username = dataFormatter.formatCellValue(row.getCell(2));
		if (Formater.isNull(username))
			throw new ResourceException("Chưa nhập thông tin nhân viên");
		SysUsers sysUsers = sysUsersDao.getSysUserByUserName(username);
		if (sysUsers == null)
			throw new ResourceException("Không tồn tại nhân viên");
		order.setSysUser(sysUsers);

		BigDecimal setupTime = new BigDecimal("0");
		try {
			if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(6)))) {
				setupTime = setupTime.add(new BigDecimal(dataFormatter.formatCellValue(row.getCell(5))));
				order.setSetupTime(setupTime);
			}
		} catch (Exception e) {

			throw new ResourceException(
					"Không đúng định dạng dữ liệu : " + dataFormatter.formatCellValue(row.getCell(5)));
		}
		try {
			String startTime = dataFormatter.formatCellValue(row.getCell(5));
			if (Formater.isNull(startTime))
				throw new ResourceException("Chưa nhập thời điểm bắt đẩu!");
			order.setStartTime(Formater.str2DateTime(startTime));
			String endTime = dataFormatter.formatCellValue(row.getCell(6));
			if (Formater.isNull(endTime))
				throw new ResourceException("Chưa nhập thời điểm kết thúc!");
			order.setEndTime(Formater.str2DateTime(endTime));
			BigDecimal exeTime = new BigDecimal("0");
			exeTime = exeTime
					.add(new BigDecimal((order.getEndTime().getTime() - order.getStartTime().getTime()) / 1000 / 60));
			exeTime = exeTime.subtract(setupTime);
			order.setExeTime(exeTime);
			if (order.getExeTime().compareTo(new BigDecimal(0)) <= 0)
				throw new ResourceException("Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc!");
		} catch (Exception e) {
			throw new ResourceException("Không đúng định dạng ngày tháng");
		}
		try {
			order.setTotalAmountStr(dataFormatter.formatCellValue(row.getCell(9)));
		} catch (Exception e) {
			throw new ResourceException(
					"Không đúng định dạng dữ liệu : " + dataFormatter.formatCellValue(row.getCell(9)));
		}
		try {
			order.setNgAmount(new Long(dataFormatter.formatCellValue(row.getCell(10))));
		} catch (Exception e) {

			throw new ResourceException(
					"Không đúng định dạng dữ liệu : " + dataFormatter.formatCellValue(row.getCell(10)));
		}
		try {
			order.setAmount(new BigDecimal(dataFormatter.formatCellValue(row.getCell(11))));
		} catch (Exception e) {

			throw new ResourceException(
					"Không đúng định dạng dữ liệu : " + dataFormatter.formatCellValue(row.getCell(11)));
		}
		order.setNgDescription(dataFormatter.formatCellValue(row.getCell(12)));
		return order;
	}

	@Autowired
	private AstMachineDao astMachineDao;
	@Autowired
	private QuotationItemDao quotationItemDao;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, WorkOderForm form)
			throws Exception {
		SysUsers su = getSessionUser();
		if (Boolean.TRUE.equals(su.getIsPartner())) {
			model.addAttribute("lstCompany", companyDao.getAllOrderAstName());
		} else {
			if (rightUtils.haveRight(rq, RightConstants.VIEW_DATA_ALL)) {
				model.addAttribute("lstCompany", companyDao.getAllOrderAstName());
			} else {
				model.addAttribute("lstCompany", Arrays.asList(getSessionUser().getCompany()));
			}
			form.setCompanyId(su.getCompany().getId());
			model.addAttribute("lstSysUser", sysUsersDao.getCompanyUser(getSessionUser().getCompany()));
			model.addAttribute("lstAstMachine", astMachineDao.getAll(getSessionUser().getCompany()));
		}
		model.addAttribute("listExeStepType", exeStepTypeDao.getAll());
		if (form.getQuotationItem() != null && !Formater.isNull(form.getQuotationItem().getId())) {
			quotationItemDao.load(form.getQuotationItem());
			form.setOrderCode(form.getQuotationItem().getQuotationId().getCode());
			form.setDrawingCode(form.getQuotationItem().getCode());
			form.setItemName(form.getQuotationItem().getName());
			form.setManagerCode(form.getQuotationItem().getManageCode());
		}
		model.addAttribute("customers", customerManageDao.getByType(Customer.IS_CUSTOMER));
	}

	@Autowired
	private CustomerManageDao customerManageDao;

	@Autowired
	private CompanyDao companyDao;
	@Autowired
	private ExeStepTypeDao exeStepTypeDao;

	public WorkOrderDao getWorkOrderDao() {
		return workOrderDao;
	}

	public void setWorkOrderDao(WorkOrderDao workOrderDao) {
		this.workOrderDao = workOrderDao;
	}

	@Autowired
	private ExportExcel exportExcel;

	public void exportExcel(ModelMap model, HttpServletRequest rq, HttpServletResponse response, WorkOderForm form)
			throws Exception {
		if (Formater.isNull(form.getFrDate()))
			throw new ResourceException("Cần nhập ngày tạo từ");
		Date toDate = Formater.isNull(form.getToDate()) ? Calendar.getInstance().getTime()
				: Formater.str2date(form.getToDate());
		Calendar cTodate = Calendar.getInstance();
		cTodate.setTime(toDate);
		cTodate.add(Calendar.MONTH, -3);
		cTodate.add(Calendar.DATE, -1);
		if (cTodate.getTime().after(Formater.str2date(form.getFrDate())))
			throw new ResourceException("Khoảng thời gian ngày từ - đến phải nhỏ hơn 3 tháng");

		WorkOrderDao dao = (WorkOrderDao) getDao().createCriteria();
		createSearchDAO(rq, form, dao);
		List<?> temp = (List<?>) dao.search();
		if (temp.isEmpty())
			throw new ResourceException("Không tồn tại dữ liệu xuất!");
		List<WorkOrder> workOrders = new ArrayList<WorkOrder>();
		for (Object e : temp) {
			if (e.getClass().isArray()) {
				for (Object o : (Object[]) e) {
					if (o.getClass().equals(dao.getModelClass())) {
						workOrders.add((WorkOrder) o);
					}
				}
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("reports", workOrders);
		map.put("workCode", form.getWorkCode());
		map.put("orderCode", form.getOrderCode());
		map.put("cusName", form.getCusName());
		map.put("drawCodeSearch", form.getDrawingCode());
		map.put("itemName", form.getItemName());
		map.put("manageCodeSearch", form.getManagerCode());
		map.put("frDate", form.getFrDate());
		map.put("toDate", form.getToDate());
		map.put("machineCode", form.getMachineCode());
		if (!Formater.isNull(form.getWorkSts())) {
			if (form.getWorkSts() == -1)
				map.put("workSts", "Chưa thực hiện");
			else if (form.getWorkSts() == 0)
				map.put("workSts", "Đang thực hiện");
			else if (form.getWorkSts() == 1)
				map.put("workSts", "Đã hoàn thành");
		}
		exportExcel.export("Danh sach lenh sx", response, map);
	}

	@Override
	public Boolean loadConcurrent() {
		return Boolean.TRUE;
	}

	public void loadResource(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			WorkOderForm form) throws Exception {
		String companyId = request.getParameter("companyId");
		Company company = companyDao.get(companyId);
		company.setLstAstMachine(astMachineDao.getAll(company));
		company.setLstSysUsers(sysUsersDao.getCompanyUser(company));
		returnJson(response, company);
	}
}
