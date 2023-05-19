package cic.h2h.controller.mes;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.ibm.icu.util.Calendar;

import cic.h2h.dao.hibernate.AstMachineDao;
import cic.h2h.dao.hibernate.QuotationItemDao;
import cic.h2h.dao.hibernate.QuotationItemExeDao;
import cic.h2h.dao.hibernate.WorkOrderDao;
import cic.h2h.form.QuotationItemExeForm;
import common.util.JsonUtils;
import common.util.ResourceException;
import common.util.StreamUtils;
import common.util.XmlUtils;
import entity.AstMachine;
import entity.Company;
import entity.ProductionLine;
import entity.QuotationItem;
import entity.QuotationItemExe;
import entity.WorkOrder;
import frwk.controller.CatalogController;
import frwk.controller.ClearOnFinish;
import frwk.dao.hibernate.BaseDao;
import integration.genhub.GenHub;
import integration.genhub.IFile;
import integration.genhub.SaveFormat;

@Controller
@RequestMapping(value = "/datlenhsx")
public class QuotationItemExeController extends CatalogController<QuotationItemExeForm, QuotationItemExe> {
	private static final Logger lg = Logger.getLogger(QuotationItemExeController.class);
	@Autowired
	private QuotationItemExeDao quotationItemExeDao;
	@Autowired
	private AstMachineDao astMachineDao;

	@Override
	public void createSearchDAO(HttpServletRequest request, QuotationItemExeForm form, BaseDao<QuotationItemExe> dao)
			throws Exception {

	}

	@Override
	protected void pushToJa(JSONArray ja, QuotationItemExe e, QuotationItemExeForm modelForm) throws Exception {

	}

	@Autowired
	private WorkOrderDao workOrderDao;

	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QuotationItemExeForm form)
			throws Exception {
		if (form.isMakeWoByQi()) {
			workOrderDao.save(form.getQuotationItem().getWorkOrders());
		} else {
			quotationItemExeDao.makeWorkOrder(form.getQuotationItemExe());
		}

	}

	@Override
	public BaseDao<QuotationItemExe> getDao() {

		return quotationItemExeDao;
	}

	@Override
	public String getJsp() {

		return "ke_hoach_san_xuat/lenh_san_xuat";
	}

	public void getInfoMachine(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			QuotationItemExeForm form) throws Exception {
		String machineId = request.getParameter("machineId");
		AstMachine astMachine = astMachineDao.get(AstMachine.class, machineId);
		astMachineDao.getInfoMachine(astMachine);
		QuotationItemExe qie = quotationItemExeDao.get(QuotationItemExe.class, form.getModel().getId());
		WorkOrder wo = workOrderDao.get(qie, astMachine);
		if (wo == null)
			wo = new WorkOrder(qie, astMachine);
		// So luong
		wo.setAmountStr(request.getParameter("amountStr"));
		returnJson(response, wo);
	}

	/**
	 * Tao lenh san xuat cho cong doan theo so luong may <br>
	 * Chia deu so luong chi tiet con lai cho cac LSX
	 * 
	 * @param makeWoByQi Tao lenh SX phan cong toan bo chi tiet
	 * 
	 * @param form
	 * @throws Exception
	 */
	private void makeWorkOrder(final Long numOfMachine, final QuotationItemExe qie,
			final List<AstMachine> lstAddMachine) throws Exception {
		// Loai bo LSX chua thuc hien, cache vao danh sach removes
		List<WorkOrder> removes = new ArrayList<WorkOrder>();
		Iterator<WorkOrder> woIterator = qie.getWorkOrders().iterator();
		while (woIterator.hasNext()) {
			WorkOrder wo = woIterator.next();
			if (wo.getWorkOrderExes().isEmpty()) {
				removes.add(wo);
				woIterator.remove();
			} else {
				// So luong LSX la so luong da hoan thanh
				wo.setAmount(wo.getNumOfFinishChildren());
			}

		}
		if (qie.getWorkOrders().size() > numOfMachine) {
			throw new ResourceException(
					"Công đoạn %s-%s, số lượng dây chuyền đã thực hiện %s, lớn hơn số lượng dây chuyền mới %s!",
					new Object[] { qie.getExeStepId().getStepType().getCode(), qie.getExeStepId().getStepName(),
							qie.getWorkOrders().size(), numOfMachine });
		}

		// Cac LSX bo sung
		if (qie.getWorkOrders().size() < numOfMachine) {
			for (int i = 0; i < lstAddMachine.size(); i++) {
				boolean isNew = true;
				for (WorkOrder wo : qie.getWorkOrders()) {
					if (wo.getAstMachine().getId().equals(lstAddMachine.get(i).getId())) {
						isNew = false;
						break;
					}
				}
				if (isNew)
					qie.getWorkOrders().add(new WorkOrder(qie, lstAddMachine.get(i), i + 1));
			}
		}

		// Phan chia so luong chi tiet vao cac LSX
		BigDecimal bWaitAmount = qie.getWaitAmount();
		long woAmount = bWaitAmount.longValue() / qie.getWorkOrders().size();
		// Phan le, sau khi chia deu duoc add cho cac LSX phia tren
		long remainder = bWaitAmount.longValue() % qie.getWorkOrders().size();

		for (int i = 0; i < qie.getWorkOrders().size(); i++) {
			WorkOrder workOrder = qie.getWorkOrders().get(i);
			if (i < remainder)
				workOrder.setAmount(new BigDecimal(workOrder.getAmount().longValue() + woAmount + 1));
			else
				workOrder.setAmount(new BigDecimal(workOrder.getAmount().longValue() + woAmount));
			workOrder.extraWaitTimeForMachine();
		}
	}

	private void makeWorkOrderByNumOfMachine(Long numOfMachine, QuotationItemExeForm form, Company company)
			throws Exception {
		QuotationItem qi = form.getQuotationItem();
		// Sap xep trinh tu gia cong
		Collections.sort(qi.getQuotationItemAllExeList(), new QuotationItemExe.SortQie());
		// Neu so may > so chi tiet => set lai so may = so chi tiet
		if (numOfMachine > qi.getQuality().longValue())
			numOfMachine = qi.getQuality().longValue();
		if (Boolean.TRUE.equals(form.isMakeWoByQi())) {
			List<AstMachine> lstAddMachine = astMachineDao.getTopFreeMachineForMakeWo(numOfMachine.intValue(),
					qi.getId(), company);
			for (QuotationItemExe qie : qi.getQuotationItemAllExeList()) {
				if (Boolean.TRUE.equals(qie.getExeStepId().getProgram()))
					continue;
				makeWorkOrder(numOfMachine, qie, lstAddMachine);
			}
			// Sap xep theo productLine
			qi.groupWoByProductLine();
			qi.getLstProductionLine().forEach((line -> qi.getWorkOrders().addAll(line.getLstWorkOrder())));
			return;
		}

		QuotationItemExe qie = form.getQuotationItemExe();
		// Loai bo LSX chua thuc hien
		List<WorkOrder> remove = new ArrayList<WorkOrder>();
		Iterator<WorkOrder> woIterator = qie.getWorkOrders().iterator();
		while (woIterator.hasNext()) {
			WorkOrder wo = woIterator.next();
			if (wo.getWorkOrderExes().isEmpty()) {
				remove.add(wo);
				woIterator.remove();
			} else {
				astMachineDao.updateWaitTime(wo.getAstMachine(), qie);
				// So luong LSX la so luong da hoan thanh
				wo.setAmount(wo.getNumOfFinishChildren());
			}

		}
		// Cac LSX bo sung
		if (qie.getWorkOrders().size() < numOfMachine) {
			List<AstMachine> lstAddMachine = astMachineDao.getTopFreeMachineExcludeQie(
					numOfMachine.intValue() - qie.getWorkOrders().size(), qie.getId(), getSessionUser().getCompany());
			for (int i = 0; i < lstAddMachine.size(); i++) {
				AstMachine machine = lstAddMachine.get(i);
				WorkOrder newWo = new WorkOrder(qie, machine, i + 1);
				// Giu lai Id, neu may van duoc su dung sau khi tao lenh
				qie.getWorkOrders().add(newWo);
				for (WorkOrder wo : remove) {
					if (wo.getAstMachine().getId().equals(machine.getId())) {
						newWo.setId(wo.getId());
						break;
					}
				}
			}

		}
		// Sap xep may co thoi gian cho ngan len dau
		Collections.sort(qie.getWorkOrders(), new Comparator<WorkOrder>() {
			@Override
			public int compare(WorkOrder before, WorkOrder after) {
				if (before.getWaitTime() == null)
					return -1;
				if (after.getWaitTime() == null)
					return 1;
				return before.getWaitTime().compareTo(after.getWaitTime());
			}
		});

		// So luong chi tiet cua moi LSX
		long woAmount = qie.getWaitAmount().longValue() / qie.getWorkOrders().size();
		// Phan le, sau khi chia deu
		long remainder = qie.getWaitAmount().longValue() % qie.getWorkOrders().size();

		for (int i = 0; i < qie.getWorkOrders().size(); i++) {
			WorkOrder workOrder = qie.getWorkOrders().get(i);
			if (i < remainder)
				workOrder.setAmount(new BigDecimal(workOrder.getAmount().longValue() + woAmount + 1));
			else
				workOrder.setAmount(new BigDecimal(workOrder.getAmount().longValue() + woAmount));
		}
	}

	@ClearOnFinish
	public void makeWorkOrder(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QuotationItemExeForm form)
			throws Exception {
		if (form.isMakeWoByQi()) {
			quotationItemDao.load(form.getQuotationItem());
		} else {
			quotationItemExeDao.load(form.getModel());
			form.setQuotationItem(form.getModel().getQuotationItemId());
		}

		String mWoMethod = rq.getParameter("mWoMethod");
		Long numOfMachine = null;
		if ("numOfMachine".equals(mWoMethod)) {
			numOfMachine = form.getNumOfMachine();
		} else {
			Long woAmount = null;
			// Theo thoi gian cho moi lenh
			if ("woDuration".equals(mWoMethod)) {
				// So luong chi tiet cho moi lenh
				woAmount = form.getWoDuration().multiply(new BigDecimal(60))
						.divide(form.getQuotationItemExe().getUnitExeTime(), 3, RoundingMode.HALF_UP).longValue();

			} else if ("qiExtTime".equals(mWoMethod)) {// Theo thoi gian can hoan thanh
				// So luong chi tiet moi LSX
				woAmount = form.getItemExeTime().multiply(new BigDecimal(60))
						.divide(form.getQuotationItemExe().getUnitExeTime(), 3, RoundingMode.HALF_UP).longValue();

			}
			// So luong may
			numOfMachine = form.getQuotationItemExe().getWaitAmount().longValue() / woAmount;
		}
		makeWorkOrderByNumOfMachine(numOfMachine, form, getSessionUser().getCompany());
		ObjectMapper om = JsonUtils.objectMapper2();
		// tunning performace
		om.setAnnotationIntrospector(new CustomIntrospector());
		returnJson(rs, form.isMakeWoByQi() ? form.getQuotationItem() : form.getQuotationItemExe(), om);

	}

	private static class CustomIntrospector extends JacksonAnnotationIntrospector {
		@Override
		public boolean hasIgnoreMarker(final AnnotatedMember m) {
			if (super.hasIgnoreMarker(m))
				return true;
			try {
				if (QuotationItem.class.getDeclaredField("quotationId").equals(m.getAnnotated()))
					return true;
				if (QuotationItem.class.getDeclaredField("quotationItemProList").equals(m.getAnnotated()))
					return true;
				if (QuotationItem.class.getDeclaredField("quotationItemMaterialList").equals(m.getAnnotated()))
					return true;
				if (QuotationItem.class.getDeclaredField("quotationItemExeList").equals(m.getAnnotated()))
					return true;
				if (QuotationItem.class.getDeclaredField("quotationItemExeList").equals(m.getAnnotated()))
					return true;
				if (QuotationItem.class.getDeclaredField("quotationItemAllExeList").equals(m.getAnnotated()))
					return true;

			} catch (Exception e) {
				lg.error(e.getMessage(), e);
			}
			return false;
		}
	}

	@Autowired
	private QuotationItemDao quotationItemDao;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QuotationItemExeForm form)
			throws Exception {
		List<AstMachine> lstAstMachine = astMachineDao.getAll(getSessionUser().getCompany());
		model.addAttribute("lstAstMachine", lstAstMachine);
		if (form.isMakeWoByQi()) {
			quotationItemDao.load(form.getQuotationItem());
			List<QuotationItemExe> lstQie = form.getQuotationItem().getQuotationItemAllExeList().stream()
					.filter(qie -> !Boolean.TRUE.equals(qie.getExeStepId().getProgram())).collect(Collectors.toList());
			if (lstQie.isEmpty())
				throw new ResourceException("Chưa nhập quy trình công nghệ cho chi tiết!");
			// Set de hien thi thong tin tren giao dien
			form.setQuotationItemExe(lstQie.get(0));
			form.getQuotationItem().groupWoByProductLine();
			if (form.getQuotationItem().getLstProductionLine() != null) {
				for (ProductionLine pl : form.getQuotationItem().getLstProductionLine()) {
					Collections.sort(pl.getLstWorkOrder(), new Comparator<WorkOrder>() {
						@Override
						public int compare(WorkOrder before, WorkOrder after) {
							if (before.getQuotationItemExe().getDisOrder() == null
									&& after.getQuotationItemExe().getDisOrder() == null)
								return 0;
							if (before.getQuotationItemExe().getDisOrder() == null)
								return -1;
							if (after.getQuotationItemExe().getDisOrder() == null)
								return 1;
							return before.getQuotationItemExe().getDisOrder()
									.compareTo(after.getQuotationItemExe().getDisOrder());
						}
					});
				}
				form.getQuotationItem().getLstProductionLine()
						.forEach(line -> form.getQuotationItem().getWorkOrders().addAll(line.getLstWorkOrder()));
				form.setNumOfMachine((long) form.getQuotationItem().getLstProductionLine().size());
			}
		} else {
			quotationItemExeDao.load(form.getQuotationItemExe());
			form.setQuotationItem(form.getQuotationItemExe().getQuotationItemId());
		}
		// Thoi gian con lai den khi giao hang
		if (form.getQuotationItem().getDeliverDate() != null) {
			Double remainMinute = ((double) (form.getQuotationItem().getDeliverDate().getTime()
					- Calendar.getInstance().getTime().getTime())) / 1000 / 60;
			int hour = (int) (remainMinute / 60);
			int minute = Math.abs(((int) (remainMinute - hour * 60) * 100) / 100);
			model.addAttribute("remainTime", hour + " giờ " + minute + " phút");
		}
	}

	/**
	 * Xoa wo, kg phai xoa qie
	 */
	public void del(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QuotationItemExeForm form)
			throws ResourceException {
		if (form.isMakeWoByQi()) {
			quotationItemDao.load(form.getQuotationItem());
			for (QuotationItemExe qie : form.getQuotationItem().getQuotationItemAllExeList())
				quotationItemExeDao.deleteWorkOrder(qie);

		} else {
			QuotationItemExe qie = quotationItemExeDao.get(QuotationItemExe.class, form.getModel().getId());
			quotationItemExeDao.deleteWorkOrder(qie);
		}

	}

	@Autowired
	private GenHub genHub;

	/**
	 * In lenh sx theo ban ve, kg theo ket qua tim kiem
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @param form
	 * @throws Exception
	 */
	public void printfQr(ModelMap model, HttpServletRequest rq, HttpServletResponse response, QuotationItemExeForm form)
			throws Exception {
		quotationItemDao.load(form.getQuotationItem());
		// group LSX theo danh sach may
		// groupByMachine(form.getQuotationItem());
		// Group LSX theo day chuyen
		form.getQuotationItem().groupWoByProductLine();
		if (form.getQuotationItem().getLstProductionLine() == null
				|| form.getQuotationItem().getLstProductionLine().isEmpty())
			throw new ResourceException("Cần tạo LSX và lưu thông tin trước!");
		IFile file = genHub.genFile("LSX", XmlUtils.convertObjToXML(form.getQuotationItem()), SaveFormat.PDF, null);
		getDao().clear();
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

	/**
	 * Group LSX theo danh sach may
	 * 
	 * @param qi
	 * @throws ResourceException
	 */
	private void groupByMachine(QuotationItem qi) throws ResourceException {
		for (QuotationItemExe qie : qi.getQuotationItemAllExeList()) {
			if (Boolean.TRUE.equals(qie.getExeStepId().getProgram()))
				continue;
			for (WorkOrder wo : qie.getWorkOrders()) {
				if (wo.getAstMachine().getLstWorkOrder() == null)
					wo.getAstMachine().setLstWorkOrder(new ArrayList<WorkOrder>());
				wo.getAstMachine().getLstWorkOrder().add(wo);
				if (qi.getLstAsignedMachine() == null)
					qi.setLstAsignedMachine(new ArrayList<AstMachine>());
				if (!qi.getLstAsignedMachine().contains(wo.getAstMachine()))
					qi.getLstAsignedMachine().add(wo.getAstMachine());
				// Khu de quy vo han khi serialize xml
				wo.setAstMachine(null);
			}
		}
		if (qi.getLstAsignedMachine() == null || qi.getLstAsignedMachine().isEmpty())
			throw new ResourceException("Cần tạo LSX và lưu thông tin trước!");

	}
}
