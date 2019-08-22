package fct.ciai.general.security.repository;

import fct.ciai.general.security.model.Role;
import fct.ciai.general.security.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);

    Set<Role> findByNameIn(List<RoleName> roleNames);
}