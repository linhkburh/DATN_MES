package common.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;

public class Base64Utils {
	/**
	 * Ma hoa input stream thanh base64
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static String encodeFile(InputStream inputStream) throws IOException {
		byte[] targetArray = new byte[inputStream.available()];
		inputStream.read(targetArray);
		inputStream.close();
		return java.util.Base64.getEncoder().encodeToString(targetArray);
	}
	/**
	 * Convert chuoi base64 sang file
	 * @param encodedBase64BytesFile Chuoi base64 la ket qua ma hoa base64 cua mang byte noi dung file
	 * @param fullFilePath Duong dan tuyet doi den file ket qua
	 * @throws IOException
	 */
	public static void parseFile(String encodedBase64BytesFile, String fullFilePath) throws IOException {
		byte[] fileContent = java.util.Base64.getDecoder().decode(encodedBase64BytesFile);
		InputStream bis = new ByteArrayInputStream(fileContent);
		File f = new File(fullFilePath);
		if(!f.exists())
			f.createNewFile();
		OutputStream fos = new FileOutputStream(f);
		byte[] buffer = new byte[1024];
		int len;
		while ((len = bis.read(buffer)) != -1) {
			fos.write(buffer, 0, len);
		}
		fos.close();
		bis.close();
		
	}

}
