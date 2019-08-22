package fct.ciai.general.ecma.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "event_proposals")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventProposal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String title;

    @Size(max = 500)
    private String description;

    @Size(max = 500)
    private String goals;

    @Size(max = 250)
    private String neededMaterials;

    @Min(0)
    private Double budget;

    @NotBlank
    @Size(max = 500)
    private String workPlan;

    @ManyToOne
    private User author;

    @ManyToOne
    private Company involvedCompany;

    @ManyToOne
    private User assignedReviewer;

    @OneToOne(mappedBy = "eventProposal", cascade = CascadeType.ALL, orphanRemoval = true)
    private EventProposalReview review;

    @OneToMany(mappedBy = "eventProposal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @NotNull
    private boolean approved;

    public boolean hasAssignedReviewer() {
        return assignedReviewer != null;
    }

    public boolean hasPositiveReview() {
        return review != null && review.isApproved();
    }

    @Override
    public String toString() {
        return "EventProposal{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", goals='" + goals + '\'' +
                ", neededMaterials='" + neededMaterials + '\'' +
                ", budget=" + budget +
                ", workPlan='" + workPlan + '\'' +
                '}';
    }
}
