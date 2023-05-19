package frwk.form;

import entity.frwk.SysRoles;

public class SysRolesForm extends SearchForm<SysRoles> {
	
	SysRoles sysRoles = new SysRoles();
	
	
	public SysRoles getSysRoles() {
		return sysRoles;
	}


	public void setSysRoles(SysRoles sysRoles) {
		this.sysRoles = sysRoles;
	}


	@Override
	public SysRoles getModel() {
		return  sysRoles;
	}
	
}
