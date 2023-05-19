package frwk.dao.hibernate.sys;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import entity.frwk.SysMenu;

@Repository(value = "sysMenuDao")
public class SysMenuDao extends SysDao<SysMenu> {

	@SuppressWarnings("unchecked")
	public List<SysMenu> getAllLeaf() {
		return getThreadSession().createCriteria(SysMenu.class)
				.add(Restrictions.or(Restrictions.isNull("deActive"), Restrictions.eq("deActive", Boolean.FALSE)))
				.add(Restrictions.sqlRestriction("not exists (select 1 from sys_menu s where s.parent = {alias}.id)"))
				.list();
	}

}
