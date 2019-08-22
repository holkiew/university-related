package fct.ciai.general.security.repository;

import fct.ciai.general.security.model.AuthUser;
import fct.ciai.general.security.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
    Optional<AuthUser> findByEmail(String email);

    Optional<AuthUser> findByUsernameOrEmail(String username, String email);

    List<AuthUser> findByRolesContaining(Role role);

    List<AuthUser> findByIdIn(List<Long> userIds);

    Optional<AuthUser> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
