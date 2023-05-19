package frwk.dao.hibernate.sys;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import entity.frwk.MenuTemp;
import entity.frwk.SysMenu;

@Repository(value = "menuTempDao")
public class MenuTempDao extends SysDao<MenuTemp> {

	@SuppressWarnings("unchecked")
	public List<MenuTemp> getAllLeaf() {
		return getThreadSession().createCriteria(MenuTemp.class)
				.add(Restrictions.or(Restrictions.isNull("deActive"), Restrictions.eq("deActive", Boolean.FALSE)))
				.add(Restrictions.sqlRestriction("not exists (select 1 from menu_temp s where s.parent = {alias}.id)"))
				.list();
	}

}
