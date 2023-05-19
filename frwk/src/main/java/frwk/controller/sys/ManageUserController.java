package frwk.controller.sys;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import org.hibernate.exception.ConstraintViolationException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import common.util.Formater;
import common.util.ResourceException;
import entity.BssFactoryUnit;
import entity.Company;
import entity.Department;
import entity.frwk.SysDictParam;
import entity.frwk.SysParam;
import entity.frwk.SysRoles;
import entity.frwk.SysRolesUsers;
import entity.frwk.SysRolesUsersId;
import entity.frwk.SysUserMail;
import entity.frwk.SysUsers;
import frwk.constants.RightConstants;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.CompanyDao;
import frwk.dao.hibernate.sys.DepartmentDao;
import frwk.dao.hibernate.sys.RightUtils;
import frwk.dao.hibernate.sys.RoleDao;
import frwk.dao.hibernate.sys.SysDictParamDao;
import frwk.dao.hibernate.sys.SysParamDao;
import frwk.dao.hibernate.sys.SysUsersDao;
import frwk.dao.hibernate.sys.UserDao;
import frwk.form.ManageUserForm;
import frwk.utils.ApplicationContext;
import frwk.utils.Utils;

@Controller
@RequestMapping("/manageUser")
public class ManageUserController extends CatalogController<ManageUserForm, SysUsers> {

	private static Logger lg = Logger.getLogger(ManageUserController.class);
	/**
	 * Password mac dinh
	 */
	private static final String DEFAULT_PASSWORD_PLAN_TEXT = "cncTechMes123";

	@Autowired
	private CompanyDao companyDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private SysUsersDao sysUsersDao;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private SysParamDao sysParamDao;

	@Autowired
	private DepartmentDao departmentDao;
	private Boolean isRSA = Boolean.FALSE;
	private ArrayList<SysRoles> dsNhomQuyenCha;
	private String comIdFilter;

	protected String getReportTitle() {
		return null;
	}

	protected String getTemplateFileName() {
		return null;
	}

	public void changePIN(ModelMap map, HttpServletRequest rq, HttpServletResponse rp, ManageUserForm form)
			throws IOException {
		rp.setContentType("text/plain;charset=utf-8");
		PrintWriter pw = rp.getWriter();
		ApplicationContext appContext = (ApplicationContext) rq.getSession()
				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
		if (!rightUtils.haveRight(RightConstants.MNRSA, appContext)) {
			pw.print(getText("not_access"));
			pw.flush();
			pw.close();
			return;
		}
		// String oldPIN = "";
		String result = "";

		pw.print(result);
		pw.flush();
		pw.close();
	}

	public String unsubscriber(ModelMap map, HttpServletRequest rq, HttpServletResponse rp, ManageUserForm form)
			throws IOException {
		rp.setContentType("text/plain;charset=utf-8");
		PrintWriter pw = rp.getWriter();
		ApplicationContext appContext = (ApplicationContext) rq.getSession()
				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
		if (!rightUtils.haveRight(RightConstants.MNRSA, appContext)) {
			pw.print(getText("not_access"));
			pw.flush();
			pw.close();
			return null;
		}

		pw.print("SUCCESS");

		pw.flush();
		pw.close();
		return null;
	}

	private static final BCryptPasswordEncoder encode = new BCryptPasswordEncoder();

	public void resetPass(ModelMap map, HttpServletRequest rq, HttpServletResponse rp, ManageUserForm form)
			throws Exception {
//		if (!rightUtils.haveRight(RightConstants.MNUSER_RESET_PASS,
//				(ApplicationContext) rq.getSession().getAttribute(ApplicationContext.APPLICATIONCONTEXT)))
//			throw new ResourceException("not_access");		
		SysUsers su = form.getModel();
		// String newPass = new RandomPassWord(8, 20).nextString();
		sysUsersDao.resetPassWord(su, encode.encode(DEFAULT_PASSWORD_PLAN_TEXT));
		su.setPassWordPlainText(DEFAULT_PASSWORD_PLAN_TEXT);
		returnJson(rp, su);

	}

	@Override
	public void edit(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ManageUserForm form)
			throws Exception {
		String id = rq.getParameter("id");
		SysUsers user = sysUsersDao.getUser(id);
		user.setpWExpriredDateStr(expiredPassWordDate(user.getPwdDate()));
//		if (!Formater.isNull(user.getCompanyId()))
//			user.setLstDepartment(companyDao.getChild(user.getCompanyId()));
		if (user.getCompany() != null) {
			List<Department> departments = departmentDao.getAllByCompanyOrderAscName(user.getCompany().getId());
			StringBuffer lstResult = new StringBuffer();
			String dep = null;
			for (Department department : departments) {
				dep += "<option value='" + department.getId() + "' >" + department.getCode() + "-"
						+ department.getName() + "</option>";
			}
			lstResult.append("<option value=''></option>" + dep);
			user.setStrDepartment(lstResult.toString());

			List<BssFactoryUnit> bssFactoryUnits = userDao.getFactoryByCompany(user.getCompany().getId());
			StringBuffer lstBssFactoryUnit = new StringBuffer();
			String bss = null;
			for (BssFactoryUnit item : bssFactoryUnits) {
				bss += "<option value='" + item.getId() + "' >" + item.getCode() + "</option>";
			}
			lstBssFactoryUnit.append("<option value=''></option>" + bss);
			user.setStrBssFactoryUnits(lstBssFactoryUnit.toString());
		}
		returnJson(rs, user);
	}

	@Autowired
	MessageSource messageSource;

	private String expiredPassWordDate(Date pwDate) {
		List<SysParam> expriredDays = sysParamDao.getByType("PW_EXPIRED_IN");
		Date date = new Date();
		if (!Formater.isNull(expriredDays)) {
			Calendar c = Calendar.getInstance();
			c.setTime(pwDate);
			c.add(Calendar.DATE, Integer.parseInt(expriredDays.get(0).getValue()));
			long dateTime = 60 * 24 * 60 * 1000;
			String dateStr = "";
			long diff = 0;
			if (c.getTime().after(date)) {
				diff = c.getTimeInMillis() - date.getTime();
				dateStr = String.format(
						messageSource.getMessage("PW_WR", null, "Default", LocaleContextHolder.getLocale()),
						diff / dateTime);
				return dateStr;
			} else {
				return messageSource.getMessage("PW_EX", null, "Default", LocaleContextHolder.getLocale());
			}
		}
		return null;
	}

	@Autowired
	private RightUtils rightUtils;

	public void del(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ManageUserForm form)
			throws Exception {

		if (!rightUtils.haveRight(RightConstants.MNUSER_Delete,
				(ApplicationContext) rq.getSession().getAttribute(ApplicationContext.APPLICATIONCONTEXT)))
			throw new ResourceException("not_access");
		userDao.del(form.getSu());
	}

	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ManageUserForm form)
			throws Exception {
		SysUsers su = ((ManageUserForm) form).getModel();
		if (Formater.isNull(su.getPartner().getId())
				&& (Formater.isNull(su.getCompany().getId()) || Formater.isNull(su.getDepartment().getId())))
			throw new ResourceException("NSD cần chọn đối tác hoặc thuộc phòng ban, công ty tại CNCTech");
		if (Formater.isNull(su.getCompany().getId()))
			su.setCompany(null);
//		if (editor.getCompany() != null && !"CIC".equals(editor.getCompany().getCode())) {
//			if (editor.getCompany() != null && editor.getCompany().getId() != null
//					&& !editor.getCompany().getId().equals(su.getCompanyId()))
//				throw new ResourceException("KhÃƒÆ’Ã‚Â´ng sÃƒÂ¡Ã‚Â»Ã‚Â­a dÃƒÂ¡Ã‚Â»Ã‚Â¯ liÃƒÂ¡Ã‚Â»Ã¢â‚¬Â¡u Ãƒâ€žÃ¢â‚¬ËœÃƒÂ¡Ã‚Â»Ã¢â‚¬Ëœi tÃƒÆ’Ã‚Â¡c khÃƒÆ’Ã‚Â¡c!");
//
//		}

		if (Formater.isNull(su.getId())) { // Khong duoc sua password, ngay hieu luc password

			// Kiem tra quyen tao moi
//			if (!rightUtils.haveRight(RightConstants.MNUSER_Create,
//					(ApplicationContext) rq.getSession().getAttribute(ApplicationContext.APPLICATIONCONTEXT)))
//				throw new ResourceException("not_access");

			// TODO: sua
			// passWordPlainText = new RandomPassWord(8, 12).nextString();
			su.setPassWordPlainText(DEFAULT_PASSWORD_PLAN_TEXT);
//			String passWordInDb = userDao.getHash(su.getUsername(), /* + Constants.APPLICATION_KEY */su.getPassWordPlainText());
			// new encode
			String passWordInDb = encode.encode(su.getPassWordPlainText());
			su.setPassword(passWordInDb);
			su.setPwdDate(Calendar.getInstance().getTime());
			su.setActive(Boolean.TRUE);
		} else {
			// Kiem tra quyen sua
//			if (!rightUtils.haveRight(RightConstants.MNUSER_Edit,
//					(ApplicationContext) rq.getSession().getAttribute(ApplicationContext.APPLICATIONCONTEXT)))
//				throw new ResourceException("not_access");
		}
		if (su.getIsPartner() != null && !su.getIsPartner())
			su.setPartner(null);
		Iterator<SysUserMail> itor = su.getLstMail().iterator();
		while (itor.hasNext()) {
			SysUserMail sum = itor.next();
			// Bo chon
			if (Formater.isNull(sum.getSysDictParam().getId()))
				itor.remove();
			else
				sum.setSysUsers(su);
		}
		// Gui mail thong bao
		try {
			// TODO: phai sua
			if (su.getDepartment() != null && Formater.isNull(su.getDepartment().getId()))
				su.setDepartment(null);
			userDao.save(su);
		} catch (ConstraintViolationException constraintViolationException) {
			lg.error(constraintViolationException);
			throw new ResourceException(messageSource.getMessage("ManageUser.error.username", null, "Default",
					LocaleContextHolder.getLocale()));
		}
		Utils.jsonSerialize(rs, su);
	}

	public void setDsNhomQuyenCha(ArrayList<SysRoles> dsNhomQuyenCha) {
		this.dsNhomQuyenCha = dsNhomQuyenCha;
	}

	public ArrayList<SysRoles> getDsNhomQuyenCha() {
		return dsNhomQuyenCha;
	}

	public void setIsRSA(Boolean isRSA) {
		this.isRSA = isRSA;
	}

	public Boolean getIsRSA() {
		return isRSA;
	}

	public String loadDepartmentByCompany(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			ManageUserForm form) throws IOException {
		String companyId = rq.getParameter("companyId");
		Company company = companyDao.get(companyId);

		// Chet phan dong goi nen tam thoi bo quan he trong entity 3 2 collection nay
		List<Department> departments = departmentDao.getAllByCompanyOrderAscName(companyId);
		company.setLstDepartment(departments);

		List<BssFactoryUnit> bssFactoryUnits = userDao.getFactoryByCompany(companyId);

		company.setLstBssFactoryInit(bssFactoryUnits);
		// returnJson(rs, companyDao.get(companyId));

		rs.setContentType("application/json;charset=utf-8");
		rs.setCharacterEncoding("utf-8");
		rs.setHeader("Cache-Control", "no-store");
		OutputStream os = rs.getOutputStream();
		new ObjectMapper().writeValue(os, company);
		// objectMapper2().writeValue(os, obj);
		os.flush();
		os.close();
		return null;
	}

	public void setComIdFilter(String comIdFilter) {
		this.comIdFilter = comIdFilter;
	}

	public String getComIdFilter() {
		return comIdFilter;
	}

	@Override
	public BaseDao<SysUsers> getDao() {
		return userDao;
	}

	@Override
	public void createSearchDAO(HttpServletRequest request, ManageUserForm form, BaseDao<SysUsers> dao)
			throws Exception {
		// Name
		if (!Formater.isNull(form.getStrname()))
			dao.addRestriction(Restrictions.like("name", form.getStrname().trim(), MatchMode.ANYWHERE).ignoreCase());

		if (!Formater.isNull(form.getJob_name()))
			dao.addRestriction(Restrictions.eq("companyId", form.getJob_name()));
		// username
		if (!Formater.isNull(form.getStrusername()))
			dao.addRestriction(
					Restrictions.like("username", form.getStrusername().trim(), MatchMode.ANYWHERE).ignoreCase());

		if (!Formater.isNull(form.getCompanyId()))
			dao.addRestriction(Restrictions.eq("company", new Company(form.getCompanyId())));
		if (!Formater.isNull(form.getDepartmentId()))
			dao.addRestriction(Restrictions.eq("department", new Department(form.getDepartmentId())));
		dao.addOrder(Order.asc("username"));
	}

	public void reloadDepartment(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ManageUserForm form)
			throws Exception {
		String conpanyId = rq.getParameter("conpanyId");
		List<Department> lstDepartment = departmentDao.getAllByCompanyOrderAscName(conpanyId);
		returnJsonArray(rs, lstDepartment);
	}

	@Override
	public void pushToJa(JSONArray ja, SysUsers temp, ManageUserForm modal) throws Exception {

		if (!Formater.isNull(temp.getUsername()))
			if (!Formater.isNull(temp.getUsername()))
				ja.put("<a class='characterwrap' href = '#' onclick = 'editUser(\"" + temp.getId() + "\")'>"
						+ temp.getUsername() + "</a>");
			else
				ja.put("");
		else
			ja.put("");

		ja.put(temp.getName());
		ja.put(temp.getJobName());
		if (temp.getCompany() != null)
			ja.put(temp.getCompany().getName());
		else
			ja.put("");
		if (temp.getDepartment() != null)
			ja.put(temp.getDepartment().getName());
		else
			ja.put("");
		if (temp.getShift() != null) {
			SysDictParam shift = sysDictParamDao.get(temp.getShift());
			ja.put(shift.getValue());
		} else
			ja.put("");
		if (temp.getFactoryUnit() != null) {
			BssFactoryUnit factoryUnit = userDao.get(BssFactoryUnit.class, temp.getFactoryUnit());
			if (factoryUnit != null)
				ja.put(factoryUnit.getCode() + "-" + factoryUnit.getName());
			else
				ja.put("");
		} else
			ja.put("");
		if (!Formater.isNull(temp.getEmail()))
			ja.put("<span class='characterwrap'>" + temp.getEmail() + "</span>");
		else
			ja.put("");

		if (temp.getCellPhone() != null)
			ja.put(temp.getCellPhone());
		else
			ja.put("");

	}

	@Override
	public String getJsp() {
		return "qtht/quan_ly_nguoi_dung";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ManageUserForm form)
			throws Exception {
//		ApplicationContext appContext = (ApplicationContext) rq.getSession()
//				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
//		if (appContext != null) {
//			SysUsers user = (SysUsers) appContext.getAttribute(ApplicationContext.USER);
//			List<Department> departments = departmentDao.getDepartmentByCompanyId(user.getCompany().getId());
//			if (!departments.isEmpty()) {
//				model.addAttribute("departments",departments);
//			}
//		}
//		List<Partner> dsDonVi = sysPartnerDao.getAll();
//		List<SysDictParam> dsGender = sysParamDao.getByType(RightConstants.CAT_TYPE_GENDER_1);
		form.setCompanyId(getSessionUser().getCompany().getId());
//		model.addAttribute("dsDonVi", dsDonVi);
		model.addAttribute("dsGender", sysDictParamDao.getByType(RightConstants.CAT_TYPE_GENDER_1));

		List<Company> lstCompany = null;
		if (rightUtils.haveRight(rq, RightConstants.VIEW_DATA_ALL)) {
			lstCompany = companyDao.getAllOrderAstName();
		} else {
			lstCompany = Arrays.asList(getSessionUser().getCompany());
		}
		model.addAttribute("companies", lstCompany);
		model.addAttribute("customers", userDao.getAllCus());
		model.addAttribute("lstShift", sysDictParamDao.getByType("SHIFTS"));
		model.addAttribute("lstMailWrngType", sysDictParamDao.getByType("WRNMAIL"));

	}

	@Autowired
	private SysDictParamDao sysDictParamDao;

	public void treeRole(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ManageUserForm form)
			throws Exception {
		rs.setContentType("text/plain;charset=utf-8");
		PrintWriter pw = rs.getWriter();
		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();
		SysUsers su = null;
		if (!Formater.isNull(rq.getParameter("suID"))) {
			su = sysUsersDao.getWithRole(rq.getParameter("suID"));
		}
		Boolean bNotPublicRole = Boolean.TRUE;
		array = roleDao.getTreeRolesData(bNotPublicRole, rq.getParameter("parentRoleId"), su);
		result.put("treeRoles", array);
		pw.print(result);
		pw.flush();
		pw.close();
	}

	public static final DataFormatter dataFormatter = new DataFormatter();

	@RequestMapping(value = "upload", method = RequestMethod.POST)
	public void upload(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			@RequestParam("inputFile") MultipartFile inputFile, @RequestParam("fileName") String fileName)
			throws Exception {
		Workbook wb = fileName.endsWith("xlsx") ? new XSSFWorkbook(inputFile.getInputStream())
				: new HSSFWorkbook(inputFile.getInputStream());
		Sheet sheet = wb.getSheetAt(0);
		List<SysDictParam> dsGender = sysDictParamDao.getByType(RightConstants.CAT_TYPE_GENDER_1);
		Map<String, SysDictParam> mapGender = new HashMap<String, SysDictParam>();
		for (SysDictParam item : dsGender) {
			mapGender.put(item.getCode(), item);
		}
		// Du lieu bat dau tu dong 1
		int iRowIdex = 1;
		while (true) {
			Row row = sheet.getRow(iRowIdex);
			if (row == null)
				break;
			String data = dataFormatter.formatCellValue(row.getCell(0));
			// ExcelUtils.getString(row.getCell((short) 0)); // Kiem tra cot 1 rong thi stop
			if ("eof".equals(data) || Formater.isNull(data))
				return;
			try {
				SysUsers user = new SysUsers();
				user.setCreator(getSessionUser());
				user.setPassWordPlainText(DEFAULT_PASSWORD_PLAN_TEXT);
				String passWordInDb = encode.encode(user.getPassWordPlainText());
				user.setPassword(passWordInDb);
				user.setPwdDate(Calendar.getInstance().getTime());
				user.setActive(Boolean.TRUE);
				readUserImpInf(user, row, mapGender);
			} catch (ResourceException ex) {
				String error = String.format("Lỗi xử lý dữ liệu dòng %s, chi tiết lỗi: %s",
						new Object[] { iRowIdex, ex.getMessage() });
				returnTxtHtml(rs, error);
				throw ex;
			}
			iRowIdex++;
		}
		if (iRowIdex == 1)
			throw new ResourceException("File không có dữ liệu");
	}

	private void readUserImpInf(SysUsers user, Row row, Map<String, SysDictParam> mapGender) throws Exception {
		String name = dataFormatter.formatCellValue(row.getCell(1));
		if (Formater.isNull(name))
			throw new ResourceException("Chưa nhập thông tin tên người dùng!");
		user.setName(name);
		String username = dataFormatter.formatCellValue(row.getCell(2));
		if (Formater.isNull(username))
			throw new ResourceException("Chưa nhập thông tin tên đăng nhập!");
		if (userDao.getUser(username.trim().toLowerCase()) != null)
			throw new ResourceException(String.format("Username %s đã tồn tại!", username));
		user.setUsername(username);
		user.setJobName(dataFormatter.formatCellValue(row.getCell(3)));
		String sex = dataFormatter.formatCellValue(row.getCell(4));
		if (Formater.isNull(sex))
			throw new ResourceException("Chưa nhập thông tin giới tính!");
		if ("1".equals(sex)) {
			user.setGender(mapGender.get("1").getId());
		} else if ("2".equals(sex)) {
			user.setGender(mapGender.get("0").getId());
		} else {
			user.setGender(mapGender.get("2").getId());
		}
		user.setPhone(dataFormatter.formatCellValue(row.getCell(5)));
		user.setCellPhone(dataFormatter.formatCellValue(row.getCell(6)));
		user.setEmail(dataFormatter.formatCellValue(row.getCell(7)));
		user.setIdentification(dataFormatter.formatCellValue(row.getCell(8)));
		if (!Formater.isNull(dataFormatter.formatCellValue(row.getCell(9)))) {
			try {
				Date date = Formater.str2date(dataFormatter.formatCellValue(row.getCell(9)));
				user.setDateIssue(date);
			} catch (Exception e) {
				// TODO: handle exception
				throw new ResourceException("Sai định dạng ngày tháng dd/MM/yyyy");
			}
		}
		user.setPlaceIssue(dataFormatter.formatCellValue(row.getCell(10)));
		String companyCode = dataFormatter.formatCellValue(row.getCell(11));
		if (Formater.isNull(companyCode))
			throw new ResourceException("Chưa nhập thông tin công ty");
		Company company = companyDao.getCompanyByCode(companyCode);
		if (company == null)
			throw new ResourceException(
					String.format("Mã công ty %s không tồn tại công ty trong hệ thống", companyCode));
		user.setCompany(company);
		String departmentCode = dataFormatter.formatCellValue(row.getCell(12));
		if (Formater.isNull(departmentCode))
			throw new ResourceException("Chưa nhập thông tin phòng ban");
		Department department = departmentDao.getDepartmentByCompanyAndCode(company.getId(), departmentCode);
		if (department == null)
			throw new ResourceException(
					String.format("Mã phòng ban %s không tồn tại công ty trong hệ thống", departmentCode));
		user.setDepartment(department);

		// Danh sach quyen
		String roles = dataFormatter.formatCellValue(row.getCell(13));
		// Save de lay id, dung cho viec luu role
		userDao.save(user);
		if (!Formater.isNull(roles)) {
			String[] arrRole = roles.split(",");
			for (String role : arrRole) {
				if (Formater.isNull(role))
					continue;
				SysRoles oRole = roleDao.getByCode(role.trim());
				if (oRole == null)
					throw new ResourceException("Không tồn tại nhóm quyền " + role.trim());
				userDao.save(new SysRolesUsers(new SysRolesUsersId(user.getId(), oRole.getId()), user, oRole));
			}
		}
	}

	public void download(ModelMap model, HttpServletRequest rq, HttpServletResponse response, ManageUserForm form)
			throws Exception {
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			InputStream fileInputStream = classLoader.getResourceAsStream("/templates/report/Du lieu CNCSG.xlsx");
			response.setContentType("application/vnd.ms-excel");
			response.addHeader("Content-Disposition", "attachment; filename=Du lieu NSD.xlsx");
			OutputStream responseOutputStream = response.getOutputStream();

			int bytes;
			while ((bytes = fileInputStream.read()) != -1) {
				responseOutputStream.write(bytes);
			}
			fileInputStream.close();
			responseOutputStream.close();
		} catch (Exception ex) {
			lg.error(ex);
			ex.printStackTrace();
			throw ex;
		}
	}

}
