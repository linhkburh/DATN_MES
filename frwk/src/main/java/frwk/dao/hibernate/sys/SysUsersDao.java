package frwk.dao.hibernate.sys;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import common.util.Formater;
import entity.Company;
import entity.Department;
import entity.frwk.SysRoles;
import entity.frwk.SysRolesUsers;
import entity.frwk.SysRolesUsersId;
import entity.frwk.SysUsers;
import entity.frwk.UserLog;

@Repository(value = "sysUsersDao")
public class SysUsersDao extends SysDao<SysUsers> {
	@Autowired
	private UserDao userDao;

	public void changePassword(SysUsers user, String newPass) {
		// Cap nhat ngay hieu luc password
		user.setPwdDate(Calendar.getInstance().getTime());
		// user.setPassword(userDao.getHash(user.getUsername(), newPass));
		user.setPassword(newPass);
		user.setPssWordValidTime(Boolean.TRUE);
		user.setExpireDay(60);
		getCurrentSession().update(user);
	}

	public void save(SysUsers user, String pwdText) throws Exception {
		Session session = openNewSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			// Ghi log
			UserLog log = new UserLog(user.getId());
			log.setUserId(Formater.isNull(user.getId()) ? user.getCreator().getId() : user.getModifier().getId());
			String roles = user.getRoles();
			if (!Formater.isNull(user.getId())) {
				log.setAction("edit_" + user.getClass().getName());
				SysUsers userInDB = (SysUsers) session.load(SysUsers.class, user.getId());
				// Khong cho sua 5 thong tin nay
				user.setPassword(userInDB.getPassword());
				user.setPwdDate(userInDB.getPwdDate());
				// Xoa quyen khong duoc chon
				List<SysRolesUsers> delRoles = new ArrayList<SysRolesUsers>();
				for (SysRolesUsers ur : userInDB.getSysRolesUserses()) {
					String roleId = ur.getSysRoles().getId();
					if (roles.indexOf(roleId + ",") < 0) {
						delRoles.add(ur);
						session.delete(ur);
					}
				}

				userInDB.getSysRolesUserses().removeAll(delRoles);
				user.setSysRolesUserses(userInDB.getSysRolesUserses());

				session.merge(user);

			} else {
				log.setAction("insert_" + user.getClass().getName());
				user.setSysRolesUserses(new ArrayList<SysRolesUsers>());
				session.save(user);
				// Gui mail cho khach hang
				// BufferedReader br = null;
				// String username = user.getUsername();
				// // String newPassWord = user.getPassword();
				// String emailAddress = user.getEmail();
				// try {
				//
				// String sCurrentLine;
				// String result = "";
				//
				// InputStream inStream =
				// ServletActionContext.getServletContext().getResourceAsStream("/Template_Mail/template_mail_tao_moi_nguoi_dung.html");
				// final InputStreamReader streamReader = new InputStreamReader(inStream,
				// "UTF-8");
				// br = new BufferedReader(streamReader);
				// while ((sCurrentLine = br.readLine()) != null) {
				// result += sCurrentLine;
				// }
				// result = result.replace("{username}", username);
				// result = result.replace("{password}", pwdText);

				// try {
				// Email email = new Email();
				// email.sendEmail(emailAddress, result, null);
				// } catch (Exception ex) {
				// // Khong gui duoc mail thi thoi
				// logger.error(LoggerFactory.LOG_EROR, ex);
				// }
				// } catch (IOException e) {
				// throw e;
				// } finally {
				// try {
				// if (br != null)
				// br.close();
				// } catch (IOException ex) {
				// throw ex;
				// }
				// }
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

			log.setRecordId(user.getId());
			// Noi dung thay doi
			StringWriter writer = new StringWriter();
			new ObjectMapper().writeValue(writer, user);
			log.setDetail(writer.toString());
			writer.close();

			session.save(log);

			tx.commit();

		} catch (Exception ex) {
			logger.error(ex);
			if (tx != null)
				tx.rollback();
			throw ex;

		} finally {
			session.close();
		}
	}

	private static final Logger logger = Logger.getLogger(SysUsersDao.class);

	private static boolean isNewRole(String objId, List<SysRolesUsers> urs) {
		for (SysRolesUsers ur : urs) {
			if (ur.getSysRoles().getId().equals(objId))
				return false;
		}
		return true;
	}

//	private static boolean isNewProduct(String objId, List<MapProduct> urs) {
//		for (MapProduct ur : urs) {
//			if (ur.getCatProductId().getId().equals(objId))
//				return false;
//		}
//		return true;
//	}

	public SysUsers getWithRole(String id) throws Exception {
		SysUsers user = null;
		Session ss = getThreadSession();
		user = (SysUsers) ss.get(SysUsers.class, id);
		for (SysRolesUsers userRole : user.getSysRolesUserses()) {
			if (Formater.isNull(user.getRoles()))
				user.setRoles(userRole.getSysRoles().getId());
			else
				user.setRoles(user.getRoles() + "," + userRole.getSysRoles().getId());
		}
		return user;
	}

	public SysUsers getUserById(String id) throws Exception {
		SysUsers u = null;
		Session s = openNewSession();
		Transaction tx = s.beginTransaction();
		try {
			u = (SysUsers) s.get(SysUsers.class, id);
			tx.commit();
		} catch (Exception ex) {
			ex.printStackTrace();
			if (tx != null)
				tx.rollback();
			throw ex;
		} finally {
			s.close();
		}
		return u;
	}

	public SysUsers getUser(String id) throws Exception {
		return (SysUsers) getThreadSession().get(SysUsers.class, id);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<SysUsers> listByCompanyAndRole(String companyId) {
		ArrayList<SysUsers> result = null;
		Session s = openNewSession();
		Transaction tx = null;
		try {
			tx = s.beginTransaction();
			StringBuffer sqlbuffer = new StringBuffer(
					"select distinct u.* from sys_users u,sys_roles_users ru,sys_roles r,sys_rights rg,sys_objects o ");
			sqlbuffer.append("where u.company_id = :company_id ");
			sqlbuffer.append("and u.id = ru.user_id ");
			sqlbuffer.append("and r.id = ru.role_id ");
			sqlbuffer.append("and r.id = rg.role_id ");
			sqlbuffer.append("and o.id = rg.object_id ");
			sqlbuffer.append("and o.object_id = 'SNDMNYCRT' ");
			SQLQuery query = s.createSQLQuery(sqlbuffer.toString());
			query.setParameter("company_id", companyId);
			result = (ArrayList<SysUsers>) query.addEntity(SysUsers.class).list();
			tx.commit();

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			if (tx != null)
				tx.rollback();
			throw ex;
		}
		return result;
	}

	public void resetPassWord(SysUsers sysUsers, String newPass) throws Exception {
		sysUsers = getObject(sysUsers);
		sysUsers.setPassword(newPass);
		sysUsers.setPwdDate(Calendar.getInstance().getTime());
		// if reset password then user is change password again
		sysUsers.setPasswordStatus(null);
		getCurrentSession().update(sysUsers);

	}

	@SuppressWarnings("unchecked")
	public List<SysUsers> getSysUserByPartnerId(String partnerId) {
		List<SysUsers> lstSysUsers = new ArrayList<SysUsers>();
		Criteria criteria = getCurrentSession().createCriteria(SysUsers.class);
		try {
			criteria.add(Restrictions.eq("companyId", partnerId).ignoreCase());
			lstSysUsers = criteria.list();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return lstSysUsers;
	}

	@SuppressWarnings("unchecked")
	@Cacheable(value = "getSysUserByUserName", unless = "#result == null")
	public SysUsers getSysUserByUserName(String username) {
		List<SysUsers> lstSysUsers = getCurrentSession().createCriteria(SysUsers.class)
				.add(Restrictions.eq("username", username).ignoreCase()).setMaxResults(1).list();
		return lstSysUsers.isEmpty() ? null : lstSysUsers.get(0);
	}

	@SuppressWarnings("unchecked")
	@Cacheable(value = "SysUsersDao.getCompanyUser", unless = "#result == null")
	public List<SysUsers> getCompanyUser(Company company) {
		Criteria c = getCurrentSession().createCriteria(SysUsers.class)
				.add(Restrictions.or(Restrictions.isNull("isPartner"), Restrictions.eq("isPartner", Boolean.FALSE)));
		if (company != null)
			c.add(Restrictions.eq("company", company));
		return c.addOrder(Order.asc("username")).list();
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Cacheable(value = "SysUsersDao.getDepartmentUser", unless = "#result == null")
	public List<SysUsers> getDepartmentUser(Company company, String departmentCode) {
		Criteria c = getCurrentSession().createCriteria(SysUsers.class);
		c.createAlias("department", "d");
		c.add(Restrictions.eq("d.code", departmentCode));
		if (company != null)
			c.add(Restrictions.eq("company", company));
		List<SysUsers> tmp = c.list();
		if(tmp.isEmpty())
			return null;
		return tmp;
	}

}
