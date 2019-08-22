package fct.ciai.general.ecma.user.model.dto;

import fct.ciai.general.ecma.persistence.model.Company;
import fct.ciai.general.ecma.persistence.model.EventProposal;
import fct.ciai.general.ecma.persistence.model.EventProposalReview;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserInfoDTO {
    private Long id;

    private String firstname;

    private String surname;

    private String phone;

    private Boolean isApproval;

    private Company company;

    private List<EventProposal> eventProposals = new ArrayList<>();

    private List<EventProposal> eventProposalReviewAssigments = new ArrayList<>();

    private List<EventProposalReview> reviews = new ArrayList<>();
}
