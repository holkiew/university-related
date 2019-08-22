package fct.ciai.general.ecma.eventproposal.controller;

import fct.ciai.general.ecma.eventproposal.controller.payload.request.AddEventProposalRequest;
import fct.ciai.general.ecma.eventproposal.controller.payload.request.UpdateEventProposalRequest;
import fct.ciai.general.ecma.eventproposal.controller.payload.response.GetAllProposalsResponse;
import fct.ciai.general.ecma.eventproposal.model.dto.EventProposalDTO;
import fct.ciai.general.ecma.eventproposal.service.EventProposalService;
import fct.ciai.general.ecma.persistence.model.EventProposal;
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
import java.util.stream.Collectors;

@Api(description = "Manages event proposals")
@Controller
@RequestMapping("/api/eventProposal")
@RequiredArgsConstructor
public class EventProposalController {

    private final EventProposalService eventProposalService;
    private final ModelMapper mapper;

    @ApiOperation(httpMethod = "PUT",
            value = "Adds new event proposal",
            nickname = "addEventProposal")
    @PutMapping("addEventProposal")
    public ResponseEntity addEventProposal(@LoggedUser AuthUser loggedUser, @Valid @RequestBody AddEventProposalRequest addEventProposalRequest) {
        try {
            eventProposalService.addNewEventProposal(loggedUser, addEventProposalRequest);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

    @ApiOperation(httpMethod = "GET",
            value = "get all proposals",
            nickname = "getAllProposals")
    @GetMapping("getAllProposals")
    public ResponseEntity<GetAllProposalsResponse> getAllProposals(@LoggedUser AuthUser loggedUser, @RequestParam int page, @RequestParam int pageSize) {
        Page<EventProposal> pageResponse = eventProposalService.getAllEventProposals(loggedUser, page, pageSize);
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

    @ApiOperation(httpMethod = "PATCH",
            value = "update proposal",
            nickname = "updateEventProposal")
    @PatchMapping("updateEventProposal")
    @Secured({RoleName.USER, RoleName.ADMIN})
    public ResponseEntity updateProposal(@LoggedUser AuthUser loggedUser, @RequestBody UpdateEventProposalRequest request) {
        try {
            eventProposalService.updateEventProposal(loggedUser, request);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @ApiOperation(httpMethod = "POST",
            value = "approve Proposal",
            nickname = "approveProposal")
    @PostMapping("approveProposal")
    @Secured({RoleName.USER, RoleName.ADMIN})
    public ResponseEntity approveProposal(@LoggedUser AuthUser loggedUser, @RequestParam long proposalId) {
        try {
            eventProposalService.approveEventProposal(loggedUser, proposalId);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }


    @ApiOperation(httpMethod = "DELETE",
            value = "delete eventProposal by eventProposalId",
            nickname = "deleteEventProposal",
            notes = "Only ADMIN")
    @DeleteMapping("deleteEventProposal")
    @Secured(RoleName.ADMIN)
    public ResponseEntity deleteEventProposal(@LoggedUser AuthUser loggedUser, @RequestParam Long eventProposalId) {
        try{
            eventProposalService.deleteEventProposal(loggedUser, eventProposalId);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
