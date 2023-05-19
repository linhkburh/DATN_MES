package cic.h2h.form;

import entity.AstMachine;
import frwk.form.SearchForm;

public class SysAstMachineForm extends SearchForm<AstMachine> {

	AstMachine astMachine = new AstMachine();

	String astName, astCertiNo, fromDate, toDate, userAd,companyId;
	Boolean isUsed;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getAstName() {
		return astName;
	}

	public void setAstName(String astName) {
		this.astName = astName;
	}

	public String getAstCertiNo() {
		return astCertiNo;
	}

	public void setAstCertiNo(String astCertiNo) {
		this.astCertiNo = astCertiNo;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getUserAd() {
		return userAd;
	}

	public void setUserAd(String userAd) {
		this.userAd = userAd;
	}

	public Boolean getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(Boolean isUsed) {
		this.isUsed = isUsed;
	}

	public AstMachine getAstMachine() {
		return astMachine;
	}

	public void setAstMachine(AstMachine astMachine) {
		this.astMachine = astMachine;
	}

	@Override
	public AstMachine getModel() {
		
		return astMachine;
	}

}
