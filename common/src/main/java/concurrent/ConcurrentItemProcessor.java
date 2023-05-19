package concurrent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface ConcurrentItemProcessor<T> {
	public void process(T t) throws Exception;
	public static final Logger log = LogManager.getLogger(ConcurrentItemProcessor.class);
	/**
	 * Kêt thúc luồng xử lý
	 * @param ex Exception xảy ra khi xử lý luồng
	 * @throws Exception
	 */
	public default void finalize(Exception ex) throws Exception{
		
	}
}
