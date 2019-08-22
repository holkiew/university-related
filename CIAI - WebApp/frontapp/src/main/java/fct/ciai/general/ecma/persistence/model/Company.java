package fct.ciai.general.ecma.persistence.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "companies", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "name"
        })
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotBlank
    @Size(max = 120)
    private String address;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    @Singular
    private Set<User> employees = new HashSet<>();

    @OneToMany(mappedBy = "involvedCompany", cascade = CascadeType.ALL)
    private List<EventProposal> eventProposals = new ArrayList<>();

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}