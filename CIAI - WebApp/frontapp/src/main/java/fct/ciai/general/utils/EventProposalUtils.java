package fct.ciai.general.utils;

import fct.ciai.general.ecma.persistence.model.EventProposal;
import fct.ciai.general.security.model.AuthUser;

public class EventProposalUtils {
    public static boolean isAssignedReviewer(EventProposal eventProposal, AuthUser authUser) {
        return eventProposal.getAssignedReviewer().equals(authUser.getUser());
    }
}
