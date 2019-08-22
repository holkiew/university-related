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
@ApiModel(value = "ManageCompanyRequest", description = "manage existing company")
public class ManageCompanyRequest {
    @ApiModelProperty(value = "name of company", required = true)
    @NotBlank
    private String name;

    @ApiModelProperty(value = "new name of company")
    private String newName;

    @ApiModelProperty(value = "new address of company")
    private String newAddress;

    @ApiModelProperty(value = "new company employees")
    private List<Long> companyEmployeesToAdd;

    @ApiModelProperty(value = "company empolyees to remove")
    private List<Long> companyEmployeesToRemove;

    public List<Long> getCompanyEmployeesToAdd() {
        return Objects.isNull(companyEmployeesToAdd) ? Collections.emptyList() : companyEmployeesToAdd;
    }

    public List<Long> getCompanyEmployeesToRemove() {
        return Objects.isNull(companyEmployeesToRemove) ? Collections.emptyList() : companyEmployeesToRemove;
    }
}
