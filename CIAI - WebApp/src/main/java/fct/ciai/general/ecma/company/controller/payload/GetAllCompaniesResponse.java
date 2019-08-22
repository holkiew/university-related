package fct.ciai.general.ecma.company.controller.payload;

import fct.ciai.general.ecma.company.model.CompanyInfoDTO;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "GetAllCompaniesResponse", description = "Gets pageable companies")
public class GetAllCompaniesResponse {
    private List<CompanyInfoDTO> companyDTOList;
    private int totalPages;
}
