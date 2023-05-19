package frwk.form;

import entity.frwk.BssParam;

public class BssParamForm extends SearchForm<BssParam> {
	
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

	private BssParam bssParam = new BssParam();
	
	

	public BssParam getBssParam() {
		return bssParam;
	}

	public void setBssParam(BssParam bssParam) {
		this.bssParam = bssParam;
	}

	@Override
	public BssParam getModel() {
		return  bssParam;
	}
	
}
