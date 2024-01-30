package com.devsolutions.DevSolutionsAPI.RequestBodies;

import com.devsolutions.DevSolutionsAPI.Enums.UserRoleChange;

public class RoleChangeRequest {
    private Long id;
    private String username;
    private UserRoleChange change;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public UserRoleChange getChange() {
        return change;
    }
}
