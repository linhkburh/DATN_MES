package frwk.dao.hibernate.sys;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import entity.frwk.SysMail;
import entity.frwk.SysMailAtt;
@Repository(value = "sysMailAttDao")
public class SysMailAttDao extends SysDao<SysMailAtt>{
	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<SysMailAtt> getMailAttByMail(SysMail sysMail) {
		return getCurrentSession().createCriteria(getModelClass()).add(Restrictions.eq("idSysMail", sysMail)).list();
	}
}
