package fct.ciai.general.ecma.service.ecma.user.service;

import fct.ciai.general.ecma.persistence.model.Company;
import fct.ciai.general.ecma.persistence.model.User;
import fct.ciai.general.ecma.persistence.repository.CompanyRepository;
import fct.ciai.general.ecma.user.controller.payload.ManagePartnerUserRequest;
import fct.ciai.general.ecma.user.service.UserService;
import fct.ciai.general.security.model.AuthUser;
import fct.ciai.general.security.model.Role;
import fct.ciai.general.security.model.RoleName;
import fct.ciai.general.security.repository.AuthUserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    AuthUserRepository authUserRepository;
    @Mock
    CompanyRepository companyRepository;
    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserService testedObj;

    @Test
    public void partnerUserDataShouldBeChangedAsAdmin() {
        //before
        var adminUser = createAdminUser();
        var oldCompany = Company.builder().name("oldCompanyName").employees(new HashSet<>()).build();
        var newCompany = Company.builder().name("newCompanyName").employees(new HashSet<>()).build();
        var user = User.builder().firstname("oldFirstname").surname("oldSurname").phone("00000").company(oldCompany).build();
        var authUser = AuthUser.builder().email("oldEmail").username("oldUsername").password("oldPassword").user(user).build();
        user.setAuthUser(authUser);
        var request = new ManagePartnerUserRequest("oldCompanyName",
                "oldUsername", "newUsername", "55555",
                "newSurname", "newCompanyName", "newEmail",
                "password", "newFirstname", false);

        //when
        Mockito.when(authUserRepository.findByUsername(request.username)).thenReturn(Optional.of(authUser));
        Mockito.when(companyRepository.findByName(request.getNewCompany())).thenReturn(Optional.of(newCompany));
        Mockito.when(passwordEncoder.encode(request.getNewPassword())).thenReturn("password");
        testedObj.manageUserAccountAsAdmin(adminUser, request);

        //then
        Assert.assertEquals("newCompanyName", user.getCompany().getName());
        Assert.assertEquals("newEmail", authUser.getEmail());
        Assert.assertEquals("55555", user.getPhone());
        Assert.assertEquals("newSurname", user.getSurname());
        Assert.assertEquals("password", authUser.getPassword());
    }

    private AuthUser createAdminUser() {
        var adminUser = new AuthUser();
        var adminRole = new Role();
        adminRole.setName(RoleName.ROLE_ADMIN);
        adminUser.setRoles(new HashSet<>() {{
            add(adminRole);
        }});
        return adminUser;
    }
}