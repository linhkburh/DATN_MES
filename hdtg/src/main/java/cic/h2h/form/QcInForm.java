package cic.h2h.form;

import java.util.Date;

import entity.QcIn;
import frwk.form.SearchForm;

public class QcInForm extends SearchForm<QcIn>{
	private String idQcOs, idQcIn;
	private String drawingCode, manageCodeSearch, frDate, tDate, cusId, workerSearch;
	
	private QcIn qcIn = new QcIn();
	@Override
	public QcIn getModel() {
		return qcIn;
	}
	public QcIn getQcIn() {
		return qcIn;
	}
	public void setQcIn(QcIn qcIn) {
		this.qcIn = qcIn;
	}
	
	public String getDrawingCode() {
		return drawingCode;
	}
	public String getIdQcOs() {
		return idQcOs;
	}
	public void setIdQcOs(String idQcOs) {
		this.idQcOs = idQcOs;
	}
	public void setDrawingCode(String drawingCode) {
		this.drawingCode = drawingCode;
	}
	public String getManageCodeSearch() {
		return manageCodeSearch;
	}
	public void setManageCodeSearch(String manageCodeSearch) {
		this.manageCodeSearch = manageCodeSearch;
	}
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
	public String getWorkerSearch() {
		return workerSearch;
	}
	public void setWorkerSearch(String workerSearch) {
		this.workerSearch = workerSearch;
	}
	public String getCusId() {
		return cusId;
	}
	public void setCusId(String cusId) {
		this.cusId = cusId;
	}
	public String getIdQcIn() {
		return idQcIn;
	}
	public void setIdQcIn(String idQcIn) {
		this.idQcIn = idQcIn;
	}
	
}
