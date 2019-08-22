package fct.ciai.general.ecma.eventproposalreview.controller;

import fct.ciai.general.ecma.eventproposal.controller.payload.response.GetAllProposalsResponse;
import fct.ciai.general.ecma.eventproposal.model.dto.EventProposalDTO;
import fct.ciai.general.ecma.eventproposal.service.EventProposalService;
import fct.ciai.general.ecma.eventproposalreview.controller.payload.request.AddEventProposalReviewRequest;
import fct.ciai.general.ecma.eventproposalreview.model.dto.EventProposalReviewDTO;
import fct.ciai.general.ecma.eventproposalreview.service.EventProposalReviewService;
import fct.ciai.general.ecma.persistence.model.EventProposal;
import fct.ciai.general.ecma.persistence.repository.EventProposalReviewRepository;
import fct.ciai.general.security.LoggedUser;
import fct.ciai.general.security.model.AuthUser;
import fct.ciai.general.security.model.RoleName;
import fct.ciai.general.security.model.exception.BadRequestException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Api(description = "Manages event proposal reviews")
@Controller
@RequestMapping("api/eventProposalReview")
@RequiredArgsConstructor
public class EventProposalReviewController {
    private final EventProposalReviewService eventProposalReviewService;
    private final EventProposalService eventProposalService;
    private final EventProposalReviewRepository eventProposalReviewRepository;
    private final ModelMapper mapper;

    @ApiOperation(httpMethod = "PUT",
            value = "Adds new event proposal review",
            nickname = "addEventProposalReview")
    @PutMapping("addEventProposalReview")
    public ResponseEntity addEventProposalReview(@LoggedUser AuthUser loggedUser, @Valid @RequestBody AddEventProposalReviewRequest addEventProposalReviewRequest) {
        try {
            eventProposalReviewService.addNewEventProposalReview(loggedUser, addEventProposalReviewRequest);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @ApiOperation(httpMethod = "GET",
            value = "get proposal review",
            nickname = "getProposalReview")
    @GetMapping("getProposalReview")
    public ResponseEntity<Optional<EventProposalReviewDTO>> getProposalReview(@RequestParam Long proposalId) {
        return new ResponseEntity<>(
                eventProposalReviewRepository.findByEventProposalId(proposalId).stream()
                        .map(eventProposal -> mapper.map(eventProposal, EventProposalReviewDTO.class))
                        .findFirst(),
                HttpStatus.ACCEPTED);
    }

    @ApiOperation(httpMethod = "POST",
            value = "assign reviewer to proposal",
            nickname = "assignNewProposalReviewer")
    @PostMapping("assignNewProposalReviewer")
    public ResponseEntity assignNewProposalReviewer(@LoggedUser AuthUser loggedUser, @RequestParam Long proposalId) {
        try {
            eventProposalService.assignNewProposalReviewer(loggedUser, proposalId);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @ApiOperation(httpMethod = "POST",
            value = "assign reviewer to proposal by admin",
            nickname = "getProposalReview")
    @PostMapping("assignNewProposalReviewerByAdmin")
    @Secured(RoleName.ADMIN)
    public ResponseEntity assignNewProposalReviewerByAdmin(@LoggedUser AuthUser loggedUser, @RequestParam Long proposalId, @RequestParam Long userId) {
        try {
            eventProposalService.assignNewProposalReviewerByAdmin(loggedUser, proposalId, userId);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @ApiOperation(httpMethod = "GET",
            value = "get all  proposals to review by this user",
            nickname = "getUsersProposalsToReview")
    @GetMapping("getUsersProposalsToReview")
    public ResponseEntity<GetAllProposalsResponse> getUsersProposalsToReview(@LoggedUser AuthUser loggedUser, @RequestParam int page, @RequestParam int pageSize) {
        Page<EventProposal> pageResponse = eventProposalReviewService.getUsersProposalsToReview(loggedUser, page, pageSize);
        List<EventProposalDTO> eventProposalDTOList = pageResponse.stream()
                .map(eventProposal -> {
                    EventProposalDTO dto = mapper.map(eventProposal, EventProposalDTO.class);
                    dto.setHasAssignedReviewer(eventProposal.hasAssignedReviewer());
                    return dto;
                })
                .collect(Collectors.toList());
        return new ResponseEntity<>(
                new GetAllProposalsResponse(eventProposalDTOList, pageResponse.getTotalPages()),
                HttpStatus.ACCEPTED);
    }

}
