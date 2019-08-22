package fct.ciai.general.ecma.eventproposalreview.model.dto;

import fct.ciai.general.ecma.persistence.model.EventProposal;
import fct.ciai.general.ecma.persistence.model.User;
import fct.ciai.general.ecma.user.model.dto.UserInfoDTO;
import lombok.Data;

@Data
public class EventProposalReviewDTO {
    private EventProposal eventProposal;

    private UserInfoDTO reviewer;

    private String reviewText;

    private boolean approved;
}
