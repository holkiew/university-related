package fct.ciai.general.ecma.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "event_proposal_reviews")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventProposalReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JsonIgnore
    private EventProposal eventProposal;

    @ManyToOne
    private User reviewer;

    @NotBlank
    private String reviewText;

    @NotNull
    private boolean approved;
}
