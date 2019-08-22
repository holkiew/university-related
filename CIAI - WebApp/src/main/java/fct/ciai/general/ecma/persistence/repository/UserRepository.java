package fct.ciai.general.ecma.persistence.repository;

import fct.ciai.general.ecma.persistence.model.Company;
import fct.ciai.general.ecma.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByFirstnameAndSurname(String firstname, String surname);

    Optional<User> findByPhone(String phone);

    List<User> findByIdIn(List<Long> appUserIds);

    List<User> findByCompany(Company company);

    Boolean existsByFirstnameAndSurname(String firstname, String surname);

    Boolean existsByPhone(String phone);
}
