package frwk.dao.hibernate.sys;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import entity.frwk.SysDictParam;
import entity.frwk.SysMail;
import entity.frwk.SysUserMail;

@Repository(value = "sysMailDao")
public class SysMailDao extends SysDao<SysMail> {
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<SysMail> getMailToSend() {
		return getCurrentSession().createCriteria(getModelClass())
				.add(Restrictions.or(Restrictions.isNull("status"), Restrictions.eq("status", SysMail.STS_FALSE)))
				.list();
	}

	@Autowired
	private SysDictParamDao sysDictParamDao;

	@SuppressWarnings("unchecked")
	public String getReceiptWrngMail() {
		SysDictParam waring = sysDictParamDao.getByTypeAndCode("WRNMAIL", "BKSTS");
		List<SysUserMail> lstUserMail = getCurrentSession().createCriteria(SysUserMail.class)
				.add(Restrictions.eq("sysDictParam", waring)).list();
		if (lstUserMail.isEmpty())
			return null;
		String rs = null;
		for (SysUserMail userMail : lstUserMail) {
			if (Formater.isNull(userMail.getSysUsers().getEmail()))
				continue;
			if (rs == null)
				rs = userMail.getSysUsers().getEmail();
			else
				rs = rs + ";" + userMail.getSysUsers().getEmail();
		}
		return rs;
	}

}
