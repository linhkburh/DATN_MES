package frwk.form;

import entity.frwk.SysSecurity;

public class SysSecurityForm extends SearchForm<SysSecurity> {

	SysSecurity security = new SysSecurity();
	String sCode, sName;

	public SysSecurity getSecurity() {
		return security;
	}

	public void setSecurity(SysSecurity security) {
		this.security = security;
	}

	public String getsCode() {
		return sCode;
	}

	public void setsCode(String sCode) {
		this.sCode = sCode;
	}

	public String getsName() {
		return sName;
	}

	public void setsName(String sName) {
		this.sName = sName;
	}

	@Override
	public SysSecurity getModel() {
		
		return security;
	}

}
