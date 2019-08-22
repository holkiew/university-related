package fct.ciai.general.ecma.eventproposal.model.dto;

import fct.ciai.general.ecma.persistence.model.EventProposalReview;
import lombok.Data;

@Data
public class EventProposalDTO {
    private Long id;
    private String title;
    private String description;
    private String goals;
    private String neededMaterials;
    private Double budget;
    private String workPlan;
    private boolean approved;
    private boolean hasAssignedReviewer;
    private EventProposalReview review;
}
