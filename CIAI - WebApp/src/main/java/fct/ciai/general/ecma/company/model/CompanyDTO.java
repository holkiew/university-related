package fct.ciai.general.ecma.company.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompanyDTO {
    private Long id;
    private String name;
    private String address;
}
