package frwk.form;

import java.io.Serializable;

import entity.frwk.SysUsers;

public class LoginForm extends ModelForm<SysUsers> implements Serializable {
	private static final long serialVersionUID = 1L;

	private String user, pass, rsa, RSACode, fromDate, toDate, reportType;
	private String companyId;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getRsa() {
		return rsa;
	}

	public void setRsa(String rsa) {
		this.rsa = rsa;
	}

	public String getRSACode() {
		return RSACode;
	}

	public void setRSACode(String rSACode) {
		RSACode = rSACode;
	}

	private SysUsers su;

	public SysUsers getSu() {
		return su;
	}

	public void setSu(SysUsers su) {
		this.su = su;
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

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@Override
	public SysUsers getModel() {
		return su;
	}

}
