package frwk.form;

import entity.frwk.SysObjects;

public class SysObjectForm extends SearchForm<SysObjects> {
	
	SysObjects sysObj = new SysObjects();
	public SysObjects getSysObj() {
		return sysObj;
	}
	public void setSysObj(SysObjects sysObj) {
		this.sysObj = sysObj;
	}
	@Override
	public SysObjects getModel() {
		return  sysObj;
	}

}
