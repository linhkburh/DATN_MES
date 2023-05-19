package frwk.dao.hibernate.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionImplementor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import common.util.ResourceException;
import entity.frwk.JobcodeRole;
import entity.frwk.SysObjects;
import entity.frwk.SysRights;
import entity.frwk.SysRightsId;
import entity.frwk.SysRoles;
import entity.frwk.SysRolesUsers;
import entity.frwk.SysUsers;
import entity.frwk.UserLog;

@Repository(value = "roleDao")
public class RoleDao extends SysDao<SysRoles> {

	static Logger lg = Logger.getLogger(RoleDao.class);

	public void save(SysRoles role1, SysUsers sysUsers) throws Exception {
		Session session = openNewSession();
		Transaction tx = null;
		SysRoles oldInDb = null;
		try {
			// Ghi log
			tx = session.beginTransaction();
			UserLog log = new UserLog(role1.getId());
			// log.setUserId(Formater.isNull(role1.getId()) ? role1.getCreator().getId() :
			// role1.getModifier().getId());
			log.setUserId(sysUsers.getUsername());
			Date date = new Date();
			log.setModifyTime(date);

			String rights = role1.getRights();
			if (!Formater.isNull(role1.getParentId())) {
				role1.setSysRoles((SysRoles) session.load(SysRoles.class, role1.getParentId()));
			}
			if (!Formater.isNull(role1.getId())) {
				// check quan he cha con
				oldInDb = (SysRoles) session.get(SysRoles.class, role1.getId());
				if (oldInDb != null && role1.getSysRoles() != null) {
					validateParent(oldInDb, role1.getSysRoles().getId());
				}

				SysRoles role = (SysRoles) session.load(SysRoles.class, role1.getId());
				// log.setAction("edit_" + role.getClass().getName());
				log.setAction("edit_" + role1.getClass().getName());
				// Lay cac rights bi xoa
				List<SysRights> delRight = new ArrayList<SysRights>();
				for (SysRights right : role.getSysRightses()) {
					if (rights.indexOf(right.getId() + ",") < 0) {
						session.delete(right);
						delRight.add(right);
					}
				}
				role.getSysRightses().remove(delRight);
				session.merge(role1);
			} else {
				// log.setAction("insert_" + role1.getClass().getName());
				log.setAction("insert_" + role1.getClass().getName());
				session.save(role1);
			}
			// Danh sach quyen
			if (!Formater.isNull(rights)) {
				// Khoi tao danh sach quyen
				String arrObj[] = rights.split(",");
				for (String objId : arrObj) {
					if (isNew(objId, role1.getSysRightses())) {
						SysObjects sysObj = (SysObjects) session.load(SysObjects.class, objId);
						SysRights right = new SysRights(new SysRightsId(role1.getId(), sysObj.getId()), sysObj, role1);
						right.setSysRoles(role1);
						session.save(right);
					}
				}
			}
			log.setRecordId(String.valueOf(
					getClassMetadata(role1.getClass()).getIdentifier(role1, (SessionImplementor) getCurrentSession())));
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

	@SuppressWarnings("unchecked")
	private void validateParent(SysRoles oldInDb, String parentId) throws Exception {
		// trung cha chon cha, con, chau
		if (oldInDb.getId().equals(parentId)) {
			throw new ResourceException(
					"Nh&#243;m quy&#7873;n cha tr&#249;ng v&#7899;i m&#236;nh ho&#7863;c nh&#243;m quy&#7873;n con, ch&#225;u");
		}
		// Kiem tra cq cha trung voi cq con chau
		List<SysRoles> children = null;
		Session ss = openNewSession();
		Transaction tx = null;
		try {
			tx = ss.beginTransaction();
			Criteria c = ss.createCriteria(SysRoles.class);
			c.add(Restrictions.eq("sysRoles", oldInDb));
			children = c.list();
			tx.commit();
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			throw ex;
		} finally {
			ss.close();
		}
		if (Formater.isNull(children))
			return;
		for (SysRoles child : children)
			validateParent(child, parentId);
	}

	private static final Logger logger = Logger.getLogger(RoleDao.class);

	public SysRoles getWithRight(String id) throws Exception {
		Session ss = openNewSession();
		Transaction tx = null;
		SysRoles role = null;
		try {
			tx = ss.beginTransaction();
			role = (SysRoles) ss.get(SysRoles.class, id);
			for (SysRights right : role.getSysRightses()) {
				role.setRights(role.getRights() + "," + right.getId().getObjectId());
			}
			if (role.getSysRoles() != null)
				role.setParentId(role.getSysRoles().getId());
			tx.commit();
		} catch (Exception ex) {
			logger.error(ex);
			if (tx != null)
				tx.rollback();
			throw ex;

		} finally {
			ss.close();
		}

		return role;
	}

	@SuppressWarnings("unchecked")
	public JSONArray getTreeRolesData(boolean bNotPublicRole, String parentRoleId, SysUsers su) throws Exception {
		JSONArray _result = new JSONArray();
		Session ss = openNewSession();
		Transaction tx = null;
		try {
			tx = ss.beginTransaction();
			Criteria cr = ss.createCriteria(SysRoles.class);
			if (!bNotPublicRole)
				cr.add(Restrictions.or(Restrictions.isNull("notPublic"), Restrictions.eq("notPublic", Boolean.FALSE)));
			if (!Formater.isNull(parentRoleId))
				cr.add(Restrictions.eq("sysRoles", new SysRoles(parentRoleId)));
			else
				cr.add(Restrictions.isNull("sysRoles"));
			ArrayList<SysRoles> objs = (ArrayList<SysRoles>) cr.list();
			_result = CreateRolesTreeNode(objs, bNotPublicRole, su);
			tx.commit();

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			if (tx != null)
				tx.rollback();
			throw e;
		} finally {
			ss.close();
		}
		return _result;
	}

	public JSONArray CreateRolesTreeNode(ArrayList<SysRoles> list, boolean bNotPublicRole, SysUsers su)
			throws Exception {
		JSONArray ja = new JSONArray();
		for (SysRoles sysItem : list) {
			JSONObject jo = CreateRolesTreeChildenNode(sysItem, bNotPublicRole, su);
			ja.put(jo);
		}
		return ja;
	}

	public JSONObject CreateRolesTreeChildenNode(SysRoles obj, boolean bNotPublicRole, SysUsers su) throws Exception {
		JSONObject jo = new JSONObject();

		jo.put("id", obj.getId());
		jo.put("text", obj.getCode() + " - " + obj.getDescriptionVi());
		if (su != null && su.getRoles() != null) {
			String[] arrRoles = su.getRoles().split(",");
			for (String arrItem : arrRoles) {
				if (arrItem.equals(obj.getId())) {
					if (obj.getSysRoleses(bNotPublicRole) != null && obj.getSysRoleses(bNotPublicRole).size() == 0)
						jo.put("checked", true);
					break;
				}
			}
		}
		if (obj.getSysRoleses(bNotPublicRole) != null && obj.getSysRoleses(bNotPublicRole).size() > 0) {
			jo.put("state", "closed");
			JSONArray ja = new JSONArray();
			for (SysRoles sysChildenItem : obj.getSysRoleses()) {
				JSONObject joChildren = new JSONObject();
				joChildren = CreateRolesTreeChildenNode(sysChildenItem, bNotPublicRole, su);
				if (joChildren.length() > 0) {
					ja.put(joChildren);
				}
			}
			jo.put("children", ja);
		}

		return jo;
	}

	public JSONArray getTreeObjectsData() throws Exception {
		ArrayList<SysObjects> objs = (ArrayList<SysObjects>) getThreadSession().createCriteria(SysObjects.class)
				.add(Restrictions.isNull("sysObjects")).list();
		JSONArray _result = CreateObjectsTreeNode(objs);
		return _result;
	}

	public JSONArray CreateObjectsTreeNode(ArrayList<SysObjects> list) throws Exception {
		JSONArray ja = new JSONArray();
		for (SysObjects sysItem : list) {
			JSONObject jo = CreateObjectsTreeChildenNode(sysItem);
			ja.put(jo);
		}
		return ja;
	}

	public JSONObject CreateObjectsTreeChildenNode(SysObjects obj) throws Exception {
		JSONObject jo = new JSONObject();
		jo.put("id", obj.getId());
		jo.put("text", obj.getObjectId() + " - " + obj.getName());
		if (obj.getSysObjectses() != null && obj.getSysObjectses().size() > 0) {
			jo.put("state", "closed");
			JSONArray ja = new JSONArray();
			for (SysObjects sysChildenItem : obj.getSysObjectses()) {
				JSONObject joChildren = new JSONObject();
				joChildren = CreateObjectsTreeChildenNode(sysChildenItem);
				if (joChildren.length() > 0) {
					ja.put(joChildren);
				}
			}
			jo.put("children", ja);
		}
		return jo;
	}

	private static String createHtmlOfRoot(SysRoles o, boolean isVietTin) {
		String htmlReturn = "";
		if (o.getSysRoleses(isVietTin) != null && o.getSysRoleses(isVietTin).size() > 0)
			htmlReturn = "<tr class='trparent open " + o.getParentId() + "'  id='tr" + o.getPath() + "'><td>";
		else
			htmlReturn = "<tr class = '" + o.getParentId() + "'id='tr" + o.getPath() + "'><td>";

		// td checkbox
		htmlReturn += "<input type='checkbox' onclick = 'selectRight(\"" + o.getId() + "\",\"" + o.getPath()
				+ "\", this.checked)' class='" + o.getId() + "' id = 'chk" + o.getPath() + "'/>";
		htmlReturn += "</td><td>";
		// td content
		// So luong khoang trong
		if (o.getSysRoles() == null) {
			o.setILevel(0);
		} else {
			o.setILevel(o.getSysRoles().getILevel() + 1);
		}
		String sEmpty = "";
		for (int i = 0; i < o.getILevel(); i++)
			sEmpty += "&nbsp;&nbsp;&nbsp;";

		if (o.getSysRoleses(isVietTin) != null && o.getSysRoleses(isVietTin).size() > 0) {

			htmlReturn += sEmpty + "<a href='#' onclick='expand(\"" + o.getId() + "\",\"" + o.getPath()
					+ "\")'><font id='fnt" + o.getPath()
					+ "' style='font-weight:bold'>(-)&nbsp;</font><font style='font-weight:bold'>" + o.getCode() + "-"
					+ o.getDescriptionVi() + "</font></a>";
		} else {
			htmlReturn += sEmpty + "<font>" + o.getCode() + "-" + o.getDescriptionVi() + "</font>";
		}

		htmlReturn += "</td></tr>";
		if (o.getSysRoleses(isVietTin) != null && o.getSysRoleses(isVietTin).size() > 0) {
			for (SysRoles o1 : o.getSysRoleses())
				htmlReturn += createHtmlOfRoot(o1, isVietTin);
		}
		return htmlReturn;
	}

	private static boolean isNew(String objId, List<SysRights> delRights) {
		if (Formater.isNull(delRights))
			return true;
		for (SysRights r : delRights) {
			if (r.getId().equals(objId))
				return false;
		}
		return true;
	}

	public ArrayList<SysRoles> getAllRoot() {
		return (ArrayList<SysRoles>) getThreadSession().createCriteria(SysRoles.class)
				.add(Restrictions.isNull("sysRoles")).list();
	}

	public ArrayList<SysRoles> getParentRighList(String id) {
		return (ArrayList<SysRoles>) getThreadSession().createCriteria(SysRoles.class)
				.add(Restrictions.isNull("sysRoles")).add(Restrictions.ne("id", id)).list();
	}

	public boolean checkCode(String code) {
		long count = (long) getThreadSession().createCriteria(SysRoles.class)
				.add(Restrictions.eq("code", code).ignoreCase()).setProjection(Projections.rowCount()).uniqueResult();
		return (count > 0 ? true : false);
	}

	public void checkUse(String parent) throws ResourceException {
		long count = (long) getThreadSession().createCriteria(SysRoles.class)
				.add(Restrictions.eq("sysRoles.id", parent).ignoreCase()).setProjection(Projections.rowCount())
				.uniqueResult();
		if (count > 0) {
			throw new ResourceException(
					"Kh&#244;ng x&#243;a &#273;&#432;&#7907;c nh&#243;m quy&#7873;n v&#236; c&#243; nh&#243;m quy&#7873;n con");// KhÃ´ng
																																// xÃ³a
																																// Ä‘Æ°á»£c
																																// nhÃ³m
																																// quyá»�n
																																// vÃ¬
																																// cÃ³
																																// nhÃ³m
																																// quyá»�n
																																// con
		}
	}

	public void checkUseByUser(String roleId) throws ResourceException {
		List<SysRolesUsers> rolesUsers = (List<SysRolesUsers>) getThreadSession().createCriteria(SysRolesUsers.class)
				.add(Restrictions.eq("sysRoles.id", roleId).ignoreCase()).list();
		List<SysUsers> users = new ArrayList<SysUsers>();
		if (rolesUsers != null && !rolesUsers.isEmpty()) {
			for (SysRolesUsers item : rolesUsers) {
				SysUsers user = getThreadSession().get(SysUsers.class, item.getSysUsers().getId());
				if (user != null) {
					users.add(user);
				} else {
					getThreadSession().delete(item);
				}
			}
		}
		if (users.size() > 0) {
			// KhÃ´ng xÃ³a Ä‘Æ°á»£c nhÃ³m quyá»�n vÃ¬ Ä‘ang Ä‘Æ°á»£c sá»­ dá»¥ng
			throw new ResourceException(
					"Kh&#244;ng x&#243;a &#273;&#432;&#7907;c nh&#243;m quy&#7873;n v&#236; &#273;ang &#273;&#432;&#7907;c s&#7917; d&#7909;ng");
		}
	}

	public SysRoles getByCode(String code) {
		return (SysRoles) getCurrentSession().createCriteria(SysRoles.class).add(Restrictions.eq("code", code)).uniqueResult();
	}
}
