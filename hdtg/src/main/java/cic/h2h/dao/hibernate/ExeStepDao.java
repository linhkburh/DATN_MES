package cic.h2h.dao.hibernate;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import com.ibm.icu.util.Calendar;

import common.util.Formater;
import common.util.ResourceException;
import entity.ExeStep;
import entity.ExeStepType;
import entity.frwk.SysDictParam;
import entity.frwk.SysDictType;

@Repository("exeStepDao")
public class ExeStepDao extends H2HBaseDao<ExeStep> {
	private static final Logger logger = Logger.getLogger(ExeStepDao.class);

	public List<ExeStep> getByType(String type) {
		Criteria cr = getCurrentSession().createCriteria(ExeStep.class);
		if (!Formater.isNull(type)) {
			cr.add(Restrictions.eq("stepType.id", type));
			// cr.add(Restrictions.isNull("previousVersion"));
			cr.addOrder(Order.asc("stepName"));
			return cr.list();
		}
		return null;
	}

	public ExeStep getExeStepByCodeAndType(String code, String type) {
		Criteria cr = getCurrentSession().createCriteria(ExeStep.class);
		cr.add(Restrictions.sqlRestriction("exists (select 1 from EXE_STEP_TYPE s where s.CODE = '" + type + "' )"));
		cr.add(Restrictions.eq("stepCode", code));
		cr.add(Restrictions.or(Restrictions.isNull("program"), Restrictions.eq("program", Boolean.FALSE)));
		return (ExeStep) cr.uniqueResult();
	}

	@Override
	public void save(ExeStep step) throws Exception {
		if (Formater.isNull(step.getCurrency().getId()))
			step.setCurrency(null);
		if (step.getDimension() != null && Formater.isNull(step.getDimension().getId()))
			step.setDimension(null);
		super.save(step);
		// Tam thoi Rem bo doan loang ngoang nay
//		if (Formater.isNull(step.getId())) {
//			super.save(step);
//			return;
//		}
//		CheckApproveData w = new CheckApproveData(step.getId(), "EXE_STEP");
//		getCurrentSession().doWork(w);
//		if (Boolean.TRUE.equals(w.getExistApproveData())) {
//			ExeStep clone = step.clone();
//			super.save(clone);
//			// Cap nhat endate cho version hien tai
//			step.setEndDate(Calendar.getInstance().getTime());
//			super.save(step);
//		} else {
//			super.save(step);
//		}

	}

	@Override
	public void del(ExeStep step) throws Exception {
		step = getObject(step);
		if (step.getPreviousVersion() != null) {
			step.getPreviousVersion().setEndDate(null);
			super.save(step.getPreviousVersion());
		}
		super.del(step);
	}

	@SuppressWarnings("unchecked")
	@Cacheable(value = "exeStepDao.getByTypeAndCode", unless = "#result == null")
	public ExeStep getByTypeAndCode(String stepTypeCode, String stepCode, Boolean program) throws ResourceException {
		List<ExeStepType> lstStepType = (List<ExeStepType>) getCurrentSession().createCriteria(ExeStepType.class)
				.add(Restrictions.eq("code", stepTypeCode)).setMaxResults(1).list();
		if (lstStepType == null || lstStepType.isEmpty())
			return null;
		Criteria c = getCurrentSession().createCriteria(ExeStep.class)
				.add(Restrictions.eq("stepType", lstStepType.get(0))).add(Restrictions.eq("stepCode", stepCode));
		if (program != null) {
			if (!program)
				c.add(Restrictions.or(Restrictions.isNull("program"), Restrictions.eq("program", Boolean.FALSE)));
			else
				c.add(Restrictions.eq("program", Boolean.TRUE));
		}
		List<ExeStep> tmp = c.setMaxResults(1).list();
		if (tmp.size() == 0) {
			if (Boolean.TRUE.equals(program))
				throw new ResourceException(
						String.format("Không tồn tại hình công đoạn lập trình %s, hình thức gia công %s",
								new Object[] { stepCode, stepTypeCode }));
			throw new ResourceException(String.format("Không tồn tại hình công đoạn gia công %s, hình thức gia công %s",
					new Object[] { stepCode, stepTypeCode }));
		}
		return tmp.get(0);
	}

}
