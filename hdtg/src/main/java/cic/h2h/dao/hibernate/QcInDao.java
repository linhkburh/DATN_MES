package cic.h2h.dao.hibernate;

import java.math.BigDecimal;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import entity.ExeStep;
import entity.QcIn;

@Repository(value = "qcInDao")
public class QcInDao extends H2HBaseDao<QcIn> {

	public void deletePojo(QcIn qcIn) {
		Transaction tx = null;
		Session ss = openNewSession();
		try {
			tx = ss.beginTransaction();
			ss.delete(qcIn);
			tx.commit();
		} catch (Exception ex) {
			if(tx!=null)
				tx.rollback();
			throw ex;
		} finally {
			ss.close();
		}

	}

}
