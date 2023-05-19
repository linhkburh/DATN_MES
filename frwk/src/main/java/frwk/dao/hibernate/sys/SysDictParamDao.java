package frwk.dao.hibernate.sys;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.jboss.logging.Logger;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import entity.frwk.SysDictParam;
import entity.frwk.SysDictType;

@Repository(value = "sysDictParamDao")
public class SysDictParamDao extends SysDao<SysDictParam> {
	private static final Logger logger = Logger.getLogger(SysDictParamDao.class);

	@SuppressWarnings("unchecked")
	public List<SysDictParam> getByType(String sysTypeCode) {
		List<SysDictType> tmpDicTypes = (List<SysDictType>) getCurrentSession().createCriteria(SysDictType.class)
				.add(Restrictions.eq("code", sysTypeCode)).list();
		return (List<SysDictParam>) getCurrentSession().createCriteria(SysDictParam.class)
				.add(Restrictions.eq("sysDictType", tmpDicTypes.get(0))).list();
	}

	@Override
	public void save(SysDictParam s) throws Exception {
		SysDictParam sysDictParam = (SysDictParam) s;
		if (sysDictParam.getSysDictType() != null && Formater.isNull(sysDictParam.getSysDictType().getId()))
			sysDictParam.setSysDictType(null);
		super.save(sysDictParam);
	}

	@SuppressWarnings("unchecked")
	@Cacheable(value = "SysDictParam.getByTypeAndCode", unless = "#result == null")
	public SysDictParam getByTypeAndCode(String sysTypeCode, String code) {
		List<SysDictType> tmpDicTypes = (List<SysDictType>) getCurrentSession().createCriteria(SysDictType.class)
				.add(Restrictions.eq("code", sysTypeCode)).setMaxResults(1).list();
		List<SysDictParam> tmp = (List<SysDictParam>) getCurrentSession().createCriteria(SysDictParam.class)
				.add(Restrictions.eq("sysDictType", tmpDicTypes.get(0))).add(Restrictions.eq("code", code))
				.setMaxResults(1).list();
		if (tmp.size() == 0)
			return null;
		return tmp.get(0);
	}

}
