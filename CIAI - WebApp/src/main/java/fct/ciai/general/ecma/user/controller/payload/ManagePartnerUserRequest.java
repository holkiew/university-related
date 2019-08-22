package fct.ciai.general.ecma.user.controller.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "ManagePartnerUserRequest", description = "Manage user request")
public class ManagePartnerUserRequest {

    @ApiModelProperty(value = "name of user company")
    public String userCompanyName;

    @ApiModelProperty(value = "username", required = true)
    @NotBlank
    public String username;

    @ApiModelProperty(value = "new username")
    public String newUsername;

    @ApiModelProperty(value = "phone number")
    public String phoneNumber;

    @ApiModelProperty(value = "surname")
    public String surname;

    @ApiModelProperty(value = "Company where user should be assigned")
    public String newCompany;

    @ApiModelProperty(value = "email")
    public String email;

    @ApiModelProperty(value = "new password")
    public String newPassword;

    @ApiModelProperty(value = "new firstname")
    public String firstname;

    @ApiModelProperty(value = "new firstname")
    public Boolean isApproval;
}
