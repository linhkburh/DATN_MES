package frwk.controller;

public class SearchParam {
	private Integer pageNum;
	private Integer pageSize;
	private Integer beginIndex;
	private Integer endIndex;
	private Integer toTalPage;
	private Integer toTalRecord;
	private Integer totalDisplayRecord;

	public SearchParam(int start, int amount) {
		this.beginIndex =start;
		this.pageSize = amount;
		if (this.pageNum != null) {
			beginIndex = new Integer((pageNum.intValue() - 1) * pageSize.intValue());
			endIndex = new Integer(beginIndex + pageSize.intValue());
		}
	}

	public Integer getTotalDisplayRecord() {
		return totalDisplayRecord;
	}

	public void setTotalDisplayRecord(Integer totalDisplayRecord) {
		this.totalDisplayRecord = totalDisplayRecord;
	}

	public Integer getToTalRecord() {
		return toTalRecord;
	}

	public void setToTalRecord(Integer toTalRecord) {
		this.toTalRecord = toTalRecord;
		if (pageSize != null)
			this.toTalPage = (int) Math.round(toTalRecord / pageSize + 0.5);
	}

	public Integer getToTalPage() {
		return toTalPage;
	}

	public Integer getBeginIndex() {
		return beginIndex;
	}

	public void setBeginIndex(Integer beginIndex) {
		this.beginIndex = beginIndex;
	}

	public Integer getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(Integer endIndex) {
		this.endIndex = endIndex;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
		if (this.pageSize != null) {
			beginIndex = new Integer((pageNum.intValue() - 1) * pageSize.intValue());
			endIndex = new Integer(beginIndex + pageSize.intValue());
		}
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
		if (this.pageNum != null) {
			beginIndex = new Integer((pageNum.intValue() - 1) * pageSize.intValue());
			endIndex = new Integer(beginIndex + pageSize.intValue());
		}
	}
}
