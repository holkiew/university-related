package fct.ciai.general.security.model;

public enum RoleName {
    ROLE_USER,
    ROLE_ADMIN,
    ROLE_PARTNER_ADMIN,
    ROLE_PARTNER_USER;

    public static final String USER = "ROLE_USER";
    public static final String PARTNER_USER = "ROLE_PARTNER_USER";
    public static final String PARTNER_ADMIN = "ROLE_PARTNER_ADMIN";
    public static final String ADMIN = "ROLE_ADMIN";
}
