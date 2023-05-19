package cic.h2h.controller.mes;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cic.h2h.dao.hibernate.AstMachineDao;
import cic.h2h.dao.hibernate.QuotationItemExeDao;
import cic.h2h.dao.hibernate.WorkOrderDao;
import cic.h2h.dao.hibernate.WorkOrderExeDao;
import cic.h2h.dao.hibernate.WorkProDao;
import cic.h2h.form.WorkOderForm;

import com.ibm.icu.util.Calendar;
import common.util.FormatNumber;
import common.util.Formater;
import common.util.ResourceException;

import entity.AstMachine;
import entity.Company;
import entity.QuotationItemExe;
import entity.WorkOrder;
import entity.WorkOrderExe;
import entity.WorkPro;
import entity.frwk.SysUsers;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.CompanyDao;
import frwk.dao.hibernate.sys.SysUsersDao;
import frwk.form.CompanyForm;

@Controller
@RequestMapping(value = "/datlenhsx1")
public class WorkOrderController extends CatalogController<WorkOderForm, WorkOrder> {

	private static Logger log = Logger.getLogger(WorkOrderController.class);

	@Autowired
	private WorkOrderDao workOrderDao;

	@Override
	public void createSearchDAO(HttpServletRequest request, WorkOderForm form, BaseDao<WorkOrder> dao)
			throws Exception {

	}

	@Override
	protected void pushToJa(JSONArray ja, WorkOrder e, WorkOderForm modelForm) throws Exception {
		ja.put("");
		ja.put("");
		ja.put("");
		ja.put("");
		ja.put("");
		ja.put("");
	}

	@Override
	public BaseDao<WorkOrder> getDao() {
		return workOrderDao;
	}

	@Override
	public String getJsp() {
		return "ke_hoach_san_xuat/lenh_san_xuat";
	}

	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, WorkOderForm form)
			throws Exception {
		Map<String, Integer> checkDupCode = new HashMap<String, Integer>();
		if (Formater.isNull(form.getWorkOrder().getId())) {
			form.getWorkOrder().setId(null);
			QuotationItemExe itemExe = new QuotationItemExe(form.getWorkOrder().getQuotationItemExe().getId());
			BigDecimal totalAmount = new BigDecimal("0");
			for (WorkOrder order : form.getWorkOrder().getLstWorkOrder()) {
				totalAmount = totalAmount.add(order.getAmount());
				if (totalAmount.doubleValue() > 0)
					throw new ResourceException("Tổng số chi tiết lớn hơn số lượng chi tiết của bản vẽ");
				WorkOderForm oderForm = new WorkOderForm();
				if (checkDupCode.get(order.getCode()) == null)
					checkDupCode.put(order.getCode(), 1);
				else
					throw new ResourceException("Mã Lện SX : " + order.getCode() + " bị trùng!");
				order.setCreateDate(Calendar.getInstance().getTime());
				order.setQuotationItemExe(itemExe);
				oderForm.setWorkOrder(order);
				super.save(model, rq, rs, oderForm);
			}
		}

	}

	public static final DataFormatter dataFormatter = new DataFormatter();

	@SuppressWarnings("resource")
	@RequestMapping(value = "upload", method = RequestMethod.POST)
	public void upload(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			@RequestParam("inputFile") MultipartFile inputFile, @RequestParam("fileName") String fileName)
			throws Exception {
		Workbook wb = fileName.endsWith("xlsx") ? new XSSFWorkbook(inputFile.getInputStream())
				: new HSSFWorkbook(inputFile.getInputStream());
		Sheet sheet = wb.getSheetAt(0);
		// Du lieu bat dau tu dong 4
		int iRowIdex = 3;
		Row row = null;
		while (true) {
			row = sheet.getRow(iRowIdex);
			if (row == null)
				break;
			String data = dataFormatter.formatCellValue(row.getCell(0));
			// ExcelUtils.getString(row.getCell((short) 0)); // Kiem tra cot 1 rong thi stop
			if ("eof".equals(data) || Formater.isNull(data))
				break;
			try {
				WorkOrderExe workOrderExe = createTransFromRow(row);
				workOrderExe.setUpdator(getSessionUser());
				workOrderExeDao.save(workOrderExe);
			} catch (Exception ex) {
				log.error(String.format("Loi doc du lieu dong %s", String.valueOf(iRowIdex)), ex);
				iRowIdex++;
				throw ex;
			}
			iRowIdex++;
		}

		rs.setContentType("text/html;charset=utf-8");
		PrintWriter pw = rs.getWriter();
		pw.print("1");
		pw.flush();
		pw.close();
	}

	@Autowired
	private WorkOrderExeDao workOrderExeDao;

	private WorkOrderExe createTransFromRow(Row row) throws Exception {
		WorkOrderExe wOrderExe = new WorkOrderExe();
		String code = dataFormatter.formatCellValue(row.getCell(1));
		if (Formater.isNull(code))
			throw new ResourceException("Cần nhập lệnh sản xuất");
		WorkOrder workOrder = workOrderDao.getWorkOrderByCode(code, getSessionUser().getCompany());
		if (workOrder == null)
			throw new ResourceException("Không tồn tại lệnh sản xuất " + code);
		wOrderExe.setWorkOrderId(workOrder);
		String username = dataFormatter.formatCellValue(row.getCell(2));
		if (Formater.isNull(username))
			throw new ResourceException("Cần nhập mã cán bộ");
		SysUsers sysUsers = sysUsersDao.getSysUserByUserName(username);
		if (sysUsers == null)
			throw new ResourceException("Mã cán bộ " + username + " không tồn tại");
		wOrderExe.setSysUser(sysUsers);
		wOrderExe.setAmountStr(dataFormatter.formatCellValue(row.getCell(8)));
		// Kiem tra so luong
		// Lay so lieu con lại tu thu tuc
		workOrderExeDao.getInfoWorkOrder(workOrder, null);
		BigDecimal numOfFinishChildren = workOrder.getNumOfFinishChildren() != null ? workOrder.getNumOfFinishChildren()
				: new BigDecimal("0");
		BigDecimal remainAmount = new BigDecimal(
				workOrder.getQuotationItemExe().getQuotationItemId().getQuality().doubleValue());
		remainAmount = remainAmount.subtract(numOfFinishChildren);
		// Vuot qua so luong con lai
		if (remainAmount.compareTo(wOrderExe.getAmount()) < 0)
			throw new ResourceException(
					String.format("Tổng số lượng chi tiết import của LSX %s vượt quá số lượng còn lại %s",
							new Object[] { workOrder.getCode(), Formater.num2str(remainAmount) }));

		try {
			String startTime = dataFormatter.formatCellValue(row.getCell(5));
			wOrderExe.setStartTime(Formater.str2DateTime(startTime));
			String endTime = dataFormatter.formatCellValue(row.getCell(6));
			wOrderExe.setEndTime(Formater.str2DateTime(endTime));
		} catch (Exception e) {
			throw new ResourceException("Khong dung dinh dang thoi gian");
		}

		return wOrderExe;
	}

	public void download(ModelMap model, HttpServletRequest request, HttpServletResponse response, WorkOderForm form)
			throws Exception {
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			InputStream fileInputStream = classLoader.getResourceAsStream("/templates/report/DSLSX.xlsx");
			response.setContentType("application/vnd.ms-excel");
			response.addHeader("Content-Disposition", "attachment; filename=DSLSX.xls");
			OutputStream responseOutputStream = response.getOutputStream();

			int bytes;
			while ((bytes = fileInputStream.read()) != -1) {
				responseOutputStream.write(bytes);
			}
			fileInputStream.close();
			responseOutputStream.close();
		} catch (Exception ex) {
			log.error(ex);
			ex.printStackTrace();
			throw ex;
		}
	}

	@Autowired
	private QuotationItemExeDao quotationItemExeDao;

	@Autowired
	private SysUsersDao sysUsersDao;

	@Autowired
	private WorkProDao workProDao;
	@Autowired
	private AstMachineDao astMachineDao;

	private List<WorkPro> lstWorkPro = new ArrayList<WorkPro>();
	private List<AstMachine> lstAstMachine = new ArrayList<AstMachine>();

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, WorkOderForm form)
			throws Exception {
		lstWorkPro = workProDao.getAll();
		model.addAttribute("lstWorkPro", lstWorkPro);
		lstAstMachine = astMachineDao.getAll(getSessionUser().getCompany());
		model.addAttribute("lstAstMachine", lstAstMachine);
		String id = rq.getParameter("id");
		if (!Formater.isNull(id)) {
			QuotationItemExe itemExe = quotationItemExeDao.get(QuotationItemExe.class, id);
			form.getWorkOrder().setQuotationItemExe(itemExe);
			if (!Formater.isNull(itemExe.getWorkOrders())) {
				for (WorkOrder order : itemExe.getWorkOrders()) {
					order.setAmountStr(FormatNumber.num2Str(order.getAmount()));
					astMachineDao.getInfoMachine(order.getAstMachine());
					Long timeToWork = order.getAmount().longValue()
							* order.getQuotationItemExe().getExeTime().longValue();
					BigDecimal totalTimeDelay = new BigDecimal("0");
					// TODO: Cho nay xem lai neu co dung
					// totalTimeDelay = totalTimeDelay.add(timeToWork);
					// totalTimeDelay = totalTimeDelay.add(FormatNumber.str2num(order.getTimeToWaiteMachine()));
					// order.setTimeToWork(FormatNumber.num2Str(timeToWork));
					// order.setTotalTimeDelay(FormatNumber.num2Str(totalTimeDelay));
				}
				// form.getWorkOrder().getLstWorkOrder().addAll(workOrders);
			} else {

				String code = "";
				if (itemExe != null) {
					form.getWorkOrder().setQuotationItemExe(itemExe);
					if (itemExe.getQuotationItemId() != null && itemExe.getQuotationItemId().getQuotationId() != null)
						code = itemExe.getQuotationItemId().getQuotationId().getCode() + "-"
								+ itemExe.getQuotationItemId().getCode() + "-"
								+ itemExe.getExeStepId().getStepType().getCode() + "-"
								+ itemExe.getExeStepId().getStepCode();
				}
				for (AstMachine item : lstAstMachine) {
					String codeAst = code;
					WorkOrder workOrder = new WorkOrder();
					workOrder.setId(null);
					workOrder.setAstMachine(item);
					// workOrder.setTimeToWaiteMachine(astMachineDao.getInfoMachine(item.getId()));
					codeAst = codeAst + "-" + item.getAstName();
					workOrder.setCode(codeAst);
					form.getWorkOrder().getLstWorkOrder().add(workOrder);
				}
			}

			model.addAttribute("lstWorkOrder", form.getWorkOrder().getLstWorkOrder());
		}
	}

	public void getInfoMachine(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			WorkOderForm form) throws Exception {
		String machineId = request.getParameter("machineId");
		String quationId = request.getParameter("quationId");
		if (Formater.isNull(machineId))
			throw new ResourceException("Thông tin máy không chính xác!");
		// String timeToWaite = astMachineDao.getInfoMachine(machineId);
		AstMachine astMachine = astMachineDao.get(AstMachine.class, machineId);
		QuotationItemExe itemExe = quotationItemExeDao.get(QuotationItemExe.class, quationId);
		if (itemExe == null || astMachine == null)
			throw new ResourceException("Sai thong tin lap tring");
		String code = itemExe.getQuotationItemId().getQuotationId().getCode() + "-"
				+ itemExe.getQuotationItemId().getCode() + "-" + itemExe.getExeStepId().getStepType().getCode() + "-"
				+ itemExe.getExeStepId().getStepCode() + "-" + astMachine.getAstName();
		WorkOrder order = new WorkOrder();
		// order.setTimeToWaiteMachine(timeToWaite);
		order.setCode(code);
		returnJson(response, order);
	}

	public List<WorkPro> getLstWorkPro() {
		return lstWorkPro;
	}

	public void setLstWorkPro(List<WorkPro> lstWorkPro) {
		this.lstWorkPro = lstWorkPro;
	}

	public List<AstMachine> getLstAstMachine() {
		return lstAstMachine;
	}

	public void setLstAstMachine(List<AstMachine> lstAstMachine) {
		this.lstAstMachine = lstAstMachine;
	}
}
