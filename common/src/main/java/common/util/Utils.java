package common.util;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.math.BigDecimal;

public class Utils {
	public static String generateQRCode(String qrContent, int width, int height) throws Exception {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(qrContent,
				BarcodeFormat.QR_CODE, width, height);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(bitMatrix, "PNG",
				byteArrayOutputStream);
		byte[] encodeBase64 = Base64.encodeBase64(byteArrayOutputStream.toByteArray());
		String base64Encoded = new String(encodeBase64, "UTF-8");
		return base64Encoded;
	}
	private static final Logger logger = Logger.getLogger(Utils.class);
	private static final PropertyUtilsBean pub = new PropertyUtilsBean();

	public static void copyObj(Object source, Object des, List<String> ignoreList) {
		for (Field desField : des.getClass().getDeclaredFields()) {
			if (ignoreList.contains(desField.getName()))
				continue;
			try {
				Field sourceField = source.getClass().getField(desField.getName());
				if (!sourceField.getType().equals(desField.getType()))
					continue;
			} catch (NoSuchFieldException e1) {
				continue;
			} catch (Exception e1) {
				logger.error(e1);
				continue;
			}
			try {
				pub.setProperty(des, desField.getName(), pub.getProperty(source, desField.getName()));
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}
	public static Boolean ChkDecimals(BigDecimal bigdecimal) {
		return	bigdecimal.stripTrailingZeros().scale() <= 0;
	}
}
