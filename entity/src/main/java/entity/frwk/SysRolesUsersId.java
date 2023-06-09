package entity.frwk;

// Generated Nov 13, 2019 6:00:05 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * SysRolesUsersId generated by hbm2java
 */
@Embeddable
public class SysRolesUsersId implements java.io.Serializable {

	private String userId;
	private String roleId;

	public SysRolesUsersId() {
	}

	public SysRolesUsersId(String userId, String roleId) {
		this.userId = userId;
		this.roleId = roleId;
	}

	@Column(name = "USER_ID", nullable = false, length = 40)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "ROLE_ID", nullable = false, length = 40)
	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof SysRolesUsersId))
			return false;
		SysRolesUsersId castOther = (SysRolesUsersId) other;

		return ((this.getUserId() == castOther.getUserId()) || (this.getUserId() != null
				&& castOther.getUserId() != null && this.getUserId().equals(castOther.getUserId())))
				&& ((this.getRoleId() == castOther.getRoleId()) || (this.getRoleId() != null
						&& castOther.getRoleId() != null && this.getRoleId().equals(castOther.getRoleId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result + (getUserId() == null ? 0 : this.getUserId().hashCode());
		result = 37 * result + (getRoleId() == null ? 0 : this.getRoleId().hashCode());
		return result;
	}

}
