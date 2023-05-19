package cic.h2h.controller.mes;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import cic.h2h.dao.hibernate.WorkOrderDao;
import cic.h2h.dao.hibernate.WorkOrderExeDao;
import cic.h2h.form.WorkOrderExeForm;
import cic.utils.ExportExcel;
import common.constants.Constants;
import common.util.FormatNumber;
import common.util.Formater;
import common.util.ResourceException;
import constants.RightConstants;
import entity.AstMachine;
import entity.Customer;
import entity.ExeStepType;
import entity.QuotationItem;
import entity.QuotationItemExe;
import entity.WorkOrder;
import entity.WorkOrderExe;
import entity.frwk.SysUsers;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.CompanyDao;
import frwk.dao.hibernate.sys.RightUtils;
import frwk.dao.hibernate.sys.SysDictParamDao;
import frwk.dao.hibernate.sys.SysUsersDao;

@Controller
@RequestMapping(value = "/historyPro")
public class ProductionHistoryController extends frwk.controller.SearchController<WorkOrderExeForm, WorkOrderExe> {
	@Autowired
	private RightUtils rightUtils;
	@Autowired
	private WorkOrderExeDao workOrderExeDao;

	@Override
	public void createSearchDAO(HttpServletRequest request, WorkOrderExeForm form, BaseDao<WorkOrderExe> dao)
			throws Exception {
		// Tim kiem tren woe
		if (!Formater.isNull(form.getCompanyId()))
			dao.addRestriction(Restrictions.eq("company.id", form.getCompanyId()));
		else if (!rightUtils.haveRight(request, RightConstants.VIEW_DATA_ALL))
			dao.addRestriction(Restrictions.eq("company", getSessionUser().getCompany()));
		if (!Formater.isNull(form.getShift()) || !Formater.isNull(form.getFactoryUnit())) {
			dao.createAlias("sysUser", "su");
			if (!Formater.isNull(form.getShift()))
				dao.addRestriction(Restrictions.eq("su.shift", form.getShift().trim()));
			if (!Formater.isNull(form.getFactoryUnit()))
				dao.addRestriction(Restrictions.eq("su.factoryUnit", form.getFactoryUnit().trim()));
		}
		if (!Formater.isNull(form.getWorker()))
			dao.addRestriction(Restrictions.eq("sysUser.id", form.getWorker()));

		if (!Formater.isNull(form.getFrDate()))
			dao.addRestriction(Restrictions.ge("startTime", Formater.str2DateTime(form.getFrDate())));
		if (!Formater.isNull(form.gettDate()))
			dao.addRestriction(Restrictions.le("endTime", Formater.str2DateTime(form.gettDate())));

		if (!Formater.isNull(form.getUpdatorSearch()))
			dao.addRestriction(Restrictions.eq("updator.id", form.getUpdatorSearch()));
		if (!Formater.isNull(form.getMachineSearch()))
			dao.addRestriction(Restrictions.eq("astMachine.id", form.getMachineSearch()));

		if ("1".equals(form.getTypeWorkExe()))
			dao.addRestriction(Restrictions.eq("ngRepaire", Boolean.TRUE));
		else if ("0".equals(form.getTypeWorkExe()))
			dao.addRestriction(
					Restrictions.or(Restrictions.eq("ngRepaire", Boolean.FALSE), Restrictions.isNull("ngRepaire")));
		// Tim kiem tren wo
		boolean woAlias = false;
		if (!Formater.isNull(form.getWoCode())) {
			woAlias = true;
			dao.createAlias("workOrderId", "wo");
			dao.addRestriction(Restrictions.like("wo.code", form.getWoCode().trim(), MatchMode.ANYWHERE).ignoreCase());
		}

		// Tim kiem tren qie (step)
		boolean qieAlias = false;
		if ("-1".equals(form.getTypeWorkExe()) || (form.getSource() != null && form.getSource() != -1)
				|| !Formater.isNull(form.getStepName())) {
			if (!woAlias) {
				woAlias = true;
				dao.createAlias("workOrderId", "wo");
			}

			qieAlias = true;
			dao.createAlias("wo.quotationItemExe", "qie");
			dao.createAlias("qie.exeStepId", "step");
			// Gia cong ngoai
			if ("-1".equals(form.getTypeWorkExe())) {
				ExeStepType os = exeStepTypeDao.getByCode("OS");
				dao.addRestriction(Restrictions.eq("step.stepType", os));
			}
			if (!Formater.isNull(form.getStepName()))
				dao.addRestriction(Restrictions.eq("step.stepType", new ExeStepType(form.getStepName().trim())));
			if (form.getSource() != null) {
				ExeStepType plasticType = exeStepTypeDao.getByCode("EN");
				if (form.getSource() == 1) {
					dao.addRestriction(Restrictions.eq("step.stepType", plasticType));
				} else if (form.getSource() == 0) {
					dao.addRestriction(Restrictions.ne("step.stepType", plasticType));
				}
			}

		}

		// Tim kiem tren qi
		boolean qiAlias = false;
		if (!Formater.isNull(form.getDrawingCode()) || !Formater.isNull(form.getManageCode())) {
			if (!qieAlias) {
				if (!woAlias) {
					woAlias = true;
					dao.createAlias("workOrderId", "wo");
				}
				dao.createAlias("wo.quotationItemExe", "qie");
				qieAlias = true;
			}
			qiAlias = true;
			dao.createAlias("qie.quotationItemId", "qi");
			// Tim kiem dung, ignore case
			if (!Formater.isNull(form.getDrawingCode()))
				dao.addRestriction(Restrictions.eq("qi.code", form.getDrawingCode()).ignoreCase());
			// Tim kiem dung, ignore case
			if (!Formater.isNull(form.getManageCode()))
				dao.addRestriction(Restrictions.eq("qi.manageCode", form.getManageCode()).ignoreCase());
		}

		// Tim kiem tren q
		if (!Formater.isNull(form.getOrderCode()) || !Formater.isNull(form.getFromDate())
				|| !Formater.isNull(form.getToDate()) || !Formater.isNull(form.getCusCode())) {
			if(!qiAlias) {
				if (!qieAlias) {
					if (!woAlias) {
						woAlias = true;
						dao.createAlias("workOrderId", "wo");
					}
					dao.createAlias("wo.quotationItemExe", "qie");
					qieAlias = true;
				}
				qiAlias = true;
				dao.createAlias("qie.quotationItemId", "qi");
			}
			dao.createAlias("qi.quotationId", "q");
			if (!Formater.isNull(form.getOrderCode()))
				dao.addRestriction(
						Restrictions.like("q.code", form.getOrderCode().trim(), MatchMode.ANYWHERE).ignoreCase());
			if (!Formater.isNull(form.getFromDate()))
				dao.addRestriction(Restrictions.ge("q.quotationDate", Formater.str2date(form.getFromDate())));
			if (!Formater.isNull(form.getToDate())) {
				Calendar cTodatePlusOne = Calendar.getInstance();
				cTodatePlusOne.setTime(Formater.str2date(form.getToDate()));
				cTodatePlusOne.add(Calendar.DATE, 1);
				dao.addRestriction(Restrictions.lt("q.quotationDate", cTodatePlusOne.getTime()));
			}
			if (!Formater.isNull(form.getCusCode()))
				dao.addRestriction(Restrictions.eq("q.customer.id", form.getCusCode()));
		}
		dao.addOrder(Order.desc("createDate"));
	}

	@Override
	public Boolean loadConcurrent() {
		return Boolean.TRUE;
	}

	@Override
	protected void pushToJa(JSONArray ja, WorkOrderExe e, WorkOrderExeForm modelForm) throws Exception {
		ja.put("<a class='characterwrap' href = 'workOderDetail?workOrderId=" + e.getWorkOrderId().getId() + "'>"
				+ e.getWorkOrderId().getCode() + "</a>");
		QuotationItemExe qie = e.getWorkOrderId().getQuotationItemExe();
		QuotationItem qi = qie.getQuotationItemId();
		ja.put(String.format("<font class='characterwrap' title='Tên chi tiết: %s'>%s</font>",
				new Object[] { qi.getName(), qi.getCode() }));
		ja.put(String.format(Constants.WRAP_CHAR_SPAN_HTML, qi.getManageCode()));
		// Cong doan
		if (!Formater.isNull(qie.getId())) {
			ja.put(qie.getExeStepId().getFullName());
		} else
			ja.put("");
		ja.put(e.getSysUser().getName());
		ja.put(Formater.dateTime2str(e.getStartTime()));
		ja.put(Formater.dateTime2str(e.getEndTime()));
		if ("OS".equals(qie.getExeStepId().getStepCode())) {
			ja.put("");
			ja.put("");
			ja.put("");
			ja.put("");
			ja.put("");
			ja.put(e.getTotalAmountStr());
			ja.put("");
			ja.put("");
			ja.put("");
		} else {
			ja.put(FormatNumber.num2Str(e.getExeSetupTime()));
			if (Boolean.TRUE.equals(e.getNgRepaire()))
				ja.put("");
			else
				ja.put(e.getEstTimeStr());
			ja.put(e.getExeTimeStr());
			// Hang sua khong tinh nhanh/cham
			if (Boolean.TRUE.equals(e.getNgRepaire())) {
				ja.put("");
				ja.put("");
			} else {
				double latePercent = e.getLatePercent().doubleValue();
				if (latePercent > 0) {
					ja.put(String.format("<font color='red'>%s</font>", e.getLatePercentStr()));
					ja.put(String.format("<font color='red'>%s</font>", e.getLateTimeStr()));
				} else {
					ja.put(e.getLatePercentStr());
					ja.put(e.getLateTimeStr());
				}
			}
			ja.put(e.getTotalAmountStr());
			ja.put(e.getAmountStr());
			if (e.getNgAmount() != null && e.getNgAmount() > 0) {
				ja.put(String.format("<font color='red'>%s</font>", e.getNgAmountStr()));
			} else {
				ja.put(e.getNgAmountStr());
			}
			if (e.getBrokenAmount() != null && e.getBrokenAmount() > 0) {
				ja.put(String.format("<font color='red'>%s</font>", FormatNumber.num2Str(e.getBrokenAmount())));
			} else {
				ja.put("0");
			}
		}
		ja.put(e.getUpdator() == null ? "" : e.getUpdator().getName());

		if ("OS".equals(qie.getExeStepId().getStepType().getCode())) {
			ja.put("<a title='Chuyển QC gia công ngoài' style='color:blue;' href='javascipt:;' onclick = 'edit(\""
					+ e.getId() + "\",\"OS\")'>Chuyển QC</a>");
		} else {
			ja.put("<a title='Chỉnh sửa kết quả sản xuất' style='color:blue;' href='javascipt:;' onclick = 'edit(\""
					+ e.getId() + "\",\"NOTOS\")'>" + (Boolean.TRUE.equals(e.getNgRepaire()) ? "Sửa NG" : "Gia công")
					+ "</a>");
		}
	}

	@Override
	public BaseDao<WorkOrderExe> getDao() {
		return workOrderExeDao;
	}

	@Override
	public String getJsp() {
		return "ke_hoach_san_xuat/lich_su_san_xuat";
	}

	@Autowired
	private SysUsersDao sysUsersDao;
	@Autowired
	private ExeStepTypeDao exeStepTypeDao;
	@Autowired
	private AstMachineDao astMachineDao;

	@Autowired
	private SysDictParamDao sysDictParamDao;
	@Autowired
	private CompanyDao companyDao;
	@Autowired
	private CustomerManageDao customerManageDao;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, WorkOrderExeForm form)
			throws Exception {
		SysUsers su = getSessionUser();
		if (Boolean.TRUE.equals(su.getIsPartner())) {
			model.addAttribute("lstCompany", companyDao.getAllOrderAstName());
			model.addAttribute("customers", Arrays.asList(getSessionUser().getPartner()));
		} else {
			if (rightUtils.haveRight(rq, RightConstants.VIEW_DATA_ALL)) {
				model.addAttribute("lstCompany", companyDao.getAllOrderAstName());
			} else {
				model.addAttribute("lstCompany", Arrays.asList(getSessionUser().getCompany()));
			}
			form.setCompanyId(su.getCompany().getId());
			model.addAttribute("lstSysUser", sysUsersDao.getCompanyUser(su.getCompany()));
			model.addAttribute("lstAstMachine", astMachineDao.getAll(su.getCompany()));

			model.addAttribute("customers", customerManageDao.getByType(Customer.IS_CUSTOMER));
		}
		model.addAttribute("lstStepType", exeStepTypeDao.getAll());
		model.addAttribute("lstErrorCause", sysDictParamDao.getByType("ERROR_CAUSE_EXE"));
		model.addAttribute("lstShift", sysDictParamDao.getByType("SHIFTS"));
		model.addAttribute("lstFactoryUnit", sysDictParamDao.getByType("FACTORY_UNIT"));
		form.setSource((byte) -1);
	}

	public WorkOrderExeDao getWorkOrderExeDao() {
		return workOrderExeDao;
	}

	public void setWorkOrderExeDao(WorkOrderExeDao workOrderExeDao) {
		this.workOrderExeDao = workOrderExeDao;
	}

	public void delWorkOrderExe(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			WorkOrderExeForm form) throws Exception {
		String workOrderExeId = request.getParameter("workOrderExeId");
		WorkOrderExe orderExe = workOrderExeDao.get(WorkOrderExe.class, workOrderExeId);
		if (Formater.isNull(workOrderExeId) || orderExe == null)
			throw new ResourceException("Không tồn tại lệnh sản xuất!");
		form.setWorkOrderExe(orderExe);
		workOrderExeDao.del(form.getWorkOrderExe());

	}

	public static final DataFormatter dataFormatter = new DataFormatter();

	@RequestMapping(value = "upload", method = RequestMethod.POST)
	public void upload(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			@RequestParam("inputFile") MultipartFile inputFile, @RequestParam("fileName") String fileName)
			throws Exception {
		Workbook wb = fileName.endsWith("xlsx") ? new XSSFWorkbook(inputFile.getInputStream())
				: new HSSFWorkbook(inputFile.getInputStream());
		Sheet sheet = wb.getSheetAt(0);
		// Du lieu bat dau tu dong 4
		int iRowIdex = 3;
		Date createdDate = Calendar.getInstance().getTime();
		while (true) {
			Row row = sheet.getRow(iRowIdex);
			if (row == null)
				return;
			String data = dataFormatter.formatCellValue(row.getCell(1));
			// ExcelUtils.getString(row.getCell((short) 0)); // Kiem tra cot 2 rong thi stop
			if ("eof".equals(data) || Formater.isNull(data))
				return;
			try {
				WorkOrderExe workOrderExe = makeWoe(row);
				workOrderExe.setUpdator(getSessionUser());
				workOrderExe.setCreateDate(createdDate);
				workOrderExeDao.save(workOrderExe);
			} catch (ResourceException ex) {
				String error = String.format("Lỗi đọc dữ liệu dòng %s, chi tiết lỗi: %s",
						new Object[] { (iRowIdex + 1), ex.getMessage() });
				returnTxtHtml(rs, error);
				throw ex;
			}
			iRowIdex++;
		}

	}

	@Autowired
	private WorkOrderDao workOrderDao;

	private WorkOrderExe makeWoe(Row row) throws Exception {
		String code = dataFormatter.formatCellValue(row.getCell(2));
		WorkOrder workOrder = workOrderDao.getWorkOrderByCode(code, getSessionUser().getCompany());
		if (workOrder == null)
			throw new ResourceException("Không tồn tại lệnh sản xuất! : " + code);
		WorkOrderExe woExe = new WorkOrderExe(workOrder);
		String username = dataFormatter.formatCellValue(row.getCell(3));
		if (Formater.isNull(username))
			throw new ResourceException("Chưa nhập thông tin nhân viên");
		SysUsers sysUsers = sysUsersDao.getSysUserByUserName(username);
		if (sysUsers == null)
			throw new ResourceException("Không tồn tại nhân viên :" + username);
		woExe.setSysUser(sysUsers);
		String machineCode = dataFormatter.formatCellValue(row.getCell(5));
		if (Formater.isNull(machineCode))
			throw new ResourceException("Chưa nhập thông tin mã máy");
		AstMachine astMachine = astMachineDao.getMachineByCode(machineCode, getSessionUser().getCompany());
		if (astMachine == null)
			throw new ResourceException("Không tồn tại máy có mã :" + machineCode);
		woExe.setAstMachine(astMachine);
		BigDecimal setupTime = new BigDecimal("0");
		try {
			if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(6)))) {
				setupTime = setupTime.add(new BigDecimal(dataFormatter.formatCellValue(row.getCell(6))));
				woExe.setSetupTime(setupTime);
			}
		} catch (Exception e) {

			throw new ResourceException(
					"Không đúng định dạng dữ liệu : " + dataFormatter.formatCellValue(row.getCell(6)));
		}
		String startTime = dataFormatter.formatCellValue(row.getCell(7));
		if (Formater.isNull(startTime))
			throw new ResourceException("Chưa nhập thời điểm bắt đầu");
		woExe.setStartTime(Formater.str2DateTime(startTime));
		String endTime = dataFormatter.formatCellValue(row.getCell(8));
		if (Formater.isNull(endTime))
			throw new ResourceException("Chưa nhập thời điểm kết thúc!");
		woExe.setEndTime(Formater.str2DateTime(endTime));
		if (woExe.getSetupTime() != null) {
			if (new BigDecimal(0).compareTo(woExe.getSetupTime()) > 0)
				throw new ResourceException("Thời gian setup phải >=0");
			if (woExe.getExeTime().compareTo(new BigDecimal(0)) <= 0)
				throw new ResourceException("Khoảng thời gian bắt đầu, kết thúc phải lớn hơn thời gian Setup!");
		} else {
			if (woExe.getExeTime().compareTo(new BigDecimal(0)) <= 0)
				throw new ResourceException("Thời gian bắt đầu phải nhỏ hơn thời gian kết thúc!");
		}
		woExe.setNgRepaire("x".equals(dataFormatter.formatCellValue(row.getCell(14))) ? true : false);
		if (woExe.getNgRepaire()) {
			if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(12))))
				throw new ResourceException("Sửa NG không được nhập số lượng lỗi (NG)");
			try {
				woExe.setNgAmount(null);
			} catch (Exception e) {

				throw new ResourceException(
						"Không đúng định dạng dữ liệu : " + dataFormatter.formatCellValue(row.getCell(11)));
			}
		} else {
			try {
				woExe.setNgAmount(new Long(dataFormatter.formatCellValue(row.getCell(11))));
			} catch (Exception e) {

				throw new ResourceException(
						"Không đúng định dạng dữ liệu : " + dataFormatter.formatCellValue(row.getCell(11)));
			}
		}
		try {
			woExe.setTotalAmountStr(dataFormatter.formatCellValue(row.getCell(9)));
		} catch (Exception e) {

			throw new ResourceException(
					"Không đúng định dạng dữ liệu : " + dataFormatter.formatCellValue(row.getCell(9)));
		}

		try {
			woExe.setAmount(new BigDecimal(dataFormatter.formatCellValue(row.getCell(10))));
		} catch (Exception e) {

			throw new ResourceException(
					"Không đúng định dạng dữ liệu : " + dataFormatter.formatCellValue(row.getCell(10)));
		}
		try {
			woExe.setBrokenAmount(Long.parseLong(dataFormatter.formatCellValue(row.getCell(12))));
		} catch (Exception e) {

			throw new ResourceException(
					"Không đúng định dạng dữ liệu : " + dataFormatter.formatCellValue(row.getCell(12)));
		}
		woExe.setNgDescription(dataFormatter.formatCellValue(row.getCell(13)));
		if (woExe.getTotalAmount().doubleValue() < woExe.getAmount().doubleValue())
			throw new ResourceException("Số lượng hoàn thành phải nhỏ hơn hoặc bằng tổng số!");
		long amountChk;
		if (woExe.getNgRepaire())
			amountChk = woExe.getAmount().longValue() + woExe.getBrokenAmount().longValue();
		else
			amountChk = woExe.getAmount().longValue() + woExe.getNgAmount().longValue()
					+ woExe.getBrokenAmount().longValue();

		if (amountChk != woExe.getTotalAmount().longValue())
			throw new ResourceException("Số lượng hoàn thành (OK) + Số lượng NG + Số lượng hủy phải bằng tổng số!");

		return woExe;
	}

	@Autowired
	private ExportExcel exportExcel;

	public void exportExcel(ModelMap model, HttpServletRequest rq, HttpServletResponse response, WorkOrderExeForm form)
			throws Exception {
		if (Formater.isNull(form.getFrDate()))
			throw new ResourceException("Phải nhập thời điểm bắt đầu từ!");
		Date toDate = Formater.isNull(form.gettDate()) ? Calendar.getInstance().getTime()
				: Formater.str2DateTime(form.gettDate());
		Calendar cTodate = Calendar.getInstance();
		cTodate.setTime(toDate);
		cTodate.add(Calendar.MONTH, -3);
		cTodate.add(Calendar.DATE, -1);
		if (cTodate.getTime().after(Formater.str2DateTime(form.getFrDate())))
			throw new ResourceException("Khoảng thời gian bắt đầu - kết thúc phải nhỏ hơn hoặc bằng 3 tháng!");

		WorkOrderExeDao dao = (WorkOrderExeDao) getDao().createCriteria();
		createSearchDAO(rq, form, dao);
		List<?> temp = (List<?>) dao.search();
		if (temp.isEmpty())
			throw new ResourceException("Không tồn tại dữ liệu xuất!");
		List<WorkOrderExe> workOrderExes = new ArrayList<WorkOrderExe>();
		for (Object e : temp) {
			if (e.getClass().isArray()) {
				for (Object o : (Object[]) e) {
					if (o.getClass().equals(dao.getModelClass())) {
						workOrderExes.add((WorkOrderExe) o);
					}
				}
			} else {
				workOrderExes.add((WorkOrderExe) e);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("reports", workOrderExes);
		map.put("drawCodeSearch", form.getDrawingCode());
		map.put("manageCodeSearch", form.getManageCode());
		map.put("workCode", form.getWoCode());
		map.put("orderCode", form.getOrderCode());
		map.put("frDate", form.getFrDate());
		map.put("tDate", form.gettDate());
		map.put("fromDate", form.getFromDate());
		map.put("toDate", form.getToDate());
		map.put("stepName", form.getStepName());
		map.put("worker", form.getWorker());

		exportExcel.export("Lich_su_san_xuat", response, map);
	}

	public void fixbug(ModelMap model, HttpServletRequest rq, HttpServletResponse response, WorkOrderExeForm form)
			throws Exception {
		WorkOrderExeDao dao = (WorkOrderExeDao) getDao().createCriteria();
		dao.createAlias("workOrderId", "wo");
		dao.createAlias("wo.quotationItemExe", "qie");
		dao.createAlias("qie.quotationItemId", "qi");
		dao.addRestriction(Restrictions.sqlRestriction(
				"exists (select 1 from managecodetemp m, work_order wo, quotation_item qi, quotation_item_exe qie \r\n"
						+ "        where wo.id = {alias}.work_order_id and wo.quotation_item_exe_id = qie.id \r\n"
						+ "        and qie.quotation_item_id = qi.id and qi.manage_code = m.code)"));
		dao.addOrder(Order.asc("qi.manageCode"));
		List<?> temp = (List<?>) dao.search();
		if (temp.isEmpty())
			throw new ResourceException("Không tồn tại dữ liệu xuất!");
		List<WorkOrderExe> workOrderExes = new ArrayList<WorkOrderExe>();
		for (Object e : temp) {
			if (e.getClass().isArray()) {
				for (Object o : (Object[]) e) {
					if (o.getClass().equals(dao.getModelClass())) {
						workOrderExes.add((WorkOrderExe) o);
					}
				}
			} else {
				workOrderExes.add((WorkOrderExe) e);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("reports", workOrderExes);
		map.put("drawCodeSearch", form.getDrawingCode());
		map.put("manageCodeSearch", form.getManageCode());
		map.put("workCode", form.getWoCode());
		map.put("orderCode", form.getOrderCode());
		map.put("frDate", form.getFrDate());
		map.put("tDate", form.gettDate());
		map.put("fromDate", form.getFromDate());
		map.put("toDate", form.getToDate());
		map.put("stepName", form.getStepName());
		map.put("worker", form.getWorker());

		exportExcel.export("Lich_su_san_xuat", response, map);
	}

	public void download(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			WorkOrderExeForm form) throws Exception {
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream fileInputStream = classLoader.getResourceAsStream("/templates/report/DSLSX.xlsx");
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=DSLSX.xlsx");
		OutputStream responseOutputStream = response.getOutputStream();

		int bytes;
		while ((bytes = fileInputStream.read()) != -1) {
			responseOutputStream.write(bytes);
		}
		fileInputStream.close();
		responseOutputStream.close();
		response.flushBuffer();
	}
	
}
