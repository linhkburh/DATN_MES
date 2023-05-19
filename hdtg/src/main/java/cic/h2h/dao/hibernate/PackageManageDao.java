package cic.h2h.dao.hibernate;

import org.springframework.stereotype.Repository;

import entity.DLVPackage;

@Repository(value = "packageManageDao")
public class PackageManageDao extends H2HBaseDao<DLVPackage> {
	public void save(DLVPackage dLVPackage) throws Exception {
		getCurrentSession().save(dLVPackage);
	}
}
