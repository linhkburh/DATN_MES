package frwk.dao.hibernate.sys;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import entity.frwk.LogAction;

@Repository(value = "logActionDao")
public class LogActionDao extends SysDao<LogAction> {

	@SuppressWarnings("unchecked")
	public LogAction getFunctionNameByClassName(String ClassName) {
		List<LogAction> temp = getThreadSession().createCriteria(LogAction.class)
				.add(Restrictions.eq("className", ClassName)).list();
		if (Formater.isNull(temp))
			return null;
		return temp.get(0);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<LogAction> getAll() {
		return (ArrayList<LogAction>) getThreadSession().createCriteria(LogAction.class)
				.add(Restrictions.ne("className", "hibernatedto.Transaction")).list();
	}

}
