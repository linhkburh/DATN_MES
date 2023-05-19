package frwk.bean;

import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import common.constants.Constants;
import common.util.AESUtils;
import entity.frwk.SysParam;
import frwk.dao.hibernate.sys.SysParamDao;

@Configuration
public class FrwkBean {
	@Autowired
	private SysParamDao sysParamDao;

	@Bean
	public JavaMailSender getJavaMailSender() throws Exception {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		SysParam p = sysParamDao.getSysParamByCode(Constants.MAIL_SVR);
		mailSender.setHost(p.getValue().trim());
		p = sysParamDao.getSysParamByCode(Constants.MAIL_SVR_PORT);
		mailSender.setPort(Integer.parseInt(p.getValue().trim()));
		p = sysParamDao.getSysParamByCode(Constants.MAIL_USR);
		mailSender.setUsername(p.getValue().trim());
		p = sysParamDao.getSysParamByCode(Constants.MAIL_PWD);
		mailSender.setPassword(AESUtils.decrypt(p.getValue().trim()));
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.connectiontimeout", "30000");
		props.put("mail.smtp.timeout", "60000");
		return mailSender;
	}
}
