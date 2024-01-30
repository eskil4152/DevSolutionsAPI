package com.devsolutions.DevSolutionsAPI.Tools;

import com.devsolutions.DevSolutionsAPI.Enums.UserRole;

public class ChangeRole {
    public static UserRole getNextHigherRole(UserRole currentRole) {
        return switch (currentRole) {
            case USER -> UserRole.MODERATOR;
            case MODERATOR -> UserRole.ADMIN;
            default -> null;
        };
    }

    public static UserRole getNextLowerRole(UserRole currentRole) {
        return switch (currentRole) {
            case ADMIN -> UserRole.MODERATOR;
            case MODERATOR -> UserRole.USER;
            default -> null;
        };
    }
}
