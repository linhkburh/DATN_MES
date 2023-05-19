package cic.h2h.controller.mes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.icu.util.Calendar;

import cic.h2h.dao.hibernate.BssFactoryUnitDao;
import cic.h2h.dao.hibernate.CustomerManageDao;
import cic.h2h.dao.hibernate.ExchRateDao;
import cic.h2h.dao.hibernate.ExeStepDao;
import cic.h2h.dao.hibernate.ExeStepTypeDao;
import cic.h2h.dao.hibernate.MaterialDao;
import cic.h2h.dao.hibernate.MaterialTypeDao;
import cic.h2h.dao.hibernate.QuotationDao;
import cic.h2h.dao.hibernate.QuotationItemDao;
import cic.h2h.dao.hibernate.QuotationItemExeDao;
import cic.h2h.dao.hibernate.ReduceByAmountDao;
import cic.h2h.form.QuotationItemForm;
import cic.utils.ExportExcel;
import cic.utils.JasperUtils;
import common.sql.DataSourceConfiguration;
import common.util.FormatNumber;
import common.util.Formater;
import common.util.JsonUtils;
import common.util.ReflectionUtils;
import common.util.ResourceException;
import constants.Constants;
import constants.Mes;
import entity.Customer;
import entity.ExchRate;
import entity.ExeStep;
import entity.ExeStepType;
import entity.Material;
import entity.MaterialType;
import entity.ParentItem;
import entity.Quotation;
import entity.QuotationItem;
import entity.QuotationItemExe;
import entity.QuotationItemMaterial;
import entity.ReduceByAmount;
import entity.WorkOrder;
import entity.WorkPro;
import entity.frwk.BssParam;
import entity.frwk.SysDictParam;
import entity.frwk.SysFile;
import entity.frwk.SysParam;
import frwk.bean.FTPUtils;
import frwk.bean.FtpInf;
import frwk.constants.ApproveConstants;
import frwk.controller.CatalogController;
import frwk.controller.ClearOnFinish;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.BssParamDao;
import frwk.dao.hibernate.sys.CompanyDao;
import frwk.dao.hibernate.sys.RightUtils;
import frwk.dao.hibernate.sys.SysDictParamDao;
import frwk.dao.hibernate.sys.SysFileDao;
import frwk.dao.hibernate.sys.SysParamDao;
import frwk.dao.hibernate.sys.SysUsersDao;
import frwk.utils.ApplicationContext;

@Controller
@RequestMapping("/item")
public class QuotationItemController extends CatalogController<QuotationItemForm, QuotationItem> {

	private static Logger log = Logger.getLogger(QuotationItemController.class);

	@Autowired
	private QuotationItemDao quotationItemDao;
	@Autowired
	private QuotationDao quotationDao;
	@Autowired
	private ExeStepDao exeStepDao;
	@Autowired
	private MaterialDao materialDao;

	@Override
	public void createSearchDAO(HttpServletRequest request, QuotationItemForm form, BaseDao<QuotationItem> dao)
			throws Exception {
		if (!Formater.isNull(form.getKeyWord())) {
			dao.addRestriction(Restrictions.or(
					Restrictions.like("code", form.getKeyWord(), MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.or(Restrictions.like("name", form.getKeyWord(), MatchMode.ANYWHERE).ignoreCase(),
							Restrictions.or(
									Restrictions.like("manageCode", form.getKeyWord(), MatchMode.ANYWHERE).ignoreCase(),
									Restrictions.like("workOderNumber", form.getKeyWord(), MatchMode.ANYWHERE)
											.ignoreCase()))));
		}
		if (!Formater.isNull(form.getKeyword_code()))
			dao.addRestriction(
					Restrictions.like("code", form.getKeyword_code().trim(), MatchMode.ANYWHERE).ignoreCase());
		if (!Formater.isNull(form.getKeyword_name()))
			dao.addRestriction(
					Restrictions.like("name", form.getKeyword_name().trim(), MatchMode.ANYWHERE).ignoreCase());
		if (form.getQuotation() != null && !Formater.isNull(form.getQuotation().getId()))
			dao.addRestriction(Restrictions.eq("quotationId", form.getQuotation()));

		if (!Formater.isNull(form.getItemCode())) {
			dao.addRestriction(Restrictions.like("code", form.getItemCode().trim(), MatchMode.ANYWHERE).ignoreCase());
		}

		if ("select".equals(form.getTo())) {
			// Chi tim chi tiet thuoc bao gia khac
			String currentQuotationId = request.getParameter("currentQuotationId");
			if (!Formater.isNull(currentQuotationId))
				dao.addRestriction(Restrictions.ne("quotationId", new Quotation(currentQuotationId)));
		}
		dao.addOrder(Order.desc("createDate"));
	}

	@Autowired
	private BssParamDao bssParamDao;

	@Override
	protected void pushToJa(JSONArray ja, QuotationItem e, QuotationItemForm modelForm) throws Exception {
		for (QuotationItemExe qi : e.getQuotationItemAllExeList()) {
			if (!Boolean.TRUE.equals(qi.getExeStepId().getProgram()))
				e.getQuotationItemExeList().add(qi);
		}
		if ("select".equals(modelForm.getTo())) {
			ja.put(e.getQuotationId().getCustomer().getCode());
			ja.put(e.getQuotationId().getCustomer().getOrgName());
			ja.put(e.getQuotationId().getQuotationDateStr());
		}
		ja.put(e.getWorkOderNumber());
		ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + e.getId() + "\")'>" + e.getCode() + "</a>");

		ja.put(e.getName());
		ja.put(e.getManageCode());
		if (e.getQuotationItemMaterialList().size() > 0) {
			QuotationItemMaterial temp = e.getQuotationItemMaterialList().iterator().next();
			// Vat lieu
			if (temp.getMarteriaBackupId() == null)
				ja.put(temp.getMarterialId().getCode());
			else {
				ja.put(temp.getMarterialId().getCode() + "/" + temp.getMarteriaBackupId().getCode());
			}
			// Kich thuoc
			String demention = "";
			if (temp.getSizeLong() != null)
				demention += " L: " + temp.getSizeLongStr() + ";";
			if (temp.getSizeWidth() != null)
				demention += " W: " + temp.getSizeWidthStr() + ";";
			if (temp.getSizeWeigh() != null)
				demention += " H: " + temp.getSizeWeighStr() + ";";
			// The tich
			BigDecimal volum = temp.getVolumn();
			if (volum != null)
				demention += " V: " + FormatNumber.num2Str(volum) + ";";
			// Trong luong
			if (temp.getSizeWeigh() != null)
				demention += " M: " + temp.getSizeWeighStr() + ";";

			if (Formater.isNull(demention)) {
				ja.put("");
			} else {
				demention = demention.substring(0, demention.length() - 1);
				ja.put(demention.trim());
			}

		} else {
			ja.put("");
			ja.put("");
		}
		ja.put(e.getTotalPriceStr());
		if (e.getQuality() != null)
			ja.put(FormatNumber.num2Str(BigDecimal.valueOf(e.getQuality().longValue())));
		else
			ja.put("");
		ja.put(e.getTotalPriceStr());

		if (!"select".equals(modelForm.getTo())) {
			ja.put(e.getStatusDescription());
		} else {
			ja.put(Formater.date2str(e.getCreateDate()));
			ja.put(e.getCreator());
		}
//		ja.put("<img src='data:image/jpeg;base64," + Utils.generateQRCode(e.getCode(), 50, 50)
//				+ "' height=\"80\" width=\"80\">");

	}

	@Autowired
	private RightUtils rightUtils;

	@Override
	public BaseDao<QuotationItem> getDao() {
		return quotationItemDao;
	}

	@Override
	public String getJsp() {
		return "bao_gia/chi_tiet_bao_gia";
	}

	@Autowired
	private SysUsersDao sysUsersDao;
	@Autowired
	private BssFactoryUnitDao bssFactoryUnitDao;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QuotationItemForm form)
			throws Exception {
		String quotationId = rq.getParameter("quotationId");
		// Danh sach file
		ObjectMapper om = JsonUtils.objectMapper2();
		List<SysDictParam> lstDrwngFile = sysDictParamDao.getByType("DOC");
		model.addAttribute("lstFile", StringEscapeUtils.escapeEcmaScript(om.writeValueAsString(lstDrwngFile)));
		List<SysDictParam> lstProFileType = sysDictParamDao.getByType("PRO");
		model.addAttribute("lstProFileType", StringEscapeUtils.escapeEcmaScript(om.writeValueAsString(lstProFileType)));
		Quotation q = new Quotation();
		if (!Formater.isNull(quotationId)) {
			q = quotationDao.get(quotationId);
			// Tinh so tien quy doi
			if (q.getCurrency() == null || Formater.isNull(q.getCurrency().getValue())
					|| q.getCurrency().getValue().equals("USD"))
				q.setExchangePrice(q.getPrice());
			else {
				ExchRate usd = exchRateDao.getByCurrencyAndDate("USD", q.getQuotationDate());
				ExchRate currency = exchRateDao.getByCurrencyAndDate(q.getCurrency(), q.getQuotationDate());
				if (usd != null && currency != null && usd.getExchRate() != null && currency.getExchRate() != null
						&& currency.getExchRate().compareTo(new BigDecimal(0)) != 0 & q.getPrice() != null)
					q.setExchangePrice(q.getPrice().multiply(usd.getExchRate()).divide(currency.getExchRate(), 3,
							RoundingMode.HALF_UP));
			}
			form.setQuotation(q);
//			if (RightUtils.haveAction(rq, "method=toApprove"))
//				model.addAttribute("toApprove", true);
//			if (RightUtils.haveAction(rq, "method=approve"))
//				model.addAttribute("approve", true);
			ApplicationContext appContext = (ApplicationContext) rq.getSession()
					.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
			if (rightUtils.haveRight("QUOTATION_TOAPP", appContext))
				model.addAttribute("toApprove", true);
			if (rightUtils.haveRight("QUOTATION_APP", appContext))
				model.addAttribute("approve", true);

		}
		model.addAttribute("lstWorker", sysUsersDao.getCompanyUser(getSessionUser().getCompany()));
		model.addAttribute("lstAc", sysUsersDao.getDepartmentUser(null, Mes.DEPT_CODE_AC));
		model.addAttribute("lstTech", sysUsersDao.getDepartmentUser(getSessionUser().getCompany(), Mes.DEPT_CODE_PRO));
		model.addAttribute("lstPlan", sysUsersDao.getDepartmentUser(getSessionUser().getCompany(), Mes.DEPT_CODE_PLAN));

		model.addAttribute("quotation", q);
		model.addAttribute("listMaterial", materialDao.getAll());
		List<ExeStepType> listExeStepType = exeStepTypeDao.getAll();
		for (ExeStepType type : listExeStepType)
			type.sparateExeType();
		// model.addAttribute("jaExeType",
		// StringEscapeUtils.escapeEcmaScript(JsonUtils.writeToString(listExeStepType)));
		model.addAttribute("jaExeType", StringEscapeUtils.escapeEcmaScript(om.writeValueAsString(listExeStepType)));
		model.addAttribute("listExeStepType", listExeStepType);
		model.addAttribute("listCurrency", sysDictParamDao.getByType(Constants.CAT_TYPE_CURRENCY));
		// Loai tien mac dinh
		SysDictParam usd = sysDictParamDao.getByTypeAndCode(Constants.CAT_TYPE_CURRENCY, "USD");
		model.addAttribute("defaultCurrency", usd.getId());
		// Phan tram hao ton nhien lieu mac dinh
		SysParam defaultWaste = sysParamDao.getSysParamByCode("WASTE");
		try {
			model.addAttribute("defaultWaste", FormatNumber.num2Str(new BigDecimal(defaultWaste.getValue())));
		} catch (Exception ex) {
			model.addAttribute("defaultWaste", "10");
		}
		model.addAttribute("to", form.getTo());
		model.addAttribute("drawingTypes", sysDictParamDao.getByType(Constants.CAT_DRAWVING_TYPE));
		model.addAttribute("lstCompany", companyDao.getAllOrderAstName());
		model.addAttribute("lstFactoryUnit", bssFactoryUnitDao.getFactoryByCompany(getSessionUser().getCompany()));
		model.addAttribute("lstsiggle", sysDictParamDao.getByType("SIGGLE"));
	}

	@Autowired
	private CompanyDao companyDao;
	@Autowired
	private ExeStepTypeDao exeStepTypeDao;

	@Autowired
	private SysParamDao sysParamDao;

	@Autowired
	private SysDictParamDao sysDictParamDao;

	public void loadDataExeType(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			QuotationItemForm form) throws Exception {
		String stepType = request.getParameter("stepType");
		response.setContentType("text/plain;charset=utf-8");
		PrintWriter pw = response.getWriter();
		// JSONArray array = null;
		List<ExeStep> lst = exeStepDao.getByType(stepType);
		JSONObject jsonObject = new JSONObject();
		JSONArray array = new JSONArray(new ObjectMapper().writeValueAsString(lst));
		jsonObject.put("exeStep", array);
		pw.print(jsonObject);
		pw.close();
	}

	@Autowired
	private QuotationItemExeDao quotationItemExeDao;

	private void validateqie(QuotationItemExe qie, int idx) throws ResourceException {
		if (Formater.isNull(qie.getId()))
			return;
		// Lay du lieu duoc db
		qie.setExeStepId(exeStepDao.get(ExeStep.class, qie.getExeStepId().getId()));
		QuotationItemExe oldExe = quotationItemExeDao.get(QuotationItemExe.class, qie.getId());
		if (!oldExe.getWorkPros().isEmpty() || !oldExe.getWorkOrders().isEmpty()) {
			if (!oldExe.getExeStepId().getId().equals(qie.getExeStepId().getId())) {
				if (Boolean.TRUE.equals(qie.getExeStepId().getProgram())) {
					throw new ResourceException(
							"Lập trình, dòng %s: đã tồn tại lệnh sản xuất, không được phép chọn lại hình thức gia công %s thành %s!",
							new Object[] { idx + 1, oldExe.getExeStepId().getStepType().getName(),
									qie.getExeStepId().getStepType().getName() });
				} else {
					throw new ResourceException(
							"Gia công, dòng %s: đã tồn tại lệnh sản xuất, không được phép chọn lại công đoạn %s thành %s!",
							new Object[] { idx + 1, oldExe.getExeStepId().getStepName(),
									qie.getExeStepId().getStepName() });
				}

			}

		}
	}

	@Autowired
	private CustomerManageDao customerManageDao;

	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QuotationItemForm form)
			throws Exception {
		// Kiem tra quyen
		if (Formater.isNull(form.getQuotationItem().getId())) {
			form.getQuotationItem().setQuotationId(form.getQuotation());
			if (!rightUtils.haveAction(rq, "method=save&saveType=createNew"))
				throw new ResourceException("B&#7841;n kh&#244;ng c&#243; quy&#7873;n th&#234;m m&#7899;i!");
		} else {
			if (!rightUtils.haveAction(rq, "method=save&saveType=update"))
				throw new ResourceException("B&#7841;n kh&#244;ng c&#243; quy&#7873;n s&#7917;a!");
		}
		form.getQuotationItem().setManageCode(form.getQuotationItem().getManageCode().trim());
		// ma quan ly goc
		if (!Formater.isNull(form.getQuotationItem().getRootManageCode())) {
			form.getQuotationItem().setRootManageCode(form.getQuotationItem().getRootManageCode().trim());
			if (form.getQuotationItem().getRootManageCode().equalsIgnoreCase(form.getQuotationItem().getManageCode()))
				throw new ResourceException("Mã quản lý trùng mã quản lý gốc",
						new Object[] { form.getQuotationItem().getRootManageCode() });
			QuotationItem root = quotationItemDao.getByManageCode(form.getQuotationItem().getRootManageCode(),
					getSessionUser().getCompany());
			if (root == null)
				throw new ResourceException("Mã quản lý gốc: %s không tồn tại",
						new Object[] { form.getQuotationItem().getRootManageCode() });
		}
		// Vat lieu
		for (QuotationItemMaterial item : form.getQuotationItem().getQuotationItemMaterialList()) {
			if (Formater.isNull(item.getId()))
				item.setId(null);
			// Khong chon vat lieu thay the
			if (Formater.isNull(item.getMarteriaBackupId().getId()))
				item.setMarteriaBackupId(null);
			// Khong chon vat lieu
			if (Formater.isNull(item.getMarterialId().getId()))
				item.setMarterialId(null);
			item.setQuotationItemId(form.getQuotationItem());
		}
		// Gia cong
		List<QuotationItemExe> allExe = form.getQuotationItem().getQuotationItemAllExeList();
		List<QuotationItemExe> pro = form.getQuotationItem().getQuotationItemProList();
		// Add, check trung cong doan lap trinh
		for (int i = 0; i < pro.size(); i++) {
			QuotationItemExe exe = pro.get(i);
			exe.setDisOrder((short) i);
			validateqie(exe, i);
			for (int j = 0; j < i; j++) {
				QuotationItemExe addedExe = form.getQuotationItem().getQuotationItemAllExeList().get(j);
				if (exe.getExeStepId().getId().equals(addedExe.getExeStepId().getId()))
					throw new ResourceException("Công đoạn lập trình dòng %s trùng với dòng %s",
							new Object[] { i + 1, j + 1 });
			}
			form.getQuotationItem().getQuotationItemAllExeList().add(exe);
			exe.setQuotationItemId(form.getQuotationItem());
		}
		// Tong thoi gian gia cong chi tiet
		form.getQuotationItem().setExeTime(new BigDecimal(0));
		// Add, check trung cong doan gia cong
		for (int i = 0; i < form.getQuotationItem().getQuotationItemExeList().size(); i++) {
			QuotationItemExe exe = form.getQuotationItem().getQuotationItemExeList().get(i);
			exe.setDisOrder((short) i);
			validateqie(exe, i);
			allExe.add(exe);
			// form.getQuotationItem().setExeTime(form.getQuotationItem().getExeTime().add(exe.getEstTime()));
			exe.setQuotationItemId(form.getQuotationItem());
			// Add phan tu dau tien luon khong bi trung
			if (allExe.size() == pro.size())
				continue;
			for (int j = pro.size(); j < i + pro.size(); j++) {
				QuotationItemExe addedExe = allExe.get(j);
				if (exe.getExeStepId().getId().equals(addedExe.getExeStepId().getId()))
					throw new ResourceException("Công đoạn gia công dòng %s trùng với dòng %s",
							new Object[] { i + 1, j + 1 - pro.size() });
			}
		}

		// Tinh gia thanh
		form.getQuotationItem().calulatePrice();
		if (Formater.isNull(form.getQuotationItem().getId())) {
			form.getQuotationItem().setCreateDate(Calendar.getInstance().getTime());
			form.getQuotationItem().setCreator(getSessionUser().getId());
			form.getQuotationItem().setCompany(getSessionUser().getCompany());
			form.getQuotationItem().setDepartment(getSessionUser().getDepartment());
		} else {
			// Lay lai thong tin ngay tao, nguoi tao doi DB
			QuotationItem old = quotationItemDao.getObject(form.getQuotationItem());
			if (old.getQuality().longValue() != form.getQuotationItem().getQuality().longValue()) {
				for (QuotationItemExe qie : old.getQuotationItemAllExeList()) {
					if (Boolean.TRUE.equals(qie.getExeStepId().getProgram()))
						continue;
					if (!qie.getWorkOrders().isEmpty())
						throw new ResourceException(
								"Chi tiết đã tạo LSX, không được thay đổi số lượng, cần tạo mã quản lý mới cho chi tiết bổ sung!");
				}
			}

			if (old.getQuotationId().getStatus() != null
					&& !old.getQuotationId().getStatus().equals(ApproveConstants.APP_STS_NEW)
					&& !old.getQuotationId().getStatus().equals(ApproveConstants.APP_STS_UN_APP))
				throw new ResourceException("Tr&#7841;ng th&#225;i b&#225;o gi&#225; kh&#244;ng h&#7907;p l&#7879;!");
			form.getQuotationItem().setCreateDate(old.getCreateDate());
			form.getQuotationItem().setCreator(old.getCreator());
			for (QuotationItemExe exe : form.getQuotationItem().getQuotationItemExeList()) {
				if (exe.getWorker() != null && Formater.isNull(exe.getWorker().getId()))
					exe.setWorker(null);
			}
			List<String> lstDelFTP = new ArrayList<String>();
			List<QuotationItemExe> lstDel = new ArrayList<QuotationItemExe>();
			for (QuotationItemExe oldQie : old.getQuotationItemAllExeList()) {
				boolean checkDel = true;
				for (QuotationItemExe newQie : allExe) {
					if (Formater.isNull(newQie.getId()))
						continue;
					if (oldQie.getId().equals(newQie.getId())) {
						checkDel = false;
						continue;
					}
				}
				if (checkDel) {
					lstDel.add(oldQie);
					List<SysFile> lstFile = sysFileDao.getFileByRecordId(oldQie.getId());
					for (SysFile file : lstFile) {
						lstDelFTP.add(file.getId());
						sysFileDao.del(file);
					}
				}
			}
			if (!Formater.isNull(lstDelFTP))
				ftpUtils.removeFile(ftpInf, lstDelFTP);
			for (QuotationItemExe item : lstDel)
				quotationItemExeDao.del(item);

		}
		// Luu thong tin chi tiet
		quotationItemDao.save(form.getQuotationItem());
		returnJson(rs, form.getQuotationItem());

	}

	@Autowired
	private SysFileDao sysFileDao;

	public void loadQuotationItem(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			QuotationItemForm form) throws Exception {
		String id = request.getParameter("id");
		response.setContentType("text/plain;charset=utf-8");
		PrintWriter pw = response.getWriter();
		// JSONArray array = null;
		QuotationItem quotation = quotationItemDao.getObject(QuotationItem.class, id);
		JSONObject obj = new JSONObject(new ObjectMapper().writeValueAsString(quotation));
		pw.print(obj);
		pw.close();
	}

	public void loadStepByType(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			QuotationItemForm form) throws IOException {
		String stepTypeId = request.getParameter("stepTypeId");
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Cache-Control", "no-store");
		PrintWriter pw = response.getWriter();
		List<ExeStep> lst = exeStepDao.getByType(stepTypeId);
		String lstAsJson = new ObjectMapper().writeValueAsString(lst);
		JSONArray jsonArray = new JSONArray(lstAsJson);
		pw.print(jsonArray);
		pw.close();

	}

	public void loadPriceByMaterial(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			QuotationItemForm form) throws Exception {
		String materialId = request.getParameter("materialId");
		Material m = materialDao.getObject(Material.class, materialId);
		returnJson(response, m);
	}

	public void loadPriceByExeStep(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			QuotationItemForm form) throws Exception {
		String exeStepId = request.getParameter("exeStepId");
		response.setContentType("text/plain;charset=utf-8");
		PrintWriter pw = response.getWriter();
		ExeStep m = exeStepDao.getObject(ExeStep.class, exeStepId);
		if (m.getInitPrice() == null) {
			BssParam opr = bssParamDao.getSysParamByCode("OPR");
			if (opr != null) {
				try {
					m.setInitPrice(new BigDecimal(opr.getValue()));
				} catch (Exception ex) {
					log.error(ex);
				}
			}

		}

		JSONObject obj = new JSONObject(new ObjectMapper().writeValueAsString(m));
		pw.print(obj);
		pw.close();
	}

	@Autowired
	private ExchRateDao exchRateDao;

	public void caculatorMaterial(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			QuotationItemForm form) throws Exception {
		Material material = null, backUpMarterial = null;
		String backUpMarterialId = request.getParameter("backUpMarterialId");
		String marterialId = request.getParameter("marterialId");
		String quotationDateStr = request.getParameter("quotationDate");
		String quotationId = request.getParameter("quotationId");

		Date exchangeDate = Formater.isNull(quotationId) ? Formater.str2date(quotationDateStr)
				: quotationDao.get(Quotation.class, quotationId).getQuotationDate();
		BigDecimal exchangRate = null;
		if (!Formater.isNull(backUpMarterialId)) {
			// Khong quan tam den material
			backUpMarterial = materialDao.getObject(Material.class, backUpMarterialId);
			exchangRate = exchRateDao.getRate(backUpMarterial.getCurrency(), exchangeDate);
		} else {
			material = materialDao.getObject(Material.class, marterialId);
			exchangRate = exchRateDao.getRate(material.getCurrency(), exchangeDate);
		}
		String sizeLong = request.getParameter("sizeLong");
		String sizeWidth = request.getParameter("sizeWidth");
		String sizeHeight = request.getParameter("sizeHeight");
		String sizeWeigh = request.getParameter("sizeWeigh");
		String quality = request.getParameter("quality");
		String waste = request.getParameter("waste");

		String changeSizeWeight = request.getParameter("isChangeSizeWeight");
		Boolean isChangeSizeWeight = "true".equalsIgnoreCase(changeSizeWeight);
		String currenPriceStr = request.getParameter("currenPriceStr");

		QuotationItemMaterial qMaterial = new QuotationItemMaterial(material, backUpMarterial,
				FormatNumber.str2num(sizeLong), FormatNumber.str2num(sizeWidth), FormatNumber.str2num(sizeHeight),
				FormatNumber.str2num(sizeWeigh), FormatNumber.str2num(quality), exchangRate, isChangeSizeWeight,
				currenPriceStr, FormatNumber.str2num(waste));
		returnJson(response, qMaterial);
	}

	public void caculatorExeStep(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			QuotationItemForm form) throws Exception {
		String exeStepId = request.getParameter("exeStepId");
		String exeTime = request.getParameter("exeTime");
		ExeStep exe = exeStepDao.getObject(ExeStep.class, exeStepId);

		if (Formater.isNull(exeTime)) {
			// Chi lay don gia, khong tinh
			QuotationItemExe itemExe = new QuotationItemExe(exe, null);
			if (exe.getInitPrice() == null) {
				// Lay gia default tra lai cho client
				BssParam opr = bssParamDao.getSysParamByCode("OPR");
				if (opr != null) {
					try {
						BigDecimal initPrice = new BigDecimal(opr.getValue());
						exe.setInitPrice(initPrice);
						returnJson(response, itemExe);
						exeStepDao.evict(exe);
						return;
					} catch (Exception ex) {
						log.error(ex);
					}
				}
			}
			returnJson(response, itemExe);
			return;
		}
		QuotationItemExe itemExe = new QuotationItemExe(exe, FormatNumber.str2num(exeTime));
		returnJson(response, itemExe);
		exeStepDao.evict(exe);
	}

	public void toApprove(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			QuotationItemForm form) throws Exception {
		String quotationId = request.getParameter("quotationId");
		Quotation quotation = quotationDao.getObject(Quotation.class, quotationId);
		if (quotation.getQuotationItemList().size() <= 0)
			throw new ResourceException("Ch&#432;a t&#7891;n t&#7841;i b&#7843;n ghi chi ti&#7871;t!");
		Short iCurrentStatus = quotation.getStatus();
		if (iCurrentStatus != null && !ApproveConstants.APP_STS_NEW.equals(iCurrentStatus)
				&& !ApproveConstants.APP_STS_UN_APP.equals(iCurrentStatus))
			throw new ResourceException("Tr&#7841;ng th&#225;i kh&#244;ng h&#7907;p l&#7879;!");

		String note = request.getParameter("note");
		String quotationDateStr = request.getParameter("quotationDate");
		String quotation_currency = request.getParameter("quotation_currency");
		String ratMachineStr = request.getParameter("ratMachine");

		quotation.setQuotationDateStr(quotationDateStr);
		if (!Formater.isNull(quotation_currency))
			quotation.setCurrency(new SysDictParam(quotation_currency));
		else
			quotation.setCurrency(null);
		quotation.setNote(note);
		BigDecimal ratMachine = FormatNumber.str2num(ratMachineStr);
		quotation.setApproveDate(Calendar.getInstance().getTime());
		quotation.setApprover(getSessionUser().getUsername());
		quotation.setStatus(Short.valueOf("1"));
		// Truong hop thay doi rate
		if (ratMachine.compareTo(quotation.getRatMachine()) != 0) {
			// Cap nhat don gia cho mot so OP
			for (QuotationItem itm : quotation.getQuotationItemList()) {
				for (QuotationItemExe exe : itm.getQuotationItemExeList()) {
					if (exe.getExeStepId().getInitPrice() == null) {
						BigDecimal initPrice = itm.getOpInitPrice().subtract(itm.getReduce());
						initPrice = initPrice.multiply(quotation.getRatMachine()).divide(new BigDecimal(60), 3,
								RoundingMode.HALF_UP);
						exe.setInitPrice(initPrice);
					}
				}
			}
			quotation.setRatMachine(ratMachine);

		}
		quotationDao.save(quotation);

	}

	public void cancelToApprove(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			QuotationItemForm form) throws Exception {
		String quotationId = request.getParameter("quotationId");
		Quotation quotation = quotationDao.getObject(Quotation.class, quotationId);
		Short iCurrentStatus = quotation.getStatus();
		if (!ApproveConstants.APP_STS_TO_APP.equals(iCurrentStatus))
			throw new ResourceException("Tr&#7841;ng th&#225;i kh&#244;ng h&#7907;p l&#7879;!");
		quotation.setApproveDate(Calendar.getInstance().getTime());
		quotation.setApprover(getSessionUser().getUsername());
		quotation.setStatus(Short.valueOf("0"));
		quotationDao.save(quotation);

	}

	public void approve(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			QuotationItemForm form) throws Exception {
		String quotationId = request.getParameter("quotationId");
		String status = request.getParameter("status");
		String note = request.getParameter("note");
		if (!Formater.isNull(quotationId)) {
			Quotation quotation = quotationDao.getObject(Quotation.class, quotationId);
			Short iCurrentStatus = quotation.getStatus();
			if (!ApproveConstants.APP_STS_TO_APP.equals(iCurrentStatus))
				throw new ResourceException("Tr&#7841;ng th&#225;i kh&#244;ng h&#7907;p l&#7879;!");
			quotation.setApproveDate(Calendar.getInstance().getTime());
			quotation.setApprover(getSessionUser().getUsername());
			quotation.setStatus(Short.valueOf(status));
			quotation.setNote(note);
			quotationDao.save(quotation);
		}
	}

	public void cancelApprove(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			QuotationItemForm form) throws Exception {
		String quotationId = request.getParameter("quotationId");
		String note = request.getParameter("note");
		if (!Formater.isNull(quotationId)) {
			Quotation quotation = quotationDao.getObject(Quotation.class, quotationId);
			Short iCurrentStatus = quotation.getStatus();
			if (!ApproveConstants.APP_STS_APP.equals(iCurrentStatus))
				throw new ResourceException("Tr&#7841;ng th&#225;i kh&#244;ng h&#7907;p l&#7879;!");
			quotation.setStatus(ApproveConstants.APP_STS_NEW);
			quotation.setNote(note);
			quotation.setDone(null);
			quotationDao.save(quotation);
		}
	}

	public void updateQuatation(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			QuotationItemForm form) throws Exception {
		if (Formater.isNull(form.getQuotation().getCustomer().getId()))
			throw new ResourceException("Chưa chọn khách hàng!");
		Customer cus = customerManageDao.getObject(form.getQuotation().getCustomer());
		if (!Formater.isNull(form.getQuotation().getId())) {
			Quotation qinDb = quotationDao.getObject(form.getQuotation());
			ReflectionUtils.ReconcileObj<Quotation> reconcileObj = new ReflectionUtils.ReconcileObj<Quotation>() {
				@Override
				public void reconcile(Quotation desObj, Quotation sourceObj, List<String> lstIgnoreFields)
						throws Exception {
					ReflectionUtils.ReconcileObj.super.reconcile(desObj, sourceObj, lstIgnoreFields);
					// 3 gia tri lay tu giao dien
					desObj.setCustomer(cus);
					// Giai doan nay coi bao gia la don hang => can hard code done
					desObj.setDone((short) 1);
					desObj.setCurrency(sourceObj.getCurrency());
					desObj.setCompany(sourceObj.getCompany());
				}
			};
			reconcileObj.reconcile(qinDb, form.getQuotation(),
					new ArrayList<String>(Arrays.asList("createDate", "quotationEndDate", "creator", "status")));

			// Truong hop thay doi rate
			Boolean reload = Boolean.FALSE;
			if (form.getQuotation().getRatMachine().compareTo(qinDb.getRatMachine()) != 0) {
				// Cap nhat don gia cho mot so OP
				for (QuotationItem itm : qinDb.getQuotationItemList()) {
					for (QuotationItemExe exe : itm.getQuotationItemExeList()) {
						if (exe.getExeStepId().getInitPrice() == null) {
							BigDecimal initPrice = itm.getOpInitPrice().subtract(itm.getReduce());
							initPrice = initPrice.multiply(qinDb.getRatMachine()).divide(new BigDecimal(60), 3,
									RoundingMode.HALF_UP);
							exe.setInitPrice(initPrice);
							if (!reload)
								reload = Boolean.TRUE;
						}
					}
				}
			}
			qinDb.setRatMachine(form.getQuotation().getRatMachine());
			if (reload) {
				returnJson(response, new Quotation(qinDb.getId()));
			}
		}
	}

	@ClearOnFinish
	public void preview(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			QuotationItemForm form) throws IOException, ResourceException {
		String quotationId = request.getParameter("squotationId");
		Quotation quotation = quotationDao.get(quotationId);
		if (quotation.getQuotationItemList().size() > 20)
			throw new ResourceException("Không preview được với đơn hàng > 20 bản vẽ!");
		returnJson(response, quotation);
	}

	public void refreshQuotation(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			QuotationItemForm form) throws Exception {
		String quotationId = request.getParameter("quotationId");
		Quotation quotation = quotationDao.getObject(Quotation.class, quotationId);
		returnJson(response, quotation);
	}

	private BigDecimal getOpPrice(String drawingType, String marterialId) {
		SysDictParam dictParam = sysDictParamDao.getObject(SysDictParam.class, drawingType);
		Material material = materialDao.getObject(Material.class, marterialId);
		return getOpPrice(dictParam, material);

	}

	private BigDecimal getOpPrice(SysDictParam drawingType, Material material) {
		BigDecimal initPrice = null;
		if (material.getMaterialType() != null) {
			if (Constants.DRAWVING_TYPE_D.equalsIgnoreCase(drawingType.getCode())) {
				initPrice = material.getMaterialType().getInitPriceD();
			} else if (Constants.DRAWVING_TYPE_TB.equalsIgnoreCase(drawingType.getCode())) {
				initPrice = material.getMaterialType().getInitPriceTB();
			} else if (Constants.DRAWVING_TYPE_K.equalsIgnoreCase(drawingType.getCode())) {
				initPrice = material.getMaterialType().getInitPriceK();
			}
		}
		return initPrice;
	}

	public void initOpPrice(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			QuotationItemForm form) throws Exception {
		String drawingType = request.getParameter("drawingType");
		String marterialId = request.getParameter("marterialId");
		String backUpMarterialId = request.getParameter("backUpMarterialId");

		if (!Formater.isNull(backUpMarterialId))
			marterialId = backUpMarterialId;
		BigDecimal initPrice = getOpPrice(drawingType, marterialId);

		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		pw.print(initPrice == null || initPrice == null ? "0" : FormatNumber.num2Str(initPrice));
		pw.close();
	}

	@Autowired
	private JasperUtils jasperUtils;

	public void exp(ModelMap model, HttpServletRequest request, HttpServletResponse response, QuotationItemForm form)
			throws Exception {
		String type = request.getParameter("type");
		String quotationId = request.getParameter("quotationId");

		if (Formater.isNull(quotationId)) {
			throw new ResourceException(
					"kh\\u00F4ng t\\u1ED3n t\\u1EA1i b\\u00E1o c\\u00E1o li\\u00EAn h\\u1EC7 qu\\u1EA3n tr\\u1ECB vi\\u00EAn ki\\u1EC3m tra l\\u1EA1i");
		}
		Quotation q = quotationDao.getObject(Quotation.class, quotationId);
		if (q == null || Formater.isNull(q.getId()))
			throw new ResourceException(
					"kh\\u00F4ng t\\u1ED3n t\\u1EA1i b\\u00E1o c\\u00E1o li\\u00EAn h\\u1EC7 qu\\u1EA3n tr\\u1ECB vi\\u00EAn ki\\u1EC3m tra l\\u1EA1i");

		Connection connection = quotationDao.getConnection();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("p_quotation_id", quotationId);
			if (q.getCompany() != null) {
				map.put("p_company_code", q.getCompany().getCode());
				map.put("p_company_name", q.getCompany().getName());
			}
			if (q.getCurrency() != null) {
				map.put("p_currency_code", q.getCurrency().getValue());
			}
			map.put("p_quotation_date", Formater.date2str(q.getCreateDate()));
			List<String> subs = new ArrayList<String>();
			subs.add("Report_09BV_subrp");
			subs.add("Report_09BV_subrp1");
			subs.add("Report_09BV_subrp2");
			InputStream result = jasperUtils.createReport("Report_09BV", map, type, "Report_09BV", connection, subs);
			response.setContentType("application/octet-stream");
			response.addHeader("Content-Disposition", "attachment; filename=Report_09BV." + type);
			OutputStream responseOutputStream = response.getOutputStream();
			int bytes;
			while ((bytes = result.read()) != -1) {
				responseOutputStream.write(bytes);
			}
			result.close();
			responseOutputStream.flush();
			responseOutputStream.close();
		} catch (Exception e) {
			log.error("export", e);
			throw e;
		} finally {
			DataSourceConfiguration.releaseSqlResources(null, null, connection);
		}

	}

	@Autowired
	private ExportExcel exportExcel;

	public void expExelJrxl(ModelMap model, HttpServletRequest request, HttpServletResponse rs, QuotationItemForm form)
			throws Exception {
		String quotationId = request.getParameter("quotationId");
		List<QuotationItem> items = quotationItemDao.getQuotationItems(quotationId);
		Quotation quotation = quotationDao.getObject(Quotation.class, quotationId);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("quotationItems", items);
		if (quotation != null)
			map.put("totalPrice", quotation.getPriceStr());
		exportExcel.export("Danh_sach_bao_gia", rs, map);

	}

	public void download(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			QuotationItemForm form) throws Exception {
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			InputStream fileInputStream = classLoader.getResourceAsStream("/templates/report/Template_nhap_bg.xlsx");
			response.setContentType("application/vnd.ms-excel");
			response.addHeader("Content-Disposition", "attachment; filename=Template_nhap_bg.xlsx");
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

	@RequestMapping(value = "upload", method = RequestMethod.POST)
	public void upload(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			@RequestParam("inputFile") MultipartFile inputFile, @RequestParam("fileName") String fileName)
			throws Exception {
		quotation.upload(model, rq, rs, inputFile, fileName);
	}

	@Autowired
	private QuotationController quotation;
	public static final DataFormatter dataFormatter = new DataFormatter();

	@Override
	@ClearOnFinish
	public void edit(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QuotationItemForm form)
			throws Exception {
		QuotationItem drawing = quotationItemDao.getObject(QuotationItem.class, rq.getParameter("id"));
		for (QuotationItemExe exe : drawing.getQuotationItemAllExeList()) {
			if (Boolean.TRUE.equals(exe.getExeStepId().getProgram()))
				drawing.getQuotationItemProList().add(exe);
			else {
				drawing.getQuotationItemExeList().add(exe);
			}
		}
		Collections.sort(drawing.getQuotationItemProList(), new QuotationItemExe.SortQie());
		Collections.sort(drawing.getQuotationItemExeList(), new QuotationItemExe.SortQie());
		// Tranh serialize nhieu
		drawing.setQuotationId(new Quotation(drawing.getQuotationId().getId()));
		returnJson(rs, drawing);
		quotationDao.clear();

	}

	public void delQuotation(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QuotationItemForm form)
			throws Exception {
		String quotationId = rq.getParameter("quotationId");
		Quotation q = quotationDao.getObject(Quotation.class, quotationId);
		if (q.getStatus() != null && q.getStatus() != 0 && q.getStatus() != 3)
			throw new ResourceException(
					"Kh&#244;ng &#273;&#432;&#7907;c x&#243;a b&#225;o gi&#225; sau khi chuy&#7875;n ph&#234; duy&#7879;t!");
		for (QuotationItem qi : q.getQuotationItemList()) {
			for (QuotationItemExe qie : qi.getQuotationItemAllExeList()) {
				if (!qie.getWorkOrders().isEmpty())
					throw new ResourceException(
							"Đơn hàng đã tạo lệnh sản xuất cho chi tiết mã %s, tên: %s, không thể xóa!",
							new Object[] { qie.getQuotationItemId().getCode(), qie.getQuotationItemId().getName() });
				if (!qie.getWorkPros().isEmpty())
					throw new ResourceException(
							"Đơn hàng đã phân công cán bộ lập trình cho chi tiết mã %s, tên: %s, không thể xóa!",
							new Object[] { qie.getQuotationItemId().getCode(), qie.getQuotationItemId().getName() });
			}
		}

		quotationDao.del(q);
	}

	public void updateExchangePrice(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			QuotationItemForm form) throws Exception {
		String quotationId = rq.getParameter("quotationId");
		String quotationDateStr = rq.getParameter("quotationDate");
		Date quotationDate = Formater.str2date(quotationDateStr);
		String currency = rq.getParameter("currency");
		Quotation q = quotationDao.getObject(Quotation.class, quotationId);
		ExchRate usd = exchRateDao.getByCurrencyAndDate("USD", quotationDate);
		ExchRate quotationCurrency = exchRateDao
				.getByCurrencyAndDate(sysDictParamDao.getObject(new SysDictParam(currency)), quotationDate);
		BigDecimal exchangePrice = usd.getExchRate() != null && quotationCurrency != null
				&& quotationCurrency.getExchRate() != null && q.getPrice() != null
						? q.getPrice().multiply(usd.getExchRate()).divide(quotationCurrency.getExchRate(), 3,
								RoundingMode.HALF_UP)
						: null;
		returnTxtHtml(rs, FormatNumber.num2Str(exchangePrice));
	}

	public void checkCode(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QuotationItemForm form)
			throws IOException {
		String code = rq.getParameter("code");
		String id = rq.getParameter("id");
		Boolean bExist = quotationItemDao.exists(id, code.trim());
		returnTxtHtml(rs, bExist.toString());

	}

	@Autowired
	private MaterialTypeDao materialTypeDao;
	@Autowired
	private ReduceByAmountDao reduceByAmountDao;

	public void reloadReduce(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QuotationItemForm form)
			throws ParseException, IOException {
		String marterial = rq.getParameter("marterial");
		MaterialType mType = materialTypeDao.get(MaterialType.class, marterial);
		log.info(mType);
		String squality = rq.getParameter("quality");
		Long quality = FormatNumber.str2Long(squality);
		ReduceByAmount reduceByAmount = reduceByAmountDao.getByQuality(quality);
		rs.setContentType("text/html");
		PrintWriter pw = rs.getWriter();
		pw.print(reduceByAmount == null || reduceByAmount.getAmountReduce() == null ? "0"
				: FormatNumber.num2Str(reduceByAmount.getAmountReduce()));
		pw.close();

	}

	public void getOpPrice(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QuotationItemForm form)
			throws IOException {
		String exeStepId = rq.getParameter("exeStepId");
		ExeStep exeStep = exeStepDao.getObject(ExeStep.class, exeStepId);
		returnJson(rs, exeStep);
	}

	public void del(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QuotationItemForm form)
			throws Exception {
		QuotationItem qi = quotationItemDao.get(QuotationItem.class, form.getQuotationItem().getId());
		for (QuotationItemExe qie : qi.getQuotationItemAllExeList()) {
			for (WorkOrder wo : qie.getWorkOrders()) {
				if (!wo.getWorkOrderExes().isEmpty())
					throw new ResourceException("Lệnh sản xuất %s đã tiến hành sản xuất, không thể xóa!", wo.getCode());
			}
			for (WorkPro wp : qie.getWorkPros()) {
				if (wp.getDuration() != null)
					throw new ResourceException("Lệnh lập trình %s đã được thực hiện, không thể xóa!", wp.getCode());
			}
		}
		List<String> ids = new ArrayList<String>();
		// Xoa file ban ve
		List<SysFile> files = sysFileDao.getFileByRecordId(qi.getId());
		for (SysFile file : files) {
			ids.add(file.getId());
			sysFileDao.del(file);
		}
		// Xoa file lap trinh
		for (QuotationItemExe qie : qi.getQuotationItemAllExeList()) {
			files = sysFileDao.getFileByRecordId(qie.getId());
			for (SysFile file : files) {
				ids.add(file.getId());
				sysFileDao.del(file);
			}
		}
		if (!ids.isEmpty())
			ftpUtils.removeFile(ftpInf, ids);
		quotationItemDao.del(qi);
	}

	@Autowired
	private FTPUtils ftpUtils;
	@Autowired
	private FtpInf ftpInf;

}
