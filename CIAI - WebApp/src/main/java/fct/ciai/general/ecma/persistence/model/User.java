package fct.ciai.general.ecma.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fct.ciai.general.security.model.AuthUser;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 40)
    private String firstname;

    @Size(max = 40)
    private String surname;

    @Size(max = 20)
    private String phone;

    @NotNull
    private Boolean isApproval;

    @OneToOne
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private AuthUser authUser;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Company company;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private List<EventProposal> eventProposals = new ArrayList<>();

    @OneToMany(mappedBy = "assignedReviewer", cascade = CascadeType.ALL)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private List<EventProposal> eventProposalReviewAssigments = new ArrayList<>();

    @OneToMany(mappedBy = "reviewer", cascade = CascadeType.ALL)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private List<EventProposalReview> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private List<Comment> comments = new ArrayList<>();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", surname='" + surname + '\'' +
                ", phone='" + phone + '\'' +
                ", authUser=" + authUser +
                '}';
    }
}
