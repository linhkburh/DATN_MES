package frwk.bean;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import common.util.JsonUtils;
import common.util.ResourceException;

@Service(value = "ftpUtils")
public class FTPUtils {
	private static final Logger logger = Logger.getLogger(FTPUtils.class);

	public void storeFile(FtpInf ftpInf, InputStream inputStream, String remoteFile) throws Exception {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(ftpInf.getHost(), ftpInf.getPort());
			login(ftpClient, ftpInf);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			boolean existDir = ftpClient.changeWorkingDirectory(ftpInf.getFolder());
			if (!existDir)
				makeDirectory(ftpClient, ftpInf.getFolder());
			boolean result = ftpClient.storeFile(remoteFile, inputStream);
			inputStream.close();
			if (!result)
				throw new Exception("store file to FTP server fail");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				logger.error(ex);
			}

		}
	}

	public void removeFile(FtpInf ftpInf, List<String> lstFileName) throws Exception {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(ftpInf.getHost(), ftpInf.getPort());
			login(ftpClient, ftpInf);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.changeWorkingDirectory(ftpInf.getFolder());
			for (String file : lstFileName) {
				if (!ftpClient.deleteFile(file))
					throw new Exception("Fails remove file: " + file);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				logger.error(ex);
			}

		}
	}
	public void removeFile(FtpInf ftpInf, String fileName) throws Exception {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(ftpInf.getHost(), ftpInf.getPort());
			login(ftpClient, ftpInf);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.changeWorkingDirectory(ftpInf.getFolder());
			if (!ftpClient.deleteFile(fileName))
				throw new Exception("Fails remove file: " + fileName);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				logger.error(ex);
			}

		}
	}
	public void storeFiles(FtpInf ftpInf, InputStream[] inputStreams, String directory, String[] remoteFileNames)
			throws Exception {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(ftpInf.getHost(), ftpInf.getPort());
			ftpClient.login(ftpInf.getUser(), ftpInf.getPass());
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

			boolean bChkExistPath = ftpClient.changeWorkingDirectory(directory);
			if (!bChkExistPath)
				makeDirectory(ftpClient, directory);
			for (int i = 0; i < inputStreams.length; i++) {
				boolean result = ftpClient.storeFile(remoteFileNames[i], inputStreams[i]);
				inputStreams[i].close();
				if (!result) {
					logger.error(
							String.format("store file %s to FTP server fail", new Object[] { remoteFileNames[i] }));

					throw new Exception(
							String.format("store file %s to FTP server fail", new Object[] { remoteFileNames[i] }));
				}
			}

		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				logger.error(ex);
			}

		}
	}

	/**
	 * Tao moi va chuyen den thu muc vua tao
	 * 
	 * @param ftpClient
	 * @param directory
	 * @throws IOException
	 */
	private void makeDirectory(FTPClient ftpClient, String directory) throws IOException {
		String[] fldPadd = directory.split("/");
		for (String s : fldPadd) {
			boolean executeOk = false;
			executeOk = ftpClient.changeWorkingDirectory(s);
			if (!executeOk) {
				executeOk = ftpClient.makeDirectory(s);
				executeOk = ftpClient.changeWorkingDirectory(s);
			}

		}

	}

	public boolean existFile(FtpInf ftpInf, String filePath) throws Exception {

		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(ftpInf.getHost(), ftpInf.getPort());
			ftpClient.login(ftpInf.getUser(), ftpInf.getPass());
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			FTPFile[] remoteFiles = ftpClient.listFiles(filePath);
			if (remoteFiles == null || remoteFiles.length == 0)
				return false;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				logger.error(ex);
			}

		}
		return true;
	}

	public void downloadFile(FtpInf ftpInf, String localFilePath, String remoteFilePath) throws Exception {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(ftpInf.getHost(), ftpInf.getPort());
			ftpClient.login(ftpInf.getUser(), ftpInf.getPass());
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(localFilePath));
			boolean success = ftpClient.retrieveFile(remoteFilePath, outputStream);
			outputStream.close();
			if (!success)
				logger.info("Download file not sucess!");
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				logger.error(ex);
			}

		}

	}

	public void copyFileBeetWeen2Server(FtpInf svSource, String soureFullPathFile, FtpInf svDes, String desFullPathFile)
			throws IOException {
		try {
			FTPClient ftpClientSource = svSource.start();
			InputStream inputStream = ftpClientSource.retrieveFileStream(soureFullPathFile);
			FTPClient ftpClientDes = svDes.start();
			int sparateFileIdx = desFullPathFile.lastIndexOf("/");
			String desDirectory = desFullPathFile.substring(0, sparateFileIdx);
			String desFile = desFullPathFile.substring(sparateFileIdx + 1);
			boolean bChkExistPath = ftpClientDes.changeWorkingDirectory(desDirectory);
			boolean result = false;
			if (!bChkExistPath) {
				makeDirectory(ftpClientDes, desDirectory);
				result = ftpClientDes.storeFile(desFile, inputStream);
			} else {
				result = ftpClientDes.storeFile(desFile, inputStream);
			}
			if (!result) {
				logger.error("Khong chuyen duoc file giua 2 server");
				throw new Exception("Khong chuyen duoc file giua 2 server");
			}
		} catch (Exception e) {
			try {
				svSource.end();
			} finally {
				svDes.end();
			}

		}

	}

	private static void login(FTPClient ftpClient, FtpInf ftpInf) throws Exception {
		boolean ok = ftpClient.login(ftpInf.getUser(), ftpInf.getPass());
		if (!ok)
			throw new ResourceException(String.format("Ftp inf login false: %s", JsonUtils.writeToString(ftpInf)));
	}

	public void downloadFile(FtpInf ftpInf, OutputStream outputStream, String filePath) throws Exception {
		FTPClient ftpClient = new FTPClient();
		ftpClient.setControlEncoding("UTF-8");
		try {
			ftpClient.connect(ftpInf.getHost(), ftpInf.getPort());
			login(ftpClient, ftpInf);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			ftpClient.changeWorkingDirectory(ftpInf.getFolder());
			boolean success = ftpClient.retrieveFile(filePath, outputStream);
			outputStream.close();
			if (!success)
				throw new ResourceException("Download file not sucess!");
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				logger.error(ex);
			}

		}

	}

}
