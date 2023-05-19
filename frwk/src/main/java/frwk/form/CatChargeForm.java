package frwk.form;

import entity.frwk.CatCharge;

public class CatChargeForm extends SearchForm<CatCharge> {
	
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

	private CatCharge catCharge = new CatCharge();

	public CatCharge getCatCharge() {
		return catCharge;
	}

	public void setCatCharge(CatCharge catCharge) {
		this.catCharge = catCharge;
	}

	@Override
	public CatCharge getModel() {
		return  catCharge;
	}
	
}
