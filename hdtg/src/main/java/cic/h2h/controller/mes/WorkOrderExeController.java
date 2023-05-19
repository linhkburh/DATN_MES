package cic.h2h.controller.mes;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ibm.icu.util.Calendar;

import cic.h2h.dao.hibernate.AstMachineDao;
import cic.h2h.dao.hibernate.QuotationItemDao;
import cic.h2h.dao.hibernate.QuotationItemProcessDao;
import cic.h2h.dao.hibernate.WorkOrderDao;
import cic.h2h.dao.hibernate.WorkOrderExeDao;
import cic.h2h.form.WorkOrderExeForm;
import common.util.FormatNumber;
import common.util.Formater;
import common.util.ResourceException;
import constants.RightConstants;
import entity.Quotation;
import entity.QuotationItem;
import entity.QuotationItemProcess;
import entity.WorkOrder;
import entity.WorkOrderExe;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.RightUtils;
import frwk.dao.hibernate.sys.SysDictParamDao;
import frwk.dao.hibernate.sys.SysUsersDao;

@Controller
@RequestMapping(value = "/workOderDetail")
public class WorkOrderExeController extends CatalogController<WorkOrderExeForm, WorkOrderExe> {

	private static final Logger lg = Logger.getLogger(WorkOrderExeController.class);

	@Autowired
	private WorkOrderExeDao workOrderExeDao;

	@Override
	public void createSearchDAO(HttpServletRequest request, WorkOrderExeForm form, BaseDao<WorkOrderExe> dao)
			throws Exception {
		if (form.getWorkOrder() != null)
			dao.addRestriction(Restrictions.eq("workOrderId", form.getWorkOrder()));
		dao.addOrder(Order.desc("startTime"));
		dao.addOrder(Order.desc("createDate"));
	}

	@Autowired
	private SysUsersDao sysUsersDao;

	@Override
	protected void pushToJa(JSONArray ja, WorkOrderExe e, WorkOrderExeForm modelForm) throws Exception {
		if ("OS".equals(e.getWorkOrderId().getQuotationItemExe().getExeStepId().getStepType().getCode())) {
			pushToJaOs(ja, e, modelForm);
			return;
		}

		if (e.getSysUser() != null)
			ja.put(e.getSysUser().getName());
		else
			ja.put("");

		if (e.getStartTime() != null)
			ja.put(Formater.dateTime2str(e.getStartTime()));
		else
			ja.put("0");
		if (e.getEndTime() != null)
			ja.put(Formater.dateTime2str(e.getEndTime()));
		else
			ja.put("0");
		// Thoi gian du kien
		if (Boolean.TRUE.equals(e.getNgRepaire())) {
			ja.put("");
		}else {
			ja.put(FormatNumber.num2Str(e.getAmount().multiply(e.getWorkOrderId().getQuotationItemExe().getUnitExeTime())));
			
		}
		// Thoi gian thuc hien
		ja.put(Formater.num2str(e.getExeTime()));
		if (e.getSetupTime() != null)
			ja.put(FormatNumber.num2Str(e.getSetupTime()));
		else
			ja.put("");
		// So luong
		ja.put(e.getTotalAmountStr());
		ja.put(e.getAmountStr());
		// so luong hong
		if (e.getNgAmount() == null || e.getNgAmount() == 0)
			ja.put(FormatNumber.num2Str(e.getNgAmount()));
		else
			ja.put("<font color ='red'>" + FormatNumber.num2Str(e.getNgAmount()) + "</front>");
		// so luong huy
		if (e.getBrokenAmount() == null || e.getBrokenAmount() == 0)
			ja.put(FormatNumber.num2Str(e.getBrokenAmount()));
		else
			ja.put("<font color ='red'>" + FormatNumber.num2Str(e.getBrokenAmount()) + "</front>");
		// Thoi gian trung binh
		if (e.getAmount().doubleValue() == 0) {
			ja.put("");
		} else {
			BigDecimal woeTimeAvg = e.getExeTime().divide(e.getAmount(), RoundingMode.HALF_UP);
			ja.put(FormatNumber.num2Str(woeTimeAvg));
		}

		// Cham
		BigDecimal planTime = new BigDecimal(e.getAmount().doubleValue());
		planTime = planTime.abs().multiply(e.getWorkOrderId().getQuotationItemExe().getUnitExeTime());
		BigDecimal late = e.getExeTime().subtract(planTime);
		// Hang sua khong tinh nhanh cham
		if (Boolean.TRUE.equals(e.getNgRepaire())) {
			ja.put("");
			ja.put("");
		} else {
			if (late.compareTo(new BigDecimal(0)) > 0) {
				ja.put("<div style='background:red;'>" + Formater.num2str(late) + "</div>");
				// Phan tram
				if (planTime.doubleValue() == 0)
					ja.put("");
				else
					ja.put("<div style='background:red;'>"
							+ Formater
									.num2str(late.multiply(new BigDecimal(100)).divide(planTime, RoundingMode.HALF_UP))
							+ "</div>");
			} else {
				ja.put("<div style='background:#1bf982;'>" + Formater.num2str(late) + "</div>");
				// Phan tram
				ja.put("<div style='background:#1bf982;'>"
						+ Formater.num2str(late.multiply(new BigDecimal(100)).divide(planTime, RoundingMode.HALF_UP))
						+ "</div>");
			}
		}
		if (e.getUpdator() != null)
			ja.put(e.getUpdator().getName());
		else
			ja.put("");
		ja.put("<a title='Chỉnh sửa kết quả sản xuất' style='color:blue;' href='javascipt:;' onclick = 'edit(\""
				+ e.getId() + "\")'>" + (Boolean.TRUE.equals(e.getNgRepaire()) ? "Sửa NG" : "Gia công") + "</a>");
	}

	private void pushToJaOs(JSONArray ja, WorkOrderExe e, WorkOrderExeForm modelForm) {
		if (e.getSysUser() != null)
			ja.put(e.getSysUser().getName());
		else
			ja.put("");

		if (e.getStartTime() != null)
			ja.put(Formater.dateTime2str(e.getStartTime()));
		else
			ja.put("0");
		if (e.getEndTime() != null)
			ja.put(Formater.dateTime2str(e.getEndTime()));
		else
			ja.put("0");
		// So luong
		ja.put(e.getTotalAmountStr());
		if (e.getUpdator() != null)
			ja.put(e.getUpdator().getName());
		else
			ja.put("");
		if ("OS".equals(e.getWorkOrderId().getQuotationItemExe().getExeStepId().getStepType().getCode())) {
			ja.put("<a title='Chuyển QC gia công ngoài' style='color:blue;' href='javascipt:;' onclick = 'edit(\""
					+ e.getId() + "\")'>" + "Chuyển QC" + "</a>");
		} else {
			ja.put("<a title='Chỉnh sửa kết quả sản xuất' style='color:blue;' href='javascipt:;' onclick = 'edit(\""
					+ e.getId() + "\")'>" + (Boolean.TRUE.equals(e.getNgRepaire()) ? "Sửa NG" : "Gia công") + "</a>");
		}

	}

	@Autowired
	private RightUtils rightUtils;
	@Autowired
	private QuotationItemProcessDao quotationItemProcessDao;

	@Override
	public void save(ModelMap model, HttpServletRequest request, HttpServletResponse response, WorkOrderExeForm form)
			throws Exception {
		WorkOrderExe woExe = form.getWorkOrderExe();
		workOrderDao.load(woExe.getWorkOrderId());
		// update
		if (!Formater.isNull(woExe.getId())) {
			WorkOrderExe orderExePojo = workOrderExeDao.getPoJo(woExe.getId());
			if (orderExePojo.getUpdator() != null
					&& !getSessionUser().getId().equals(orderExePojo.getUpdator().getId()))
				rightUtils.checkRight(getSessionUser(), RightConstants.WORKODERDETAIL_CORR);
		} else {
			// Nhap realTime
			String type = request.getParameter("type");
			if (Formater.isNull(type)) {
				woExe.setEndTime(Calendar.getInstance().getTime());
				woExe.setAmount(new BigDecimal("1"));
			}
		}
		if (Boolean.TRUE.equals(woExe.getNgRepaire()))
			woExe.setNgAmount(0l);
		vaidateWoeInf(woExe);
		woExe.setCreateDate(Calendar.getInstance().getTime());
		woExe.setUpdator(getSessionUser());
		workOrderExeDao.save(woExe);
	}

	private void vaidateWoeInf(WorkOrderExe woExe) throws ResourceException {
		if(!"OS".equals(woExe.getWorkOrderId().getQuotationItemExe().getExeStepId().getStepType().getCode())) {
			if (woExe.getNgAmount() != null && woExe.getNgAmount() < 0)
				throw new ResourceException("Số lượng NG: %s nhỏ hơn 0!", FormatNumber.num2Str(woExe.getNgAmount()));
			if (woExe.getBrokenAmount() != null && woExe.getBrokenAmount() < 0)
				throw new ResourceException("Số lượng hủy: %s nhỏ hơn 0!", FormatNumber.num2Str(woExe.getBrokenAmount()));
		}
		// Sua NG
		if (Boolean.TRUE.equals(woExe.getNgRepaire())) {
			double amount = woExe.getAmount().doubleValue();
			if (amount != Math.round(amount))
				throw new ResourceException("Số lượng hoàn thành phải là số nguyên!");
		}
		// Thoi gian bat dau, ket thuc
		BigDecimal exeTime = new BigDecimal(
				(woExe.getEndTime().getTime() - woExe.getStartTime().getTime()) / 1000 / 60);
		if (exeTime.compareTo(new BigDecimal(0)) <= 0)
			throw new ResourceException("Thời điểm kết thúc phải sau thời điểm bắt đầu!");
		if (woExe.getSetupTime() != null) {
			exeTime = exeTime.subtract(woExe.getSetupTime());
			if (exeTime.compareTo(new BigDecimal(0)) <= 0)
				throw new ResourceException("Khoảng thời gian bắt đầu, kết thúc phải lớn hơn thời gian Setup!");
		}
		woExe.setExeTime(exeTime);

	}

	public void addWorkOrderDetail(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			WorkOrderExeForm form) throws Exception {
		String process = request.getParameter("process");
		if (!Formater.isNull(process)) {
			// save quotationItemProcess
			String qrManageCode = request.getParameter("qrManageCode");
			if (Formater.isNull(qrManageCode))
				throw new ResourceException("Phải scan mã quản lý!");
			String qrAmount = request.getParameter("qrAmount");
			if (Formater.isNull(qrAmount))
				throw new ResourceException("Phải nhập số lượng!");
			QuotationItemProcess qip = new QuotationItemProcess(
					quotationItemDao.getByManageCode(qrManageCode, getSessionUser().getCompany()),
					FormatNumber.str2num(qrAmount), process, getSessionUser());
			quotationItemProcessDao.save(qip);
			return;
		}
		save(model, request, response, form);

	}

	public void deleteWorkOrderDetail(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			WorkOrderExeForm form) throws Exception {
		String workOrderExeId = request.getParameter("workOrderExeId");
		WorkOrderExe orderExe = workOrderExeDao.get(WorkOrderExe.class, workOrderExeId);
		if (Formater.isNull(workOrderExeId) || orderExe == null)
			throw new ResourceException("Không tồn tại lệnh sản xuất!");
		form.setWorkOrderExe(orderExe);
		super.del(model, request, response, form);

	}

	@Override
	public BaseDao<WorkOrderExe> getDao() {

		return workOrderExeDao;
	}


	@Override
	public String getJsp() {
		if (getModelForm().getWorkOrder() == null)
			return "ke_hoach_san_xuat/quet_qr_code";
		return "ke_hoach_san_xuat/thuc_hien_lenh_sx";
	}

	@Autowired
	private WorkOrderDao workOrderDao;
	@Autowired
	private QuotationItemDao quotationItemDao;

	public void checkWoCode(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, WorkOrderExeForm form)
			throws IOException {
		String code = rq.getParameter("code");
		String process = rq.getParameter("process");
		if (Formater.isNull(process)) {
			WorkOrder wo = workOrderDao.getByCode(code);
			returnTxtHtml(rs, String.valueOf(wo != null));
		} else {
			QuotationItem qi = quotationItemDao.getByManageCode(code, getSessionUser().getCompany());
			returnTxtHtml(rs, String.valueOf(qi != null));
		}
	}

	@Autowired
	private AstMachineDao astMachineDao;

	@Autowired
	private SysDictParamDao sysDictParamDao;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, WorkOrderExeForm form)
			throws Exception {
		if (!Formater.isNull(form.getWorkOrder().getCode())) {
			WorkOrder wo = workOrderDao.getByCode(form.getWorkOrder().getCode());
			if (wo == null)
				throw new ResourceException("Lệnh sản xuất " + form.getWorkOrder().getCode() + " không tồn tại!");
			form.setWorkOrder(wo);
			form.getWorkOrderExe().setWorkOrderId(wo);
		} else {
			String workOrderId = rq.getParameter("workOrderId");
			if (!Formater.isNull(workOrderId)) {
				WorkOrder wo = workOrderDao.get(workOrderId);
				if ("OS".equals(wo.getQuotationItemExe().getExeStepId().getStepType().getCode())) {
					form.getWorkOrderExe().setSysUser(getSessionUser());
					if (wo.getNumOfFinishItem() == null)
						form.getWorkOrderExe().setTotalAmount(BigDecimal.valueOf(wo.getReadyOsAmount()));
					else
						form.getWorkOrderExe().setTotalAmount(
								BigDecimal.valueOf(wo.getReadyOsAmount()).subtract(wo.getNumOfFinishItem()));
				}
				form.setWorkOrder(wo);
				form.getWorkOrderExe().setWorkOrderId(wo);
			}
			// Cong nhan khong click tu man hinh tim kiem => khi mo chuc nang gia tri nay null
			else
				form.setWorkOrder(null);
		}
		if (form.getWorkOrder() != null) {
			form.getWorkOrderExe().setAstMachine(form.getWorkOrder().getAstMachine());
			Quotation q = form.getWorkOrder().getQuotationItemExe().getQuotationItemId().getQuotationId();
			model.addAttribute("lstSysUser", sysUsersDao.getCompanyUser(q.getCompany()));
			model.addAttribute("lstAstMachine", astMachineDao.getAll(q.getCompany()));
		}
		model.addAttribute("lstErrorCause", sysDictParamDao.getByType("ERROR_CAUSE_EXE"));
	}

}
