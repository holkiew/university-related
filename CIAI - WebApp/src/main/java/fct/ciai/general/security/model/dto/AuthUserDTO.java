package fct.ciai.general.security.model.dto;

import fct.ciai.general.security.model.Role;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class AuthUserDTO {
    private Long id;

    private String username;

    private String email;

    protected Set<Role> roles = new HashSet<>();
}
