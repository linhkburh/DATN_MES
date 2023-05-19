package cic.h2h.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.ibm.icu.util.Calendar;

import common.util.Formater;
import entity.ExeStep;
import entity.Material;
import entity.frwk.SysDictParam;
import entity.frwk.SysDictType;

@Repository("materialDao")
public class MaterialDao extends H2HBaseDao<Material> {
	@Override
	public void save(Material material) throws Exception {
		super.save(material);
		if(true)
			return;
		// TODO: ntdung Check lai doan loang ngoang nay sau
		if (Formater.isNull(material.getId())) {
			super.save(material);
			return;
		}
		CheckApproveData w = new CheckApproveData(material.getId(), "MATERIAL");
		getCurrentSession().doWork(w);
		if (Boolean.TRUE.equals(w.getExistApproveData())) {
			Material clone = material.clone();
			super.save(clone);
			// Cap nhat endate cho version hien tai
			material.setEndDate(Calendar.getInstance().getTime());
			super.save(material);
		} else {
			super.save(material);
		}

	}@Override
	public void del(Material material) throws Exception {
		material = getObject(material);
		if(material.getPreviousVersion()!=null) {
			material.getPreviousVersion().setEndDate(null);
			super.save(material.getPreviousVersion());
		}
		super.del(material);
	}
	@SuppressWarnings("unchecked")
	@Cacheable(value = "materialDao.getByCode", unless = "#result == null")
	public Material getByCode(String materialCode) {
		List<Material> tmp = (List<Material>) getCurrentSession().createCriteria(Material.class)
				.add(Restrictions.eq("code", materialCode)).setMaxResults(1).list();
		if (tmp.size() == 0)
			return null;
		return tmp.get(0);
	}
}
