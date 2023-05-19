package cic.h2h.form;

import entity.ExchRate;
import frwk.form.SearchForm;
public class ExchRateForm extends SearchForm<ExchRate>{
	private ExchRate exchRate = new ExchRate();
	private String exchDate;
	private String currency;
	@Override
	public ExchRate getModel() {
		return exchRate;
	}
	public ExchRate getExchRate() {
		return exchRate;
	}
	public void setExchRate(ExchRate exchRate) {
		this.exchRate = exchRate;
	}
	public String getExchDate() {
		return exchDate;
	}
	public void setExchDate(String exchDate) {
		this.exchDate = exchDate;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}

}
