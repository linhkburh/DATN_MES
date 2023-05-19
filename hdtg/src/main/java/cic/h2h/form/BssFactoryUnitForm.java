package cic.h2h.form;

import entity.BssFactoryUnit;
import frwk.form.SearchForm;

public class BssFactoryUnitForm extends SearchForm<BssFactoryUnit>{
	private BssFactoryUnit bssFactoryUnit = new BssFactoryUnit();
	@Override
	public BssFactoryUnit getModel() {
		return bssFactoryUnit;
	}
	public BssFactoryUnit getBssFactoryUnit() {
		return bssFactoryUnit;
	}
	public void setBssFactoryUnit(BssFactoryUnit bssFactoryUnit) {
		this.bssFactoryUnit = bssFactoryUnit;
	}

}
