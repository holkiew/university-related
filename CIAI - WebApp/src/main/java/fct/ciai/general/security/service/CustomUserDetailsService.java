package fct.ciai.general.security.service;

import fct.ciai.general.security.model.AuthUser;
import fct.ciai.general.security.repository.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AuthUserRepository authUserRepository;

    @Override
    @Transactional
    public AuthUser loadUserByUsername(String usernameOrEmail)
            throws UsernameNotFoundException {
        // Let people login with either username or email
        return authUserRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("AuthUser not found with username or email : " + usernameOrEmail)
                );
    }

    // This method is used by JWTAuthenticationFilter
    @Transactional
    public AuthUser loadUserById(Long id) {
        return authUserRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("AuthUser not found with id : " + id)
        );
    }
}