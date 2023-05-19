package frwk.dao.hibernate.sys;

import org.springframework.stereotype.Repository;

import entity.frwk.SysUsersLog;
import entity.frwk.UserLog;

@Repository(value = "userLogDao")
public class UserLogDao extends SysDao<SysUsersLog> {

	public void writeLoginLog(UserLog lg) {
		getCurrentSession().save(lg);
	}
}
