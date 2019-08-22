package fct.ciai.general.utils;

import fct.ciai.general.security.model.AuthUser;
import fct.ciai.general.security.model.RoleName;
import io.jsonwebtoken.lang.Collections;

import java.util.List;

public class UserUtils {
    public static boolean isEcmaUser(AuthUser user) {
        return user.getRoles().stream().anyMatch(role -> role.getName().equals(RoleName.ROLE_USER));
    }

    public static boolean isEcmaAdmin(AuthUser user) {
        return user.getRoles().stream().anyMatch(role -> role.getName().equals(RoleName.ROLE_ADMIN));
    }

    public static boolean isPartnerUser(AuthUser user) {
        return user.getRoles().stream().anyMatch(role -> role.getName().equals(RoleName.ROLE_PARTNER_USER));
    }

    public static boolean isPartnerAdmin(AuthUser user) {
        return user.getRoles().stream().anyMatch(role -> role.getName().equals(RoleName.ROLE_PARTNER_ADMIN));
    }

    public static boolean hasAnyRole(AuthUser user, RoleName... roles) {
        List roleNames = Collections.arrayToList(roles);
        return user.getRoles().stream().anyMatch(roleNames::contains);
    }
}
