package concurrent;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class ConcurrentProcess<T> {
	private static final Logger logger = Logger.getLogger(ConcurrentProcess.class);

	public static <T> ConcurrentProcess<T> execute(List<T> data, ConcurrentItemProcessor<T> processItem, 
			Boolean blockSync) throws Exception {
		Integer blockSize = 15;
		Integer numOfPartition = null;
		return execute(data, processItem, blockSize, numOfPartition, blockSync);

	}

	public static <T> ConcurrentProcess<T> execute(List<T> data, ConcurrentItemProcessor<T> processItem)
			throws Exception {
		Integer blockSize = 15;
		Integer numOfPartition = null;
		Boolean blockSync = Boolean.TRUE;
		return execute(data, processItem, blockSize, numOfPartition, blockSync);
	}

	public static <T> ConcurrentProcess<T> execute(List<T> data, ConcurrentItemProcessor<T> processItem,
			Integer blockSize, Integer numOfPartition, Boolean blockSync) throws Exception {
		ConcurrentProcess<T> processor = new ConcurrentProcess<T>(data, blockSize, numOfPartition, processItem,
				blockSync);
		processor.execute();
		if (processor.ex != null)
			throw processor.ex;
		return processor;
	}

	private PartitionList<T> partitionList;
	/**
	 * Kết thúc khi tất cả các block đều kết thúc, để đồng bộ với luồng chính, các block thành công/thất bại cùng nhau
	 * <br>
	 * Nếu giá trị true, không implement phương thức ConcurrentItemProcessor.finalize (phương thức xử lý thành công/thất
	 * bại của 1 block)
	 */
	private Boolean blockSync;
	/**
	 * Engine xử lý
	 */
	private ConcurrentItemProcessor<T> prcssItem;
	/**
	 * Danh sách thành công
	 */
	private List<T> lstSuccess;
	/**
	 * Danh sach không thành công
	 */
	private List<T> lstFailse;

	public List<T> getLstSuccess() {
		return lstSuccess;
	}

	public List<T> getLstFailse() {
		return lstFailse;
	}

	private Exception ex;
	private long numOfBlock;

	public void releaseItem(ConcurrentProcess<T>.BlockThread blockThread) {
		this.numOfBlock--;
		if (this.blockSync != null && this.blockSync) {
			this.lstFailse.addAll(blockThread.lstFailse);
			this.lstSuccess.addAll(blockThread.lstSuccess);
			if (this.numOfBlock == 0) {
				logger.info(String.format("Concurrent numOfBlock: %s, success item: %s, failse item: %s",
						new Object[] { this.partitionList.getSize(), this.lstSuccess.size(), this.lstFailse.size() }));
			}
		}
	}

	private ConcurrentProcess(List<T> data, Integer blockSize, Integer numOfPartition,
			ConcurrentItemProcessor<T> processItem, Boolean blockSync) throws Exception {
		this.partitionList = new PartitionList<T>(data, blockSize, numOfPartition);
		this.numOfBlock = this.partitionList.getSize();
		this.prcssItem = processItem;
		this.blockSync = blockSync;
		if (this.blockSync != null && this.blockSync) {
			this.lstSuccess = new ArrayList<>();
			this.lstFailse = new ArrayList<>();
		}
	}

	private synchronized void setEx(Exception ex) {
		if (this.ex != null)
			return;
		this.ex = ex;
	}

	/**
	 * Chia thanh partitionList.size() thread chay dong thoi, moi thread chua data partitionList.get(i)
	 * 
	 * @throws Exception
	 */
	public void execute() throws Exception {
		logger.info("Concurrent start");
		for (int i = 0; i < this.partitionList.getSize(); i++) {
			if (this.ex != null)
				break;
			new BlockThread(this.prcssItem, partitionList.get(i)).start();
		}
		if (this.blockSync) {
			while (this.numOfBlock > 0) {
				Thread.sleep(100);
			}
		}

	}

	/**
	 * Xu ly toan bo phan tu
	 * 
	 * @author nguye
	 *
	 */
	private class BlockThread extends Thread {
		private List<T> blockItems;
		private List<T> lstSuccess = new ArrayList<T>();
		private List<T> lstFailse = new ArrayList<T>();
		private ConcurrentItemProcessor<T> prcssItem;
		private Exception ex;

		private BlockThread(ConcurrentItemProcessor<T> prcssItem, List<T> blockItems) {
			this.blockItems = blockItems;
			this.prcssItem = prcssItem;
		}

		@Override
		public void run() {
			for (T t : this.blockItems) {
				// Gap loi dung lai
				if (blockSync && ConcurrentProcess.this.ex != null)
					break;
				try {
					prcssItem.process(t);
					lstSuccess.add(t);
				} catch (Exception e) {
					if (!blockSync)
						logger.error(e.getMessage(), e);
					lstFailse.add(t);
					setEx(e);
					break;
				}
			}
			if (!blockSync) {
				try {
					prcssItem.finalize(this.ex);
				} catch (Exception e) {
					setEx(e);
				}
			}
			ConcurrentProcess.this.releaseItem(this);
			if (HibernateSessionConcurrentItemProcessor.class.isAssignableFrom(prcssItem.getClass()))
				((HibernateSessionConcurrentItemProcessor<T>) prcssItem).releaseResource(this.ex);
		}

		private void setEx(Exception ex) {
			if (this.ex != null)
				return;
			// Gap loi dung lai
			if (blockSync)
				ConcurrentProcess.this.setEx(ex);
			this.ex = ex;
		}
	}

}
