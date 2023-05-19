package cic.h2h.dao.hibernate;

import org.springframework.stereotype.Repository;

import common.util.Formater;
import entity.MaterialType;

@Repository(value = "materialTypeDao")
public class MaterialTypeDao extends H2HBaseDao<MaterialType>{
	
	
	@Override
	public void save(MaterialType materialType) throws Exception {
		if (Formater.isNull(materialType.getId())) {
			super.save(materialType);
		}
		MaterialType old = getObject(MaterialType.class, materialType.getId());
		materialType.setCreateDate(old.getCreateDate());
		super.save(materialType);
	}
}
