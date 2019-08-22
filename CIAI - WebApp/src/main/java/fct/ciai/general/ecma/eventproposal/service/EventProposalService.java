package fct.ciai.general.ecma.eventproposal.service;

import fct.ciai.general.ecma.eventproposal.controller.payload.request.AddEventProposalRequest;
import fct.ciai.general.ecma.eventproposal.controller.payload.request.UpdateEventProposalRequest;
import fct.ciai.general.ecma.persistence.model.EventProposal;
import fct.ciai.general.ecma.persistence.model.User;
import fct.ciai.general.ecma.persistence.repository.EventProposalRepository;
import fct.ciai.general.ecma.persistence.repository.UserRepository;
import fct.ciai.general.ecma.user.service.UserService;
import fct.ciai.general.security.model.AuthUser;
import fct.ciai.general.security.model.exception.BadRequestException;
import fct.ciai.general.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventProposalService {
    private final EventProposalRepository eventProposalRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public void addNewEventProposal(AuthUser loggedUser, AddEventProposalRequest request) {
        EventProposal event = createEventProposal(loggedUser, request);

        if (UserUtils.isPartnerUser(loggedUser)) {
            event.setInvolvedCompany(loggedUser.getUser().getCompany());
        }

        eventProposalRepository.save(event);
    }

    public Page<EventProposal> getAllEventProposals(AuthUser loggedUser, int page, int pageSize) {
        if(UserUtils.isEcmaUser(loggedUser)) {
            return eventProposalRepository.findAll(PageRequest.of(page, pageSize));
        }
        return eventProposalRepository
                .findByInvolvedCompany(
                        loggedUser.getUser().getCompany(),
                        PageRequest.of(page, pageSize));
    }

    public Page<EventProposal> getAllEvents(AuthUser loggedUser, int page, int pageSize) {
        if(UserUtils.isEcmaUser(loggedUser)) {
            return eventProposalRepository.findByApproved(true, PageRequest.of(page, pageSize));
        }
        return eventProposalRepository
                .findByInvolvedCompanyAndApproved(loggedUser.getUser().getCompany(), true,
                        PageRequest.of(page, pageSize));
    }

    @Transactional
    public void updateEventProposal(AuthUser loggedUser, UpdateEventProposalRequest request) {
        EventProposal eventProposal = eventProposalRepository.findById(request.getId())
                .orElseThrow(() -> new BadRequestException("Non existing proposal"));
        if (loggedUser.getUser().getIsApproval()) {
            eventProposal.setBudget(request.getBudget());
            eventProposal.setDescription(request.getDescription());
            eventProposal.setGoals(request.getGoals());
            eventProposal.setWorkPlan(request.getWorkPlan());
            eventProposal.setNeededMaterials(request.getNeededMaterials());
            eventProposal.setTitle(request.getTitle());
        } else {
            throw new BadRequestException("User is not assigned to event proposal");
        }
    }

    @Transactional
    public void approveEventProposal(AuthUser loggedUser, Long proposalId) {
        EventProposal eventProposal = eventProposalRepository.findById(proposalId)
                .orElseThrow(() -> new BadRequestException("Non existing proposal"));
        if (loggedUser.getUser().getIsApproval()) {

            eventProposal.setApproved(true);
        } else {
            throw new BadRequestException("User is not assigned to event proposal");
        }
    }

    @Transactional
    public void assignNewProposalReviewer(AuthUser loggedUser, Long proposalId) {
        assignProposalReviewer(loggedUser, proposalId);
    }

    @Transactional
    public void assignNewProposalReviewerByAdmin(AuthUser loggedUser, Long proposalId, Long userId) {
        if(!UserUtils.isEcmaAdmin(loggedUser)) {
            throw new BadRequestException("User has no permissions to set review for this proposal");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("Non existing user"));
        assignProposalReviewer(user.getAuthUser(), proposalId);
    }

    @Transactional
    public void deleteEventProposal(AuthUser authUser, Long proposalId) {
            EventProposal eventProposal = eventProposalRepository.findById(proposalId)
                    .orElseThrow(() -> new BadRequestException("Non existing proposal"));
            eventProposalRepository.delete(eventProposal);
    }

    @Transactional
    public List<AuthUser> getAllInvolvedUsers(EventProposal eventProposal) {
        List<AuthUser> users = userService.findAllEcmaUsers();
        var employees = eventProposal.getInvolvedCompany().getEmployees();
        employees.forEach(employee -> users.add(employee.getAuthUser()));
        return users;
    }

    public boolean userIsInvolvedInEventProposal(AuthUser loggedUser, EventProposal eventProposal) {
        return UserUtils.isEcmaUser(loggedUser) ||
                eventProposal.getInvolvedCompany()
                        .getEmployees()
                        .contains(loggedUser.getUser());
    }

    private void assignProposalReviewer(AuthUser user, Long proposalId) {
        EventProposal proposal = eventProposalRepository.findById(proposalId)
                .orElseThrow(() -> new BadRequestException("Non existing proposal"));
        if(proposal.hasAssignedReviewer()) {
            throw new BadRequestException("Proposal has already assigned reviewer");
        }
        proposal.setAssignedReviewer(user.getUser());
    }

    private EventProposal createEventProposal(AuthUser loggedUser, AddEventProposalRequest request) {
        return EventProposal.builder()
                .budget(request.getBudget())
                .description(request.getDescription())
                .goals(request.getGoals())
                .neededMaterials(request.getNeededMaterials())
                .title(request.getTitle())
                .workPlan(request.getWorkPlan())
                .approved(false)
                .author(loggedUser.getUser())
                .build();
    }
}
