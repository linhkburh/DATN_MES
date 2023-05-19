package frwk.dao.hibernate.sys;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import common.util.NetWork;
import entity.frwk.SysJob;

@Repository(value = "sysJobDao")
public class SysJobDao extends SysDao<SysJob> {

	private static final Logger lg = Logger.getLogger(SysJobDao.class);
	public SysJob getLock(Class<?> jobClass) {
		Session ss = openNewSession();
		Transaction tx = null;
		SysJob job = null;
		try {
			tx = ss.beginTransaction();
			job = (SysJob) ss.createCriteria(SysJob.class).add(Restrictions.eq("jobClass", jobClass.getCanonicalName()))
					.uniqueResult();
			if (job == null) {
				job = new SysJob(jobClass.getCanonicalName(), SysJob.STS_RUNNING, Calendar.getInstance().getTime());
				ss.save(job);
			} else {
				if (job.getRunning() == null || SysJob.STS_STOP.equals(job.getRunning())) {
					lg.info(String.format("Last running %s job infor: start time: %s, end time: %s, running host: %s",
							new Object[] { job.getJobClass(), Formater.date2ddsmmsyyyspHHmmss(job.getStartTime()),
									Formater.date2ddsmmsyyyspHHmmss(job.getEndTime()), job.getRunningHost() }));
					job.setRunning(SysJob.STS_RUNNING);
					job.setStartTime(Calendar.getInstance().getTime());
					job.setRunningHost(NetWork.getLocalIp());
					// Chua ket thuc
					job.setEndTime(null);
				} else {
					lg.info(String
							.format("The lock %s is in use or disabled, status: %s, start time: %s, running host: %s",
									new Object[] { job.getJobClass(), job.getRunning(),
											Formater.date2ddsmmsyyyspHHmmss(job.getStartTime()),
											job.getRunningHost() }));
					job = null;
				}
			}
			tx.commit();
		} catch (Exception ex) {
			lg.error(ex.getMessage(), ex);
			if (tx != null)
				tx.rollback();
		} finally {
			ss.close();
		}
		return job;
	}

	public void releaseLock(SysJob lock) {

		lg.info("release lock " + lock.getJobClass());
		Session ss = openNewSession();
		Transaction tx = null;
		try {
			tx = ss.beginTransaction();
			lock = (SysJob) ss.get(SysJob.class, lock.getId());
			lock.setRunning(SysJob.STS_STOP);
			lock.setEndTime(Calendar.getInstance().getTime());
			lock.setRunningHost(NetWork.getLocalIp());
			tx.commit();
			lg.info("end release lock " + lock.getJobClass());
		} catch (Exception ex) {
			lg.error("error release lock " + lock.getJobClass(), ex);
			if (tx != null)
				tx.rollback();
		} finally {
			ss.close();
		}
		
	}

}
