package fct.ciai.general.security.service;

import fct.ciai.general.ecma.persistence.model.Company;
import fct.ciai.general.ecma.persistence.model.User;
import fct.ciai.general.ecma.persistence.repository.CompanyRepository;
import fct.ciai.general.security.JwtTokenProvider;
import fct.ciai.general.security.controller.payload.LoginRequest;
import fct.ciai.general.security.controller.payload.SignupRequest;
import fct.ciai.general.security.model.AuthUser;
import fct.ciai.general.security.model.Role;
import fct.ciai.general.security.model.RoleName;
import fct.ciai.general.security.model.exception.AppException;
import fct.ciai.general.security.model.exception.BadRequestException;
import fct.ciai.general.security.repository.AuthUserRepository;
import fct.ciai.general.security.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final AuthUserRepository authUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyRepository companyRepository;

    public String authenticateUserAndGetTokenString(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.generateToken(authentication);
    }

    public void validateRegisteredUser(SignupRequest request) {
        if (authUserRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username is already taken!");
        }

        if (authUserRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already taken!");

        }
    }

    public AuthUser createUser(SignupRequest request, List<RoleName> roleNames) {
        Set<Role> userRoles = roleRepository.findByNameIn(roleNames);
        if (userRoles.size() != roleNames.size()) {
            throw new AppException("AuthUser Role not set.");
        }
        User user = User.builder().surname(request.getSurname())
                .firstname(request.getFirstname())
                .surname(request.getSurname())
                .phone(request.getPhoneNumber())
                .company(getCompanyIfExists(request))
                .isApproval(false)
                .build();
        AuthUser authUser = AuthUser.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(userRoles)
                .user(user)
                .build();
        user.setAuthUser(authUser);
        return authUserRepository.save(authUser);
    }

    private Company getCompanyIfExists(SignupRequest request) {
        if (Objects.nonNull(request.getCompanyName())) {
            return companyRepository.findByName(request.getCompanyName())
                    .orElseThrow(() -> new BadRequestException("Non existent company"));
        } else {
            return null;
        }
    }
}
