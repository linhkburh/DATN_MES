package frwk.schedule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import entity.frwk.SysJob;
import frwk.dao.hibernate.sys.SysJobDao;

@Configuration
public class ScheduleConfig implements SchedulingConfigurer {

	@Autowired
	private ApplicationContext applicationContext;

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
	}

	@Autowired
	private SysJobDao sysJobDao;
	@Autowired
	private ThreadPoolTaskScheduler threadPoolTaskScheduler;

	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler() throws Exception {
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(50);
		threadPoolTaskScheduler.initialize();
		List<SysJob> lstJob = sysJobDao.getAll();
		for (SysJob job : lstJob)
			threadPoolTaskScheduler.schedule(make(job), new CronTrigger(job.getCronExpression()));
		return threadPoolTaskScheduler;
	}

	private Runnable make(SysJob job) throws Exception {
		Class<?> jobClass = Class.forName(job.getJobClass());
		return (Runnable) applicationContext.getBean(jobClass);
	}

}
