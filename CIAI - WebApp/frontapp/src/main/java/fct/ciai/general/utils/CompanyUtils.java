package fct.ciai.general.utils;

import fct.ciai.general.ecma.persistence.model.Company;
import fct.ciai.general.security.model.AuthUser;

public class CompanyUtils {
    public static boolean isAdminOfPartnerCompany(Company company, AuthUser authUser) {
        return UserUtils.isPartnerAdmin(authUser) && company.getEmployees().contains(authUser.getUser());
    }
}
