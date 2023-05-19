package frwk.dao.hibernate.sys;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import entity.frwk.SysFile;
import frwk.bean.FTPUtils;
import frwk.bean.FtpInf;
import frwk.dao.hibernate.sys.SysDao;

@Repository(value = "sysFileDao")
public class SysFileDao extends SysDao<SysFile> {

	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<SysFile> getFileByRecordId(String recordId) {
		Criteria criteria = getCurrentSession().createCriteria(SysFile.class);
		criteria.add(Restrictions.eq("recordId", recordId));
		return criteria.list();
	}
	
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public List<SysFile> getFileByRecordIds(List<String> recordIds) {
		Criteria criteria = getCurrentSession().createCriteria(SysFile.class);
		criteria.add(Restrictions.in("recordId", recordIds));
		List<SysFile> list = criteria.list();
		return list;
	}

	

	public void del(String recordId) throws Exception {
		List<SysFile> files = getFileByRecordId(recordId);
		for (SysFile file : files) 
			getCurrentSession().delete(file);
	}
	

	@SuppressWarnings({ "deprecation", "unchecked" })
	public void delByLstrecordId(List<String> recordIds) throws Exception {
		List<String> ids = new ArrayList<String>();
		Criteria criteria = getCurrentSession().createCriteria(SysFile.class);
		criteria.add(Restrictions.in("recordId", recordIds));
		List<SysFile> files = criteria.list();
		for (SysFile file : files) {
			ids.add(file.getId());
			getCurrentSession().delete(file);
		}
		ftpUtils.removeFile(ftpInf, ids);
	}
	@Autowired
	private FTPUtils ftpUtils;
	@Autowired
	private FtpInf ftpInf;
}
