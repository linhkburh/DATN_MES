package frwk.schedule;

import java.util.Calendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import common.util.NetWork;
import entity.frwk.SysJob;
import frwk.dao.hibernate.sys.SysJobDao;

public abstract class ClusterTimerTask implements Runnable {

	private static final Logger log = Logger.getLogger(ClusterTimerTask.class);
	@Autowired
	private SysJobDao sysJobDao;
	/**
	 * Hàm thực thi job, chia thành nhiều thead để tăng tốc độ xử lý. Số lượng thead phụ thuộc vào số lượng bản ghi. Mặc
	 * định mỗi thread xử lý 15 bản ghi
	 */
	@Override
	public void run() {
		SysJob lock = null;
		try {
			lock = sysJobDao.getLock(this.getClass());
			if (lock == null)
				return;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return;
		}
		log.info("Start job " + lock.getJobClass());

		try {
			execute();
			// commit
			sysJobDao.endTheadSession(null);
		} catch (Exception e) {
			log.error("Execute error job " + lock.getJobClass(), e);
			try {
				// rollback
				sysJobDao.endTheadSession(e);
			} catch (Exception cmtEx) {
				log.error(cmtEx.getMessage(), cmtEx);
			}

		} finally {
			try {
				// update release lock infor
				lock.setRunning(SysJob.STS_STOP);
				lock.setEndTime(Calendar.getInstance().getTime());
				lock.setRunningHost(NetWork.getLocalIp());
				sysJobDao.getCurrentSession().merge(lock);
				// commit lock
				sysJobDao.getCurrentSession().getTransaction().commit();
			} catch (Exception cmtEx) {
				log.error("Current session release lock false", cmtEx);
				sysJobDao.getCurrentSession().getTransaction().rollback();
				// release lock again
				sysJobDao.releaseLock(lock);
			}
		}

		log.info("End job " + lock.getJobClass());

	}
	protected abstract void execute() throws Exception;

}
