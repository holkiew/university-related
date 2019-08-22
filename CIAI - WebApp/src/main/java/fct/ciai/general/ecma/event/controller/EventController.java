package fct.ciai.general.ecma.event.controller;

import fct.ciai.general.ecma.eventproposal.controller.payload.request.AddEventProposalRequest;
import fct.ciai.general.ecma.eventproposal.controller.payload.request.UpdateEventProposalRequest;
import fct.ciai.general.ecma.eventproposal.controller.payload.response.GetAllProposalsResponse;
import fct.ciai.general.ecma.eventproposal.model.dto.EventProposalDTO;
import fct.ciai.general.ecma.eventproposal.service.EventProposalService;
import fct.ciai.general.ecma.persistence.model.EventProposal;
import fct.ciai.general.ecma.persistence.repository.EventProposalRepository;
import fct.ciai.general.security.LoggedUser;
import fct.ciai.general.security.model.AuthUser;
import fct.ciai.general.security.model.RoleName;
import fct.ciai.general.security.model.exception.BadRequestException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(description = "Manages events")
@Controller
@RequestMapping("/api/event")
@RequiredArgsConstructor
public class EventController {

    private final EventProposalService eventProposalService;
    private final ModelMapper mapper;

    @ApiOperation(httpMethod = "GET",
            value = "get all events",
            nickname = "getAllEvents")
    @GetMapping("getAllEvents")
    public ResponseEntity<GetAllProposalsResponse> getAllEvents(@LoggedUser AuthUser loggedUser, @RequestParam int page, @RequestParam int pageSize) {
        Page<EventProposal> pageResponse = eventProposalService.getAllEvents(loggedUser, page, pageSize);
        List<EventProposalDTO> eventProposalDTOList = pageResponse.stream()
                .map(eventProposal -> mapper.map(eventProposal, EventProposalDTO.class))
                .collect(Collectors.toList());
        return new ResponseEntity<>(
                new GetAllProposalsResponse(eventProposalDTOList, pageResponse.getTotalPages()),
                HttpStatus.ACCEPTED);
    }
}
