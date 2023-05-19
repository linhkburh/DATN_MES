package schedule;

import java.util.Calendar;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.hibernate.Session;
import org.hibernate.context.internal.ThreadLocalSessionContext;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import common.util.Formater;
import concurrent.ConcurrentProcess;
import concurrent.HibernateSessionConcurrentItemProcessor;
import entity.frwk.SysMail;
import entity.frwk.SysMailAtt;
import frwk.dao.hibernate.sys.SysMailDao;
import frwk.schedule.ClusterTimerTask;

@Component
@Scope("prototype")
public class SendMail extends ClusterTimerTask {
	@Autowired
	private JavaMailSender emailSender;
	@Autowired
	private SysMailDao sysMailDao;

	@Override
	protected void execute() throws Exception {
		List<SysMail> mails = sysMailDao.getMailToSend();
		if (Formater.isNull(mails))
			return;
		Calendar yesterday = Calendar.getInstance();
		yesterday.add(Calendar.DATE, -1);
		ConcurrentProcess.execute(mails, new HibernateSessionConcurrentItemProcessor<SysMail>() {
			@Override
			public void process(SysMail mail) throws Exception {
				try {
					mail = sysMailDao.get(mail.getId());
					MimeMessage message = emailSender.createMimeMessage();
					message.setFrom(((JavaMailSenderImpl) emailSender).getUsername());
					MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
					helper.setTo(mail.getReceipt().split(";"));
					if (!Formater.isNull(mail.getBcc()))
						helper.setBcc(mail.getBcc().split(";"));
					if (!Formater.isNull(mail.getCc()))
						helper.setCc(mail.getCc().split(";"));
					helper.setSubject(mail.getSubject());
					helper.setText(mail.getBody(), Boolean.TRUE.equals(mail.getHtml()));
					helper.addInline("myLogo", new ClassPathResource("/templates/Email/logo.png"));
					for (SysMailAtt sma : mail.getLstAtt())
						helper.addAttachment(sma.getFileName(), new ByteArrayResource(sma.getFileContent()));
					emailSender.send(message);
					mail.setStatus(SysMail.STS_SENDT);
					mail.setErrorMessage(null);
				} catch (Exception e) {
					mail.setErrorMessage(e.getMessage());
					// Loai bo cac mail da qua 1
					if (mail.getCreateTime() != null && mail.getCreateTime().before(yesterday.getTime()))
						mail.setStatus(SysMail.STS_SENDT);
					else
						mail.setStatus(SysMail.STS_FALSE);
					throw e;
				} finally {
					mail.setSendTime(Calendar.getInstance().getTime());
					// Save se sinh ban ghi moi, do mail da duoc gan voi SS khac
					// sysMailDao.getCurrentSession().save(mail);
					sysMailDao.getCurrentSession().merge(mail);
				}
			}

			@Override
			public void releaseResource(Exception ex) {
				Session sess = ThreadLocalSessionContext.unbind(sysMailDao.getSessionFactory());
				if (sess == null || sess.getTransaction().getStatus() != TransactionStatus.ACTIVE)
					return;
				if (ex == null)
					sess.getTransaction().commit();
				else
					sess.getTransaction().rollback();
			}

		}, 2, new Integer(15), Boolean.TRUE);
	}

}
