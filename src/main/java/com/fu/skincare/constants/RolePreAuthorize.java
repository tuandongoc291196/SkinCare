package com.fu.skincare.constants;

public final class RolePreAuthorize {
    public static final String ROLE_ADMIN = "hasRole('ADMIN')";
    public static final String ROLE_STAFF = "hasRole('STAFF')";
    public static final String IS_AUTHENTICATED = "isAuthenticated()";
    public static final String ROLE_ADMIN_STAFF = "hasRole('ADMIN') or hasRole('STAFF')";
    public static final String ROLE_ADMIN_USER = "hasRole('ADMIN') or hasRole('USER')";
    public static final String ROLE_USER = "hasRole('USER')";
}