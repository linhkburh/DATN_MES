package frwk.form;

import entity.frwk.SysParam;

public class SysParamForm extends SearchForm<SysParam> {
	
	private String  scode,sname;
	
	public String getScode() {
		return scode;
	}

	public void setScode(String scode) {
		this.scode = scode;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	SysParam sysParam = new SysParam();
	
	public SysParam getSysParam() {
		return sysParam;
	}

	public void setSysParam(SysParam sysParam) {
		this.sysParam = sysParam;
	}

	@Override
	public SysParam getModel() {
		return  sysParam;
	}
	
}
