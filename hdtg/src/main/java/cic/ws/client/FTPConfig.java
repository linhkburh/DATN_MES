package cic.ws.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import common.util.AESUtils;
import entity.frwk.SysParam;
import frwk.bean.FtpInf;
import frwk.dao.hibernate.sys.SysParamDao;

@Configuration
@ComponentScan({ "cic.*" })
public class FTPConfig {
	@Value("${FTP_HOST}")
	private String FTP_HOST;
	@Value("${FTP_AUTH}")
	private String FTP_AUTH;
	@Value("${FTP_FLD}")
	private String FTP_FLD;
	@Autowired
	private SysParamDao sysParamDao;

	@Bean
	public FtpInf ftpInf() throws Exception  {
		return createFtpInf(FTP_HOST, FTP_AUTH, FTP_FLD);
	}

	public FtpInf createFtpInf(String hostport, String authen, String folder) throws Exception {
		String host = null, user = null, pass = null;
		int port = 21;
		SysParam ftpHost = sysParamDao.getSysParamByCode(hostport);
		if (ftpHost != null) {
			if (ftpHost.getValue() != null && ftpHost.getValue().length() > 0) {
				String[] temp = ftpHost.getValue().trim().split(":");
				host = temp[0];
				if (temp.length >= 2)
					port = Integer.parseInt(temp[1]);
			}

		}
		SysParam ftpAuthen = sysParamDao.getSysParamByCode(authen);
		if (ftpAuthen != null) {
			if (ftpAuthen.getValue() != null && ftpAuthen.getValue().length() > 0) {
				String[] temp = ftpAuthen.getValue().trim().split("/");
				user = temp[0];
				if (temp.length >= 2)
					try {
						pass = AESUtils.decrypt(temp[1]);
					} catch (Exception e) {
						//pass=temp[1];
						throw e;
					}
			}

		}
		SysParam ftpFolder = sysParamDao.getSysParamByCode(folder);

		return new FtpInf(host, port, user, pass, ftpFolder.getValue().trim());
	}

}
