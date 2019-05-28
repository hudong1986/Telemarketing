package telemarketing.model;

public class Pt_role {
    private String roleCode;

    public String getRoleCode() {
		return roleCode;
	}

	private String roleName;

    public String orderField() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode == null ? null : roleCode.trim();
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }
}