package fct.ciai.general.security.controller.payload;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SignupRequest {

    @NotBlank
    @Size(min = 3, max = 15)
    private String username;

    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;

    @Size(min = 3, max = 20)
    private String surname;

    @Size(min = 3, max = 20)
    private String phoneNumber;

    @Size(min = 4, max = 40)
    private String firstname;

    @Size(min = 2, max = 40)
    private String companyName;
}
