package fct.ciai.general.ecma.user.service;

import fct.ciai.general.ecma.persistence.model.Company;
import fct.ciai.general.ecma.persistence.model.User;
import fct.ciai.general.ecma.persistence.repository.CompanyRepository;
import fct.ciai.general.ecma.persistence.repository.UserRepository;
import fct.ciai.general.ecma.user.controller.payload.ManagePartnerUserRequest;
import fct.ciai.general.ecma.user.controller.payload.ManageUserAccountRequest;
import fct.ciai.general.security.model.AuthUser;
import fct.ciai.general.security.model.Role;
import fct.ciai.general.security.model.RoleName;
import fct.ciai.general.security.model.exception.BadRequestException;
import fct.ciai.general.security.repository.AuthUserRepository;
import fct.ciai.general.security.repository.RoleRepository;
import fct.ciai.general.utils.CompanyUtils;
import fct.ciai.general.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthUserRepository authUserRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public void manageUserAccountAsAdmin(AuthUser loggedUser, ManagePartnerUserRequest request) {
        boolean isPartnerAdminRole = UserUtils.isPartnerAdmin(loggedUser);

        if (isPartnerAdminRole) {
            isPartnerAdminInSameCompanyAsUser(loggedUser, request);
        }

        var user = authUserRepository.findByUsername(request.username)
                .orElseThrow(() -> new BadRequestException("Non existent user"))
                .getUser();
        modifyUserData(user, request, isPartnerAdminRole);
    }

    public void manageUserAccount(AuthUser loggedUser, ManageUserAccountRequest request) {
        modifyUserData(loggedUser, request);
    }

    public List<AuthUser> findAllEcmaUsers() {
        Role ecmaUser = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new BadRequestException("Non existent role"));
        return authUserRepository.findByRolesContaining(ecmaUser);
    }

    public List<User> findAllByCompany(AuthUser loggedUser, Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new BadRequestException("Non existent company."));
        if(UserUtils.isEcmaAdmin(loggedUser) || CompanyUtils.isAdminOfPartnerCompany(company, loggedUser)) {
            return userRepository.findByCompany(company);
        } else {
            throw new BadRequestException("User has no permissions to see users of this company");
        }
    }

    @Transactional
    public void deleteUser(AuthUser loggedUser, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("Non existent user"));
        if(user.getAuthUser().equals(loggedUser) || UserUtils.isEcmaAdmin(loggedUser)) {
            AuthUser authUser = user.getAuthUser();
            userRepository.delete(user);
            authUserRepository.delete(authUser);
        } else {
            throw new BadRequestException("User has no permission for deleting this user.");
        }
    }

    private void isPartnerAdminInSameCompanyAsUser(AuthUser loggedUser, ManagePartnerUserRequest request) {
        boolean isInSameCompany = loggedUser.getUser().getCompany().getName().equals(request.getUserCompanyName());
        if (!isInSameCompany) {
            throw new BadRequestException("AuthUser not in same company as local admin");
        }
    }

    @Transactional
    protected void modifyUserData(User user, ManagePartnerUserRequest request, boolean isPartnerAdmin) {
        modifyUserDataAdminOnly(user, request, isPartnerAdmin);
        var authUser = user.getAuthUser();
        Optional.ofNullable(request.getEmail()).ifPresent(authUser::setEmail);
        Optional.ofNullable(request.getNewUsername()).ifPresent(authUser::setUsername);
        Optional.ofNullable(request.getNewPassword()).ifPresent(newPasswd -> authUser.setPassword(passwordEncoder.encode(request.newPassword)));
        Optional.ofNullable(request.getPhoneNumber()).ifPresent(user::setPhone);
        Optional.ofNullable(request.getSurname()).ifPresent(user::setSurname);
        Optional.ofNullable(request.getFirstname()).ifPresent(user::setFirstname);
        Optional.ofNullable(request.getIsApproval()).ifPresent(user::setIsApproval);
    }

    private void modifyUserDataAdminOnly(User user, ManagePartnerUserRequest request, boolean isPartnerAdmin) {
        if (!isPartnerAdmin) {
            companyRepository.findByName(request.getNewCompany()).ifPresent(company -> {
                user.setCompany(company);
                company.getEmployees().add(user);
            });
        }
    }

    @Transactional
    protected void modifyUserData(AuthUser authUser, ManageUserAccountRequest request) {
        var user = authUser.getUser();
        Optional.ofNullable(request.getEmail()).ifPresent(authUser::setEmail);
        Optional.ofNullable(request.getPassword()).ifPresent(newPassword -> authUser.setPassword(passwordEncoder.encode(request.password)));
        Optional.ofNullable(request.getPhoneNumber()).ifPresent(user::setPhone);
        Optional.ofNullable(request.getSurname()).ifPresent(user::setSurname);
        Optional.ofNullable(request.getFirstname()).ifPresent(user::setFirstname);
    }
}
