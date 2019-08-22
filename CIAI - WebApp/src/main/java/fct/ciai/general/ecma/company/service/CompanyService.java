package fct.ciai.general.ecma.company.service;

import fct.ciai.general.ecma.company.controller.payload.AddNewCompanyRequest;
import fct.ciai.general.ecma.company.controller.payload.ManageCompanyRequest;
import fct.ciai.general.ecma.persistence.model.Company;
import fct.ciai.general.ecma.persistence.model.User;
import fct.ciai.general.ecma.persistence.repository.CompanyRepository;
import fct.ciai.general.ecma.persistence.repository.UserRepository;
import fct.ciai.general.security.model.AuthUser;
import fct.ciai.general.security.model.exception.BadRequestException;
import fct.ciai.general.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public void addNewCompany(AddNewCompanyRequest request) {
        boolean doesCompanyExist = companyRepository.findByName(request.getName()).isPresent();
        if (doesCompanyExist) {
            throw new BadRequestException("Company with provided name already exists");
        }
        List<User> users = request.getCompanyEmployees().isEmpty() ? Collections.emptyList() : userRepository.findAllById(request.getCompanyEmployees());
        var company = Company.builder().name(request.getName()).address(request.getAddress()).employees(users).build();
        companyRepository.save(company);
    }

    @Transactional
    public void manageCompany(AuthUser authUser, ManageCompanyRequest request) {
        Company company = validatePartnerAdminCompany(authUser, request);
        if((company == null) &&
           UserUtils.isEcmaAdmin(authUser)) {
            company = companyRepository.findByName(request.getName())
                    .orElseThrow(() -> new BadRequestException("Non existing company"));
        }
        Optional.ofNullable(request.getNewAddress()).ifPresent(company::setAddress);
        Optional.ofNullable(request.getNewName()).ifPresent(company::setName);
        Set<User> companyEmployees = company.getEmployees();
        companyEmployees.addAll(userRepository.findAllById(request.getCompanyEmployeesToAdd()));
        companyEmployees.removeAll(userRepository.findAllById(request.getCompanyEmployeesToRemove()));
    }

    @Transactional
    public void deleteCompany(AuthUser authUser, Long companyId) {
        if (UserUtils.isEcmaAdmin(authUser)) {
            Company company = companyRepository.findById(companyId)
                    .orElseThrow(() -> new BadRequestException("Non existing company"));
            companyRepository.delete(company);
        } else {
            throw new BadRequestException("User has no permissions for deleting company.");
        }
    }

    public Company getCompanyInfo(AuthUser authUser, Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new BadRequestException("Non existing company"));

        if (UserUtils.isEcmaAdmin(authUser) || isUserCompanyAdmin(authUser, company)) {
            return company;
        } else {
            throw new BadRequestException("User has no permissions to see company info.");
        }
    }

    private Company validatePartnerAdminCompany(AuthUser authUser, ManageCompanyRequest request) {
        Company company = null;
        if (UserUtils.isPartnerAdmin(authUser)) {
            company = companyRepository.findByName(request.getName())
                    .orElseThrow(() -> new BadRequestException("Non existing company"));
            isUserCompanyAdmin(authUser, company);
        }
        return company;
    }

    private boolean isUserCompanyAdmin(AuthUser authUser, Company company) {
        company.getEmployees().stream()
                .filter(authUser.getUser()::equals)
                .findAny()
                .orElseThrow(() -> new BadRequestException("User is not admin of this company"));
        return true;
    }
}
