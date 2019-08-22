package fct.ciai.general.ecma.persistence.repository;

import fct.ciai.general.ecma.persistence.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByName(String name);
    Optional<Company> findByAddress(String address);
    List<Company> findByIdIn(List<Long> companyIds);
    boolean existsByName(String name);
    boolean existsByAddress(String address);
}
