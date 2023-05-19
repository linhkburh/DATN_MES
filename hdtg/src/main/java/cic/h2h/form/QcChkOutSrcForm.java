package cic.h2h.form;

import java.util.List;

import entity.QcChkOutSrc;
import entity.QcOs;
import frwk.form.SearchForm;

public class QcChkOutSrcForm extends SearchForm<QcChkOutSrc> {

	private QcChkOutSrc qcChkOutSrc = new QcChkOutSrc();

	private String frDate, tDate, worker, workCode;
	private String updatorSearch, manageCode, type, drawingCode;
	private String hisType, qcType;
	private String orderCode, cusCode;
	private List<QcOs> lstQcOs;
	public List<QcOs> getLstQcOs() {
		return lstQcOs;
	}

	public void setLstQcOs(List<QcOs> lstQcOs) {
		this.lstQcOs = lstQcOs;
	}
	public QcChkOutSrc getQcChkOutSrc() {
		return qcChkOutSrc;
	}

	public void setQcChkOutSrc(QcChkOutSrc qcChkOutSrc) {
		this.qcChkOutSrc = qcChkOutSrc;
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

	public String getWorker() {
		return worker;
	}

	public void setWorker(String worker) {
		this.worker = worker;
	}

	public String getWorkCode() {
		return workCode;
	}

	public void setWorkCode(String workCode) {
		this.workCode = workCode;
	}

	public String getUpdatorSearch() {
		return updatorSearch;
	}

	public void setUpdatorSearch(String updatorSearch) {
		this.updatorSearch = updatorSearch;
	}

	public String getManageCode() {
		return manageCode;
	}

	public void setManageCode(String manageCode) {
		this.manageCode = manageCode;
	}

	@Override
	public QcChkOutSrc getModel() {
		return qcChkOutSrc;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDrawingCode() {
		return drawingCode;
	}

	public void setDrawingCode(String drawingCode) {
		this.drawingCode = drawingCode;
	}

	public String getHisType() {
		return hisType;
	}

	public void setHisType(String hisType) {
		this.hisType = hisType;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getCusCode() {
		return cusCode;
	}

	public void setCusCode(String cusCode) {
		this.cusCode = cusCode;
	}

	public String getQcType() {
		return qcType;
	}

	public void setQcType(String qcType) {
		this.qcType = qcType;
	}

}
