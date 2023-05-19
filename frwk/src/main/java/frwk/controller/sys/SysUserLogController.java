package frwk.controller.sys;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import common.util.Formater;
import entity.frwk.LogAction;
import entity.frwk.SysUsers;
import entity.frwk.SysUsersLog;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.LogActionDao;
import frwk.dao.hibernate.sys.SysUsersDao;
import frwk.dao.hibernate.sys.UserLogDao;
import frwk.form.SysUserLogForm;

@Controller
@RequestMapping("/audit")
public class SysUserLogController extends CatalogController<SysUserLogForm,SysUsersLog> {

	static Logger lg = Logger.getLogger(SysUserLogController.class);

	@Autowired
	private UserLogDao userLogDao;

	@Autowired
	private SysUsersDao sysUsersDao;
	
	@Autowired
	private LogActionDao logActionDao;

	@Override	
	public BaseDao<SysUsersLog> getDao() {
		return userLogDao;
	}

	private SysUsersLog userlog = new SysUsersLog();
	ArrayList<LogAction> logAction;
	private List<ActionItem> actionList;
	//private List<Partner> danhSachDoiTac;


	@Override
	public void createSearchDAO(HttpServletRequest request, SysUserLogForm form,BaseDao<SysUsersLog> dao) throws Exception {
		SysUserLogForm sysUserLogForm = (SysUserLogForm) form;
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		if (!Formater.isNull(sysUserLogForm.getFromdate())) {
			try {
				dao.addRestriction(Restrictions.ge("modifyTime", df.parse(sysUserLogForm.getFromdate())));
			} catch (ParseException e) {
				lg.error(e);
			}
		}
		if (!Formater.isNull(sysUserLogForm.getTodate())) {

			try {
				Calendar c = Calendar.getInstance();
				c.setTime(df.parse(sysUserLogForm.getTodate()));
				c.add(Calendar.DATE, 1);
				dao.addRestriction(Restrictions.le("modifyTime", c.getTime()));
			} catch (ParseException e) {
				lg.error(e);
			}
		}
		// Search by username
		if (!Formater.isNull(sysUserLogForm.getUsername())) {
			dao.addRestriction(
					Restrictions.like("userId", sysUserLogForm.getUsername().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
		// Search by keyWord
		if (!Formater.isNull(sysUserLogForm.getKeyWord())) {
			dao.addRestriction(
					Restrictions.like("recordId", sysUserLogForm.getKeyWord().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
		// Search by action
		if (!Formater.isNull(sysUserLogForm.getActionLists())) {
			// like because edit_entity.XXXX_$$javasiss
			dao.addRestriction(Restrictions.like("action", sysUserLogForm.getActionLists().trim(), MatchMode.ANYWHERE)
					.ignoreCase());
			if ("entity.frwk.SysUsers".equals(sysUserLogForm.getActionLists())) {
				if (!Formater.isNull(sysUserLogForm.getKeyWord())) {
					String sql = "exists (select 1 from sys_users u where u.id = \"RECORD ID\" and upper(u.username) like '%"
							+ StringEscapeUtils.escapeSql(sysUserLogForm.getKeyWord().trim()).toUpperCase() + "%')";
					dao.addRestriction(Restrictions.sqlRestriction(sql));
				}
			}
		}
		// Search by partner
		if (!Formater.isNull(sysUserLogForm.getSpartnerId())) {
			sysUserLogForm.setSpartnerId(StringEscapeUtils.escapeSql(sysUserLogForm.getSpartnerId()));
			String sql = "exists (select 1 from sys_users u where u.username = USER_ID and u.company_id = '"
					+ StringEscapeUtils.escapeSql(sysUserLogForm.getSpartnerId()) + "')";
			dao.addRestriction(Restrictions.sqlRestriction(sql));
		}
		// Order by modifytime
		dao.addOrder(Order.desc("modifyTime"));
	}

	@Override
	public void pushToJa(JSONArray ja, SysUsersLog r, SysUserLogForm sysUserLogForm) throws Exception {
		if (r.getUserId()!=null) {
			ja.put("<span class='characterwrap'>" + r.getUserId() + "</span>");
		} else {
			ja.put("");
		}
		
//		if (!Formater.isNull(r.getAction())) {
//			ja.put(r.getAction());
//		} else {
//			ja.put("");
//		}
		
		if ("login".equals(r.getAction())) {
			ja.put("Login");
		} else {
			try {
				String[] className = r.getAction().split("_");
				LogAction logaction = logActionDao.getFunctionNameByClassName(className[1]);
				if (logaction != null) {
					String str = getFunctionName(className[0]);
					ja.put(str + logaction.getFncName());
				} else
					ja.put("");

			} catch (Exception e) {
				ja.put("");
				lg.error(e);
			}
		}
		
//		if (!Formater.isNull(r.getRecordId())) {
//			ja.put(r.getRecordId());
//		} else {
//			ja.put("");
//		}
		
		if (!"hibernatedto.SysUsers".equals(sysUserLogForm.getActionLists()))
			ja.put(r.getRecordId());
		else {
			if (Formater.isNull(r.getRecordId()))
				ja.put("");
			else {
				SysUsers u = sysUsersDao.getUserById(r.getRecordId());
				if (u != null)
					ja.put(u.getUsername());
				else
					ja.put("");
			}
		}
		
		if (!Formater.isNull(r.getDetail())) {
			ja.put("<a data-toggle=\"modal\" data-target=\"#myModal\" href = '#' onclick = 'chiTietLog(\"" + r.getId()
			+ "\")'>Chi ti&#7871;t</a>");
		} else {
			ja.put("");
		}
		
		try {
			ja.put(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(r.getModifyTime()));
		} catch (Exception e) {
			ja.put("");
			lg.error(e);
		}
	}

	@Override
	public String getJsp() {
		return "qtht/danh_muc_hoat_dong";
	}


	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysUserLogForm form)
			throws Exception {
        String str = "Danh m\u1EE5c ";
        actionList = new ArrayList<ActionItem>();
        logAction = logActionDao.getAll();
        for (LogAction log : logAction)
            actionList.add(new ActionItem(log.getClassName(), str + log.getFncName()));
        actionList.add(new ActionItem("Login", "Login"));
//        ApplicationContext appContext = (ApplicationContext)rq.getSession().getAttribute(ApplicationContext.APPLICATIONCONTEXT);
//        SysUsers su = (SysUsers)appContext.getAttribute(ApplicationContext.USER);
//        if (Utils.isVtbHo(su.getCompany()))
//            danhSachDoiTac = companyDao.getAllBranch();
//        else {
//            danhSachDoiTac = new ArrayList<Company>();
//            danhSachDoiTac.add(su.getCompany());
//        }
 //       danhSachDoiTac = sysPartnerDao.getAll();
//        sysUserLogForm.setSpartnerId(su.getCompany().getId());
        
//        model.addAttribute("dsDoiTac", danhSachDoiTac);
        model.addAttribute("actionLists", actionList);
	}

//    public void setDanhSachDoiTac(List<Partner> danhSachDoiTac) {
//        this.danhSachDoiTac = danhSachDoiTac;
//    }
//
//    public List<Partner> getDanhSachDoiTac() {
//        return danhSachDoiTac;
//    }

	private String getFunctionName(String fncName) {
		if (fncName.equals("insert"))
			return "Th\u00EAm m\u1EDBi ";
		if (fncName.equals("edit"))
			return "C\u1EADp nh\u1EADt ";
		if (fncName.equals("del"))
			return "X\u00F3a ";
		return "";
	}

	public SysUsersLog getModel() {
		return userlog;
	}

	public SysUsersLog getUserlog() {
		return userlog;
	}

	public void setUserlog(SysUsersLog userlog) {
		this.userlog = userlog;
	}
	
    public void setActionList(List<SysUserLogController.ActionItem> actionList) {
        this.actionList = actionList;
    }

    public List<SysUserLogController.ActionItem> getActionList() {
        return actionList;
    }
	
    public class ActionItem {
        private String code, name;

        public ActionItem(String code, String name) {
            this.code = code;
            this.name = name;
        }

        private ActionItem() {
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
    
    public void loadDetail(ModelMap map, HttpServletRequest rq, HttpServletResponse rp, SysUserLogForm form)
			throws IOException {
		String id = Formater.isNull(rq.getParameter("id")) ? "" : rq.getParameter("id");
		SysUsersLog userLog = null;
		userLog = userLogDao.getObject(SysUsersLog.class, id);
		returnTxtHtml(rp, userLog.getDetail());
	}
    
}