package frwk.dao.hibernate.sys;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import common.util.DateTimeUtil;
import common.util.Formater;
import common.util.ResourceException;
import entity.BssFactoryUnit;
import entity.Company;
import entity.Customer;
import entity.frwk.SysObjects;
import entity.frwk.SysParam;
import entity.frwk.SysRoles;
import entity.frwk.SysRolesUsers;
import entity.frwk.SysRolesUsersId;
import entity.frwk.SysUserMail;
import entity.frwk.SysUsers;
import entity.frwk.UserLog;
import frwk.utils.ApplicationContext;

@SuppressWarnings("unchecked")
@Repository(value = "userDao")
public class UserDao extends SysDao<SysUsers> {
	@Autowired
	private SysParamDao sysParamDao;

//	@Autowired
//	private SysPartnerDao sysPartnerDao;
	private static final Logger lg = Logger.getLogger(UserDao.class);

	public SysUsers getUserByUserName(String userName) {
		UserDao dao = new UserDao();
		dao.addRestriction(Restrictions.eq("username", userName).ignoreCase());
		ArrayList<SysUsers> temp = dao.search();
		if (Formater.isNull(temp))
			return null;
		return temp.get(0);
	}

	public SysUsers getUser(String userName) {
		List<SysUsers> lstTmp = getThreadSession().createCriteria(SysUsers.class)
				.add(Restrictions.eq("username", userName).ignoreCase()).setMaxResults(1).list();
		if (lstTmp.isEmpty())
			return null;
		return lstTmp.get(0);
	}

	public SysUsers getActiveUserByUserName(String userName) throws Exception {

		Criteria c = getCurrentSession().createCriteria(SysUsers.class);
		c.add(Restrictions.eq("username", userName).ignoreCase());
		c.add(Restrictions.eq("active", Boolean.TRUE));
		ArrayList<SysUsers> temp = (ArrayList<SysUsers>) c.list();
		if (Formater.isNull(temp))
			return null;
		SysUsers u = temp.get(0);
		// Lay luon company
//		if (u.getCompany() != null && !Formater.isNull(u.getCompany().getId()))
//			u.setCompany(sysPartnerDao.get(Partner.class, u.getCompany().getId()));

		u.setPssWordValidTime(Boolean.TRUE);
		// Kiem tra password con hieu luc
		SysParam pwPeriod = sysParamDao.getSysParamByCode("PASS_VALID_TIME");
		if (u.getPwdDate() != null && (pwPeriod != null && !Formater.isNull(pwPeriod.getValue()))) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			int iNow = Integer.parseInt(sdf.format(Calendar.getInstance().getTime()));
			Calendar validPwDate = Calendar.getInstance();
			validPwDate.setTime(u.getPwdDate());
			validPwDate.add(Calendar.DATE, Integer.parseInt(pwPeriod.getValue()));
			int iPwPeriod = Integer.parseInt(sdf.format(validPwDate.getTime()));
			Date datePwPeriod = validPwDate.getTime();
			Date currentDate = new java.util.Date();
			u.setPssWordValidTime(iPwPeriod >= iNow);
			if (DateTimeUtil.durationBetween(datePwPeriod, datePwPeriod, Calendar.DATE) <= 15)
				u.setExpireDay((int) DateTimeUtil.durationBetween(datePwPeriod, currentDate, Calendar.DATE));
		}

		return u;

	}

	public String getHash(String userName, String password) throws Exception {
		Session s = getCurrentSession();
		String strquery = "select get_hash(:username,:password) from dual";
		SQLQuery query = getCurrentSession().createSQLQuery(strquery);
		query.setParameter("username", userName + "bankpay");
		query.setParameter("password", password);
		Object o = query.uniqueResult();
		return (String) o;

	}

	public boolean haveAnyRight(String rightCode, ArrayList<SysObjects> rights) {
		SysObjects o = (SysObjects) getThreadSession().createCriteria(SysObjects.class)
				.add(Restrictions.eq("objectId", rightCode)).uniqueResult();
		return haveAnyRight(o, rights);
	}

	private static boolean haveAnyRight(SysObjects o, ArrayList<SysObjects> rights) {
		for (SysObjects right : rights)
			if (o.getId().equals(right.getId()))
				return true;
		if (o != null && !Formater.isNull(o.getSysObjectses())) {
			for (SysObjects right : o.getSysObjectses()) {
				if (haveAnyRight(right, rights))
					return true;
			}
		}

		return false;
	}

	public void save(SysUsers su, SysRoles role) throws Exception {
		Session s = openNewSession();
		Transaction tx = null;
		try {
			tx = s.beginTransaction();
			s.save(su);
			if (role != null)
				s.save(new SysRolesUsers(su, role));
			tx.commit();
		} catch (Exception ex) {
			tx.rollback();
			throw ex;
		} finally {
			s.close();
		}

	}

	public void updateUser(SysUsers user) throws Exception {
		UserLog log = new UserLog(user.getId());
		log.setModifyTime(new Date());
		getCurrentSession().merge(user);
		log.setAction("login_fail_" + user.getClass().getName());
		log.setRecordId(user.getId());
		// Noi dung thay doi
		StringWriter writer = new StringWriter();
		new ObjectMapper().writeValue(writer, user);
		log.setDetail(writer.toString());
		writer.close();
		getCurrentSession().save(log);
	}

	public void save(SysUsers user) throws Exception {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = attr.getRequest();
		ApplicationContext appContext = (ApplicationContext) request.getSession()
				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
		Date date = new Date();

		Session session = openNewSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			// Ghi log
			UserLog log = new UserLog(user.getId());
			log.setModifyTime(date);
			// log.setUserId(Formater.isNull(user.getId()) ? user.getCreator().getId() :
			// user.getModifier().getId());
			log.setUserId(((SysUsers) appContext.getAttribute(ApplicationContext.USER)).getUsername());
			String roles = user.getRoles();
			SysUsers sameUserName = getUser(user.getUsername());
			if (!Formater.isNull(user.getId())) {
				if (!sameUserName.getId().equals(user.getId()))
					throw new ResourceException("Tên đăng nhập " + user.getUsername() + " đã tồn tại!");
				log.setAction("edit_" + user.getClass().getName());
				SysUsers userInDB = (SysUsers) session.load(SysUsers.class, user.getId());
				// Khong cho sua 5 thong tin nay
				user.setPassword(userInDB.getPassword());
				user.setPwdDate(userInDB.getPwdDate());
				// Xoa quyen khong duoc chon
				List<SysRolesUsers> delRoles = new ArrayList<SysRolesUsers>();
				for (SysRolesUsers ur : userInDB.getSysRolesUserses()) {
					String roleId = ur.getSysRoles().getId();
					if (roles != null && roles.indexOf(roleId + ",") < 0) {
						delRoles.add(ur);
						session.delete(ur);
					}
				}

				userInDB.getSysRolesUserses().removeAll(delRoles);
				user.setSysRolesUserses(userInDB.getSysRolesUserses());

				if (userInDB.getPasswordStatus() != null) {
					user.setPasswordStatus(userInDB.getPasswordStatus());
				}
				// Email nhan canh bao
				for (SysUserMail inDbmail : userInDB.getLstMail()) {
					boolean delMail = true;
					for (SysUserMail currentMail : user.getLstMail()) {
						if (inDbmail.getId().equals(currentMail.getId())) {
							delMail = false;
							break;
						}
					}
					if (delMail)
						session.delete(inDbmail);
				}

				session.merge(user);
				log.setAction("edit_" + user.getClass().getName());

			} else {
				if (sameUserName != null)
					throw new ResourceException("Tên đăng nhập " + user.getUsername() + " đã tồn tại!");
				user.setSysRolesUserses(new ArrayList<SysRolesUsers>());
				session.save(user);
				log.setAction("insert_" + user.getClass().getName());
			}

			// Danh sach quyen
			if (!Formater.isNull(roles)) {
				// Them quyen moi
				for (String roleId : roles.split(",")) {
					if (isNewRole(roleId, user.getSysRolesUserses())) {
						SysRoles sysRole = (SysRoles) session.load(SysRoles.class, roleId);
						SysRolesUsers userRole = new SysRolesUsers(new SysRolesUsersId(user.getId(), sysRole.getId()),
								user, sysRole);
						userRole.setSysUsers(user);
						userRole.setSysRoles(sysRole);
						session.save(userRole);
					}
				}
			}

			// log.setRecordId(user.getId());
			log.setRecordId(String.valueOf(
					getClassMetadata(user.getClass()).getIdentifier(user, (SessionImplementor) getCurrentSession())));
			// Noi dung thay doi
			StringWriter writer = new StringWriter();
			new ObjectMapper().writeValue(writer, user);
			log.setDetail(writer.toString());
			writer.close();

			session.save(log);

			tx.commit();

		} catch (Exception ex) {
			lg.error(ex);
			if (tx != null)
				tx.rollback();
			throw ex;

		} finally {
			session.close();
		}
	}

	private static boolean isNewRole(String objId, List<SysRolesUsers> urs) {
		for (SysRolesUsers ur : urs) {
			if (ur.getSysRoles().getId().equals(objId))
				return false;
		}
		return true;
	}

	public void del(SysUsers su) throws Exception {
		getCurrentSession().load(su, su.getId());
		for (SysRolesUsers sru : su.getSysRolesUserses())
			getCurrentSession().delete(sru);
		getCurrentSession().delete(su);

	}

	public List<Customer> getAllCus() {
		return getCurrentSession().createCriteria(Customer.class).list();
	}

	public void save(SysRolesUsers sysRolesUsers) {
		getCurrentSession().save(sysRolesUsers);
	}

	public String createUserName(String username) {
		int idx = 0;
		while (true) {
			List<SysUsers> tmp = getThreadSession().createCriteria(SysUsers.class)
					.add(Restrictions.eq("username", username)).setMaxResults(1).list();
			if (tmp.isEmpty())
				return username;
			username += ++idx;
		}
	}

	@SuppressWarnings("unchecked")
	public List<BssFactoryUnit> getFactoryByCompany(String companyId) {
		@SuppressWarnings("deprecation")
		Criteria c = getCurrentSession().createCriteria(BssFactoryUnit.class);
		if (!Formater.isNull(companyId))
			c.add(Restrictions.eq("company.id", companyId));
		return c.list();
	}

}
