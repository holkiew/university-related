package fct.ciai.general.ecma.user.model.dto;

import fct.ciai.general.ecma.company.model.CompanyDTO;
import fct.ciai.general.ecma.eventproposal.model.dto.EventProposalDTO;
import fct.ciai.general.ecma.eventproposalreview.model.dto.EventProposalReviewDTO;
import fct.ciai.general.security.model.dto.AuthUserDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserExtendedInfoDTO {
    private Long id;

    private String firstname;

    private String surname;

    private String phone;

    private Boolean isApproval;

    private AuthUserDTO authUser;

    private CompanyDTO company;

    private List<EventProposalDTO> eventProposals = new ArrayList<>();

    private List<EventProposalDTO> eventProposalReviewAssigments = new ArrayList<>();

    private List<EventProposalReviewDTO> reviews = new ArrayList<>();
}
