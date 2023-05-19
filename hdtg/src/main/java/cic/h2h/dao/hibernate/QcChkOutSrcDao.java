package cic.h2h.dao.hibernate;

import java.util.Iterator;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import entity.QcChkOutSrc;
import entity.QcChkOutSrcDetail;
import entity.WorkOrderExe;


@Repository(value="qcChkOutSrcDao")
public class QcChkOutSrcDao extends H2HBaseDao<QcChkOutSrc>{

	@Autowired
	private QcChkOutSrcDao qcChkOutSrcDao;
	
	@Override
	public void save(QcChkOutSrc qcChkOut) throws Exception {
		if (Formater.isNull(qcChkOut.getId())) {
			super.save(qcChkOut);
		} else {
			QcChkOutSrc oldDB = qcChkOutSrcDao.get(QcChkOutSrc.class, qcChkOut.getId());
			qcChkOut.setCreateDate(oldDB.getCreateDate());
			updatePojoList(oldDB,"qcChkOutSrcDetails", qcChkOut, null);
			getCurrentSession().merge(qcChkOut);
		}
		
	}
	
	@Override
	/**
	 * Xoa ket qua QC
	 */
	public void del(QcChkOutSrc entity) throws Exception {
		Iterator<QcChkOutSrcDetail> it = entity.getQcChkOutSrcDetails().iterator();
		while (it.hasNext()) {
			qcChkOutSrcDao.getCurrentSession().delete(it.next());
			it.remove();
		}
		entity.setStartTime(null);
		entity.setEndTime(null);
		entity.setWorker(null);
		entity.setAmount(entity.getTotalAmount());
	}

	@SuppressWarnings("unchecked")
	public void del(WorkOrderExe woExe) {
		List<QcChkOutSrc> lstChkOut = getCurrentSession().createCriteria(QcChkOutSrc.class).add(Restrictions.eq("workOrderExe", woExe)).list();
		for(QcChkOutSrc chkOutSrc: lstChkOut)
			getCurrentSession().delete(chkOutSrc);
	}
}
