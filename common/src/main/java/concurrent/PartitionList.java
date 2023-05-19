package concurrent;

import java.util.List;

import org.apache.log4j.Logger;

public class PartitionList<T> {

	private static Logger lg = Logger.getLogger(PartitionList.class);
	private Integer blockSize = null;
	private List<T> data;

	public PartitionList(List<T> data, Integer blockSize, Integer numOfPartition) throws Exception {
		this.data = data;
		init(blockSize, numOfPartition);

	}

	/**
	 * 1.blockSize is null or 0, contruct by numOfPartition <br>
	 * 2. blockSize is not null <br>
	 * 2.1. numOfPartition is not null, numOfPartition is maximium numOfPartition. caculate numOfPartition <br>
	 * 2.1.1. caculate numOfPartition > maximium numOfPartition, init(data, null, new Integer(caculate
	 * numOfPartition))<br>
	 * 2.1. numOfPartition is null, caculate numOfPartition
	 * 
	 * @throws java.lang.Exception
	 * @param numOfPartition
	 * @param blockSize
	 * @param data
	 */
	private void init(Integer blockSize, Integer numOfPartition) throws Exception {
		if ((blockSize == null || blockSize.intValue() == 0)
				&& (numOfPartition == null || numOfPartition.intValue() == 0))
			throw new Exception("Require at lease blockSize or numOfPartition");
		if (blockSize != null && blockSize.intValue() > 0) {
			this.blockSize = blockSize;
			if (numOfPartition != null && numOfPartition.intValue() > 0) {
				if (this.size() > numOfPartition.intValue())
					this.blockSize = new Integer(data.size() / numOfPartition.intValue());
			}
		} else {
			this.blockSize = new Integer(data.size() / numOfPartition.intValue());
		}
		this.size = size();
		lg.info(String.format("init partition result: blockSize: %s, numOfPartition: %s, numOfBlock: %s",
				new Object[] { this.blockSize, numOfPartition, this.size }));

	}

	private int size;

	public int getSize() {
		return size;
	}

	private int size() {
		if (data == null || data.size() <= 0)
			return 0;
		return (data.size() + blockSize - 1) / blockSize;
	}

	public List<T> get(int iIndex) {
		int startIndex = iIndex * blockSize;
		int endIndex = Math.min((iIndex + 1) * blockSize, data.size());
		return data.subList(startIndex, endIndex);
	}
}