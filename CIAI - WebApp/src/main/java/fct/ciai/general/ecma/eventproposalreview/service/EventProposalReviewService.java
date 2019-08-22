package fct.ciai.general.ecma.eventproposalreview.service;

import fct.ciai.general.ecma.eventproposalreview.controller.payload.request.AddEventProposalReviewRequest;
import fct.ciai.general.ecma.persistence.model.EventProposal;
import fct.ciai.general.ecma.persistence.model.EventProposalReview;
import fct.ciai.general.ecma.persistence.repository.EventProposalRepository;
import fct.ciai.general.ecma.persistence.repository.EventProposalReviewRepository;
import fct.ciai.general.security.model.AuthUser;
import fct.ciai.general.security.model.exception.BadRequestException;
import fct.ciai.general.utils.EventProposalUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventProposalReviewService {
    private final EventProposalReviewRepository eventProposalReviewRepository;
    private final EventProposalRepository eventProposalRepository;

    public void addNewEventProposalReview(AuthUser loggedUser, AddEventProposalReviewRequest request) {
        EventProposalReview review = createEventProposal(loggedUser, request);
        if (EventProposalUtils.isAssignedReviewer(review.getEventProposal(), loggedUser)) {
            eventProposalReviewRepository.save(review);
        }
    }

    public Page<EventProposal> getUsersProposalsToReview(AuthUser loggedUser, int page, int pageSize) {
        return eventProposalRepository.findByReviewerIdAndReviewIsNull(loggedUser.getUser().getId(), PageRequest.of(page, pageSize));
    }

    private EventProposalReview createEventProposal(AuthUser loggedUser, AddEventProposalReviewRequest request) {
        EventProposal proposal = eventProposalRepository.findById(request.getEventProposalId())
                .orElseThrow(() -> new BadRequestException("Non existing proposal"));
        return EventProposalReview.builder()
                .eventProposal(proposal)
                .reviewer(loggedUser.getUser())
                .reviewText(request.getReviewText())
                .approved(request.isApproved())
                .build();
    }
}
