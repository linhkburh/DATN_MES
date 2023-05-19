package frwk.form;

import entity.frwk.SysUsers;

public class ManageUserForm extends SearchForm<SysUsers> {
	private String  strusername,strname,job_name;

	public String getStrusername() {
		return strusername;
	}

	public void setStrusername(String strusername) {
		this.strusername = strusername;
	}

	public String getStrname() {
		return strname;
	}

	public void setStrname(String strname) {
		this.strname = strname;
	}

	public String getJob_name() {
		return job_name;
	}

	public void setJob_name(String job_name) {
		this.job_name = job_name;
	}

	@Override
	public SysUsers getModel() {
		return su;
	}

	SysUsers su = new SysUsers();

	public SysUsers getSu() {
		return su;
	}

	public void setSu(SysUsers su) {
		this.su = su;
	}
	private String companyId, departmentId;
	public String getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyId() {
		return companyId;
	}

}
