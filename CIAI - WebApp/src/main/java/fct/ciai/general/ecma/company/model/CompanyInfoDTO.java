package fct.ciai.general.ecma.company.model;

import fct.ciai.general.ecma.persistence.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class CompanyInfoDTO {
    private Long id;

    private String name;

    private String address;

    private Set<User> employees = new HashSet<>();
}
