package cic.h2h.controller.report;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import cic.h2h.dao.hibernate.WorkOrderDao;
import common.util.Formater;
import common.util.JsonUtils;
import constants.RightConstants;
import entity.DashBroadNG;
import entity.frwk.SysParam;
import entity.frwk.SysUsers;
import frwk.controller.CommonController;
import frwk.dao.hibernate.sys.CompanyDao;
import frwk.dao.hibernate.sys.RightUtils;
import frwk.dao.hibernate.sys.SysParamDao;
import frwk.form.LoginForm;

@Controller
@RequestMapping("/index")
public class IndexController extends CommonController<LoginForm, SysUsers> {

	@Autowired
	private SysParamDao sysParamDao;

	@Autowired
	private WorkOrderDao workOrderDao;
	
	@Autowired
	private CompanyDao companyDao;

	@Override
	public String getJsp() {
		return "base/index";
	}	
	@Autowired
	private RightUtils rightUtils;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, LoginForm form)
			throws Exception {
		SysUsers user = getSessionUser();
		String day = expiredPassWordDate(user.getPwdDate());
		model.addAttribute("expriredDay", day);
		Calendar oneWeekAgo = Calendar.getInstance();
		oneWeekAgo.add(Calendar.DATE, -7);
		form.setFromDate(Formater.date2str(oneWeekAgo.getTime()));
		// Cong ty thanh vien
		if (rightUtils.haveRight(rq, RightConstants.VIEW_DATA_ALL)) {
			model.addAttribute("companies", companyDao.getAllOrderAstName());
		} else {
			model.addAttribute("companies", Arrays.asList(getSessionUser().getCompany()));
		}
		form.setCompanyId(user.getCompany().getId());
		// maps
		HashMap<String, List<?>> maps = workOrderDao.getDashBroad(Formater.str2date(form.getFromDate()), null, user.getCompany().getId());
		if (maps != null && maps.size() > 0)
			model.addAttribute("dashBroad", maps);
		model.addAttribute("jsonString", JsonUtils.writeToString(maps));
		// mapLine
		form.setReportType("w");
		HashMap<String, HashMap<String, List<?>>> mapLine = workOrderDao.getDashBroadLine(form.getReportType(), user.getCompany().getId());
		model.addAttribute("jsonMapLine", JsonUtils.writeToString(mapLine));		
	}

	public void loadFilter(ModelMap model, HttpServletRequest request, HttpServletResponse response, LoginForm form)
			throws Exception {
		String p_fromDate = request.getParameter("fromDate");
		String p_toDate = request.getParameter("toDate");
		String p_companyId = request.getParameter("companyId");
		HashMap<String, List<?>> maps = workOrderDao.getDashBroad(Formater.str2date(p_fromDate),
				Formater.str2date(p_toDate), p_companyId);
		returnJson(response, maps);
	}

	public void loadReportType(ModelMap model, HttpServletRequest request, HttpServletResponse response, LoginForm form)
			throws Exception {
		String reportType = request.getParameter("reportType");
		String p_companyId = request.getParameter("compnayId");
		response.setContentType("text/plain;charset=utf-8");
		PrintWriter pw = response.getWriter();
		HashMap<String, HashMap<String, List<?>>> maps = workOrderDao.getDashBroadLine(reportType, p_companyId);
		JSONObject array = null;
		array = new JSONObject(new ObjectMapper().writeValueAsString(maps));
		pw.print(array);
		pw.close();
	}
	
	public void loadReportNg(ModelMap model, HttpServletRequest request, HttpServletResponse response, LoginForm form)throws Exception {
		String reportType = request.getParameter("reportType");
		String p_companyId = request.getParameter("compnayId");
		response.setContentType("text/plain;charset=utf-8");
		PrintWriter pw = response.getWriter();
		Map<String, List<DashBroadNG>> map = workOrderDao.getDashBroadNg(reportType, p_companyId);
		//sortMapByKey(map);
		//xu ly du lieu thanh chart
		List<BigDecimal> lstDifficultNg = new ArrayList<BigDecimal>();
		List<BigDecimal> lstDifficultG = new ArrayList<BigDecimal>();
		List<BigDecimal> lstDifficultH = new ArrayList<BigDecimal>();
		List<BigDecimal> lstMediumNg = new ArrayList<BigDecimal>();
		List<BigDecimal> lstMediumG = new ArrayList<BigDecimal>();
		List<BigDecimal> lstMediumH = new ArrayList<BigDecimal>();
		List<BigDecimal> lstEasyNg = new ArrayList<BigDecimal>();
		List<BigDecimal> lstEasyG = new ArrayList<BigDecimal>();
		List<BigDecimal> lstEasyH = new ArrayList<BigDecimal>();
		Map<String, List<?>> mapRs = new HashMap<String, List<?>>();
		List<String> categories = new ArrayList<String>();
		for (Map.Entry<String, List<DashBroadNG>> entry : map.entrySet()) {
			categories.add(entry.getKey());
			setDashboardNg(entry.getValue(), lstDifficultNg, lstDifficultG, lstMediumNg, lstMediumG, lstEasyNg, lstEasyG, 
					lstDifficultH, lstDifficultH, lstEasyH);
		}
		mapRs.put("lstDifficultNg", lstDifficultNg);
		mapRs.put("lstDifficultG", lstDifficultG);
		mapRs.put("lstMediumNg", lstMediumNg);
		mapRs.put("lstMediumG", lstMediumG);
		mapRs.put("lstEasyNg", lstEasyNg);
		mapRs.put("lstEasyG", lstEasyG);
		mapRs.put("categories", categories);
		JSONObject array = new JSONObject(new ObjectMapper().writeValueAsString(sortByKey(mapRs)));
		pw.print(array);
		pw.close();
	}
	
	
	
	private void setDashboardNg(List<DashBroadNG> list, List<BigDecimal> lstDifficultNg, List<BigDecimal> lstDifficultG,
			List<BigDecimal> lstMediumNg, List<BigDecimal> lstMediumG, List<BigDecimal> lstEasyNg, List<BigDecimal> lstEasyG,
			List<BigDecimal> lstDifficultH, List<BigDecimal> lstMediumH, List<BigDecimal> lstEasyH) {
		int count = 0;
		Boolean isDifficult = false;
		Boolean isMedium = false;
		Boolean iseasy = false;
		for (DashBroadNG item : list) {
			count++;
			if ("Khó".equalsIgnoreCase(item.getLevel())) {
				isDifficult = true;
				lstDifficultNg.add(item.getNgAmount());
				lstDifficultG.add(item.getAmount());
				lstDifficultH.add(item.getBrokenAmount());
			} else if ("Trung bình".equalsIgnoreCase(item.getLevel())) {
				isMedium = true;
				lstMediumNg.add(item.getNgAmount());
				lstMediumG.add(item.getAmount());
				lstMediumH.add(item.getBrokenAmount());
			} else if ("Dễ".equalsIgnoreCase(item.getLevel())) {
				iseasy = true;
				lstEasyNg.add(item.getNgAmount());
				lstEasyG.add(item.getAmount());
				lstEasyH.add(item.getBrokenAmount());
			}
		}
		for (int j=0; j < (3-count); j++) {
			if (!isDifficult) {
				lstDifficultNg.add(new BigDecimal("0"));
				lstDifficultG.add(new BigDecimal("0"));
				lstDifficultH.add(new BigDecimal("0"));
				isDifficult = true;
			}
			if (!isMedium) {
				lstMediumNg.add(new BigDecimal("0"));
				lstMediumG.add(new BigDecimal("0"));
				lstMediumH.add(new BigDecimal("0"));
				isMedium = true;
			}
			if (!iseasy) {
				lstEasyNg.add(new BigDecimal("0"));
				lstEasyG.add(new BigDecimal("0"));
				lstEasyH.add(new BigDecimal("0"));
				iseasy = true;
			}
		}
	}
	
	public static Map<String, List<?>> sortByKey(Map<String, List<?>> mapRs) {
		// Create a list from elements of HashMap
		List<Map.Entry<String, List<?>>> list = new LinkedList<Map.Entry<String, List<?>>>(
				mapRs.entrySet());
		// Sort the list using lambda expression
		Collections.sort(list, (i1, i2) -> i1.getKey().compareTo(i2.getKey()));
		// put data from sorted list to hashmap
		HashMap<String, List<?>> temp = new LinkedHashMap<String, List<?>>();
		for (Map.Entry<String, List<?>> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}
		return temp;
	}

	private String expiredPassWordDate(Date pwDate) {
		String expriredIn = null;
		String expriredDay = null;
		Date date = new Date();
		// So ngay hien thi canh bao den han password
		SysParam item = sysParamDao.getSysParamByCode(RightConstants.PW_EXPIRED_DAY);
		if (item == null)
			return "";
		expriredDay = item.getValue();
		// Thoi han hieu luc password
		item = sysParamDao.getSysParamByCode(RightConstants.PW_EXPIRED_IN);
		if (item == null)
			return "";
		expriredIn = item.getValue();
		Calendar c = Calendar.getInstance();
		c.setTime(pwDate);
		c.add(Calendar.DATE, Integer.parseInt(expriredIn));
		long time = 24 * 60 * 60 * 1000;
		long diff = 0;
		if (c.getTime().after(date)) {
			diff = (c.getTime().getTime() - date.getTime()) / time;
			if (diff <= Long.parseLong(expriredDay)) {
				return String.valueOf(diff);
			}
			return "";
		} else {
			return "";
		}
	}
}
