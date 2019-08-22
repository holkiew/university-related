package fct.ciai.general.ecma.company.controller.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "AddNewCompanyRequest", description = "add new company")
public class AddNewCompanyRequest {
    @ApiModelProperty(value = "name of company", required = true)
    @NotBlank
    private String name;

    @ApiModelProperty(value = "address", required = true)
    @NotBlank
    private String address;

    @ApiModelProperty(value = "company employees")
    private List<Long> companyEmployees;

    public List<Long> getCompanyEmployees() {
        return Objects.isNull(companyEmployees) ? Collections.emptyList() : companyEmployees;
    }
}
