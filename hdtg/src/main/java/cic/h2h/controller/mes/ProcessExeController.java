package cic.h2h.controller.mes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ibm.icu.util.Calendar;

import cic.h2h.dao.hibernate.AstMachineDao;
import cic.h2h.dao.hibernate.ExeStepTypeDao;
import cic.h2h.dao.hibernate.ProcessExeDao;
import cic.h2h.dao.hibernate.QuotationItemDao;
import cic.h2h.form.ProcessExeForm;
import cic.utils.ExportExcel;
import common.util.FormatNumber;
import common.util.Formater;
import common.util.ResourceException;
import entity.ProcessExe;
import entity.frwk.SysUsers;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysUsersDao;

@Controller
@RequestMapping(value = "/processExe")
public class ProcessExeController extends CatalogController<ProcessExeForm, ProcessExe> {
	@Autowired
	private ProcessExeDao processExeDao;

	@Override
	public void createSearchDAO(HttpServletRequest request, ProcessExeForm form, BaseDao<ProcessExe> dao)
			throws Exception {
		dao.addRestriction(Restrictions.eq("company", getSessionUser().getCompany()));
		if (!Formater.isNull(form.getWorker()))
			dao.addRestriction(Restrictions.eq("sysUser.id", form.getWorker()));

		if (!Formater.isNull(form.getFrDate()))
			dao.addRestriction(Restrictions.ge("startTime", Formater.str2date(form.getFrDate())));
		if (!Formater.isNull(form.gettDate())) {
			Calendar t = Calendar.getInstance();
			t.setTime(Formater.str2date(form.gettDate()));
			t.add(Calendar.DATE, 1);
			dao.addRestriction(Restrictions.lt("startTime", t.getTime()));
		}
		if (!Formater.isNull(form.getUpdatorSearch()))
			dao.addRestriction(Restrictions.eq("updator", new SysUsers(form.getUpdatorSearch())));
		if (!Formater.isNull(form.getType())) {
			dao.addRestriction(Restrictions.eq("type", form.getType()));
		}

		// qi
		boolean qiAlias = false;
		if (!Formater.isNull(form.getDrawingCode()) || !Formater.isNull(form.getManageCode())) {
			dao.createAlias("quotationItem", "qi");
			qiAlias = true;
			// Tim kiem dung, ignore case
			if (!Formater.isNull(form.getDrawingCode())) {
				dao.addRestriction(Restrictions.eq("qi.code", form.getDrawingCode()).ignoreCase());
			}
			// Tim kiem dung, ignore case
			if (!Formater.isNull(form.getManageCode())) {
				dao.addRestriction(Restrictions.eq("qi.manageCode", form.getManageCode()).ignoreCase());
			}
		}
		if (!Formater.isNull(form.getOrderCode()) || !Formater.isNull(form.getFromDate())
				|| !Formater.isNull(form.getToDate()) || !Formater.isNull(form.getCusName())
				|| !Formater.isNull(form.getCusCode())) {
			if (!qiAlias) {
				dao.createAlias("quotationItem", "qi");
				qiAlias = true;
			}
			dao.createAlias("qi.quotationId", "q");
			if (!Formater.isNull(form.getOrderCode()))
				dao.addRestriction(
						Restrictions.like("q.code", form.getOrderCode().trim(), MatchMode.ANYWHERE).ignoreCase());

			if (!Formater.isNull(form.getFromDate())) {
				dao.addRestriction(Restrictions.ge("q.quotationDate", Formater.str2date(form.getFromDate())));
			}
			if (!Formater.isNull(form.getToDate())) {
				Calendar cTodatePlusOne = Calendar.getInstance();
				cTodatePlusOne.setTime(Formater.str2date(form.getToDate()));
				cTodatePlusOne.add(Calendar.DATE, 1);
				dao.addRestriction(Restrictions.lt("q.quotationDate", cTodatePlusOne.getTime()));
			}
			if (!Formater.isNull(form.getCusName()) || !Formater.isNull(form.getCusCode())) {
				dao.createAlias("q.customer", "cus");
				if (!Formater.isNull(form.getCusName()))
					dao.addRestriction(Restrictions.like("cus.orgName", form.getCusName().trim(), MatchMode.ANYWHERE)
							.ignoreCase());
				if (!Formater.isNull(form.getCusCode()))
					dao.addRestriction(
							Restrictions.like("cus.code", form.getCusCode().trim(), MatchMode.ANYWHERE).ignoreCase());
			}
		}

		dao.addOrder(Order.desc("createDate"));
	}

	@Override
	protected void pushToJa(JSONArray ja, ProcessExe e, ProcessExeForm modelForm) throws Exception {
		if (e.getQuotationItem() != null) {
			ja.put(e.getQuotationItem().getCode());
			ja.put(e.getQuotationItem().getManageCode());
		} else {
			ja.put("");
			ja.put("");
		}
		if (e.getSysUser() != null)
			ja.put(e.getSysUser().getName());
		else
			ja.put("");
		if (e.getStartTime() != null)
			ja.put(Formater.date2ddsmmsyyyspHHmmss(e.getStartTime()));
		else
			ja.put("");
		if (e.getEndTime() != null)
			ja.put(Formater.date2ddsmmsyyyspHHmmss(e.getEndTime()));
		else
			ja.put("");
		if (e.getTotalAmountEstTime() != null)
			ja.put(Formater.num2str(e.getTotalAmountEstTime()));
		else
			ja.put("");
		if (e.getExeTime() != null)
			ja.put(Formater.num2str(e.getExeTime()));
		else
			ja.put("");
		if (e.getTotalAmountEstTime() != null) {
			long cham = e.getExeTime().longValue()-e.getTotalAmountEstTime();
			double chamPercent = cham*100/e.getTotalAmountEstTime();
			if(cham>0) {
				ja.put("<font color ='red'>" + FormatNumber.num2Str(chamPercent) + "</font>");
				ja.put("<font color ='red'>" + FormatNumber.num2Str(cham) + "</font>");				
			}else {
				ja.put(FormatNumber.num2Str(chamPercent));
				ja.put( FormatNumber.num2Str(cham));
			}
		}else {
			ja.put("");
			ja.put("");
		}
		ja.put(Formater.num2str(e.getTotalAmount()));
		ja.put(Formater.num2str(e.getAmount()));
		ja.put(Formater.num2str(e.getNgAmount()));
//		if ("QC".equals(e.getType()))
		if (e.getBrokenAmount() != null)
			ja.put(Formater.num2str(e.getBrokenAmount()));
		else
			ja.put("");
		if (e.getUpdator() != null)
			ja.put(e.getUpdator().getName());
		else
			ja.put("");

		ja.put("<a style='color:blue;' href='javascipt:;' onclick = 'editProductionHistory(\"" + e.getId() + "\",\""
				+ Formater.date2ddsmmsyyyspHHmmss(e.getStartTime()) + "\",\""
				+ Formater.date2ddsmmsyyyspHHmmss(e.getEndTime()) + "\",\"" + FormatNumber.num2Str(e.getAmount())
				+ "\",\"" + e.getSysUser().getId() + "\",\"" + FormatNumber.num2Str(e.getTotalAmount()) + "\",\""
				+ FormatNumber.num2Str(e.getBrokenAmount()) + "\",\"" + FormatNumber.num2Str(e.getNgAmount()) + "\",\""
				+ (Formater.isNull(e.getNgDescription()) ? "" : e.getNgDescription()) + "\",\""
				+ ((e.getNgRepaire() == null || Boolean.FALSE.equals(e.getNgRepaire())) ? "false" : "true") + "\")'> "
				+ (Boolean.TRUE.equals(e.getNgRepaire()) ? "Sửa NG"
						: "CL".equals(e.getType()) ? "Gia công" : "Cập nhật")
				+ " </a>");
	}

	@Override
	public BaseDao<ProcessExe> getDao() {
		return processExeDao;
	}

	@Autowired
	private QuotationItemDao quotationItemDao;

	public void addProcessExe(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ProcessExeForm form)
			throws Exception {
		String processExeId = rq.getParameter("processExeId");
		ProcessExe processExe = processExeDao.get(processExeId);
		if (processExe == null)
			throw new ResourceException("Không tồn tại chi tiết chuyển");
		String rpAmount = rq.getParameter("rpAmount");
		processExe.setNgAmountStr(rpAmount);
		String startTime = rq.getParameter("startTime");
		processExe.setStartTimeStr(startTime);
		String endTime = rq.getParameter("endTime");
		processExe.setEndTimeStr(endTime);

		String ngAmount = rq.getParameter("ngAmount");
		processExe.setBrokenAmountStr(ngAmount);
		String ngDes = rq.getParameter("ngDes");
		processExe.setNgDescription(ngDes);
		if (processExe.getStartTime().after(processExe.getEndTime()))
			throw new ResourceException("Thời điểm kết thúc phải sau thời điểm bắt đầu!");
		String amount = rq.getParameter("amount");
		processExe.setAmountStr(amount);
		String totalAmount = rq.getParameter("totalAmount");
		processExe.setTotalAmountStr(totalAmount);

		processExe.setType(rq.getParameter("type"));
		if (processExe.getNgRepaire() != null && !processExe.getNgRepaire()) {
			if ((processExe.getTotalAmount().doubleValue() - processExe.getAmount().doubleValue()
					- processExe.getNgAmount().doubleValue()) < 0)
				throw new ResourceException("Số lượng hoàn thành phải nhỏ hơn hoặc bằng tổng số!");
		}

		String userid = rq.getParameter("userid");
		SysUsers worker = sysUsersDao.getUser(userid);
		processExe.setSysUser(worker);
		processExe.setUpdator(getSessionUser());
		processExe.setExeTime(
				new BigDecimal((processExe.getEndTime().getTime() - processExe.getStartTime().getTime()) / 1000 / 60));
		processExe.setCreateDate(new Date());

		// save process exe
		processExeDao.save(processExe);
	}

	@Override
	public String getJsp() {
		return "ke_hoach_san_xuat/lich_su_NguoiQC";
	}

	@Autowired
	private SysUsersDao sysUsersDao;
	@Autowired
	private ExeStepTypeDao exeStepTypeDao;
	@Autowired
	private AstMachineDao astMachineDao;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ProcessExeForm form)
			throws Exception {
		model.addAttribute("lstStepType", exeStepTypeDao.getAll());
		model.addAttribute("lstSysUser", sysUsersDao.getCompanyUser(getSessionUser().getCompany()));
		model.addAttribute("lstAstMachine", astMachineDao.getAll(getSessionUser().getCompany()));
		if ("QC".equals(form.getType()))
			form.setHisType("hQc");

	}

	public void delWorkOrderExe(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ProcessExeForm form)
			throws Exception {
		String processExeId = rq.getParameter("processExeId");
		ProcessExe orderExe = processExeDao.get(processExeId);
		processExeDao.del(orderExe);

	}

	@Autowired
	private ExportExcel exportExcel;

	public void exportExcel(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ProcessExeForm form)
			throws Exception {
		if (Formater.isNull(form.getFrDate()))
			throw new ResourceException("Phải nhập thời điểm bắt đầu từ");
		Date toDate = Formater.isNull(form.gettDate()) ? Calendar.getInstance().getTime()
				: Formater.str2date(form.gettDate());
		Calendar cTodate = Calendar.getInstance();
		cTodate.setTime(toDate);
		cTodate.add(Calendar.MONTH, -3);
		cTodate.add(Calendar.DATE, -1);
		if (cTodate.getTime().after(Formater.str2date(form.getFrDate())))
			throw new ResourceException("Khoảng thời gian bắt đầu - kết thúc phải nhỏ hơn hoặc bằng 3 tháng!");

		ProcessExeDao dao = (ProcessExeDao) getDao().createCriteria();
		createSearchDAO(rq, form, dao);
		List<?> temp = (List<?>) dao.search();
		if (temp.isEmpty())
			throw new ResourceException("Không tồn tại dữ liệu xuất!");
		List<ProcessExe> workOrderExes = new ArrayList<ProcessExe>();
		for (Object e : temp) {
			if (e.getClass().isArray()) {
				for (Object o : (Object[]) e) {
					if (o.getClass().equals(dao.getModelClass())) {
						workOrderExes.add((ProcessExe) o);
					}
				}
			}else {
				ProcessExe processExe = (ProcessExe) e;
				workOrderExes.add(processExe);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("reports", workOrderExes);
		map.put("drawCodeSearch", form.getDrawingCode());
		map.put("manageCodeSearch", form.getManageCode());
		map.put("orderCode", form.getOrderCode());
		map.put("frDate", form.getFrDate());
		map.put("tDate", form.gettDate());
		map.put("fromDate", form.getFromDate());
		map.put("toDate", form.getToDate());
		map.put("worker", form.getWorker());
		map.put("workerUp", form.getUpdatorSearch());
		if ("CL".equals(form.getType()))
			exportExcel.export("Lich_su_gia_cong_nguoi", rs, map);
		else
			exportExcel.export("Lich_su_gia_cong_qc", rs, map);

	}

}
