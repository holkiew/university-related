package fct.ciai.general.ecma.company.controller;

import fct.ciai.general.ecma.company.controller.payload.AddNewCompanyRequest;
import fct.ciai.general.ecma.company.controller.payload.GetAllCompaniesResponse;
import fct.ciai.general.ecma.company.controller.payload.ManageCompanyRequest;
import fct.ciai.general.ecma.company.model.CompanyInfoDTO;
import fct.ciai.general.ecma.company.service.CompanyService;
import fct.ciai.general.ecma.persistence.model.Company;
import fct.ciai.general.ecma.persistence.repository.CompanyRepository;
import fct.ciai.general.security.LoggedUser;
import fct.ciai.general.security.model.AuthUser;
import fct.ciai.general.security.model.RoleName;
import fct.ciai.general.security.model.exception.BadRequestException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(description = "Manages companies")
@Controller
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final CompanyRepository companyRepository;
    private final ModelMapper modelMapper;

    @ApiOperation(httpMethod = "POST",
            value = "Adds new company",
            nickname = "addNewCompany",
            notes = "Only ADMIN")
    @PostMapping("addNewCompany")
    @Secured(RoleName.ADMIN)
    public ResponseEntity addNewCompany(@Valid @RequestBody AddNewCompanyRequest addNewCompanyRequest) {
        try {
            companyService.addNewCompany(addNewCompanyRequest);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "PATCH",
            value = "manage company",
            nickname = "manageCompany",
            notes = "Only ADMIN or PARTNER_ADMIN")
    @PatchMapping("manageCompany")
    @Secured({RoleName.ADMIN, RoleName.PARTNER_ADMIN})
    public ResponseEntity manageCompany(@LoggedUser AuthUser authUser, @Valid @RequestBody ManageCompanyRequest manageCompanyRequest) {
        try {
            companyService.manageCompany(authUser, manageCompanyRequest);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Get all company",
            nickname = "getAllCompanies")
    @GetMapping("getAllCompanies")
    public ResponseEntity<GetAllCompaniesResponse> getAllCompanies(@RequestParam int page, @RequestParam int pageSize) {
        Page<Company> pageResponse = companyRepository.findAll(PageRequest.of(page, pageSize));
        List<CompanyInfoDTO> companyDTOList = pageResponse.stream()
                .map(user -> modelMapper.map(user, CompanyInfoDTO.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(
                new GetAllCompaniesResponse(companyDTOList, pageResponse.getTotalPages()),
                HttpStatus.ACCEPTED);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Get company info",
            nickname = "getCompanyInfo",
            notes = "USER OR PARTNER_ADMIN")
    @GetMapping("getCompanyInfo")
    public ResponseEntity getCompanyInfo(@LoggedUser AuthUser authUser, Long companyId) {
        return new ResponseEntity<>(
                modelMapper.map(companyService.getCompanyInfo(authUser, companyId), CompanyInfoDTO.class),
                HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "DELETE",
            value = "delete company by companyId",
            nickname = "deleteCompany",
            notes = "Only ADMIN")
    @DeleteMapping("deleteCompany")
    @Secured(RoleName.ADMIN)
    public ResponseEntity deleteCompany(@LoggedUser AuthUser loggedUser, Long companyId) {
        try{
            companyService.deleteCompany(loggedUser, companyId);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
