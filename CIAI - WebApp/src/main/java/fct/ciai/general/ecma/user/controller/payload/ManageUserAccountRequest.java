package fct.ciai.general.ecma.user.controller.payload;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "ManageUserAccountRequest", description = "Manages user account settings")
public class ManageUserAccountRequest {
    @ApiModelProperty(value = "new firstname")
    public String firstname;

    @ApiModelProperty(value = "phone number")
    public String phoneNumber;

    @ApiModelProperty(value = "surname")
    public String surname;

    @ApiModelProperty(value = "email")
    public String email;

    @ApiModelProperty(value = "new password")
    public String password;
}