package cic.h2h.form;

import entity.QcOs;
import entity.QcOsDetail;
import frwk.form.SearchForm;

public class OutSourcedHistoryForm extends SearchForm<QcOs>{
	private String frDate, tDate, user, workCode;
	private String cus, managerCode;
	QcOs qcos = new QcOs();
	
	public String getFrDate() {
		return frDate;
	}
	public void setFrDate(String frDate) {
		this.frDate = frDate;
	}
	public String gettDate() {
		return tDate;
	}
	public void settDate(String tDate) {
		this.tDate = tDate;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getWorkCode() {
		return workCode;
	}
	public void setWorkCode(String workCode) {
		this.workCode = workCode;
	}
	
	public String getCus() {
		return cus;
	}
	public void setCus(String cus) {
		this.cus = cus;
	}
	public String getManagerCode() {
		return managerCode;
	}
	public void setManagerCode(String managerCode) {
		this.managerCode = managerCode;
	}
	@Override
	public QcOs getModel() {
		return qcos;
	}
	
	public QcOs getQcos() {
		return qcos;
	}
	public void setQcos(QcOs qcos) {
		this.qcos = qcos;
	}

	private String drwingCode;

	public String getDrwingCode() {
		return drwingCode;
	}
	public void setDrwingCode(String drwingCode) {
		this.drwingCode = drwingCode;
	}
}
