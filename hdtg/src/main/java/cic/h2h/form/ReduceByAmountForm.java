package cic.h2h.form;

import entity.ReduceByAmount;
import frwk.form.SearchForm;

public class ReduceByAmountForm extends SearchForm<ReduceByAmount> {

	ReduceByAmount reduceByAmount = new ReduceByAmount();
	String codeSearch;

	public String getCodeSearch() {
		return codeSearch;
	}

	public void setCodeSearch(String codeSearch) {
		this.codeSearch = codeSearch;
	}

	public ReduceByAmount getReduceByAmount() {
		return reduceByAmount;
	}

	public void setReduceByAmount(ReduceByAmount reduceByAmount) {
		this.reduceByAmount = reduceByAmount;
	}

	@Override
	public ReduceByAmount getModel() {
		
		return reduceByAmount;
	}

}
