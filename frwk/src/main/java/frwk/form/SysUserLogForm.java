package frwk.form;

import entity.frwk.SysUsersLog;

public class SysUserLogForm extends SearchForm<SysUsersLog> {
	
    private String username,actionLists;
    private String spartnerId, todate, fromdate;
    private String keyWord;
    
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getActionLists() {
		return actionLists;
	}

	public void setActionLists(String actionLists) {
		this.actionLists = actionLists;
	}

	public String getSpartnerId() {
		return spartnerId;
	}

	public void setSpartnerId(String spartnerId) {
		this.spartnerId = spartnerId;
	}
	
	public String getTodate() {
		return todate;
	}

	public void setTodate(String todate) {
		this.todate = todate;
	}

	public String getFromdate() {
		return fromdate;
	}

	public void setFromdate(String fromdate) {
		this.fromdate = fromdate;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	SysUsersLog sysUsersLog = new SysUsersLog();
	
	public SysUsersLog getSysUsersLog() {
		return sysUsersLog;
	}

	public void setSysUsersLog(SysUsersLog sysUsersLog) {
		this.sysUsersLog = sysUsersLog;
	}

	@Override
	public SysUsersLog getModel() {
		return  sysUsersLog;
	}
}
