package common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtils {
	public static void copy(InputStream source, OutputStream target) throws IOException {
		byte[] buf = new byte[1024];
		int length;
		while ((length = source.read(buf)) > 0)
			target.write(buf, 0, length);
		source.close();
		target.close();
	}

	public static byte[] toByArr(InputStream source) throws IOException {
		byte[] targetArray = new byte[source.available()];
		source.read(targetArray);
		source.close();
		return targetArray;
	}

}
