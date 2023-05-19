package frwk.form;

import entity.frwk.SysFile;

public class SysFileForm extends SearchForm<SysFile> {
	private Boolean readOnly;
	public Boolean getReadOnly() {
		return readOnly;
	}
	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}

	private SysFile sysFile = new SysFile();
	private String type;

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	private String parentId, to;
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public SysFile getSysFile() {
		return sysFile;
	}

	public void setSysFile(SysFile sysFile) {
		this.sysFile = sysFile;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	@Override
	public SysFile getModel() {

		return sysFile;
	}

}
