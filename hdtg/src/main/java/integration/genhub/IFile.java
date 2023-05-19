package integration.genhub;

import java.io.InputStream;

public interface IFile {
	public Integer getFileType();
	public String getFileName();
	public InputStream getIs();
	public String getErrorCode();
	public String getErrorDes();
	public Integer getContentLength();
	public void close() throws Exception;
}
