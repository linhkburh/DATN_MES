package cic.h2h.form;

import entity.ProStatus;
import frwk.form.SearchForm;

public class ProStatusForm extends SearchForm<ProStatus>{
	ProStatus proStatus = new ProStatus();
	private String woCode, naWorker, frDate, toDate, drCode, orCode, machiningSts;
	@Override
	public ProStatus getModel() {
		// TODO Auto-generated method stub
		return proStatus;
	}
	public ProStatus getProStatus() {
		return proStatus;
	}
	public void setProStatus(ProStatus proStatus) {
		this.proStatus = proStatus;
	}
	public String getWoCode() {
		return woCode;
	}
	public void setWoCode(String woCode) {
		this.woCode = woCode;
	}
	public String getNaWorker() {
		return naWorker;
	}
	public void setNaWorker(String naWorker) {
		this.naWorker = naWorker;
	}
	public String getFrDate() {
		return frDate;
	}
	public void setFrDate(String frDate) {
		this.frDate = frDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getDrCode() {
		return drCode;
	}
	public void setDrCode(String drCode) {
		this.drCode = drCode;
	}
	public String getOrCode() {
		return orCode;
	}
	public void setOrCode(String orCode) {
		this.orCode = orCode;
	}
	public String getMachiningSts() {
		return machiningSts;
	}
	public void setMachiningSts(String machiningSts) {
		this.machiningSts = machiningSts;
	}
	
}
