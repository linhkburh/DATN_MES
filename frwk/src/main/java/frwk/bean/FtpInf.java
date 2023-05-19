package frwk.bean;

import java.io.IOException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.jboss.logging.Logger;

public class FtpInf {
	private static final Logger logger = Logger.getLogger(FtpInf.class);
	private String host;
	private int port;
	private String user;
	private String pass;
	private String folder;

	private FTPClient ftpClient = null;

	public FtpInf(String host, int port, String user, String pass,String folder) {
		this.host = host;
		this.port = port;
		this.user = user;
		this.pass = pass;
		this.folder = folder;

	}

	public void end() throws IOException {
		try {
			if (ftpClient.isConnected()) {
				ftpClient.logout();
				ftpClient.disconnect();
			}
		} catch (IOException ex) {
			logger.error(ex);
			throw ex;
		}
	}

	public String getFolder() {
		return folder;
	}

	public String getHost() {
		return host;
	}

	public String getPass() {
		return pass;
	}

	public int getPort() {
		return port;
	}

	public String getUser() {
		return user;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public FTPClient start() throws Exception {
		ftpClient = new FTPClient();
		try {
			ftpClient.connect(this.host, this.port);
			ftpClient.login(this.user, this.pass);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		} catch (Exception e) {
			end();
			throw e;
		}
		ftpClient.enterLocalPassiveMode();
		return ftpClient;
	}
}
